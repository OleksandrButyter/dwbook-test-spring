
package com.butyter.contact.core.db.dao;

import java.util.List;

import com.butyter.contact.core.db.model.Contact;

public interface ContactDAO {

    List<Contact> getAllContacts();

    Contact getContactById(int id);

    void deleteContact(int id);

    int createContact(Contact contact);

    void updateContact(Contact contact);
}
