package pl.op.danex11.alarmscalculator840c;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    boolean initializationDone = false;
    //private
    int adCounter = 0;
    final String[] result = {"waiting"};

    //Detect app going to background, foreground
    //AppLifecycleObserver appLifecycleObserver = new AppLifecycleObserver();

    //Alarm enter view
    EditText alrmtyped;
    FrameLayout framelayout;

//    List<String> testDeviceIds = Arrays.asList("BE1C908D4C08BF07D2D06FB52FFC9020");
//    RequestConfiguration configuration =
//            new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();

    public static void hideSoftwareKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }


//    public void showSoftKeyboard(View view){
//        Log.w("logtag", "showkeyboard");
//        view.requestFocus();
//        if(view.requestFocus()){
//            Log.w("logtag", "requestfocus");
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            Log.w("logtag", "imm" + imm);
//            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(MainActivity.this, "onCreate()", Toast.LENGTH_LONG).show();

        //showKeyboard();
        //  Log.v("lineseparatoris", System.getProperty("line.separator"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        //background
        //ColorDrawable transp = new ColorDrawable();
        int vdarkgray = getResources().getColor(R.color.sim_very_dark_gray);
        ColorDrawable backgr_vdarkgray = new ColorDrawable(vdarkgray);
        getWindow().setBackgroundDrawable(backgr_vdarkgray);


        new Handler().postDelayed(() -> { //your code
            //Detect app going to background, foreground
            //  ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);

            //Alarm enter view
            alrmtyped = findViewById(R.id.alarmtyped);
//            alrmtyped.setInputType(InputType.TYPE_CLASS_NUMBER);
            alrmtyped.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
            imm.showSoftInput(alrmtyped, InputMethodManager.SHOW_IMPLICIT);


            //ENTER     ENTER   ENTER   ENTER
            alrmtyped.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    //CLICKED Enter
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String alarm = checkAlarmErrors(alrmtyped.getText().toString());
                        //vvvvvvvvvvvv
                        CalculateAlarm calca = new CalculateAlarm(alarm);
                        //^^^^^^^^^^^^
                        result[0] = calca.getDbAddress();
                        EditText resultETxt = findViewById(R.id.dbbresult);
                        resultETxt.setText(result[0]);
                        return true;
                    }
                    return false;
                }
            });
        }, 500);

    }

    public String checkAlarmErrors(String alarmtyped) {
        Log.w("logtag", "typed " + alarmtyped);
        boolean wrongAlert = false;

        if (alarmtyped.length() != 6) {
            Toast.makeText(MainActivity.this, "Alarm - needs to be 6 digits long.", Toast.LENGTH_LONG).show();
            wrongAlert = true;
            alarmtyped = "000000";
            return alarmtyped;
        }

        String digs12 = alarmtyped.substring(0, 2);
        Log.w("logtag", "digs12 " + digs12);
        if (!digs12.equals("50") && !digs12.equals("51") && !digs12.equals("52") && !digs12.equals("53") && !digs12.equals("54") && !digs12.equals("55") && !digs12.equals("56") && !digs12.equals("57") && !digs12.equals("58")
                && !digs12.equals("60") && !digs12.equals("70")) {
            Toast.makeText(MainActivity.this, "Alarm - first two digits wrong.", Toast.LENGTH_LONG).show();
            wrongAlert = true;
            alarmtyped = "000000";
            return alarmtyped;
        }

        String dig3 = alarmtyped.substring(2, 3);
        Log.w("logtag", "dig3 " + dig3);
        //System.out.print(dig3);
        if (!dig3.equals("0") && !dig3.equals("1") && !dig3.equals("2") && !dig3.equals("3")) {
            Toast.makeText(MainActivity.this, "Alarm - mistaken third digit.", Toast.LENGTH_LONG).show();
            wrongAlert = true;
        }

        String digs34 = alarmtyped.substring(2, 4);
        Log.w("logtag", "digs34 " + digs34);
        if ((digs12.equals("70") && Integer.parseInt(digs34) > 31)
                ||
                (digs12.equals("60") && Integer.parseInt(digs34) > 18)
                ||
                digs12.equals("50") && !digs34.equals("00") && !digs34.equals("01") && !digs34.equals("02") && !digs34.equals("03") && !digs34.equals("11") && !digs34.equals("12") && !digs34.equals("13")) {
            Toast.makeText(MainActivity.this, "Alarm - third/fourth digits incorrect.", Toast.LENGTH_LONG).show();
            wrongAlert = true;
        }

        String digs56 = alarmtyped.substring(4, 6);
        Log.w("logtag", "digs56 " + digs56);
        if ((digs12.equals("70") && Integer.parseInt(digs56) > 63)
                ||
                (digs12.equals("60") && Integer.parseInt(digs56) > 15)
                ||
                digs12.equals("50") && Integer.parseInt(digs56) > 63) {
            Toast.makeText(MainActivity.this, "Alarm - fifth and sixth digits too high.", Toast.LENGTH_LONG).show();
            wrongAlert = true;
        }

        if (wrongAlert) {
            alarmtyped = "000000";
        }
        if (!wrongAlert) {
            //OK!
            //CALCULATE
            //hideSoftKeyboard(this.getCurrentFocus());
            Toast.makeText(MainActivity.this, "OK !", Toast.LENGTH_LONG).show();
            EditText alrmtyped = findViewById(R.id.alarmtyped);
            hideSoftKeyboard(alrmtyped);
            //show ad
            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            Log.w("logtag", "adloaded!!!!");
        }
        return alarmtyped;
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, InputMethodManager.H);
        // imm.hideSoftInputFromWindow(alrmtyped,InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public void loadBug(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "alarmscalc840d.userreport@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.emsubject));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.emmessage));
        startActivity(Intent.createChooser(intent, "Report bugs and observations via email:"));
    }


    public void loadCoffee(View view) {
        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
        myWebLink.setData(Uri.parse(getResources().getString(R.string.coffeelink)));
        startActivity(myWebLink);
    }


    public void loadInfo(View view) {
        FrameLayout framelayout = findViewById(R.id.framelayout);
        framelayout.setTranslationZ(+10);
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View viewL =
                inflater.inflate(R.layout.infolayout, framelayout, false);
        framelayout.addView(viewL);
        // .(fireText.replace("\\n", "\n"));
        //        setText(findViewById(R.string.info).replace("\\n", "\n"));;
    }

    public void removeInfo(View view) {
        FrameLayout framelayout = findViewById(R.id.framelayout);
        framelayout.setTranslationZ(-10);
        framelayout.removeAllViews();
    }


//    private void closeKeyboard() {
//        // this will give us the view
//        // which is currently focus
//        // in this layout
//        View view = this.getCurrentFocus();
//
//        // if nothing is currently
//        // focus then this will protect
//        // the app from crash
//        if (view != null) {
//
//            // now assign the system
//            // service to InputMethodManager
//            InputMethodManager manager
//                    = (InputMethodManager)
//                    getSystemService(
//                            Context.INPUT_METHOD_SERVICE);
//            manager
//                    .hideSoftInputFromWindow(
//                            view.getWindowToken(), 0);
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> { //your code
            framelayout = findViewById(R.id.framelayout);
            framelayout.setTranslationZ(-10);

            //delete this line for release version
//            MobileAds.setRequestConfiguration(configuration);
            //AdMob 2 : initialize
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });


        }, 600);
    }

    //onRestart is called after onStop


    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(MainActivity.this, "onRestart()", Toast.LENGTH_LONG).show();
        //getCurrentFocus().clearFocus();
        alrmtyped = findViewById(R.id.alarmtyped);
        alrmtyped.clearFocus();
        // hideSoftKeyboard(alrmtyped);
        hideSoftwareKeyboard(this);
        //closeKeyboard();


    }

    @Override
    protected void onStop() {
        super.onStop();
//
//        appLifecycleObserver.onEnterBackground();

        //lad new ad when app is not visible
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//
//        View current = getCurrentFocus();
//        if (current != null) current.clearFocus();
        //prevent keyboard staying onscreen when minimalized
//        hideSoftKeyboard(alrmtyped);
//        closeKeyboard();

        // Check if no view has focus:
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);}


    }


//    public class AppLifecycleObserver implements LifecycleObserver {
//
//        //public  final String TAG = pl.op.danex11.alarmscalculator840c.AppLifecycleObserver.class.getName();
//
//        @OnLifecycleEvent(Lifecycle.Event.ON_START)
//        public void onEnterForeground() {
//            // Toast.makeText(MainActivity.this, "onEnterForeground()", Toast.LENGTH_LONG).show();
//            //clearfocus
//
//            //run the code we need
//            // closeKeyboard();
//            //clear focus
//            // alrmtyped = findViewById(R.id.alarmtyped);
//            // View current = getCurrentFocus();
//            //  if (current != null) current.clearFocus();
//            // hideSoftKeyboard(alrmtyped);
//
//        }
//
//        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//        public void onEnterBackground() {
//          //  Log.w("logtag", "onEnterBackground");
//         //   alrmtyped = findViewById(R.id.alarmtyped);
////            alrmtyped.setInputType(InputType.TYPE_CLASS_NUMBER);
//            //getCurrentFocus().clearFocus();
//            //closeKeyboard();
//            //hideSoftKeyboard(alrmtyped);
////            closeKeyboard();
////            alrmtyped = findViewById(R.id.alarmtyped);
////            View current = getCurrentFocus();
////            if (current != null) current.clearFocus();
//            //run the code we need
//        }
//
//    }


}