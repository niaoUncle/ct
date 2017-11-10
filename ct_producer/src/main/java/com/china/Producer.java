package com.china;



import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Producer {



    //生产数据;     call1       call2            data_time              date_time_ts        duration flag
    //用于存放电话号码，准备随机产生数据使用
    private List<String> phoneList = null;

    //存放电话号码与 联系人姓名的映射，置于Map集合中
    private Map<String, String> contacts = null;


    /**
     * 该方法用于获取phoneList和contacts这两个集合
     */
    public void initContacts() {

        phoneList =InitDate.getPhoneList();
        contacts = InitDate.getContacts();

    }

    //随机生成两个电话号码
    private String producerLog() {
        String call1 = null;
        String call2 = null;

        //生成主叫电话号码
        call1 = phoneList.get((int) (Math.random() * phoneList.size()));

        while (true) {
            //生成被叫电话号码
            call2 = phoneList.get((int) (Math.random() * phoneList.size()));
            if (!call1.equals(call2)) break;

        }

        //  通话建立时间:yyyy-MM-dd,月份：0~11，天：1~31
        String data_time = randomDate("2016-01-01", "2017-12-31");



        //生成通话持续时间[1, 1201)的整数 单位：秒
        int dura = (int) ((Math.random() * 60 * 20) + 1);

        //格式化通话时长，保证每个时间都是4位的
        DecimalFormat format = new DecimalFormat("0000");

        String duration = format.format(dura);

        //这个标记可以省略，生产的数据都是主叫数据就可以了
        // String flag = "1";

        String result =call1 + "," + call2 + "," + data_time + "," + duration +"\r\n";

        System.out.print(result); //用于测试查看数据

        return result;
    }

    /**
     * 根据传入的范围，在该时间范围 生成通话 建立的时间点
     *
     * @param startDate 开始时间
     * @param stopDate  结束时间
     * @return 2017-05-24 09:18:20
     */
    private String randomDate(String startDate, String stopDate) {

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf1.parse(startDate);
            Date stop = sdf1.parse(stopDate);

            //判断时间合法性
            if (start.getTime() >= stop.getTime()) return null;

            //起始时间时间戳 + random(结束时间时间戳 - 起始时间时间戳) = 随出来的时间戳
            long point = start.getTime() + (long) (Math.random() * (stop.getTime() - start.getTime()));

            //格式化日期
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = sdf2.format(point);

            return result;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 将产生的日志写入到本地文件calllog中
     */
    private void writeLog(String filePath){

        OutputStreamWriter outputStreamWriter = null;

        try{

            //true表示追加，false表示覆盖
          OutputStream outputStream  = new FileOutputStream(filePath,false);

          //使用OutputStreamWriter可以设置编码格式化
            outputStreamWriter= new OutputStreamWriter(outputStream,"UTF-8");

            while (true){

                String log = producerLog();

                try {

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                outputStreamWriter.write(log);//将产生的通话日志写出去

                outputStreamWriter.flush();//刷新及时写出
            }

        }catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args ==null || args.length <=0){

            System.out.println("non args!!!!");

            return;
        }
        Producer producer = new Producer();

        producer.initContacts(); //初始化通话日志

        producer.writeLog(args[0]); //将日子写出

    }
}
