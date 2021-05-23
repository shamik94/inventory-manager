package com.meaty.seller.mapper;

import com.meaty.seller.R;
import com.meaty.seller.model.firebase.DailySession;
import com.meaty.seller.model.firebase.Fish;
import com.meaty.seller.model.listItem.FishListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishListItemMapper {

    public FishListItemMapper() {
    }

    public ArrayList<FishListItem> map(List<Fish> fishes, DailySession dailySession) {
        ArrayList<FishListItem> fishListItems = new ArrayList<>();

        if (fishes.isEmpty() || dailySession == null) {
            return fishListItems;
        }

        Map<String, Fish> fishMap = new HashMap<>();
        for (Fish fish : fishes) fishMap.put(fish.getId(), fish);
        Map<String, Double> startInventoryMap = dailySession.getStartInventory();

        for (Map.Entry<String, Double> entry : startInventoryMap.entrySet()) {
            if (fishMap.get(entry.getKey()) != null) {
                FishListItem fishListItem = new FishListItem(
                        fishMap.get(entry.getKey()).getId(),
                        fishMap.get(entry.getKey()).getName(),
                        fishMap.get(entry.getKey()).getPrice(),
                        R.drawable.common_google_signin_btn_icon_dark_normal,
                        false);
                fishListItems.add(fishListItem);
            }
        }
        return fishListItems;
    }
}
