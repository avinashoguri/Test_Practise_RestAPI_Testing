import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import files.Payload;
import files.ReUsable;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class StaticJson {


	@Test
	public static void addBook() throws IOException {
		RestAssured.baseURI="http://216.10.245.166";
		String responce=given().log().all().queryParam("Content-Type","application/json")
				.body(GenerateStringFromResource("C:\\Users\\Malempati Parvathi\\Desktop\\"
						+ "original\\Test_Practise_RestAPI_Testing\\src\\test\\java\\files\\addbook.json")).
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
	//
	public static String GenerateStringFromResource(String path) throws IOException {

		return new String(Files.readAllBytes(Paths.get(path)));

	}
}
