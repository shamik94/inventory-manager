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
import android.widget.EditText;
import android.widget.TextView;

import com.meaty.meatyselleradmin.R;
import com.meaty.meatyselleradmin.model.firebase.Fish;

import java.util.ArrayList;
import java.util.List;

public class UpdateFishListAdapter extends ArrayAdapter {
    private List<Fish> fishes;
    private Context context;

    public UpdateFishListAdapter(Activity context, List<Fish> fishes) {
        super(context, R.layout.seller_cart_list_row_item, fishes);
        this.fishes = new ArrayList<>();
        this.fishes.addAll(fishes);
        this.context = context;
    }

    private static class ViewHolder {
        TextView tvFishNameUpdatePrice;
        EditText etFishPrice;
    }

    public List<Fish> getList() {
        return fishes;
    }

    public int getCount() {
        return fishes.size();
    }

    public Fish getItem(int position) {
        return fishes.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.update_fish_price_list_row_item, null);

            holder = new ViewHolder();
            holder.tvFishNameUpdatePrice = convertView.findViewById(R.id.tvFishNameUpdatePrice);
            holder.etFishPrice = convertView.findViewById(R.id.etFishPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.etFishPrice.addTextChangedListener(new TextWatcher() {
            Double fishPrice = 0.0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()) {
                    fishPrice = Double.parseDouble(charSequence.toString());
                } else {
                    fishPrice = 0.0;
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                Fish item = fishes.get(position);
                item.setPrice(fishPrice);
            }
        });

        Fish item = fishes.get(position);
        holder.tvFishNameUpdatePrice.setText(item.getName().toUpperCase());
        holder.tvFishNameUpdatePrice.setTag(item.getName());

        return convertView;
    }
}

