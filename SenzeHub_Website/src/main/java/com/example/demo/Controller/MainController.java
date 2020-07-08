package com.example.demo.Controller;

import java.io.IOException;

import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.ProductsService;
import com.example.demo.DAO.UserService;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;

@RestController
public class MainController {

public MainController()
{
}
@GetMapping("/Sign_In")
@ResponseBody
public String Log_In(@RequestParam String user_mail,@RequestParam String user_password) throws InterruptedException, ExecutionException, IOException
{
	System.out.println("Success");
	UserService user_ser = new UserService();
	String reply_data = user_ser.Sign_In(user_mail,user_password);
	
	System.out.println(reply_data);
	
	return reply_data;
}

@PostMapping("/Sign_Up")
@ResponseBody
public void Sign_Up(@RequestParam String user_mail,@RequestParam String acc_name,@RequestParam String password) throws InterruptedException, ExecutionException 
{
	System.out.println("Create User....");
	UserService user_ser = new UserService();
	user_ser.Sign_Up(user_mail,acc_name,password);
	System.out.println("Congratulation!...New User Created...");
}

@GetMapping("/Product_List")
@ResponseBody
public String Product_List() throws InterruptedException, ExecutionException, IOException
{
	System.out.println("Product list");
	ProductsService product_ser = new ProductsService();
	String reply_data = product_ser.Get_Product_List();
	
	System.out.println(reply_data);
	
	return reply_data;
}

@GetMapping("/Detail_Product")
@ResponseBody
public String Detail_Product(@RequestParam String product_detail_id) throws InterruptedException, ExecutionException, IOException
{
	System.out.println("Detail Product");
	ProductsService product_ser = new ProductsService();
	String reply_data = product_ser.Get_Specific_Product(product_detail_id);
	
	System.out.println(reply_data);
	
	return reply_data;
}

@GetMapping("/Product_Name")
public String Get_Specific_Product(@RequestParam("product_id") String product_id) throws InterruptedException, ExecutionException, IOException
{
	System.out.println("--- Product Name ---");
	ProductsService product_ser = new ProductsService();
	String backend_data = product_ser.Get_Specific_Product(product_id);
	System.out.println("Reply_Data");
	System.out.println(backend_data);
	return backend_data;
}


@GetMapping("/Search_Product")
@ResponseBody
public String Get_Search_Product(@RequestParam String search_product) throws InterruptedException, ExecutionException, IOException 
{
	System.out.println("--- Search Product ---");
	ProductsService pro_service = new ProductsService();
	
	String reply_data = pro_service.Get_Search_Product(search_product);
	
	System.out.println("Reply_data");
	System.out.println(reply_data);
	return reply_data;
	
}

@PostMapping("/Create_Product")
public void Create_Product(@RequestParam("product_image_count") String product_image_count,@RequestParam("product_id") String product_id,@RequestParam("product_name") String product_name,@RequestParam("product_detail") String product_detail,@RequestParam("product_brand") String product_brand,@RequestParam("product_category") String product_category,@RequestParam("product_price") String product_price,@RequestParam("product_quantity") String product_quantity) throws InterruptedException, ExecutionException, NumberFormatException, IOException 
{
	System.out.println("Create Product....");
	ProductsService product_ser = new ProductsService();
	String create_status = product_ser.Create_Product(product_id,product_name.toString(),product_detail.toString(),product_brand.toString(),product_category.toString(),Integer.parseInt(product_price.toString()),Integer.parseInt(product_image_count),Integer.parseInt(product_quantity));
	System.out.println("Congratulation!" + product_name + " is registered in online-store!");
}
@GetMapping("/Charge")
public void Charge_Total_Cost(@RequestParam("token") String token_id, @RequestParam("amount") String amount) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, IOException
{
System.out.println("--- Charge ---");
System.out.println(token_id);
ProductsService product_ser = new ProductsService();
String create_status = product_ser.Check_Out(token_id,amount);

}
}