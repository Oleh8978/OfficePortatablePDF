package oleh.app.documenteditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Reader extends AppCompatActivity implements  OnPageChangeListener {
    PDFView pdfView;
    public InterstitialAd interstitialAd;
    private AdView adView;
    public String pageNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        //small banner
        MobileAds.initialize(this, "ca-app-pub-6131354410731725/4327678198");

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView.loadAd(adRequest1);
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
                     Intent intent = new Intent(Reader.this, MainActivity.class);
                     startActivity(intent);
                     finish();

                }catch (Exception e){
                    ///
                }
            }
        });
        // finish function adds

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        bottomNavigationView.setSelectedItemId(R.id.reader);

        Intent intent = getIntent();
        String location = intent.getStringExtra(MainActivity.SEND_TEXT);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromUri(Uri.parse(location))
                .defaultPage(0)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                    progressBar.setVisibility(View.GONE);
                                                          }
                      })
                .onPageChange((OnPageChangeListener) this)
                .load();






    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Intent option = null;
                    switch (menuItem.getItemId()){
                        case R.id.home:
                            if (interstitialAd.isLoaded()) {
                                interstitialAd.show(); // show banner
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case R.id.reader:
                            Toast.makeText(getApplicationContext(), "You are on the Reader section ", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.exit:
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            finish();
                            System.exit(1);
                            break;

                    }
                    return true;
                }

            };

    @Override
    public void onPageChanged(int page, int pageCount) {
            pageNumber = String.valueOf(page);
        TextView pagetextnumber = (TextView) findViewById(R.id.pageNumber);
        pagetextnumber.setText(pageNumber);
            //do what you want with the pageNumber
        }

}
