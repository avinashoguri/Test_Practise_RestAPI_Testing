import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
import files.ReUsable;
import groovy.transform.EqualsAndHashCode;

public class LibreryBookMGNTDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Validating if add place api 

		//Given : all input details
		//When : Submit Api resource/HttP request
		//Then : Validate the responce

		RestAssured.baseURI="https://rahulshettyacademy.com";

		//Add place
		String addRes=given().log().all().queryParam("KEY", "qaclick123").header("Content-Type","application/json")
				.body(Payload.addPlace()).when().post("/maps/api/place/add/json")
				.then().log().all().statusCode(200)
				.body("scope", equalTo("APP"))
				.header("Server","Apache/2.4.18 (Ubuntu)")
				.extract().response().asString();

		System.out.println(addRes);

		JsonPath jp=new JsonPath(addRes);
		String placeId=jp.getString("place_id");


		System.out.println(placeId);

		//Update place

		String newAddress="T8 JPnage, 9th phase";
		given().log().all().queryParam("KEY", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}")
		.when().put("/maps/api/place/update/json")
		.then().log().all().statusCode(200)
		.body("msg",equalTo("Address successfully updated"));

		//Get place

		String getRes=RestAssured
				.given().log().all().queryParam("key", "qaclick123")
				.queryParam("place_id",placeId )
				.when().get("/maps/api/place/get/json")
				.then().log().all().statusCode(200)
				.body("address",equalTo(newAddress))
				.extract().response().asString();

		System.out.println(getRes);

		JsonPath js=ReUsable.rawToJson(getRes);
		//JsonPath js=new JsonPath(getRes);
		String updatedAdderess=js.getString("address");

		Assert.assertEquals(updatedAdderess, newAddress);
		System.out.println(updatedAdderess);


	}

}
