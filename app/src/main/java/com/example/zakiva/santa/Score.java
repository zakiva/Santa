package com.example.zakiva.santa;

import android.app.Activity;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zakiva.santa.Helpers.Drawing;
import com.example.zakiva.santa.Helpers.Infra;
import com.example.zakiva.santa.Helpers.Storage;
import com.example.zakiva.santa.Models.FacebookSignUp;
import com.example.zakiva.santa.Models.GoogleSignUp;

import java.util.ArrayList;
import java.util.List;

//import com.vungle.publisher.VunglePub;

public class Score extends AppCompatActivity {

    private int SIGN_UP_TIME = 2000;
    static int TRY_CANDIES_PRICE = 100;
    Bundle extras;
    TextView scoreTextView;
    TextView candiesTextView;
    TextView expTextView;
    ProgressBar expBar;
    long score;
    String gameType;
    double mProgressStatus = 0;
    long EXP_SIZE = 2000;
    Activity activity;
    ImageView mainIcon;
    TextView scoreTitle;
    RelativeLayout messageBox;
    TextView playButton;
    TextView shareScore;
    RelativeLayout activityBackground;
    RelativeLayout signUpPopUp;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static String globalPhoneNumber;
    public static String globalContent;


    //final VunglePub vunglePub = VunglePub.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initFields();
        displayScreenAccordingToScore();
        activityBackground.getBackground().setAlpha(0);
        if (score != -1 && Storage.getStringPreferences("signedUpType",this.getApplicationContext()).equals("NONE")) {
            signUpWindow();
        }
        if (score != -1)
            Infra.addGameToUser(gameType, score);
        displayCandies();
        calcAndDisplayExp();
    }

    public void displayScreenAccordingToScore() {
        if (score != -1) {
            //set main icon to score section - remove icon
            mainIcon.setVisibility(View.GONE);
            //mainIcon.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.score_section, null));

            //show score labels
            scoreTextView.setText("Your score is: " + score);
            scoreTextView.setVisibility(View.VISIBLE);
            scoreTitle.setVisibility(View.VISIBLE);

            //set message box below score
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins((int) Drawing.convertDpToPixel(15, this), (int) Drawing.convertDpToPixel(20, this), (int) Drawing.convertDpToPixel(15, this), (int) Drawing.convertDpToPixel(0, this));
            p.addRule(RelativeLayout.BELOW, R.id.score);
            messageBox.setLayoutParams(p);

            //set text of CTA to retry
            playButton.setText("Retry! (100 candis)");

            //show share score
            shareScore.setVisibility(View.VISIBLE);
        } else {

            //don't show score labels
            scoreTextView.setVisibility(View.GONE);
            scoreTitle.setVisibility(View.GONE);

            //set main icon according to game type
            switch (gameType) {
                case "trivia":
                    mainIcon.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.trivia_section, null));
                    break;
                case "drawing":
                    mainIcon.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.drawdis_section, null));
                    break;
                default:
                    return;
            }
        }

    }

/*    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }*/

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Score.this, Games.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        changeBackgroundOpacity(0, View.GONE);
        displayCandies();
        Log.d(MainActivity.TAG, ">>>>>>>>>>ON RESTART SCORE ");
        //displayExp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeBackgroundOpacity(0, View.GONE);
        displayCandies();
        closePopUp();
        Log.d(MainActivity.TAG, ">>>>>>>>>>ON Resume SCORE ");
    }

    public void initFields() {
        activity = this;
        extras = getIntent().getExtras();
        activityBackground = (RelativeLayout) findViewById(R.id.activityBackground);
        scoreTextView = ((TextView) findViewById(R.id.score));
        candiesTextView = ((TextView) findViewById(R.id.candies));
        expTextView = ((TextView) findViewById(R.id.exp));
        expBar = ((ProgressBar) findViewById(R.id.expBar));
        mainIcon = ((ImageView) findViewById(R.id.mainIconScoreSection));
        scoreTitle = ((TextView) findViewById(R.id.scoreTitle));

        score = extras.getLong("score");
        gameType = extras.getString("game");
        messageBox = ((RelativeLayout) findViewById(R.id.messageRectangle));
        playButton = ((TextView) findViewById(R.id.playButton));
        shareScore = ((TextView) findViewById(R.id.shareScoreOnBar));
        signUpPopUp = ((RelativeLayout) findViewById(R.id.signUpPopUp));
    }

    public void displayCandies() {
        candiesTextView.setText(MainActivity.candies + " candies");
    }


    public void playButtonClicked(View v) {
        if (extras == null)
            return;

        if (!updateCandies()) { // don't start game (and inform the user need more candies?)
            notifyNoCandies();
            return;
        }

        switch (gameType) {
            case "trivia":
                startActivity(new Intent(Score.this, TriviaGame.class));
                break;
            case "drawing":
                startActivity(new Intent(Score.this, DrawingGame.class));
                break;
            default:
                return;
        }
    }

    public boolean updateCandies() {
        if (MainActivity.candies < TRY_CANDIES_PRICE)
            return false;
        long new_candies = MainActivity.candies - TRY_CANDIES_PRICE;
        Infra.addCandiesToUser(new_candies);
        MainActivity.setCandies(new_candies);
        //displayCandies(); prefer not to use this.
        return true;
    }

    public void homeScreenButtonClicked(View view) {
        startActivity(new Intent(Score.this, Prize.class));
    }


    public void videoAdButtonClicked(View view) {
        /*if (vunglePub.isAdPlayable()) {
            vunglePub.playAd();
        }
        else {
            // Ad is not ready
            Log.d("Oops: ", "Can't load video!");
        }*/
    }


    public void calcAndDisplayExp() {
        long exp = MainActivity.exp;
        mProgressStatus = exp;
        expBar.setProgress((int) exp);
        expTextView.setText("" + exp);
        if (extras != null && score != -1) {
            exp += score;
            if (exp > EXP_SIZE) {
                exp %= EXP_SIZE;
                Infra.addExpToUser(exp);
                expSizeReached(exp);
            } else {
                Infra.addExpToUser(exp);
                showProgressAnimation(exp, false, 0);
            }
        } else {
            //its the first time we are here (not retry). init views anyway above in this method
        }
    }

    void showProgressAnimation(final long newExp, final boolean expReached, final long expAfterSize) {

        Log.d(MainActivity.TAG, " showProgressAnimation  -- newEXP , exp reached, expAfterSize " + newExp + "," + expReached + "," + expAfterSize);

        final Handler mHandler = new Handler();

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                double delta = 0.05;
                while (mProgressStatus < newExp) {

                    //set "frames rate"
                    mProgressStatus += delta;
                    //we can play with this too:
                    //delta += 0.000003;

                    //another option instead of delta
                    /*
                    if (newExp - mProgressStatus < 100)
                        mProgressStatus += 0.08;
                    else
                        mProgressStatus += 0.04;
                        */
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            expBar.setProgress((int) mProgressStatus);
                        }
                    });
                }


                Log.d(MainActivity.TAG, "if (expReached): ");

                // have to run this on the main thread, not in the background
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        if (expReached) {
                            showAlertDialogForExpSizeReached(expAfterSize);
                        } else {
                            expTextView.setText("" + (int) mProgressStatus);
                        }
                    }
                });

            }
        }).start();
    }

    public void expSizeReached(long newExp) {
        //add more candies.. tell the user WOW..
        //show complete animation before new progress animation
        Log.d(MainActivity.TAG, "expSizeReached: ");
        showProgressAnimation(EXP_SIZE, true, newExp);
    }

    //make this function generic by passing it arguments (and move to INFRA)
    public void showAlertDialogForExpSizeReached(final long newExp) {

        Log.d(MainActivity.TAG, "showAlertDialogForExpSizeReached ");

        AlertDialog alertDialog = new AlertDialog.Builder(Score.this).create();
        alertDialog.setTitle("congratrs!!");
        alertDialog.setMessage("you've reached the EXP SIZE_PIXELS");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mProgressStatus = 0;
                        showProgressAnimation(newExp, false, 0);
                    }
                });
        alertDialog.show();
    }

    public void shareViaAllAppsPossible(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "You are invited to download Windis!";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Windis");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    public void sendSms(String number, String content) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, content, null, null);
            Toast.makeText(Score.this, "Sms sent successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e1) {
            Toast.makeText(Score.this, "Error: can't send sms", Toast.LENGTH_LONG).show();
        }
    }

    public void sendSmsWithPermissions(String number, String content) {
        globalPhoneNumber = number;
        globalContent = content;
        Log.d("aaaaaaaaaa: ", "1");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("aaaaaaaaaa: ", "2");
            //Log.d("aaaaaaaaaabbb: ", Manifest.permission.SEND_SMS);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Log.d("aaaaaaaaaa: ", "3");
                //Log.d("aaaaaaaaaabbb: ", Manifest.permission.SEND_SMS);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                Log.d("aaaaaaaaaa: ", "4");
                //Log.d("aaaaaaaaaabbb: ", Manifest.permission.SEND_SMS);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(globalPhoneNumber, null, globalContent, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent Successfully!", Toast.LENGTH_LONG).show();
            Log.d("aaaaaaaaaa: ", "5");
            // Reward here
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("aaaaaaaaaa: ", "6");
                    //Log.d("aaaaaaaaaabbb: ", Manifest.permission.SEND_SMS);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(globalPhoneNumber, null, globalContent, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent Successfully!", Toast.LENGTH_LONG).show();
                    // Reward here
                } else {
                    Log.d("aaaaaaaaaa: ", "7");
                    //Log.d("aaaaaaaaaabbb: ", Manifest.permission.SEND_SMS);
                    Toast.makeText(getApplicationContext(), "Please allow permissions. if you pressed 'Never ask again' change it through Android settings", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    public void inviteFriendClicked(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, 1);
        //sendSmsWithPermissions("+972526216383", "Please download windis!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                String finalNumber = number.replaceAll("[^0-9]", "");
                sendSmsWithPermissions(finalNumber, "Please download windis!");
            }
        }
    }

    public void notifyNoCandies() {
        Toast toast = Toast.makeText(this, "Need more candies mateee!!", Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        toast.show();
    }

    public boolean isContainMultipleStrings(String word, String[] subWords) {
        for (String s : subWords) {
            if (word.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public void shareScoreButtonClicked(View view) {
        //List of apps that we would like to let the user share with
        String[] apps = new String[]{"messaging", "sms", "mms", "whatsapp", "facebook", "mail", "twit", "google+", "hangout", "viber"};
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                targetedShare.setType("text/plain"); // put here your mime type
                if (isContainMultipleStrings(info.activityInfo.packageName.toLowerCase(), apps) || isContainMultipleStrings(info.activityInfo.name.toLowerCase(), apps)) {
                    targetedShare.putExtra(Intent.EXTRA_TEXT, "You are invited to download Windis! " + "My score is " + Long.toString(score));
                    targetedShare.setPackage(info.activityInfo.packageName);
                    targetedShareIntents.add(targetedShare);
                }
            }
            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Choose sharing method");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        }
    }

    public void backFromScoreClicked(View view) {
        startActivity(new Intent(Score.this, Games.class));
    }

    public void signUpWindow() {
            playButton.setClickable(false);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playButton.setClickable(true);
                    changeBackgroundOpacity(220,View.VISIBLE);
                    signUpPopUp.setVisibility(View.VISIBLE);

                   // startActivity(new Intent(Score.this, SignUp.class));
                }
            }, SIGN_UP_TIME);
        }

    public void changeBackgroundOpacity(int opacity, int visible) {
        activityBackground.getBackground().setAlpha(opacity);
        activityBackground.setVisibility(visible);
    }

    public void goToGoogleSignIn(View view){
        startActivity(new Intent(Score.this, GoogleSignUp.class));
    }
    public void goToFacebookSignIn(View view){
        startActivity(new Intent(Score.this, FacebookSignUp.class));
    }

    public void backToScoreButton(View view) {
        closePopUp();
    }

    public void closePopUp () {
        playButton.setClickable(true); // already true probably
        changeBackgroundOpacity(0,View.GONE);
        signUpPopUp.setVisibility(View.GONE);
    }

}