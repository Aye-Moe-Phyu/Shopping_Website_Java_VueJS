package com.example.demo;


import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.Model.*;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Component
public class MyRunner implements CommandLineRunner {
    public static final String COL_NAME="User";
	private static final Logger logger = LoggerFactory.getLogger(SenzeHubWebsiteApplication.class);
    
    public void run(String... args) throws Exception {
    	System.out.println("Initialize Firebase db..");
    	
    	User new_user = new User("Mee Mee","meemee1996@gmail.com","PhyuPhyu@151996");
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(new_user.getAccount()).set(new_user);
        
        new_user = new User("Aye Moe Phyu","ayemoephyu151996@gmail.com","PhyuPhyu@151996");
        dbFirestore = FirestoreClient.getFirestore();
        collectionsApiFuture = dbFirestore.collection(COL_NAME).document(new_user.getAccount()).set(new_user);
        
        System.out.println("Complete Database Initialization!");
        
    }
    
}