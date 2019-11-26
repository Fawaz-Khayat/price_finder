package com.fawwazkhayyat.pricefinder;
//https://developer.android.com/topic/libraries/architecture/viewmodel
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FireStoreViewModel extends ViewModel {

    private FirebaseFirestore db;
    private String PATH_COLLECTION_STORES = "stores";
    private MutableLiveData<Store[]> stores;
    private MutableLiveData<Product> product;
    private MutableLiveData<String> productImageRefPath;

    public FireStoreViewModel() {
        this.db = FirebaseFirestore.getInstance();
    }

    LiveData<Store[]> getStores(){
        if (stores == null){
            stores = new MutableLiveData<>();
            loadStores();
        }
        return stores;
    }

    LiveData<Product> getProduct(String storeId, String barcode){

        if(product == null){
            product = new MutableLiveData<>();
            Product tempProduct = new Product(barcode);;
            loadProduct(storeId, barcode);
        }
        return product;
    }

    private void loadStores(){
        db.collection(PATH_COLLECTION_STORES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("DEBUG_TAG","In FireStore onComplete");
                        final List storesDocList = task.getResult().getDocuments();
                        int length = storesDocList.size();
                        //
                        Store[] tempStores = new Store[length];
                        String info = "";
                        for(int i= 0;i<length;i++){
                            String id, name, address, country, province;
                            double tax;
                            GeoPoint geoPoint;

                            id = ((QueryDocumentSnapshot)storesDocList.get(i)).getId();
                            Store tempStore = new Store(id);

                            if(((DocumentSnapshot)storesDocList.get(i)).contains("name"))
                                name = ((DocumentSnapshot) storesDocList.get(i)).get("name").toString();
                            else
                                name = id;
                            tempStore.setName(name);

                            if(((DocumentSnapshot)storesDocList.get(i)).contains("address")){
                                address = ((DocumentSnapshot) storesDocList.get(i)).get("address").toString();
                                tempStore.setAddress(address);
                            }


                            if(((DocumentSnapshot)storesDocList.get(i)).contains("country")){
                                country = ((DocumentSnapshot) storesDocList.get(i)).get("country").toString();
                                tempStore.setCountry(country);
                            }


                            if(((DocumentSnapshot)storesDocList.get(i)).contains("province")){
                                province = ((DocumentSnapshot) storesDocList.get(i)).get("province").toString();
                                tempStore.setProvince(province);
                            }


                            if(((DocumentSnapshot)storesDocList.get(i)).contains("geopoint")){
                                geoPoint = (GeoPoint) ((DocumentSnapshot) storesDocList.get(i)).get("geopoint");
                                tempStore.setGeopoint(geoPoint);
                            }
                            if(((DocumentSnapshot)storesDocList.get(i)).contains("tax")){
                                tax = (double) ((DocumentSnapshot) storesDocList.get(i)).get("tax");
                                tempStore.setTax(tax);
                            }
                            tempStores[i] = tempStore;
                        }
                        stores.postValue(tempStores);
                    }
                });
    }

    private void loadProduct(String storeId, String barcode){
        db.document("/stores/"+ storeId +"/products/" + barcode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Product tempProduct = new Product(barcode);
                        DocumentSnapshot docResult = task.getResult();
                        if(docResult!=null){
                            if(docResult.contains("name"))
                                tempProduct.setName((String) docResult.get("name"));
                            if(docResult.contains("price"))
                                tempProduct.setPrice((double)docResult.getDouble("price"));
                        }
                        Log.d("DEBUG_TAG", "loadProduct onComplete:");

                        // another data call to get the image reference
                        ////////////////////////
                        db.document("/products/"+barcode)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Log.d("DEBUG_TAG", "onSuccess: loadProductImageRef");
                                        if(documentSnapshot!=null) {
                                            if (documentSnapshot.contains("imageRefPath")) {

                                                tempProduct.setImageRefPath(documentSnapshot.getString("imageRefPath"));
                                                //todo
                                                // remove below commented line
                                                //tempProduct.setImageRef("images/pexels_photo_2535207.jpeg");
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("DEBUG_TAG", "onFailure: " + e);
                                    }
                                })
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        Log.d("DEBUG_TAG", "onComplete: loadProductImageRef");
                                        product.setValue(tempProduct);
                                    }
                                });
                        /////////////////////
                    }
                });
    }
}
