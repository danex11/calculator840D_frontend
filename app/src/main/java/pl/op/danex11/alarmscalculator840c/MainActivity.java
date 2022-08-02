package pl.op.danex11.alarmscalculator840c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class MainActivity extends AppCompatActivity {

    boolean initializationDone = false;
    String TAG = "tag";
    private RewardedAd mRewardedAd;
    //private
    int adCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        // retain this fragment

       // AdView adView = new AdView(this);
        //adView.setAdSize(AdSize.BANNER);
       // adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
// TODO: Add adView to your view hierarchy.
        /*
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
// Place the ad view.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //LinearLayout adContainer = <container>;
        adContainer.addView(adView, params);

        // initialize the Google Mobile Ads SDK
        // initializes the SDK and calls back a completion listener once initialization is complete (or after a 30-second timeout).
        // This needs to be done only once, ideally at app launch.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                initializationDone = true;
                Log.e(TAG, "initializationDone = true Set");
                //loadad loads rewardedAd
               // loadAd();

            }
        });
         */
        // AdMob 1 : add view
        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdMob 2 : initialize
        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //AdMob 3 : request and load an ad
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        /*
            //set fullscreen callback
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.e(TAG, "Ad was shown.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.e(TAG, "Ad was dismissed.");
                    mRewardedAd = null;
                }
            });
*/
        final String[] result = {"waiting"};
        // your text box
        EditText alrmtyped = (EditText) findViewById(R.id.alarmtyped);

        alrmtyped.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //CLICKED Enter
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    CalculateAlarm calca = new CalculateAlarm(alrmtyped.getText().toString());
                    result[0] = calca.getDbAddress();
                    EditText resultETxt = findViewById(R.id.dbbresult);
                    resultETxt.setText(result[0]);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });
    }




    //AD
    void loadAd() {
        // IF (){}
        if (initializationDone == true) {
            Log.e(TAG, "initializationDone == true");
            //load rewarded ad object
            AdRequest adRequest = new AdRequest.Builder().build();

            RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.e(TAG, loadAdError.getMessage());
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            Log.e(TAG, "Ad was loaded.");
                            showAd();
                        }
                    });
        }
    }
    void showAd(){
        //show the ad
        Log.e(TAG, "Show Ad block started");
        if (mRewardedAd != null) {
            Log.e(TAG, "mRewardedAd != null inside \"show the ad\"");
            Activity activityContext = MainActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.e(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    adCounter++;
                }
            });
        } else { Log.e(TAG, "The rewarded ad wasn't ready yet."); }

    }







    public void loadBug(View view){

    }


    public void loadCoffee(View view){
        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
        myWebLink.setData(Uri.parse("https://buycoffee.to/danex11"));
        startActivity(myWebLink);
    }




    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }





    @Override
    protected  void onResume(){
        super.onResume();
//        //Layout params
//        RelativeLayout layout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        layout.setLayoutParams(params);
//        //set layout
//        //setContentView(layout);
//
//        TextView textview = new TextView(this);
//        textview.setText("Number of completely watched rewarded ads: " + adCounter);
//        textview.setTextSize(15);
//        //add textview to layout
//        layout.addView(textview);
//
//        //load ad button
//        Button button = new Button(this);
//        button.setText("Watch");
//        button.setPadding(0,100,0,0);
//        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
//        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        layout.addView(button,buttonParams);
//        //loading ad progress bar
//        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
//        layout.addView(progressBar);
//        progressBar.setVisibility(View.INVISIBLE);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Do something in response to button click
//                progressBar.setVisibility(View.VISIBLE);
//                loadAd();
//            }
//        });





    }


}