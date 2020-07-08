package com.example.demo.DAO;

import java.io.IOException;

import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import net.minidev.json.JSONObject;

//CRUD operations
	@Service
	public class UserService {

	    public static final String COL_NAME="User";

	    // Sign_Up
	    public String Sign_Up(String user_name,String acc_name,String password) throws InterruptedException, ExecutionException 
	    {
	    	User new_user = new User(user_name,acc_name,password);
	        Firestore dbFirestore = FirestoreClient.getFirestore();
	        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(new_user.getAccount()).set(new_user);
	        return collectionsApiFuture.get().getUpdateTime().toString();
	    }
	    
	    // Sign_In
	    public String Sign_In(String mail_acc,String password) throws InterruptedException, ExecutionException, IOException 
	    {
	    	System.out.println("----- Sign In -----");
	    	System.out.println("Input Mail Account: \t" + mail_acc);
	    	System.out.println("Input Password: \t" + password);
	    	
	    	String reply_data = "";
	        Firestore dbFirestore = FirestoreClient.getFirestore();
	        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(mail_acc);
	        ApiFuture<DocumentSnapshot> future = documentReference.get();

	        DocumentSnapshot document = future.get();

	        User user = null;
	        
	        if(document.exists()) 
	        {    
	            user = document.toObject(User.class);
	            
	            System.out.println("Real Password:    \t "+ user.getPassword());
	            
	            if(user.getPassword().equals(password))
	            {
	            	 reply_data = Success_JSON(user);
	            }
	            else
	            {
	            	reply_data = Fail_JSON("wrong_password");
	            }
	            }
	        else 
	        {
	        	reply_data = Fail_JSON("no_user");
	        }
	        
	        return reply_data;
	    }

	    public String updateuserDetails(User person) throws InterruptedException, ExecutionException {
	        Firestore dbFirestore = FirestoreClient.getFirestore();
	        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(person.getName()).set(person);
	        return collectionsApiFuture.get().getUpdateTime().toString();
	    }

	    public String deleteuser(String name) {
	        Firestore dbFirestore = FirestoreClient.getFirestore();
	        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(name).delete();
	        return "Document with user ID "+name+" has been deleted";
	    }
	    
	    
	    
	    public String Success_JSON(User content_user) throws IOException
	    {
	    	JSONObject obj = new JSONObject();

	        obj.put("status","success");
	        obj.put("error_type","none");
	        obj.put("mail",content_user.getAccount());
	        obj.put("name",content_user.getName());
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
