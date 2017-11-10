package com.china.analysis.kv.impl;

import com.china.analysis.kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 时间维度类
 */
public class DateDimension extends BaseDimension{

    //时间维度主键
    private int id;

    //时间维度：当前通话信息所在年
    private int year;

    //时间维度：当前通话信息所在月,如果按照年来统计信息，则month为-1
    private int month;

    //时间维度：当前通话信息所在日,如果按照年来统计信息，则day为-1。
    private int day;

    public DateDimension() {
        super();
    }

    public DateDimension(int year, int month, int day) {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateDimension that = (DateDimension) o;

        if (id != that.id) return false;
        if (year != that.year) return false;
        if (month != that.month) return false;
        return day == that.day;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    //实现多级比较
    @Override
    public int compareTo(BaseDimension o) {
        if (o == this) return 0;
        DateDimension dateDimension = (DateDimension)o;

        int tmp = Integer.compare(this.id ,dateDimension.getId());
        if (tmp != 0)return  tmp;

        tmp = Integer.compare(this.year,dateDimension.getYear());
        if (tmp != 0) return  tmp;

        tmp = Integer.compare(this.month,dateDimension.getMonth());
        if (tmp != 0) return  tmp;

        tmp=   Integer.compare(this.day,dateDimension.getDay());

        return tmp ;

        ///空指针异常前写法如下
       /* DateDimension dateDimension =(DateDimension)o;

        int tmp = this.year - dateDimension.getYear();
        if (tmp != 0) return tmp;

        tmp = this.month - dateDimension.getMonth();
        if (tmp != 0) return tmp;

        tmp = this.day - dateDimension.getDay();
        return tmp;*/

        /*if(this.year == dateDimension.getYear() ){
            if(this.month == dateDimension.getMonth()){
                return this.day - dateDimension.getDay();
            }else {
                return this.month - dateDimension.getMonth();
            }
        }else {
            return this.year - dateDimension.getYear();
        }*/
    }

    //序列化
    @Override
    public void write(DataOutput out) throws IOException {

        out.writeInt(this.year);
        out.writeInt(this.month);
        out.writeInt(this.day);

    }

    //反序列化
    @Override
    public void readFields(DataInput in) throws IOException {

        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();

    }

    @Override
    public String toString() {
        return "DateDimension{" +

                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
