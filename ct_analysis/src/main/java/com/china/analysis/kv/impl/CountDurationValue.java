package com.china.analysis.kv.impl;

import com.china.analysis.kv.base.BaseValue;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CountDurationValue extends BaseValue{

    //某个维度通话次数总和
    private int callSum;

    //某个维度通话时间总和
    private int callDurationSum;

    public CountDurationValue() {
        super();
    }

    public CountDurationValue(int callSum, int callDurationSum) {
        super();
        this.callSum = callSum;
        this.callDurationSum = callDurationSum;
    }

    public int getCallSum() {
        return callSum;
    }

    public void setCallSum(int callSum) {
        this.callSum = callSum;
    }

    public int getCallDurationSum() {
        return callDurationSum;
    }

    public void setCallDurationSum(int callDurationSum) {
        this.callDurationSum = callDurationSum;
    }

    //序列化
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.callSum);
        out.writeInt(this.callDurationSum);
    }

    //反序列化
    @Override
    public void readFields(DataInput in) throws IOException {
        this.callSum= in.readInt();
       this.callDurationSum= in.readInt();
    }
}
