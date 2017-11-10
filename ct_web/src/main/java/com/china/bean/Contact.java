package com.china.bean;

public class Contact {
    private Integer id;
    private String telephone;
    private String name;

    public Contact() {
        super();
    }

    public Contact(int id, String telephone, String name) {
        super();
        this.id = id;
        this.telephone = telephone;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", telephone='" + telephone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
