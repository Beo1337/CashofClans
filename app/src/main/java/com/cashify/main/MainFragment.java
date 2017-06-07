package com.cashify.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cashify.R;
import com.cashify.add.AddActivity;
import com.cashify.base.MoneyHelper;
import com.cashify.base.Refreshable;
import com.cashify.database.DatabaseHelper;


// Lightweight fragment version of Main activity
// To get views, we must work with getActivity() here, otherwise explodes with null pointer exceptions.
public class MainFragment extends Fragment implements Refreshable {

    private TextView totalAmountView;       // View that display combined sum of income and expenses
    private SharedPreferences sharedPref;   // Computation needs that for some reason
    private DatabaseHelper dbHelper;        // Database helper object to retrieve database entries

    // Context for Database helper (and others) provided through onAttach event
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbHelper = new DatabaseHelper(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // On View instantiation, load and return fragment layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // Calculate amount of money left (hopefully) and refresh view
    public void refresh() {
        double amount = MoneyHelper.count(MoneyHelper.filter(dbHelper.getEntries()));
        totalAmountView = (TextView) getActivity().findViewById(R.id.total_amount);

        if (totalAmountView == null) return;
        totalAmountView.setText(String.valueOf(amount) + "€");
        totalAmountView.setTextColor(amount < 0 ? Color.RED : Color.GREEN);
    }

    // Once we are ready to go, set up event logic here and display stuff
    @Override
    public void onStart() {
        super.onStart();
        refresh(); // Get a first tally of income and expenses


        // Get the big plus and minus buttons and attach click handlers to them
        // These open up new intents to add entries, after that is done refresh as above
        Button addButton, subButton;
        //Buttons for shortcuts
        final ImageButton shortcutButton1,shortcutButton2,shortcutButton3,shortcutButton4,shortcutButton5,shortcutButton6;
        addButton = (Button) getActivity().findViewById(R.id.main_add);
        subButton = (Button) getActivity().findViewById(R.id.main_sub);

        shortcutButton1 = (ImageButton) getActivity().findViewById(R.id.imageButton1);
        shortcutButton2 = (ImageButton) getActivity().findViewById(R.id.imageButton2);
        shortcutButton3 = (ImageButton) getActivity().findViewById(R.id.imageButton3);
        shortcutButton4 = (ImageButton) getActivity().findViewById(R.id.imageButton4);
        shortcutButton5 = (ImageButton) getActivity().findViewById(R.id.imageButton5);
        shortcutButton6 = (ImageButton) getActivity().findViewById(R.id.imageButton6);




        shortcutButton1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortcut(shortcutButton1);
                    }
                }
        );

        shortcutButton2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortcut(shortcutButton2);
                    }
                }
        );

        shortcutButton3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortcut(shortcutButton3);
                    }
                }
        );

        shortcutButton4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortcut(shortcutButton4);
                    }
                }
        );

        shortcutButton5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortcut(shortcutButton5);
                    }
                }
        );

        shortcutButton6.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortcut(shortcutButton6);
                    }
                }
        );

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AddActivity.class);
                        i.putExtra("mode", "add");
                        startActivityForResult(i, 0);
                        refresh();
                    }
                }
        );

        subButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AddActivity.class);
                        i.putExtra("mode", "sub");
                        startActivityForResult(i, 0);
                        refresh();
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void shortcut(View v) {
        ImageButton b = (ImageButton) this.getActivity().findViewById(v.getId());
        Log.i("MainActivity", "Shortcut: " + b.getTag().toString() + " gewählt");
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", b.getTag().toString());
        startActivityForResult(i, 0);
    }
}
