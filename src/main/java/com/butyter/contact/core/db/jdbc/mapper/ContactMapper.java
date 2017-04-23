package com.butyter.contact.core.db.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.butyter.contact.core.db.model.Contact;



public class ContactMapper implements RowMapper<Contact> {

    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id"));
        contact.setFirstName(rs.getString("firstname"));
        contact.setLastName(rs.getString("lastname"));
        contact.setPhone(rs.getString("phone"));
        return contact;
    }
   
}
