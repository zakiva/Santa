package com.example.zakiva.santa.Helpers;

import android.app.Activity;
import android.util.Log;

import com.example.zakiva.santa.R;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

/**
 * Created by Ariel on 9/4/2016.
 */
public class VungleAds {
    final VunglePub vunglePub = VunglePub.getInstance();

    private final EventListener vungleListener = new EventListener(){

        @Deprecated
        @Override
        public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
            // This method is deprecated and will be removed. Please do not use it.
            // Please use onAdEnd instead.
        }

        @Override
        public void onAdStart() {
            // Called before playing an ad
        }

        @Override
        public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {
            //Reward here!
            Log.d("Reward: ", "Won!!");
        }

        @Override
        public void onAdPlayableChanged(boolean isAdPlayable) {
            // Called when the playability state changes. if isAdPlayable is true, you can now
            // play an ad.
            // If false, you cannot yet play an ad.
        }

        @Override
        public void onAdUnavailable(String reason) {
            // Called when VunglePub.playAd() was called, but no ad was available to play
        }

    };

    public void vungleInit(Activity firstActivity){
        final String app_id = firstActivity.getString(R.string.vungle_app_id);
        vunglePub.init(firstActivity, app_id);
        vunglePub.setEventListeners(vungleListener);
    }
}
