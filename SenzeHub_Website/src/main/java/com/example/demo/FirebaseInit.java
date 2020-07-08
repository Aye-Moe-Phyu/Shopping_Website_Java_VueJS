package com.example.demo;

import java.io.FileInputStream;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


@Service
	public class FirebaseInit {

	    @PostConstruct
	    public void initialize() {
	        try {
	        	FileInputStream serviceAccount = new FileInputStream("/Users/asus/Spring CA/SenzeHub_Website/src/main/resources/ecommerce-907bd-firebase-adminsdk-zsz99-ab20845ab4.json");
	        	FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                  .setDatabaseUrl("https://ecommerce-907bd.firebaseio.com/user").setStorageBucket("ecommerce-907bd.appspot.com").build();
	                FirebaseApp.initializeApp(options);
	                                	  
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	}

