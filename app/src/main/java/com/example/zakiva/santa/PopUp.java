package com.example.zakiva.santa;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


public class PopUp extends AppCompatActivity {

    private PopupWindow pwindo;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.test_pop_up);
        }

        public  void initiatePopupWindow(View view) {
            try {
                LayoutInflater inflater = (LayoutInflater) PopUp.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.pop_up,(ViewGroup) findViewById(R.id.signUpPopUp));
                pwindo = new PopupWindow(layout, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                pwindo.showAtLocation(layout,Gravity.CENTER,0,0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void setBtnClosePopup(View view){
        pwindo.dismiss();
        }
}