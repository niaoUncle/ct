package com.china.analysis.format;

import com.china.analysis.converter.imlp.DimensionConverter;
import com.china.analysis.kv.base.BaseDimension;
import com.china.analysis.kv.base.BaseValue;

import com.china.analysis.kv.impl.ComDimension;
import com.china.analysis.kv.impl.CountDurationValue;
import com.china.constants.Constants;
import com.china.utils.JDBCSingleson;
import com.china.utils.JDBCUtil;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MySQLOutputFormat extends OutputFormat<BaseDimension, BaseValue> {
    @Override
    public RecordWriter<BaseDimension, BaseValue> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        //创建jdbc连接
        Connection conn = null;

        try {
            conn = JDBCSingleson.getConn();
            //关闭自动提交，以便于批量提交
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new IOException(e);
        }

        return new MysqlRecordWrite(conn);

    }

    @Override
    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
        // // 校检输出
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {

        String name = context.getConfiguration().get(FileOutputFormat.OUTDIR);

        Path output = name == null ? null : new Path(name);

        return new FileOutputCommitter(output, context);
    }

    //内部类
    static class MysqlRecordWrite extends RecordWriter<BaseDimension, BaseValue> {
        private Connection conn = null;
        private DimensionConverter dimensionConverter = null;
        private PreparedStatement preparedStatement = null;
        private int batchNumber = 0;

        public MysqlRecordWrite(Connection conn) {
            this.conn = conn;
            this.batchNumber = Constants.JDBC_DEFAULT_BATCH_NUMBER;
            this.dimensionConverter = new DimensionConverter();

        }

        @Override
        public void write(BaseDimension key, BaseValue value) throws IOException, InterruptedException {

            try {
                int count = 0;

                //on duplicate key update 如果没有就插入，有就更新
                String sql = "INSERT INTO tb_call (id_date_contact,id_date_dimension,id_contact,call_sum,call_duration_sum) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE id_date_contact = ?;";

                if (preparedStatement == null) {


                    preparedStatement = conn.prepareStatement(sql);

                }

                int i = 0;

                ComDimension comDimension = (ComDimension) key;

                CountDurationValue countDurationValue = (CountDurationValue) value;

                int id_date_dimension
                        = dimensionConverter.getDemensionId(comDimension.getDateDimension());

                int id_contact
                        = dimensionConverter.getDemensionId(comDimension.getContactDimension());

                int call_sum = countDurationValue.getCallSum();

                int call_duration_sum
                        = countDurationValue.getCallDurationSum();

                String id_date_contact = id_date_dimension + "_" + id_contact;

                preparedStatement.setString(++i, id_date_contact);

                preparedStatement.setInt(++i, id_date_dimension);

                preparedStatement.setInt(++i, id_contact);

                preparedStatement.setInt(++i, call_sum);

                preparedStatement.setInt(++i, call_duration_sum);


                preparedStatement.setString(++i, id_date_contact);
                preparedStatement.addBatch();

                //当前缓存了多少个sql语句等待批量执行，计数器
                count++;

                //批量提交
                if (count >= this.batchNumber) {

                    preparedStatement.executeUpdate();//批量提交

                    //连接提交
                    conn.commit();

                    count = 0;
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {


            try {
                preparedStatement.executeBatch();
                this.conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                JDBCUtil.close(conn, preparedStatement, null);
            }
        }


    }
}
