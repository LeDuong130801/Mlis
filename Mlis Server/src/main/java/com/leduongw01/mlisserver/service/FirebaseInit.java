//package com.leduongw01.mlisserver.service;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.firestore.Firestore;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//public class FirebaseInit {
//    public void init(){
//        FileInputStream serviceAccount =
//                null;
//        try {
//            serviceAccount = new FileInputStream("./serviceAccountKey.json");
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
