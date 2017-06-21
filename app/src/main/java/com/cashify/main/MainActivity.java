package com.cashify.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.cashify.R;
import com.cashify.base.MoneyHelper;
import com.cashify.base.Refreshable;
import com.cashify.category.CategoryAddFragment;
import com.cashify.password.PasswordFiler;

import java.security.NoSuchAlgorithmException;

// Main activity with tabs
// Load the layout and set up things as well as the fragment adapter,
// MainFragmentsAdapter handles everything else

public class MainActivity extends AppCompatActivity implements PasswordCheckFragment.Listener {

    private final DialogFragment passwordFragment = new PasswordCheckFragment();
    private PasswordFiler passwordFiler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wtab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.app_name));
        passwordFragment.setCancelable(false);

        try {
            passwordFiler = new PasswordFiler(getApplicationContext());
            if (passwordFiler.hasSetPassword()) passwordFragment.show(
                    getSupportFragmentManager(),
                    "password_check_diag"
            );
        } catch (NoSuchAlgorithmException ex) {

        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new MainFragmentsAdapter(getSupportFragmentManager(), MainActivity.this)
        );

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private final int tabs[] = new int[]{
                    R.string.title_activity_main,
                    R.string.title_activity_overview,
                    R.string.title_activity_einstellungen,
                    R.string.title_activity_help
            };

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionbar.setSubtitle(
                        getResources().getString(tabs[position])
                );
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

        View v = menu.findItem(R.id.switch_container).getActionView();
        Switch calSwitch = (Switch) v.findViewById(R.id.calendar_switch);

        calSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MoneyHelper.setFilter();
                } else {
                    MoneyHelper.unsetFilter();
                }
                refreshFragments();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new DatePickerFragment().show(getSupportFragmentManager(), "");
        return true;
    }

    private void refreshFragments() {
        for (Fragment f :  getSupportFragmentManager().getFragments()){
            if (f instanceof Refreshable) ((Refreshable) f).refresh();
        }
    }

    @Override
    public void onPasswordEntered(String password) {
        if (!passwordFiler.checkPassword(password)) {
            passwordFragment.dismiss();
            passwordFragment.show(getSupportFragmentManager(), "password_check_diag");
        } else {
            passwordFragment.dismiss();
        }
    }

    @Override
    public void onCancel() {
        finish();
    }
}
