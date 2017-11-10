package com.china.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


//用于创建生成的properties对象
public class PropertiesUtil {

    public  static Properties properties = null;

    static{
        // 加载配置属性
        try {
            //以 流的方式读取配置文件
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("kafka.properties");

            //创建一个配置文件对象
            properties = new Properties();

            //对象加载配置文件的流
            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回配置信息，根据属性名获取属性值
    public static String getproperties(String key){

        return properties.getProperty(key);
    }


}
