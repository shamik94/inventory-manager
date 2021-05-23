package com.meaty.meatyselleradmin.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.meaty.meatyselleradmin.R;
import com.meaty.meatyselleradmin.model.firebase.Fish;
import com.meaty.meatyselleradmin.util.SellerCartListAdapter;
import com.meaty.meatyselleradmin.util.UpdateFishListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateFishPriceActivity extends AppCompatActivity {

    private FirebaseFirestore database;
    private UpdateFishListAdapter dataAdapter;
    private ListView listView;
    private List<Fish> fishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fish_price);

        listView = findViewById(R.id.lvUpdateFishPrice);
        database = FirebaseFirestore.getInstance();
        fishes = new ArrayList<>();

        fetchDataAndPopulateList();
        buttonClick();
    }

    private void fetchDataAndPopulateList() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Fish List");
        dialog.show();
        // TODO add onFailure Listeners
        database.collection("fish")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Fish fish = document.toObject(Fish.class);
                                fish.setId(document.getId());
                                fish.setPrice(0.0);
                                fishes.add(fish);
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            displayListView();
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            showMessage("Failed To Fetch Data");
                        }
                    }
                });
    }


    private void buttonClick() {
        Button myButton = findViewById(R.id.btnUpdateFishPrice);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder responseText = new StringBuilder();
                responseText.append("The following were selected...\n");
                List<Fish> items = dataAdapter.getList();

                updateFishPrice(items);

                showMessage(responseText.toString());
            }
        });
    }

    private void updateFishPrice(List<Fish> items) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Updating Prices");
        dialog.show();
        // Get a new write batch
        WriteBatch batch = database.batch();

        // Set the value of fishes
        for (Fish item : items) {
            if (item.getPrice() <= 0 ){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                continue;
            }
            DocumentReference fishRef = database.collection("fish").document(item.getId());
            batch.set(fishRef, item);
        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (task.isSuccessful() ) {
                    showMessage("Updated Fish Prices Successfully");
                } else {
                    showMessage("Failed to update fish prices");
                }
            }
        });
    }


    private void displayListView() {
        //create an ArrayAdapter from the String Array
        dataAdapter = new UpdateFishListAdapter(this, fishes);
        ;
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private void showMessage(String message) {
        Toast.makeText(UpdateFishPriceActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
