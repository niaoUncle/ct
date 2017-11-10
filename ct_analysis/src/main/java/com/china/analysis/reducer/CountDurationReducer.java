package com.china.analysis.reducer;

import com.china.analysis.kv.impl.ComDimension;
import com.china.analysis.kv.impl.CountDurationValue;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


import java.io.IOException;

public class CountDurationReducer extends Reducer<ComDimension,Text,ComDimension,CountDurationValue> {

    @Override
    protected void reduce(ComDimension key, Iterable<Text> values, Context context) throws IOException, InterruptedException {


        int callSum = 0;

        int durationSum =0;

        for (Text text: values){

            //统计通话次数
            callSum++;
            //统计通话时间
            durationSum += Integer.valueOf(text.toString());
        }

        //使用通话次数  通话时间 封装成对像写出去
        CountDurationValue countDurationValue = new CountDurationValue(callSum, durationSum);

        //将统计结果写出去
         context.write(key,countDurationValue);


    }
}
