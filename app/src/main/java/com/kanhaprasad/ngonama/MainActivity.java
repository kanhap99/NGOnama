package com.kanhaprasad.ngonama;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;


public class MainActivity extends AppCompatActivity {
    //declarations
    private WebView about,events,blog, search;
    private WebView webViews[] = new WebView[4];
    private ProgressBar progress;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.about, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.events, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.search, R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.blog, R.color.colorAccent);
// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#C3C6C7"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#97D734"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#C3C6C7"));

// Add or remove notification for each item
        bottomNavigation.setNotification("4", 1);
        bottomNavigation.setNotification("", 1);

        //initializing the webviews
        about = (WebView) findViewById(R.id.about_us);
        events = (WebView) findViewById(R.id.events);
        search = (WebView) findViewById(R.id.search);
        blog = (WebView) findViewById(R.id.blog);

        //initializing webviews array for later
        webViews[0] = about;
        webViews[1] = events;
        webViews[2] = search;
        webViews[3] = blog;


        //config the inbuilt browser for each webview
        for(WebView webview: webViews) {
            configWebView(webview);
        }
        //load each webviews URL's

        about.loadUrl("http://ngonama.org/about-us");
        events.loadUrl("http://ngonama.org/forums");
        search.loadUrl("http://ngonama.org/search-ngo");
        blog.loadUrl("http://ngonama.org/blog");


        //Progress bar for textview
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        progressText = (TextView) findViewById(R.id.progressText);

        // Set listener for bottom nav
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                toggle(position);
                return true;
            }
        });
    }

    //method to display noInternet webview incase there is no internet connection




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

       // return super.onOptionsItemSelected(item);
    //}

    @Override
    public void onBackPressed() {
        if (events.isFocused() && events.canGoBack())
            events.goBack();
        else if (blog.isFocused() && blog.canGoBack())
            blog.goBack();
        else if (search.isFocused() && search.canGoBack())
            search.goBack();
        else
            super.onBackPressed();
    }
    private void toggle(int position){
        WebView views[] = {about, events, search, blog};
        int count = 0;
        for(WebView view: views) {
            if(position == count)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.INVISIBLE);
            count++;
        }
    }
    private void configWebView(final WebView webview) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webview.loadUrl("javascript:  (function (){ " +
                        "document.getElementsByTagName('header')[0].style.display=\"none\";" +
                        "document.getElementById('header-title').style.display=\"none\";"+
                        "document.getElementById('header-very-top').style.display=\"none\";"+
                        "document.getElementById('sidebar').style.display=\"none\";"+
                        "document.getElementsByClassName('footer-bottom')[0].style.display=\"none\";"+
                        "document.getElementById('whats-new-form').style.display=\"none\";"+
                        "}) () ");
                MainActivity.this.progress.setVisibility(View.INVISIBLE);
                MainActivity.this.progressText.setVisibility(View.INVISIBLE);
            }
        });
        webview.setWebChromeClient(new MyWebViewClient());


    }
    public void setValue(int progress) {
        this.progress.setProgress(progress);

    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            MainActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }
}
