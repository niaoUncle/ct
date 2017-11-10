package com.china.analysis.kv.impl;

import com.china.analysis.kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class ContactDimension extends BaseDimension {
    //数据库主键

    //姓名
    private String name;

    //手机号码
    private String telephone;

    public ContactDimension() {
        super();

    }


    public ContactDimension(String telephone, String name) {
        super();
        this.telephone = telephone;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactDimension that = (ContactDimension) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return telephone != null ? telephone.equals(that.telephone) : that.telephone == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(BaseDimension o) {
        ContactDimension contactDimension = (ContactDimension) o;

        int tmp = this.telephone.compareTo(contactDimension.getTelephone());
        if(tmp == 0){
            tmp = this.name.compareTo(contactDimension.getName());
        }
        return tmp;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.name);
        out.writeUTF(this.telephone);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.name = in.readUTF();
        this.telephone = in.readUTF();
    }

    @Override
    public String toString() {
        return "ContactDimension{" +
                "name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
