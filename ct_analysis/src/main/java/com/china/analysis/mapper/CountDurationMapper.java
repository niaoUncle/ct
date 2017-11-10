package com.china.analysis.mapper;



import com.china.analysis.kv.impl.ComDimension;
import com.china.analysis.kv.impl.ContactDimension;
import com.china.analysis.kv.impl.DateDimension;

import com.china.constants.InitContacts;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;


import java.io.IOException;
import java.util.Map;

public class CountDurationMapper extends TableMapper<ComDimension, Text> {

    //存放联系人电话与姓名的映射
    private Map<String, String> concacts;

    private byte[] family = Bytes.toBytes("f1");

    //综合维度
    private ComDimension comDimension = new ComDimension();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //存放联系人电话与姓名的映射
        concacts= InitContacts.getContact();
    }

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
    //        super.map(key, value, context);
        //获取数据
        //01_15837312345_20170810141024_13738909097_1_0180
        String rowKey = Bytes.toString(value.getRow()); //空指针异常前写法value.getRow();

        //切片后为一个字符数组
        String[] splits = rowKey.split("_");

        String flag =splits[4];

        //如果不是主叫数据直接返回
        if (StringUtils.equals(flag,"0"))return;

        //只拿到主叫数据即可
        String call1 = splits[1];
        String call2 = splits[3];
        String dateTime = splits[2];
        String duration = splits[5];

        //处理时间维度//2017  08  10  141024
        int year = Integer.valueOf(dateTime.substring(0,4));
        int month = Integer.valueOf(dateTime.substring(4,6));
        int day = Integer.valueOf(dateTime.substring(6,8));

        DateDimension dateDimensionYear = new DateDimension(year, -1, -1);
        DateDimension dateDimensionMonth = new DateDimension(year, month, -1);
        DateDimension dateDimensionDay = new DateDimension(year, month,day);

        ///************************************************************************************//
        //第一个电话号码  contactDimension_er联系人维度
        //空指针异常是name无法现实，那么是空的
        ContactDimension contactDimension_er = new ContactDimension(call1, concacts.get(call1));

        //把联系人维度添加到综合维度
        comDimension.setContactDimension(contactDimension_er);

        //设置时间维度 _年
        comDimension.setDateDimension(dateDimensionYear);
        //通过context上下文写出 以联系人和年组成的综合维度
        context.write(comDimension, new Text(duration) );

        //设置时间维度 _月
        comDimension.setDateDimension(dateDimensionMonth);
        //通过context上下文写出 以联系人和年组成的综合维度
        context.write(comDimension, new Text(duration) );

        //设置时间维度 _日
        comDimension.setDateDimension(dateDimensionDay);
        //通过context上下文写出 以联系人和年组成的综合维度
        context.write(comDimension, new Text(duration) );


        ///************************************************************************************//
        //第二个人
        //被叫电话号码  contactDimension_er联系人维度
        ContactDimension contactDimension_ee = new ContactDimension(call2,concacts.get(call2));

        //把联系人维度添加到综合维度
        comDimension.setContactDimension(contactDimension_ee);

        //设置时间维度 _年
        comDimension.setDateDimension(dateDimensionYear);
        //通过context上下文写出 以联系人和年组成的综合维度
        context.write(comDimension, new Text(duration) );

        //设置时间维度 _月
        comDimension.setDateDimension(dateDimensionMonth);
        //通过context上下文写出 以联系人和年组成的综合维度
        context.write(comDimension, new Text(duration) );

        //设置时间维度 _日
        comDimension.setDateDimension(dateDimensionDay);
        //通过context上下文写出 以联系人和年组成的综合维度
        context.write(comDimension, new Text(duration) );



    }
}

