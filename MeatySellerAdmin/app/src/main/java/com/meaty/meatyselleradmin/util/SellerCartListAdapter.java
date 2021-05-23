package com.meaty.meatyselleradmin.util;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.meaty.meatyselleradmin.R;
import com.meaty.meatyselleradmin.model.SellerCartListItem;

import java.util.ArrayList;
import java.util.List;

public class SellerCartListAdapter extends ArrayAdapter {
    private List<SellerCartListItem> sellerCartListItems;
    private Context context;

    public SellerCartListAdapter(Activity context, List<SellerCartListItem> sellerCartListItems) {
        super(context, R.layout.seller_cart_list_row_item, sellerCartListItems);
        this.sellerCartListItems = new ArrayList<>();
        this.sellerCartListItems.addAll(sellerCartListItems);
        this.context = context;
    }

    private static class ViewHolder {
        TextView tvFishName;
        EditText etFishWeightKg;
        CheckBox cbFishListSeller;
    }

    public List<SellerCartListItem> getList() {
        return sellerCartListItems;
    }

    public int getCount() {
        return sellerCartListItems.size();
    }

    public SellerCartListItem getItem(int position) {
        return sellerCartListItems.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.seller_cart_list_row_item, null);

            holder = new ViewHolder();
            holder.tvFishName = convertView.findViewById(R.id.tvFishName);
            holder.etFishWeightKg = convertView.findViewById(R.id.etFishWeightKg);
            holder.cbFishListSeller = convertView.findViewById(R.id.cbFishListSeller);
            convertView.setTag(holder);

            final ViewHolder finalHolder = holder;
            holder.cbFishListSeller.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (finalHolder.cbFishListSeller.isChecked()) {
                        sellerCartListItems.get(position).setChecked(true);
                    } else {
                        sellerCartListItems.get(position).setChecked(false);
                    }
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.etFishWeightKg.addTextChangedListener(new TextWatcher() {
            Double inputKg = 0.0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()) {
                    inputKg = Double.parseDouble(charSequence.toString());
                } else {
                    inputKg = 0.0;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                SellerCartListItem item = sellerCartListItems.get(position);
                item.setWeightInKg(inputKg);
            }
        });

        SellerCartListItem item = sellerCartListItems.get(position);
        holder.tvFishName.setText(item.getFishName().toUpperCase());
        holder.tvFishName.setTag(item.getFishName());

        return convertView;
    }
}

