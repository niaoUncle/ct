package com.china.dao;

import com.china.utils.PropertiesUtil;
import com.china.utils.HBaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HBaseDAO {

    private int regions;//区域
    private String nameSpace;//命名空间
    private String tableName;//表名
    private String flag; //标识是主叫还是被叫
    private SimpleDateFormat simpleDateFormat;//格式化
    private static Configuration conf  = null; //配置文件
    private HTable callLogTable; //呼叫日志表

    static {
            //在加载该类时初始化的HBase的配置信息
            conf = HBaseConfiguration.create();
    }

    //构造器
    public HBaseDAO(){
        //格式化时间
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //获取表名
        tableName = PropertiesUtil.getproperties("hbase.table.name");

        //获取分区号
        regions = Integer.valueOf(PropertiesUtil.getproperties("hbase.regions.count"));

        //获取命名空间
        nameSpace = PropertiesUtil.getproperties("hbase.namespace");

        //获取标识，根据标识判断是否是主叫
        flag = PropertiesUtil.getproperties("hbase.caller.flag");

        if(!HBaseUtil.isExistTable(conf,tableName)){

            HBaseUtil.initNamespace(conf,nameSpace);

            HBaseUtil.createTable(conf,tableName,"f1","f2");

        }

    }

    /**
     * 15596505995,17519874292,2017-03-11 00:30:19,0652
     * 将当前数据put到HTable中
     * @param log
     */
    public void put(String log){
        try {

            callLogTable = new HTable(conf, tableName);

            String[] split = log.split(",");

            String call1 =split[0];

            String call2 =split[1];

            String dateAndTime =split[2];

            String timestamp = null;

            try {

               timestamp = String.valueOf(simpleDateFormat.parse(dateAndTime).getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String date = dateAndTime.split(" ")[0].replace("-","");

            String time = dateAndTime.split(" ")[1].replace(":","");

            String duration = split[3];

            String regionHash = HBaseUtil.genPartitionCode(call1, date, regions);

            String rowKey = HBaseUtil.genRowKey(regionHash, call1, date + time, call2,flag,duration);

            Put put = new Put(Bytes.toBytes(rowKey));

            put.add(Bytes.toBytes("f1"),Bytes.toBytes("call1"), Bytes.toBytes(call1));

            put.add(Bytes.toBytes("f1"), Bytes.toBytes("call2"), Bytes.toBytes(call2));

            put.add(Bytes.toBytes("f1"),Bytes.toBytes("date_time"),Bytes.toBytes(date + time));

            put.add(Bytes.toBytes("f1"),Bytes.toBytes("date_time_ts"),Bytes.toBytes(timestamp));

            put.add(Bytes.toBytes("f1"),Bytes.toBytes("duration"),Bytes.toBytes(duration));

            put.add(Bytes.toBytes("f1"),Bytes.toBytes("flag"),Bytes.toBytes(flag));

            callLogTable.put(put);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
