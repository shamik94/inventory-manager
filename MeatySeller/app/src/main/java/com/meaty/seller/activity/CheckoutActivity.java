package com.meaty.seller.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.meaty.seller.R;
import com.meaty.seller.mapper.CheckoutListItemMapper;
import com.meaty.seller.mapper.SalesMapper;
import com.meaty.seller.model.firebase.Sales;
import com.meaty.seller.model.listItem.CheckoutListItem;
import com.meaty.seller.model.listItem.FishListItem;
import com.meaty.seller.util.CheckoutListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private ListView listView;
    private CheckoutListAdapter dataAdapter;
    private Map<String, FishListItem> fishListItemMap;
    private static final String SALES_COLLECTION_NAME = "sales";
    private static final String USER_SHARED_PREFERENCES = "userPreferences";
    private static final String DAILY_SESSION_ID_SHARED_PREFERENCES = "dailySessionId";
    private static final String UID_SHARED_PREFERENCES = "uid";
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        setup();
        displayListView();
        checkoutButtonClick();
    }

    private void checkoutButtonClick() {
        Button myButton = findViewById(R.id.btnSell);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Double> checkoutKgs = new HashMap<>();
                ArrayList<CheckoutListItem> items = dataAdapter.getList();
                boolean isItemUnselected = false;
                for (int i = 0; i < items.size(); i++) {
                    CheckoutListItem item = items.get(i);
                    if (item.getSalesGram() == 0 && item.getSalesKg() == 0) {
                        isItemUnselected = true;
                        break;
                    }
                    Double weightInKgs = item.getSalesKg() + (0.001 * item.getSalesGram());
                    checkoutKgs.put(item.getFishId(), weightInKgs);
                }
                if (items.isEmpty()) {
                    showMessage("Cart is Empty");
                    return;
                }
                if (isItemUnselected) {
                    showMessage("Enter all the fields");
                    return;
                }
                generateAlertAndSaveToDB(checkoutKgs);
            }
        });
    }

    private void generateAlertAndSaveToDB(final Map<String, Double> checkoutKgs) {
        SalesMapper mapper = new SalesMapper();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("CONFIRM BILL");
        alert.setMessage(mapper.generateBill(checkoutKgs, fishListItemMap));
        final Sales sales = mapper.map(checkoutKgs, fishListItemMap, getSellerId());

        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateSalesDataToDB(sales);
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    private void updateSalesDataToDB(Sales sales){
        database.collection(SALES_COLLECTION_NAME)
                .add(sales)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(CheckoutActivity.this, SuccessCheckoutActivity.class);
                        HomeScreenActivity.homeScreenActivity.finish();
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed");
                    }
                });
    }

    private void createAlert(String bill) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("CONFIRM BILL");
        alert.setMessage(bill);
    }

    private void setup() {
        listView = findViewById(R.id.lvFishListCheckout);
        database = FirebaseFirestore.getInstance();
        fishListItemMap = new HashMap<>();
        for (FishListItem fishListItem : HomeScreenActivity.fishListItems) fishListItemMap.put(fishListItem.getFishId(), fishListItem);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void displayListView() {
        ArrayList<String> selectedFishIds = getIntent().getStringArrayListExtra("selectedFishIdsArray");
        List<CheckoutListItem> items = new CheckoutListItemMapper().map(selectedFishIds, HomeScreenActivity.fishListItems);

        //create an ArrayAdapter from the String Array
        dataAdapter = new CheckoutListAdapter(this, items);
        ;
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private String getDailySaleId() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return userSharedPreference.getString(DAILY_SESSION_ID_SHARED_PREFERENCES, null);
    }

    private void showMessage(String message) {
        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private String getSellerId() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return userSharedPreference.getString(UID_SHARED_PREFERENCES, null);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
