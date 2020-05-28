package files;
import files.*;
import org.jvnet.staxex.StAxSOAPBody.Payload;
import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {


		JsonPath js=new JsonPath(files.Payload.courscePrize());
		//Print No of courses returned by API
		int course=js.getInt("courses.size()");
		
		System.out.println(course);
		
		//Print Purchase Amount
		
		int purchaseAmount=js.getInt("dashboard.purchaseAmount");
		
		System.out.println(purchaseAmount);
		
		//Print Title of the first course
		String titleFirst=js.get("courses[0].title");
		System.out.println(titleFirst);

		//Print All course titles and their respective Prices
		//Print no of copies sold by RPA Course
		//Verify if Sum of all Course prices matches with Purchase Amount
		
		
		String title="RPA";
		int sum=0;
		for(int i=0;i<course;i++) {
			String titleName=js.get("courses["+i+"].title");
			int price=js.get("courses["+i+"].price");
			int copies=js.get("courses["+i+"].copies");
			
			System.out.println("Title :"+titleName);
			System.out.println("Price :"+price);
			System.out.println("Price :"+copies);
			sum=sum+(price*copies);
			if(titleName.equalsIgnoreCase(title)) {
				int copys=js.getInt("courses["+i+"].copies");
				System.out.println("copyes :"+copys);
				
			}
			System.out.println("");
			
		}
		System.out.println(sum);
		Assert.assertEquals(sum, purchaseAmount);
		System.out.println("Test passed");

	}
}

