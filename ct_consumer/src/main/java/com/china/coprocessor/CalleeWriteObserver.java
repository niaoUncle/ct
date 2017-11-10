package com.china.coprocessor;

import com.china.utils.HBaseUtil;
import com.china.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class CalleeWriteObserver extends BaseRegionObserver {

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        super.postPut(e, put, edit, durability);

        //1、获取需要操作的表
        String targerTableName = PropertiesUtil.getproperties("hbase.table.name");

        //2、获取当前操作的表
        String currrentTableName = e.getEnvironment().getRegion().getRegionInfo().getTable().getNameAsString();


        //3、判断需要操作的表是否就是当前表，如果不是，则return
        if (!StringUtils.equals(targerTableName,currrentTableName)) return;

        //4、得到当前插入数据的值并封装新的数据，oriRowkey举例：
        String oriRowKey = Bytes.toString(put.getRow());

        String[] split = oriRowKey.split("_");

        String flag = split[4];

        //如果当前插入的是被叫数据，则直接返回(因为默认提供的数据全部为主叫数据)
        if (StringUtils.equals(flag,"0")) return;

        //当前插入的数据描述
        String caller = split[1];

        String callee = split[3];

        String dateTime = split[2];

        String duratoin = split[5];

        String timestamp = null;

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

            timestamp = String.valueOf(sdf.parse(dateTime).getTime());

        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        //组装新的数据所在分区号
        int region = Integer.valueOf(PropertiesUtil.getproperties("hbase.regions.count"));

         String regionHashCode =HBaseUtil.genPartitionCode(callee,dateTime,region);

         //没有差不进去数据
        String newFlag = "0";

        String rowKey = HBaseUtil.genRowKey(regionHashCode,callee,dateTime,caller,newFlag,duratoin);

        //开始存放被叫数据
        Put newPut = new Put(Bytes.toBytes(rowKey));

         newPut.add(Bytes.toBytes("f2"), Bytes.toBytes("call1"), Bytes.toBytes(callee));
         newPut.add(Bytes.toBytes("f2"), Bytes.toBytes("call2"), Bytes.toBytes(caller));
         newPut.add(Bytes.toBytes("f2"), Bytes.toBytes("date_time"), Bytes.toBytes(dateTime));
         newPut.add(Bytes.toBytes("f2"), Bytes.toBytes("date_time_ts"), Bytes.toBytes(timestamp));
         newPut.add(Bytes.toBytes("f2"), Bytes.toBytes("duration"), Bytes.toBytes(duratoin));
         newPut.add(Bytes.toBytes("f2"), Bytes.toBytes("flag"), Bytes.toBytes(newFlag));

         //获取表对象 多了TableName.META_TABLE_NAME.valueOf
        HTableInterface hTable = e.getEnvironment().getTable(TableName.valueOf(targerTableName));

        //插入数据
        hTable.put(newPut);

        hTable.close();

    }
}
