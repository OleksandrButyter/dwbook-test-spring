
package com.butyter.contact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.butyter.contact.model.Contact;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactTest {

	private static final String BASE_TARGET = "http://localhost:9000/contact";

	private static Client client;

	private static int startContactListSize;

	private static Contact contact;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		client = ClientBuilder.newClient();
		contact = new Contact(0, "TestFirstName", "TestLastName", "067-7777777");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client.close();
	}

	@Test
	public void test01GetContactList() {
		Response response = client.target(BASE_TARGET).request(MediaType.APPLICATION_JSON).get();
		List<Contact> contacts = response.readEntity(new GenericType<List<Contact>>() {
		});
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		startContactListSize = contacts.size();
		assertTrue(startContactListSize > 0);		
	}

	@Test
	public void test02CreateContact() {
		Response response = client.target(BASE_TARGET).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		contact.setId(Integer.parseInt(
				response.getLocation().getPath().substring(response.getLocation().getPath().lastIndexOf('/') + 1)));
		assertEquals(startContactListSize + 1, getContactsSize());
	}

	@Test
	public void test03UpdateContact() {
		contact.setFirstName("newFirstName");
		Response response = client.target(BASE_TARGET).path(String.valueOf(contact.getId()))
				.request(MediaType.APPLICATION_JSON).put(Entity.entity(contact, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertTrue(contact.equals(response.readEntity(Contact.class)));
	}

	@Test
	public void test04GetContactById() {
		Response response = client.target(BASE_TARGET).path(String.valueOf(contact.getId()))
				.request(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertTrue(contact.equals(response.readEntity(Contact.class)));
	}

	@Test
	public void test05DeleteContact() {
		Response response = client.target(BASE_TARGET).path(String.valueOf(contact.getId())).request().delete();
		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
		assertEquals(startContactListSize, getContactsSize());
	}

	private int getContactsSize() {
		return client.target(BASE_TARGET).request(MediaType.APPLICATION_JSON).get()
				.readEntity(new GenericType<List<Contact>>() {
				}).size();
	}

}
