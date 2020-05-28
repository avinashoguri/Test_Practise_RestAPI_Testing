import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReUsable;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.sql.Array;
import java.util.List;

public class DynamicJson {

	@Test(dataProvider="AddBookData")
	public static void addBook(String isbn,String aisle) {
		RestAssured.baseURI="http://216.10.245.166";
		String responce=given().log().all().queryParam("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle)).
		when().post("Library/Addbook.php")
		.then().log().all().assertThat().extract().response().asString();			
		
		
		
		JsonPath js=ReUsable.rawToJson(responce);
		String ID=js.get("ID");
		
		
		System.out.println("ID is :"+ID);
		
		String responce1=given().log().all().queryParam("Content-Type","application/json")
				.body("{\r\n" + 
						" \r\n" + 
						"\"ID\" : \""+ID+"\"\r\n" + 
						" \r\n" + 
						"}").
				when().post("/Library/DeleteBook.php")
				.then().log().all().assertThat().extract().response().asString();			
				
				
				
				JsonPath js1=ReUsable.rawToJson(responce1);
				String MSG=js1.get("msg");
				System.out.println(ID+MSG);
		
		
		
	}
	@DataProvider(name="AddBookData")
	public Object[][] getData() {
		
		return new Object[][] {{"klik","121"},{"qxs","00121"},{"oiipiu","0765760"}};
	
	
	
}
}
