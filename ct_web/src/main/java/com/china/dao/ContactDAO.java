package com.china.dao;

import com.china.bean.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ContactDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Contact> getContacts(){
        String sql = "SELECT id, telephone, name FROM tb_contacts;";
        BeanPropertyRowMapper<Contact> contactBeanPropertyRowMapper = new BeanPropertyRowMapper<>(Contact.class);
        List<Contact> contactList = jdbcTemplate.query(sql, contactBeanPropertyRowMapper);
        return contactList;
    }

    public List<Contact> getContactWithId(HashMap<String, String> hashMap){
        String sql = "SELECT id, telephone, name FROM tb_contacts WHERE id = :id;";
        BeanPropertyRowMapper<Contact> contactBeanPropertyRowMapper = new BeanPropertyRowMapper<>(Contact.class);
        List<Contact> contactList = namedParameterJdbcTemplate.query(sql, hashMap, contactBeanPropertyRowMapper);
        return contactList;
    }
}
