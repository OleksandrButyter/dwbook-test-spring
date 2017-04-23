package com.butyter.contact.core.db.dao.impl.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.butyter.contact.core.db.dao.ContactDAO;
import com.butyter.contact.core.db.jdbc.mapper.ContactMapper;
import com.butyter.contact.core.db.model.Contact;


@Repository
public class JDBCContactDAO implements ContactDAO{

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public List<Contact> getAllContacts() {
        return jdbc.query("SELECT * FROM contact", new ContactMapper());
    } 
    
    public Contact getContactById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbc.queryForObject("SELECT * FROM contact WHERE id = :id", params, new ContactMapper());
    }
    
    public void deleteContact(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        jdbc.update("DELETE FROM contact WHERE id = :id", params);
    }

    public int createContact(Contact contact) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(contact);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("INSERT INTO contact (firstname, lastname, phone) VALUES (:firstName, :lastName, :phone)", params, keyHolder, new String[] { "id" });
        return keyHolder.getKey().intValue();
    }

    public void updateContact(Contact contact) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(contact);
        jdbc.update("UPDATE contact SET firstname = :firstName, lastname = :lastName, phone = :phone WHERE id = :id",
                params);
    }
    
}
