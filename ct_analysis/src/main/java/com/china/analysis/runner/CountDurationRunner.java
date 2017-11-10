package com.china.analysis.runner;

import com.china.analysis.format.MySQLOutputFormat;
import com.china.analysis.kv.impl.ComDimension;
import com.china.analysis.kv.impl.CountDurationValue;
import com.china.analysis.mapper.CountDurationMapper;
import com.china.analysis.reducer.CountDurationReducer;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;


public class CountDurationRunner implements Tool{
    private Configuration conf = null;

    @Override
    public void setConf(Configuration conf) {

        //必须导入hbase的环境变量到hadoop_classpath中

        //conf是hadoop的配置，在追加一些hBase的conf配置
        this.conf = HBaseConfiguration.create(conf);

    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    @Override
    public int run(String[] args) throws Exception {

        // 0 获取一个配置对像
        Configuration conf = this.getConf();

        // 1 创建job

        Job job = Job.getInstance(conf);

        // 2 设置三个类
        job.setJarByClass(CountDurationRunner.class);
        this.setHBaseInputConfig(job); //为Job设置Mapper
        job.setReducerClass(CountDurationReducer.class); //为Job设置Reducer
        // 3 设置输出的数据类型
        job.setOutputKeyClass(ComDimension.class);
        job.setOutputValueClass(CountDurationValue.class);

        // 4 //为Job设置OutputFormat

       // java.lang.NoClassDefFoundError: org/apache/hadoop/hbase/HBaseConfiguration
       // job.setMapOutputValueClass(MySQLOutputFormat.class);设置类输类型错误导致没有再到类的错误
        job.setOutputFormatClass(MySQLOutputFormat.class);
        return job.waitForCompletion(true )? 0:1;

    }


    private void setHBaseInputConfig(Job job) {
        Configuration conf = this.conf;
        HBaseAdmin admin = null;

        try {
            admin = new HBaseAdmin(conf);
            //如果表不存在则直接返回，抛个异常

            if(! admin.tableExists("ns_telecom:calllog"))
                throw new RuntimeException("Unable to find the specified table.");

            //获取表的描述器
            Scan scan = new Scan();
            scan.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("ns_telecom:calllog"));
            TableMapReduceUtil.initTableMapperJob("ns_telecom:calllog",scan,
                                                CountDurationMapper.class,ComDimension.class,
                                                Text.class,job ,true);



        } catch (IOException e) {

            e.printStackTrace();

        }finally {

            if(admin != null)

                try {

                    admin.close();

                } catch (IOException e) {

                    e.printStackTrace();
                }
        }

    }

    public static void main(String[] args) {
        try {
            int status = ToolRunner.run(new CountDurationRunner(),args);
            System.exit(status);
            if(status == 0){
                System.out.println("Success");

            }
            else {
                System.out.println("Fail");
            }
        } catch (Exception e) {
            System.out.println("Fail.....");
            e.printStackTrace();
        }

    }

}


