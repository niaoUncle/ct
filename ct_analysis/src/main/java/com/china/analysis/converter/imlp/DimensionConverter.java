package com.china.analysis.converter.imlp;

import com.china.analysis.converter.IConverter;
import com.china.analysis.kv.base.BaseDimension;
import com.china.analysis.kv.impl.ContactDimension;
import com.china.analysis.kv.impl.DateDimension;
import com.china.utils.JDBCSingleson;
import com.china.utils.JDBCUtil;
import com.china.utils.LRUCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
     * 维度对象转维度id类
     */
public class DimensionConverter implements IConverter {
        //日志记录类，注意导包的正确性
        private  static final Logger logger =  LoggerFactory.getLogger(DimensionConverter.class);

        //每个线程保留自己的Connection实例
      private ThreadLocal<Connection> threadLocal =  new ThreadLocal<>();

    //创建数据缓存队列
    private LRUCache<String,Integer> lruCache = new LRUCache(3000);

    public DimensionConverter() {
        // JVM虚拟机关闭时，尝试关闭数据库连接  ew Runnable() 匿名方法
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("stopping mysql connection...");
                JDBCUtil.close(threadLocal.get(),null,null);
                logger.info("mysql connection is successfully closed");

            }
        }));

    }

/**
 * 1、判断内存缓存中是否已经有该维度的id，如果存在则直接返回该id
 * 2、如果内存缓存中没有，则查询数据库中是否有该维度id，如果有，则查询出来，返回该id，并缓存到内存中。
 * 3、如果数据库中也没有该维度id，则直接插入一条新的维度信息，成功插入后，重新查询该维度，返回该id，并缓存到内存中。
 */
 @Override
    public int getDemensionId(BaseDimension dimension) throws IOException {

     //1、根据传入的维度对象取得该维度对象对应的cachekey
     String cahcheKey = genCacheKey(dimension);

     //2、判断缓存中是否存在该cacheKey的缓存

     if (lruCache.containsKey(cahcheKey)) {
         return  lruCache.get(cahcheKey);///????

     }

     //3、缓存中没有，查询数据库
     String[] sqls = null;

     if (dimension instanceof DateDimension) {
         // 时间维度表tb_dimension_date
         sqls = genDateDimensionSQL();

     } else if (dimension instanceof ContactDimension) {
         //联系人表tb_contacts
         sqls = genContactsSQL();
     } else {
         //抛出Checked异常，提醒调用者可以自行处理。
         throw new IOException("Cannot match the dimession,unknown dimension.");
     }
     try {
         Connection conn = this.getConnection();
         int id = -1;

         //获取id
         synchronized(this){
        id = execSQL(conn,cahcheKey,sqls,dimension);
         }
         //返回id；
         return  id;
     } catch (Exception e) {
         e.printStackTrace();
         throw new IOException(e);
     }

 }

    @Override
    public void close() throws IOException {
     //关闭连接
     logger.info("stopping mysql connection...");
     JDBCUtil.close(threadLocal.get(),null,null);
     logger.info("mysql connection is successfully closed");

    }


    /**
     * @param conn
     * @param
     * @param sqls      第一个为查询语句，第二个为插入语句
     * @param dimension
     * @return
     */
    private int execSQL(Connection conn, String cahcheKey, String[] sqls, BaseDimension dimension) throws  SQLException{

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            //1、假设数据库中有该条数据
            //封装查询sql语句
            preparedStatement = conn.prepareStatement(sqls[0]);
            setArgments(preparedStatement,dimension);

            //执行查询
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getInt(1);
            }

            //2、假设 数据库中没有该条数据
            //封装插入sql语句

            preparedStatement = conn.prepareStatement(sqls[1]);
            setArgments(preparedStatement, dimension);
            preparedStatement.executeUpdate();

            //关闭连接
            JDBCUtil.close(null,preparedStatement,resultSet);

            //重新获取id，调用自己即可。
            preparedStatement = conn.prepareStatement(sqls[0]);

            setArgments(preparedStatement,dimension);

            //执行查询
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            JDBCUtil.close(null,preparedStatement,resultSet);
//        }
        throw  new RuntimeException("Failed to get id");

    }

    private void setArgments(PreparedStatement preparedStatement, BaseDimension dimension) throws SQLException {
        int i =0;

        if (dimension instanceof  DateDimension){
            DateDimension dateDimension= (DateDimension) dimension;

            preparedStatement.setInt(++i,dateDimension.getYear());
            preparedStatement.setInt(++i,dateDimension.getMonth());
            preparedStatement.setInt(++i,dateDimension.getDay());

        }else if (dimension instanceof  ContactDimension){
            ContactDimension contactDimension =(ContactDimension)dimension;

            preparedStatement.setString(++i, contactDimension.getTelephone());
            preparedStatement.setString(++i, contactDimension.getName());

        }
    }

    /**
     * 尝试获取数据库连接对象：先从线程缓冲中获取，没有可用连接则创建。
     *
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {

        Connection conn = null;
        synchronized(this){
            conn = threadLocal.get();

            if(conn ==null || conn.isClosed() || conn.isValid(3)){

                conn = JDBCSingleson.getConn();

            }

            threadLocal.set(conn);
        }
     return  conn;
    }

    /**
     * 生成联系人的数据库查询语句和插入语句
     *
     * @return
     */
    private String[] genContactsSQL() {

        String query = " SELECT id FROM tb_contacts WHERE telephone = ? AND NAME = ? ORDER BY id";

        String insert = "INSERT INTO tb_contacts(telephone, NAME) VALUES(?, ?);";

     return  new String[]{query,insert};

    }

    /**
     * 生成时间维度的数据库查询语句和插入语句
     *
     * @return
     */
    private String[] genDateDimensionSQL() {
        String query = "select id from tb_dimension_date where year =? and month =? and day =? order by id;";
        String insert ="insert into tb_dimension_date(year,month,day) values (?,?,?);";
     return  new String[]{query,insert};
    }

    /**
     * LRUCACHE中缓存的键值对形式例如：<date_dimension20170820, 3>
     *
     * @param dimension
     * @return
     */
    private String genCacheKey(BaseDimension dimension) {
        StringBuilder sb = new StringBuilder();
        if (dimension instanceof  DateDimension){
            DateDimension dateDimension = (DateDimension) dimension;

            //拼装缓存id对应的key
            sb.append("date_dimension");

            sb.append(dateDimension.getYear()).append(dateDimension.getMonth()).append(dateDimension.getDay());

        }else if (dimension instanceof ContactDimension){

            ContactDimension contactDimension =(ContactDimension)dimension;

            //拼装缓存id对应的key

            sb.append("contactDimension");

            sb.append(contactDimension.getTelephone()).append(contactDimension.getName());

        }

        if (sb.length() <= 0) throw new RuntimeException("Cannot create cachekey."+ dimension);
            return sb.toString();

    }




}
