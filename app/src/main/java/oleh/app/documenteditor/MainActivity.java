package oleh.app.documenteditor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public InterstitialAd interstitialAd; // banner

   public static final String SEND_TEXT = "";
    Button select;


    private static final int REQUEST_CODE = 43; //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // start
        MobileAds.initialize(this, "ca-app-pub-6131354410731725~8555246551");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-6131354410731725/7126066076");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        //finish

        //function for adds
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                try {


                }catch (Exception e){
                    ///
                }
            }
        });
        // finish function adds

        RelativeLayout relativeLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        bottomNavigationView.setSelectedItemId(R.id.home);

    }


    private void startSearch(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data != null) {
                Uri uri = data.getData();
                String location = uri.toString();
                Toast.makeText(this, "Path:" + uri.getPath(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Reader.class);
                i.putExtra(String.valueOf(SEND_TEXT),  location);
                startActivity(i);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                     Intent option = null;
                     switch (menuItem.getItemId()){
                         case R.id.home:
                             startSearch();
                             break;
                         case R.id.reader:
                             Toast.makeText(getApplicationContext(), "Please choose storage to select the file", Toast.LENGTH_LONG).show();
                             break;
                         case R.id.exit:
                             if (interstitialAd.isLoaded()) {
                                 interstitialAd.show(); // show banner
                             } else {
                                 moveTaskToBack(true);
                                 android.os.Process.killProcess(android.os.Process.myPid());
                                 finish();
                                 System.exit(1);

                             }
                             break;
                     }
                     return true;
                }

            };



 }