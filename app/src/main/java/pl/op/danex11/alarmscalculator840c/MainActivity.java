package pl.op.danex11.alarmscalculator840c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
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
    int adCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // initialize the Google Mobile Ads SDK
        // initializes the SDK and calls back a completion listener once initialization is complete (or after a 30-second timeout).
        // This needs to be done only once, ideally at app launch.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                initializationDone = true;
                Log.e(TAG, "initializationDone = true SEt");
                loadAd();
            }
        });
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



    }


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
        } else {
            Log.e(TAG, "The rewarded ad wasn't ready yet.");
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