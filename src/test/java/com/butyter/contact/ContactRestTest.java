package com.butyter.contact;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.butyter.contact.model.Contact;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactRestTest {

	private static Contact contact;

	private static int startContactListSize;

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:9000";
		RestAssured.basePath = "contact";
		contact = new Contact(0, "TestFirstName", "TestLastName", "067-7777777");
	}

	@Test
	public void test01GetContactList() {
		Response response = given().when().get("/").then().statusCode(Status.OK.getStatusCode()).extract().response();
		Contact[] contacts = response.getBody().as(Contact[].class);
		startContactListSize = contacts.length;
		assertTrue(startContactListSize > 0);
	}

	@Test
	public void test02CreateContact() {
		Response response = given().contentType("application/json").body(contact).when().post("/").then()
				.statusCode(Status.CREATED.getStatusCode()).extract().response();
		String location = response.header("Location");
		contact.setId(Integer.parseInt(location.substring(location.lastIndexOf("/") + 1)));
		assertEquals(startContactListSize + 1, getContactsSize());
	}

	@Test
	public void test03UpdateContact() {
		contact.setFirstName("newFirstName");
		Response response = given().contentType("application/json").body(contact).when()
				.put(String.valueOf(contact.getId())).then().statusCode(Status.OK.getStatusCode()).extract().response();
		assertTrue(contact.equals(response.getBody().as(Contact.class)));
	}

	@Test
	public void test04GetContactById() {
		Response response = given().when().get(String.valueOf(contact.getId())).then()
				.statusCode(Status.OK.getStatusCode()).extract().response();
		assertTrue(contact.equals(response.getBody().as(Contact.class)));
	}

	@Test
	public void test05DeleteContact() {

		given().when().delete(String.valueOf(contact.getId())).then().statusCode(Status.NO_CONTENT.getStatusCode())
				.extract().response();
		assertEquals(startContactListSize, getContactsSize());
	}

	private int getContactsSize() {
		return given().when().get("/").getBody().as(Contact[].class).length;
	}
}
