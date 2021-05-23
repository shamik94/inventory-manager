package com.meaty.seller.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.meaty.seller.R;
import com.meaty.seller.mapper.FishListItemMapper;
import com.meaty.seller.model.firebase.DailySession;
import com.meaty.seller.model.firebase.Fish;
import com.meaty.seller.model.listItem.FishListItem;
import com.meaty.seller.util.FishListAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {

    private ListView listView;
    private FishListAdapter dataAdapter;
    private FirebaseFirestore database;
    private List<Fish> fishes;
    private List<DailySession> dailySessions;
    // TODO change design to get this from local DB
    public static List<FishListItem> fishListItems;
    public static Activity homeScreenActivity;

    private static final String SESSION_COLLECTION_NAME = "session";
    private static final String DAILY_SESSION_COLLECTION_NAME = "daily_session";
    private static final String FISH_COLLECTION_NAME = "fish";
    private static final String SELLER_ID = "sellerId";
    private static final String ACTIVE = "active";
    private static final String USER_SHARED_PREFERENCES = "userPreferences";
    private static final String UID_SHARED_PREFERENCES = "uid";
    private static final String DAILY_SESSION_ID_SHARED_PREFERENCES = "dailySessionId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setup();
        fetchDataAndPopulateList();
        sellButtonClick();
    }

    private void setup() {
        listView = findViewById(R.id.lvFishList);
        database = FirebaseFirestore.getInstance();
        fishes = new ArrayList<>();
        dailySessions = new ArrayList<>();
        homeScreenActivity = this;
    }

    private void fetchDataAndPopulateList() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Fish List");
        dialog.show();
        // TODO add onFailure Listeners
        database.collection(FISH_COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Fish fish = document.toObject(Fish.class);
                                fish.setId(document.getId());
                                fishes.add(fish);
                            }
                            database.collection(SESSION_COLLECTION_NAME).document("20200717").collection(DAILY_SESSION_COLLECTION_NAME)
                                    .whereEqualTo(SELLER_ID, getSellerId())
                                    .whereEqualTo(ACTIVE, true)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    DailySession ds = document.toObject(DailySession.class);
                                                    ds.setDailySessionId(document.getId());
                                                    dailySessions.add(ds);
                                                }
                                                fishListItems = new FishListItemMapper().map(fishes, getLatestDailySale());
                                                if (getLatestDailySale() != null){
                                                    saveDailySaleIdToSharedPreferences(getLatestDailySale().getDailySessionId());
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
                        } else {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            showMessage("Failed To Fetch Data");
                        }
                    }
                });
    }

    private DailySession getLatestDailySale() {
        if (dailySessions.isEmpty()) {
            return null;
        }
        //TODO implement comparator
        return dailySessions.get(0);
    }

    private String getSellerId() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return userSharedPreference.getString(UID_SHARED_PREFERENCES, null);
    }

    private void displayListView() {
        //create an ArrayAdapter from the String Array
        dataAdapter = new FishListAdapter(this, fishListItems);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private void sellButtonClick() {
        Button myButton = findViewById(R.id.btnSell);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder responseText = new StringBuilder();
                responseText.append("The following were selected...\n");

                List<FishListItem> items = dataAdapter.getList();
                ArrayList<String> selectedFishIds = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    FishListItem item = items.get(i);
                    if (item.getChecked()) {
                        responseText.append("\n").append(item.getFishName());
                        selectedFishIds.add(item.getFishId());
                    }
                }

                if (selectedFishIds.isEmpty()) {
                    showMessage("No items selected");
                    return;
                }

                showMessage(responseText.toString());
                Intent intent = new Intent(HomeScreenActivity.this, CheckoutActivity.class);
                intent.putStringArrayListExtra("selectedFishIdsArray", selectedFishIds);
                startActivity(intent);
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(HomeScreenActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveDailySaleIdToSharedPreferences(String dailySaleId) {
        if (dailySaleId == null || dailySaleId.isEmpty()) {
            return;
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(UID_SHARED_PREFERENCES, null).equals(dailySaleId)){
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DAILY_SESSION_ID_SHARED_PREFERENCES, dailySaleId);
        editor.apply();
    }
}
