package com.fawwazkhayyat.pricefinder;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.Arrays;
import java.util.List;

public class AppFirestoreDatabase {
    private final String PATH_COLLECTION_STORES = "stores";
    private final String PATH_COLLECTION_PRODUCTS = "products";
    private final String PATH_COLLECTION_ERRORS ="errors";
    private final FirebaseFirestore db;
    private String[] stores;

    AppFirestoreDatabase(){
        db =FirebaseFirestore.getInstance();
        db.collection(PATH_COLLECTION_STORES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List usersDocList = task.getResult().getDocuments();

                        int length = usersDocList.size();
                        stores = new String[length];
                        for(int i= 0;i<length;i++){
                            stores[i] = ((QueryDocumentSnapshot)usersDocList.get(i)).getId();
                        }
                        Log.d("ON_COMPLETE", Arrays.toString(stores));
                    }
                });
    }

    public String[] getStores() {
        return stores;
    }
}
