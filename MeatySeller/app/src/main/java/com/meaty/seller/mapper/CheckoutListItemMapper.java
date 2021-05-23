package com.meaty.seller.mapper;

import com.meaty.seller.R;
import com.meaty.seller.model.listItem.CheckoutListItem;
import com.meaty.seller.model.listItem.FishListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutListItemMapper {

    public CheckoutListItemMapper(){

    }

    public List<CheckoutListItem> map(ArrayList<String> selectedFishIds, List<FishListItem> fishListItems) {
        List<CheckoutListItem> checkoutListItems= new ArrayList<>();

        Map<String, FishListItem> fishListItemMap = new HashMap<>();
        for (FishListItem fishListItem : fishListItems) fishListItemMap.put(fishListItem.getFishId(), fishListItem);

        for (String selectedFishId : selectedFishIds) {
            if (fishListItemMap.get(selectedFishId) != null) {
                CheckoutListItem checkoutListItem = new CheckoutListItem(selectedFishId,
                        fishListItemMap.get(selectedFishId).getFishName(),
                        fishListItemMap.get(selectedFishId).getFishPrice(),
                        R.drawable.common_google_signin_btn_icon_dark_normal);
                checkoutListItems.add(checkoutListItem);
            }
        }
        return checkoutListItems;
    }
}
