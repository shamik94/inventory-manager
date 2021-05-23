package com.meaty.meatyselleradmin.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.meaty.meatyselleradmin.R;
import com.meaty.meatyselleradmin.model.SellerCartListItem;
import com.meaty.meatyselleradmin.model.firebase.DailySession;
import com.meaty.meatyselleradmin.model.firebase.Fish;
import com.meaty.meatyselleradmin.model.firebase.SellerProfile;
import com.meaty.meatyselleradmin.util.SellerCartListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateSellerCartActivity extends AppCompatActivity {

    private FirebaseFirestore database;
    private SellerCartListAdapter dataAdapter;
    private ListView listView;
    private List<SellerCartListItem> sellerCartListItems;
    private List<SellerProfile> sellerProfiles;
    private String selectedSellerId;
    private TextView tvChooseSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_seller_cart);

        listView = findViewById(R.id.lvSellerCartFishList);
        tvChooseSeller = findViewById(R.id.tvChooseSeller);
        database = FirebaseFirestore.getInstance();
        sellerCartListItems = new ArrayList<>();
        sellerProfiles = new ArrayList<>();

        fetchDataAndPopulateList();
        fetchSellers();
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
                                SellerCartListItem item = new SellerCartListItem(fish.getId(), fish.getName(), false);
                                sellerCartListItems.add(item);
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

    private void displayListView() {
        //create an ArrayAdapter from the String Array
        dataAdapter = new SellerCartListAdapter(this, sellerCartListItems);
        ;
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private void showMessage(String message) {
        Toast.makeText(UpdateSellerCartActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void buttonClick() {
        Button myButton = findViewById(R.id.btnUpdateSellerCart);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSellerId == null || selectedSellerId.isEmpty()) {
                    showMessage("Please Select Seller");
                    return;
                }
                StringBuilder responseText = new StringBuilder();
                responseText.append("The following were selected...\n");
                Map<String, Double> startInventory = new HashMap<>();

                List<SellerCartListItem> items = dataAdapter.getList();
                ArrayList<String> selectedFishIds = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    SellerCartListItem item = items.get(i);
                    if (item.getChecked()) {
                        responseText.append("\n").append(item.getFishName());
                        selectedFishIds.add(item.getFishId());
                        if (item.getWeightInKg() > 0) {
                            startInventory.put(item.getFishId(), item.getWeightInKg());
                        }
                    }
                }

                if (selectedFishIds.isEmpty()) {
                    showMessage("No items selected");
                    return;
                }

                updateDailySession(startInventory);

                showMessage(responseText.toString());
            }
        });
    }

    private void updateDailySession(Map<String, Double> startInventory) {
        DailySession dailySession = new DailySession();
        dailySession.setActive(true);
        dailySession.setCreatedAt(Timestamp.now());
        dailySession.setSellerId(selectedSellerId);
        dailySession.setUpdatedAt(Timestamp.now());
        dailySession.setStartInventory(startInventory);
        database.collection("session").document("20200717").collection("daily_session")
                .add(dailySession)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showMessage("Successfully Added Inventory");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed to add Inventory");
                    }
                });

    }

    private void fetchSellers() {
        tvChooseSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchSellersFromDB();
            }
        });
    }

    private void fetchSellersFromDB() {

        if (!sellerProfiles.isEmpty()) {
            showSellersInDialog();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Sellers");
        dialog.show();
        database.collection("seller")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SellerProfile sellerProfile = document.toObject(SellerProfile.class);
                                sellerProfile.setSellerId(document.getId());
                                sellerProfiles.add(sellerProfile);
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                showSellersInDialog();
                            }
                        } else {
                            showMessage("Failed to fetch Sellers");
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void showSellersInDialog() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(UpdateSellerCartActivity.this);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<SellerProfile> arrayAdapter = new ArrayAdapter<>(UpdateSellerCartActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(sellerProfiles);
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String sellerName = Objects.requireNonNull(arrayAdapter.getItem(which)).getFirstName() + " " + Objects.requireNonNull(arrayAdapter.getItem(which)).getLastName();
                AlertDialog.Builder builderInner = new AlertDialog.Builder(UpdateSellerCartActivity.this);
                builderInner.setMessage(sellerName);
                selectedSellerId = Objects.requireNonNull(arrayAdapter.getItem(which)).getSellerId();
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvChooseSeller.setText(sellerName);
                        dialog.dismiss();
                        System.out.println(selectedSellerId);
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

}
