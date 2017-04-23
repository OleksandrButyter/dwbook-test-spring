package com.butyter.contact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.butyter.contact.core.db.dao.ContactDAO;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ContactsApi;
import io.swagger.client.model.ContactDTO;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@WebAppConfiguration
public class ContactSwaggerTest {

	private static final String BASE_TARGET = "http://localhost:9000";

	private static ContactsApi contactApi;

	private static int startContactListSize;

	private static ContactDTO contact;
	
	@Autowired
	private ContactDAO contactDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApiClient client = new ApiClient();
		client.setBasePath(BASE_TARGET);
		contactApi = new ContactsApi(client);
		contact = new ContactDTO();
		contact.setId(0);
		contact.setFirstName("TestFirstName");
		contact.setLastName("TestLastName");
		contact.setPhone("067-7777777");
	}

	@Test
	public void test01GetContactList() throws ApiException {
		contactDAO.deleteContact(6);
		List<ContactDTO> contacts = contactApi.getAllContacts();
		// assertEquals(Response.Status.OK.getStatusCode(),
		// response.getStatus());
		startContactListSize = contacts.size();
		assertTrue(startContactListSize > 0);
	}

	@Test
	public void test02CreateContact() throws ApiException {
		List<String> location = contactApi.createContactWithHttpInfo(contact).getHeaders().get("Location");
		contact.setId(Integer.parseInt(location.get(0).substring(location.get(0).lastIndexOf('/') + 1)));

		// Response response =
		// client.target(BASE_TARGET).request(MediaType.APPLICATION_JSON)
		// .post(Entity.entity(contact, MediaType.APPLICATION_JSON));
		// assertEquals(Response.Status.CREATED.getStatusCode(),
		// response.getStatus());
		// contact.setId(Integer.parseInt(
		// response.getLocation().getPath().substring(response.getLocation().getPath().lastIndexOf('/')
		// + 1)));
		assertEquals(startContactListSize + 1, getContactsSize());
	}

	@Test
	public void test03UpdateContact() throws ApiException {
		contact.setFirstName("newFirstName");

		// Response response =
		// client.target(BASE_TARGET).path(String.valueOf(contact.getId()))
		// .request(MediaType.APPLICATION_JSON).put(Entity.entity(contact,
		// MediaType.APPLICATION_JSON));
		// assertEquals(Response.Status.OK.getStatusCode(),
		// response.getStatus());
		assertTrue(contact.equals(contactApi.updateContact(contact.getId(), contact)));
	}

	@Test
	public void test04GetContactById() throws ApiException {
		// Response response =
		// client.target(BASE_TARGET).path(String.valueOf(contact.getId()))
		// .request(MediaType.APPLICATION_JSON).get();
		// assertEquals(Response.Status.OK.getStatusCode(),
		// response.getStatus());
		assertTrue(contact.equals(contactApi.getContactById(contact.getId())));

	}

	@Test
	public void test05DeleteContact() throws ApiException {
		// Response response =
		// client.target(BASE_TARGET).path(String.valueOf(contact.getId())).request().delete();
		// assertEquals(Response.Status.NO_CONTENT.getStatusCode(),
		// response.getStatus());
		contactApi.deleteContact(contact.getId());
		assertEquals(startContactListSize, getContactsSize());
	}

	private int getContactsSize() throws ApiException {
		return contactApi.getAllContacts().size();
	}

}
