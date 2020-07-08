package com.example.demo.DAO;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.example.demo.Model.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import net.minidev.json.JSONObject;

public class ProductsService {
	
	
    public static final String COL_NAME="Product";

	// Product List
    public String Get_Product_List() throws InterruptedException, ExecutionException, IOException 
    {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
    	//asynchronously retrieve multiple documents
    	ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).get();
    	// future.get() blocks on response
    	List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    	
    	List<Products> product_list = new ArrayList<Products>();
    	
    	for (DocumentSnapshot document : documents) {
    	  Products sample_product = document.toObject(Products.class);
    	  System.out.println(sample_product);
    	  product_list.add(sample_product);
    	}
    	JSONObject obj = new JSONObject();
        obj.put("status","success");
        obj.put("error_type","none");
    	obj.put("Product_List",product_list);
    	StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
    	
        System.out.println("Return Data => " + jsonText);
        return jsonText;
    }
    
    // Create Product
    public String Create_Product(String uniqueID,String name, String detail, String brand, String category, int price,int product_image_count, int product_quantity) throws InterruptedException, ExecutionException {
    	
    	Products new_product = new Products(uniqueID,name,detail,brand,category,price,product_image_count,product_quantity);
    	
		Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(new_product.getId()).set(new_product);
        return collectionsApiFuture.get().getUpdateTime().toString();
	}
	
    // Get Search Product
    public String Get_Search_Product(String search_text) throws InterruptedException, ExecutionException, IOException
    {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
    	//asynchronously retrieve multiple documents
    	ApiFuture<QuerySnapshot> future = dbFirestore.collection(COL_NAME).get();
    	// future.get() blocks on response
    	List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    	
    	List<Products> product_list = new ArrayList<Products>();
    	for (DocumentSnapshot document : documents) {
    	  Products sample_product = document.toObject(Products.class);
    	  if(sample_product.getName().toLowerCase().contains(search_text.toLowerCase())|| sample_product.getCategory().toLowerCase().contains(search_text.toLowerCase())
    			  || sample_product.getBrand().toLowerCase().contains(search_text.toLowerCase())) {
    		  System.out.println(sample_product.brand);
    		  product_list.add(sample_product);
    	  }
    	  else {
    		  System.out.println("Nothing Match!");
    	  }
    	  
    	}
    	JSONObject obj = new JSONObject();
        obj.put("status","success");
        obj.put("error_type","none");
    	obj.put("Product_List",product_list);
    	StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
    	
        System.out.println("Return Data => " + jsonText);
        return jsonText;
    	
    }
    // Get Specific Name
    public String Get_Specific_Product(String product_id) throws InterruptedException, ExecutionException, IOException
    {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(product_id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        Products specific_product = null;

    	String reply_data = "";
    	
        if(document.exists()) 
        {    
        	specific_product = document.toObject(Products.class);
            reply_data = Success_JSON(specific_product);

        }
        else 
        {
        	reply_data = Fail_JSON("no_product");
        }
        return reply_data;
    }
    //Payment
    public String Check_Out(String token_id,String amount) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, IOException
    {
    	String jsonText = "";
        String TEST_STRIPE_SECRET_KEY = "sk_test_51GvJJbDyK93lH1aLg39xaXARMOUI7fpGXpldyvDYD4QoD5fgvTqs3M0swLYploOQytvzNXgHyxjfx4DhbQASIm5t00OmeMPzbU";
    	Stripe.apiKey = TEST_STRIPE_SECRET_KEY;
    	
    	Map<String, Object> chargeParams = new HashMap<String, Object>();
    	chargeParams.put("amount", Integer.parseInt(amount)*100);
    	chargeParams.put("currency", "sgd");
    	chargeParams.put("description", "Monthly Charges");
    	chargeParams.put("card", token_id);
    	
    	try {
    		  // Submit charge to credit card 
    		  System.out.println("Making Payment!");
    		  Charge charge = Charge.create(chargeParams);
    	      System.out.println("CHARGE: " + charge);
    	      JSONObject obj = new JSONObject();
    	      obj.put("status","success");
    	      obj.put("error_type","none");
    	      StringWriter out = new StringWriter();
    	      obj.writeJSONString(out);
    	      jsonText = out.toString();
    	      System.out.println("Reply Data =>" + jsonText);
    	      return jsonText;
    	    } catch (CardException e) {
    		  // Transaction was declined
    		  System.out.println("Status is: " + e.getCode());
    		  System.out.println("Message is: " + e.getMessage());
    		  JSONObject obj = new JSONObject();
    	      obj.put("status","fail");
    	      obj.put("error_type","err");
    	      StringWriter out = new StringWriter();
    	      obj.writeJSONString(out);
    	      jsonText = out.toString();
    	      System.out.println("Reply Data =>" + jsonText);
    	      return jsonText;
    		} catch (RateLimitException e) {
    			  // Too many requests made to the API too quickly
    		} catch (InvalidRequestException e) {
    		  // Invalid parameters were supplied to Stripe's API
    	    } catch (AuthenticationException e) {
    		  // Authentication with Stripe's API failed (wrong API key?)
    		} catch (APIConnectionException e) {
    		  // Network communication with Stripe failed
    		 } catch (StripeException e) {
    		  // Generic error
    		} catch (Exception e) {
    		  // Something else happened unrelated to Stripe
    		}	
    	System.out.println(jsonText);
    	return jsonText;
    }
    
    
    public String Success_JSON(Products content_product) throws IOException
    {
    	JSONObject obj = new JSONObject();


        obj.put("status","success");
        obj.put("error_type","none");
        obj.put("name",content_product.getName());
        obj.put("detail",content_product.getDetail());
        obj.put("brand",content_product.getBrand());
        obj.put("category",content_product.getCategory());
        obj.put("id",content_product.getId());
        obj.put("price",content_product.getPrice());
        obj.put("image_count", content_product.getImage_count());
        obj.put("quantity", content_product.getQuantity());
        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
        return jsonText;
    }
    public String Fail_JSON(String err_type) throws IOException
    {
    	JSONObject obj = new JSONObject();
        obj.put("status","fail");
        obj.put("error_type",err_type);
        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
        return jsonText;
    }
}
