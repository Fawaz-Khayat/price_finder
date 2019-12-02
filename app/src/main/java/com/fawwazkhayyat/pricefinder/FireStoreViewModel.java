package com.fawwazkhayyat.pricefinder;
//https://developer.android.com/topic/libraries/architecture/viewmodel
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
                        DocumentSnapshot docResult = task.getResult();

                        if(docResult.exists()){
                            Product tempProduct = new Product(barcode);
                            if(docResult.contains("description"))
                                tempProduct.setDescription(docResult.getString("description"));
                            if(docResult.contains("price"))
                                tempProduct.setPrice(docResult.getDouble("price"));
                            if(docResult.contains("isTaxable"))
                                tempProduct.setTaxable(docResult.getBoolean("isTaxable"));
                            product.setValue(tempProduct);
                        }
                        else
                            product.setValue(null);
                        Log.d("DEBUG_TAG", "loadProduct onComplete:");
                    }
                });
    }
}
