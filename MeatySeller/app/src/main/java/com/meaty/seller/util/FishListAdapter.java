package com.meaty.seller.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.meaty.seller.R;
import com.meaty.seller.model.listItem.FishListItem;

import java.util.ArrayList;
import java.util.List;

public class FishListAdapter extends ArrayAdapter {
    private List<FishListItem> fishList;
    private Context context;

    public FishListAdapter(Activity context, List<FishListItem> fishList) {
        super(context, R.layout.fish_list_row_item, fishList);
        this.fishList = new ArrayList<>();
        this.fishList.addAll(fishList);
        this.context = context;
    }

    private static class ViewHolder {
        TextView tvFishName;
        TextView tvFishPrice;
        ImageView ivFishImage;
        CheckBox cbFishListSeller;
    }

    public List<FishListItem> getList() {
        return fishList;
    }

    public int getCount() {
        return fishList.size();
    }

    public FishListItem getItem(int position) {
        return fishList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.fish_list_row_item, null);

            holder = new ViewHolder();
            holder.tvFishName = convertView.findViewById(R.id.tvFishName);
            holder.tvFishPrice = convertView.findViewById(R.id.tvFishPrice);
            holder.ivFishImage = convertView.findViewById(R.id.ivFishImage);
            holder.cbFishListSeller = convertView.findViewById(R.id.cbFishListSeller);
            convertView.setTag(holder);

            final ViewHolder finalHolder = holder;
            holder.cbFishListSeller.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (finalHolder.cbFishListSeller.isChecked()) {
                        fishList.get(position).setChecked(true);
                    } else {
                        fishList.get(position).setChecked(false);
                    }
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FishListItem item = fishList.get(position);
        holder.tvFishName.setText(item.getFishName().toUpperCase());
        holder.tvFishPrice.setText(item.getPriceString());
        holder.ivFishImage.setImageResource(item.getImageId());
        holder.tvFishName.setTag(item.getFishName());

        return convertView;
    }
}

