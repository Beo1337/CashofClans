package com.cashify.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.cashify.R;
import com.cashify.overview.OverviewFragment;
import com.cashify.settings.SettingsFragment;

public class MainFragmentsAdapter extends FragmentPagerAdapter {

    // Identify tabs by icon and position, no text needed
    private final int tabIcons[] = new int[]{
            R.drawable.home,
            R.drawable.chart_donut,
            R.drawable.settings,
            R.drawable.help_circle
    };

    private final Context context;

    public MainFragmentsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabIcons.length;
    }

    // This is where we decide what do to for each tab
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new OverviewFragment();
            case 2:
                return (Fragment) new SettingsFragment();
            default:
                return TestFragment.newInstance(position + 1);
        }
    }

    // Set tab title - in our case set up a nice icon
    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sbBuilder = new SpannableStringBuilder(" ");
        Drawable drawable = ContextCompat.getDrawable(context, tabIcons[position]);
        drawable.setBounds(0, 0, 48, 48);
        ImageSpan iSpan = new ImageSpan(drawable);
        sbBuilder.setSpan(iSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sbBuilder;
    }
}