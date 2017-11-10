package com.china.analysis.kv.impl;

import com.china.analysis.kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

//综合维度，关联了时间维度和联系人维度
public  class ComDimension extends BaseDimension {

    //时间维度
     private DateDimension dateDimension ;

    //联系人维度
   private ContactDimension contactDimension ;

    public ComDimension() {
        super();
        dateDimension = new DateDimension();
        contactDimension = new ContactDimension();

    }

    public ComDimension(DateDimension dateDimension, ContactDimension contactDimension) {
        super();
        this.dateDimension = dateDimension;
        this.contactDimension = contactDimension;
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    public ContactDimension getContactDimension() {
        return contactDimension;
    }

    public void setContactDimension(ContactDimension contactDimension) {
        this.contactDimension = contactDimension;
    }

    @Override
    public int compareTo(BaseDimension o) {
      if (this == o) return 0;

      ComDimension comDimension =(ComDimension) o;

      int tmp = this.dateDimension.compareTo(comDimension.getDateDimension());
      if (tmp != 0)return  tmp;

      tmp = this.contactDimension.compareTo(comDimension.getContactDimension());

    return tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComDimension that = (ComDimension) o;

        if (dateDimension != null ? !dateDimension.equals(that.dateDimension) : that.dateDimension != null)
            return false;
        return contactDimension != null ? contactDimension.equals(that.contactDimension) : that.contactDimension == null;
    }

    @Override
    public int hashCode() {
        int result = dateDimension != null ? dateDimension.hashCode() : 0;
        result = 31 * result + (contactDimension != null ? contactDimension.hashCode() : 0);
        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
       this.dateDimension.write(out);
       this.contactDimension.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.dateDimension.readFields(in);
        this.contactDimension.readFields(in);
    }


}
