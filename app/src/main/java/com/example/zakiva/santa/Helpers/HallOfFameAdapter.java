package com.example.zakiva.santa.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.zakiva.santa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by zakiva on 11/12/16.
 */

public class HallOfFameAdapter extends ArrayAdapter<String[]> {


    public HallOfFameAdapter(Context context, ArrayList<String[]> items) {
        super(context,R.layout.hall_of_fame_row ,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.hall_of_fame_row, parent, false);

        final String[] item = getItem(position);

        RelativeLayout rl = (RelativeLayout) customView.findViewById(R.id.full_row_hall_of_fame);

        rl.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                winner_chosen(v, item);
            }
        });

        TextView name = (TextView) customView.findViewById(R.id.name);
        TextView prize = (TextView) customView.findViewById(R.id.prize);
        TextView response = (TextView) customView.findViewById(R.id.response);
        TextView date = (TextView) customView.findViewById(R.id.date);

        name.setText(item[4]);
        prize.setText(item[5]);
        response.setText(item[1]);
        date.setText(item[0]);

        final ImageView image = (ImageView) customView.findViewById(R.id.imageViewWinnerFace);
        //Picasso.with(getContext()).load(item[3]).into(image);
        //Glide.with(getContext()).load(item[3]).into(image);

        //Picasso.with(getContext()).load(item[3]).transform(new CircleTransform()).fetch();
        Picasso.with(getContext()).load(item[3]).placeholder(R.drawable.face_placeholder).transform(new CircleTransform()).into(image);
        //Picasso.with(getContext()).load(item[3]).placeholder(R.drawable.face_placeholder).into(image);
        return customView;
    }

    private void winner_chosen(View v, String [] item) {
        /*
        Intent intent = new Intent(v.getContext(), new_order_screen.class);
        intent.putExtra("name", name);
        v.getContext().startActivity(intent);
        */
    }



}