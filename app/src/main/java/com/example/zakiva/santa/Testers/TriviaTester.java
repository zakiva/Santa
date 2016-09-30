package com.example.zakiva.santa.Testers;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.example.zakiva.santa.MainActivity;
import com.example.zakiva.santa.Models.Generator;
import com.example.zakiva.santa.R;
import com.example.zakiva.santa.Trivia;

import java.util.Random;

/**
 * Created by zakiva on 9/30/16.
 */

public class TriviaTester {

    public static void runRandomQuestions (final Button b) throws InterruptedException {
        final Generator generator = new Generator ();

        for (int i = 0; i < 100; i++) {

            Thread.sleep(100);

            Log.d(MainActivity.TAG, ">>>>>>>>>i =  " + i);

            Handler handler = new Handler();

            handler.postDelayed(new Runnable()
            {
                public void run()
                {
                    b.setBackgroundColor(Color.GREEN);

                    int n = new Random().nextInt(3);

                    if (n == 0)
                        Trivia.loadQuestionToScreen(generator.bandToAlbum());
                    else if (n == 1)
                        Trivia.loadQuestionToScreen(generator.countryToCapital());
                    else
                        Trivia.loadQuestionToScreen(generator.bandToYear());
                }
            }, 9000);
        }
    }

}
