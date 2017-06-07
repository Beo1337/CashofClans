package com.cashify.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        totalAmountView.setText(String.valueOf(amount) + "€");
        if (amount < 0)
            totalAmountView.setTextColor(Color.RED);
        else
            totalAmountView.setTextColor(Color.GREEN);
    }

    // Once we are ready to go, set up event logic here and display stuff
    @Override
    public void onStart() {
        super.onStart();
        refresh(); // Get a first tally of income and expenses


        // Get the big plus and minus buttons and attach click handlers to them
        // These open up new intents to add entries, after that is done refresh as above
        Button addButton, subButton;
        addButton = (Button) getActivity().findViewById(R.id.main_add);
        subButton = (Button) getActivity().findViewById(R.id.main_sub);

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
}
