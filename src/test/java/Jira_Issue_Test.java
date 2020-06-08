import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import sun.swing.SwingUtilities2.Section;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.params.AllClientPNames;
import org.testng.Assert;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Filter.Chain;

public class Jira_Issue_Test {

	public static void main(String[] args) {
		RestAssured.baseURI="http://localhost:8080";


		SessionFilter section=new SessionFilter();
		String username="admin";
		String password="admin";
		String Key="10028";
		String updateComment="yes, I am updated";




		//Login JIRA
		String responce=given().log().all().header("Content-Type","application/json")
				.body("{\r\n" + 
						"    \"username\": \""+username+"\",\r\n" + 
						"    \"password\": \""+password+"\"\r\n" + 
						"}")
				.filter(section).when().post("/rest/auth/1/session")
				.then().assertThat().log().all().statusCode(200)
				.extract().response().asString();

		//Create issue

		/*

		String responce1=given().log().all().header("Content-Type","application/json")
				.body("{\r\n" + 
						"    \"fields\": {\r\n" + 
						"       \"project\":\r\n" + 
						"       {\r\n" + 
						"          \"key\": \"AUT\"\r\n" + 
						"       },\r\n" + 
						"       \"summary\": \"issue update.\",\r\n" + 
						"       \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\r\n" + 
						"       \"issuetype\": {\r\n" + 
						"          \"name\": \"Bug\"\r\n" + 
						"       }\r\n" + 
						"   }\r\n" + 
						"}")
				.filter(section).when().post("/rest/api/2/issue")
				.then().assertThat().statusCode(201).extract().response().asString();


		JsonPath js1=new JsonPath(responce1);
		Key=js.get("id");
		 */

		//Updating Issue
		String responce2=given().log().all().pathParam("Key", Key).header("Content-Type","application/json")
				.body("{\r\n" + 
						"    \"body\": \""+updateComment+"\",\r\n" + 
						"    \"visibility\": {\r\n" + 
						"        \"type\": \"role\",\r\n" + 
						"        \"value\": \"Administrators\"\r\n" + 
						"    }\r\n" + 
						"}")
				.filter(section).when().post("/rest/api/2/issue/{Key}/comment")
				.then().assertThat().statusCode(201).extract().response().asString();

		JsonPath js2=new JsonPath(responce2);
		String commentId=js2.get("id");

		//Attaching File

		given().log().all().header("X-Atlassian-Token","no-check").filter(section)
		.pathParam("Key", Key)
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("C:\\Users\\Malempati Parvathi\\Desktop\\original\\Test_Practise_RestAPI_Testing\\jira.txt"))

		.when().post("/rest/api/2/issue/{Key}/attachments").then().log().all()
		.assertThat().statusCode(200);


		//get issue

		String responce3=given()
				.pathParam("Key", Key).filter(section)
				//.queryParam("fields", "comment")
				.when().get("/rest/api/2/issue/{Key}")
				.then().assertThat().statusCode(200).extract().response().asString();

		System.out.println(responce3);
		JsonPath js3=new JsonPath(responce3);
		int commentIdscount=js3.getInt("fields.comment.comments.size()");
		System.out.println(commentIdscount);
		for(int i=0;i<=commentIdscount;i++) {
			String commentIssueId=js3.getString("fields.comment.comments["+i+"].id").toString();
			System.out.println(commentIssueId);
			if(commentIssueId.equalsIgnoreCase(commentId)) {
				String commentMessage=js3.getString("fields.comment.comments["+i+"].body").toString();
				System.out.println(commentMessage);
				Assert.assertEquals(commentMessage, updateComment);
			}

		}


	}
}
