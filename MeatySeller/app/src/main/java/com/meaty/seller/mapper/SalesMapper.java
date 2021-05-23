package com.meaty.seller.mapper;

import com.google.firebase.Timestamp;
import com.meaty.seller.model.firebase.Sales;
import com.meaty.seller.model.listItem.FishListItem;

import java.util.HashMap;
import java.util.Map;

public class SalesMapper {

    public SalesMapper() {
    }

    public Sales map(Map<String, Double> checkoutKgs,
                     Map<String, FishListItem> fishListItemMap,
                     String sellerId
                     ) {
        Sales sales = new Sales();
        Map<String, Double> fishKgMap = new HashMap<>();
        Map<String, Double> fishSalesMap = new HashMap<>();
        Double totalSale = 0.0;
        for (Map.Entry<String, Double> entry : checkoutKgs.entrySet()) {
            String fishId = entry.getKey();
            Double fishCheckoutKg = entry.getValue();
            if (fishListItemMap.get(fishId) != null) {
                Double fishSale = fishListItemMap.get(fishId).getFishPrice() * fishCheckoutKg;
                fishKgMap.put(fishId, fishCheckoutKg);
                fishSalesMap.put(fishId, fishSale);
                totalSale += fishSale;
            }
        }
        sales.setFishKgMap(fishKgMap);
        sales.setFishSalesMap(fishSalesMap);
        sales.setUpdatedAt(Timestamp.now());
        sales.setSellerId(sellerId);
        sales.setTotalSale(totalSale);
        return sales;
    }

    public String generateBill(Map<String, Double> checkoutKgs, Map<String, FishListItem> fishListItemMap) {
        StringBuilder bill = new StringBuilder();
        Double totalSale = 0.0;
        for (Map.Entry<String, Double> entry : checkoutKgs.entrySet()) {
            String fishId = entry.getKey();
            Double fishCheckoutKg = entry.getValue();
            if (fishListItemMap.get(fishId) != null) {
                Double fishSale = fishListItemMap.get(fishId).getFishPrice() * fishCheckoutKg;
                bill.append(fishListItemMap.get(fishId).getFishName()).append("\t").append(fishSale).append("\n");
                totalSale += fishSale;
            }
        }
        bill.append("TOTAL AMOUNT").append("\t").append(totalSale);
        return bill.toString();
    }
}
