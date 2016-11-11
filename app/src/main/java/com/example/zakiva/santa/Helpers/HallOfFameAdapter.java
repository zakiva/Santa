package com.example.zakiva.santa.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zakiva.santa.R;

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

        /*

        Button choose = (Button) customView.findViewById(R.id.button_choose);

        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                winner_chosen(v, item[0], item[1]);
            }
        });

        */

        TextView name = (TextView) customView.findViewById(R.id.name);

        name.setText(item[0]);

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