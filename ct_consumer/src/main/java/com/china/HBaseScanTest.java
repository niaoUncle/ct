package com.china;


import com.china.utils.DateTimeUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;



public class HBaseScanTest {
    private static Configuration conf = null;
    static {
        conf = HBaseConfiguration.create();
    }

    @Test
    public  void scanTest() throws Exception{

        String call = "13407209608";

        String startPoint = "2016-01-01";
        String stopPoint = "2017-12-30";

        //获取表
        HTable hTable = new HTable(conf,"ns_telecom:calllog");

        //获取扫描对象
        Scan scan = new Scan();

        //根据传入的开始与结束时间，确定每一组扫描rowkey范围的对象
        DateTimeUtil dateTimeUtil = new DateTimeUtil(call, startPoint, stopPoint);

        //如何有下一个就循环
        while(dateTimeUtil.hasNext()){


            //取得下一组rowKey 是一个字符数组
            String[] rowKey = dateTimeUtil.next();

            //为scan对象设置扫描开始时间
            scan.setStartRow(Bytes.toBytes(rowKey[0]));

            //为scan对象设置扫描结束时间
            scan.setStopRow(Bytes.toBytes(rowKey[1]));

            System.out.println("时间范围" + rowKey[0].substring(15, 21)
                                    + "---------------------------"
                                         + rowKey[1].substring(15, 21));

            //获取一个扫描结果集
            ResultScanner results = hTable.getScanner(scan);

            //每一个rowkey对应一个result
            for (Result result:results ) {
                //每一个rowkey里面包含多个cell
                Cell[] cells = result.rawCells();

                StringBuilder sb = new StringBuilder();

                sb.append(Bytes.toString(result.getRow())).append(",");

                for (Cell c :cells ) {
                        sb.append(Bytes.toString(CellUtil.cloneValue(c))).append(",");
                }
                System.out.println(sb.toString());
            }

        }

    }
}
