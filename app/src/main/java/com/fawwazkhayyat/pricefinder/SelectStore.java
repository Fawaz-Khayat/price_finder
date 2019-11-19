package com.fawwazkhayyat.pricefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SelectStore extends AppCompatActivity {
    Spinner spinner_selectStore;
    TextView textView_storeInfo;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);

        spinner_selectStore = findViewById(R.id.spinner_selectStore);
        textView_storeInfo = findViewById(R.id.textView_storeInfo);

        db = FirebaseFirestore.getInstance();
        String PATH_COLLECTION_STORES = "stores";
        db.collection(PATH_COLLECTION_STORES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List usersDocList = task.getResult().getDocuments();

                int length = usersDocList.size();
                String[] stores = new String[length];
                for(int i= 0;i<length;i++){
                    if(((DocumentSnapshot)usersDocList.get(i)).contains("name"))
                        stores[i] = ((DocumentSnapshot) usersDocList.get(i)).get("name").toString();
                    else
                        stores[i] = ((QueryDocumentSnapshot)usersDocList.get(i)).getId();
                }
                setSpinnerAdapter(stores);
                spinner_selectStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String info ="";
                        if(((DocumentSnapshot)usersDocList.get(position)).contains("address"))
                            info = ((DocumentSnapshot) usersDocList.get(position)).get("address").toString();
                        textView_storeInfo.setText(info);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void setSpinnerAdapter(Object[] stores){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this ,
                R.layout.support_simple_spinner_dropdown_item,
                stores);
        spinner_selectStore.setAdapter(arrayAdapter);
    }

    public void gotoBasket(View view){
        final String EXTRA_NAME = "com.fawwazkhayyat.pricefinder";
        Intent intent = new Intent(this, Basket.class);
        intent.putExtra(EXTRA_NAME, "NEW");

        startActivity(intent);
    }
}
