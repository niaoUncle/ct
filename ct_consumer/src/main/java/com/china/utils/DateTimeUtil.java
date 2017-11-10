package com.china.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtil {

    /**
     * 1、根据传入的开始与结束时间，确定每一组扫描rowkey范围
     * 2、判断当前组是否为最后一组
     */
        //2017-01-01 2017-05-01
        private String call;
        private String startPoint;
        private String stopPoint;

        //格式化设置
        private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");

        //获取配置中的分区数
        private int regions = Integer.valueOf(PropertiesUtil.getproperties("hbase.regions.count"));

        //用于存放每一组rowkey区间
        private List<String[]> list;

        //一个全局索引
        private int index;

        //构造器
        public DateTimeUtil(String call, String startDatePoint, String stopDatePoint) {
            this.index = 0;
            this.call = call;
            this.startPoint = startDatePoint;
            this.stopPoint = stopDatePoint;
            list = new ArrayList<>();
            init();//根据参入的参数格式化时间，并生成  “rowkey”
        }

    /**
     * regionHash_158373123456_20170501000000
     * regionHash_158373123456_20170601000000
     */

    private void init() {
        try {
            //把传如的字符串的时间格式化为日期格式
            Date startPointDate = sdf1.parse(startPoint);
            Date stopPointDate = sdf1.parse(stopPoint);


            //获取一个日期对象 startCalendar
            Calendar startCalendar = Calendar.getInstance();
            //为日期对象设置一个开始时间
            startCalendar.setTime(startPointDate);

            //获取一个日期对象 “curStartCalendar  curStopCalendar“
            Calendar curStartCalendar = Calendar.getInstance();
            Calendar curStopCalendar = Calendar.getInstance();

            //当前日期对象 “curStartCalendar ”开始时间
            curStartCalendar.setTime(startPointDate);

            //设置curStopCalendar 的结束时间为开始时间从加一个月，= 结束时间
            curStopCalendar.setTime(startPointDate);

            //当前结束时间 curStopCalendar 为开始时间加1个月
            curStopCalendar.add(Calendar.MONTH, 1);

            //如果当前结束时间小于最后的结束时间（用户查询的最后时间）则进入循环
            while (curStopCalendar.getTimeInMillis() <= stopPointDate.getTime()){

                String currStartPoint = sdf2.format(new Date(curStartCalendar.getTimeInMillis()));
                //生成分区号的字符串
                String regionHash = HBaseUtil.genPartitionCode(call, currStartPoint, regions);

                String[] arrRow = new String[2];

                //查询范围的开始rowkey
                arrRow[0] = regionHash + "_" + call + "_" + sdf2.format(curStartCalendar.getTime());

                //查询范围的结束rowkey
                arrRow[1] = regionHash + "_" + call + "_" + sdf2.format(curStopCalendar.getTime());

                list.add(arrRow);//添加到rowkey数组中

                //     并移动到下一个查寻时间段
                curStartCalendar.add(Calendar.MONTH, 1);
                curStopCalendar.add(Calendar.MONTH, 1);


            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否还有下一组rowkey
     *
     * @return
     */
    public boolean hasNext() {
        if(index < list.size())
            return true;
        else
            return false;
    }

    /**
     * 取得下一组rowKey
     *
     * @return
     */
    public String[] next() {
        return list.get(index++);
    }


}
