package com.meaty.seller.util;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.meaty.seller.R;
import com.meaty.seller.model.listItem.CheckoutListItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutListAdapter extends ArrayAdapter {
    private ArrayList<CheckoutListItem> checkoutList;
    private Context context;

    public CheckoutListAdapter(Activity context, List<CheckoutListItem> checkoutList) {
        super(context, R.layout.checkout_list_row_item, checkoutList);
        this.checkoutList = new ArrayList<>();
        this.checkoutList.addAll(checkoutList);
        this.context = context;
    }

    private static class ViewHolder {
        TextView tvFishNameCheckout;
        ImageView ivFishImageCheckout;
        ImageView ivDeleteCheckout;
        EditText etKgCheckout;
        EditText etGramCheckout;
    }

    public ArrayList<CheckoutListItem> getList() {
        return checkoutList;
    }

    public int getCount() {
        return checkoutList.size();
    }

    public CheckoutListItem getItem(int position) {
        return checkoutList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.checkout_list_row_item, null);

            holder = new ViewHolder();
            holder.tvFishNameCheckout = convertView.findViewById(R.id.tvFishNameCheckout);
            holder.ivFishImageCheckout = convertView.findViewById(R.id.ivFishImageCheckout);
            holder.ivDeleteCheckout = convertView.findViewById(R.id.ivDeleteCheckout);
            holder.ivDeleteCheckout = convertView.findViewById(R.id.ivDeleteCheckout);
            holder.etKgCheckout = convertView.findViewById(R.id.etKgCheckout);
            holder.etGramCheckout = convertView.findViewById(R.id.etGramCheckout);
            convertView.setTag(holder);

            final ViewHolder finalHolder = holder;
            holder.ivDeleteCheckout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (checkoutList.get(position) != null) {
                        checkoutList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            holder.etKgCheckout.addTextChangedListener(new TextWatcher() {
                Double inputKg = 0.0;
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    inputKg = Double.parseDouble(charSequence.toString());
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    CheckoutListItem item = checkoutList.get(position);
                    item.setSalesKg(inputKg);
                }
            });
            holder.etGramCheckout.addTextChangedListener(new TextWatcher() {
                Double inputGram = 0.0;
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    inputGram = Double.parseDouble(charSequence.toString());
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    CheckoutListItem item = checkoutList.get(position);
                    item.setSalesGram(inputGram);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CheckoutListItem item = checkoutList.get(position);
        holder.tvFishNameCheckout.setText(item.getFishName().toUpperCase());
        holder.ivFishImageCheckout.setImageResource(item.getImageId());
        holder.tvFishNameCheckout.setTag(item.getFishName());

        return convertView;
    }
}

