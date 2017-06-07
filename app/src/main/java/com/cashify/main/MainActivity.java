package com.cashify.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cashify.R;
import com.cashify.base.DatePickerFragment;
import com.cashify.base.Refreshable;

// Main activity with tabs
// Load the layout and set up the fragment adapter,
// adapter handles everything else

public class MainActivity extends AppCompatActivity {

    private MainFragmentsAdapter fragAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wtab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.title_activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentsAdapter(
                getSupportFragmentManager(),
                MainActivity.this,
                actionbar
        ));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private final String tabs[] = new String[]{
                    "Start",
                    "Übersicht",
                    "Einstellungen",
                    "Hilfe"
            };

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionbar.setSubtitle(tabs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                new DatePickerFragment().show(getSupportFragmentManager(), "");
        }
        return true;
    }


}
