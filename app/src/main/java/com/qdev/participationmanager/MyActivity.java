/* Copyright (C) 2015 Payton Quinn
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package com.qdev.participationmanager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

// Sets up the main activity containing an option for adding new classes, an "about" option,
// and two tabs: one for calling on students from various classes, the other for
// listing current statistics about each class and students within those classes.
public class MyActivity extends Activity {
    private ActionBar.Tab tab1, tab2, tab3;
    private fragmentTab1 fT1;
    private Fragment fragmentTab2;
    private ActionBar actionBar;
    private final Context context;

    // Constructs a new MyActivity
    public MyActivity() {
        actionBar = null;
        fT1 = new fragmentTab1();
        fragmentTab2 = new fragmentTab2();
        context = this;
    }

    // Creates and loads the app's two classes and statistics tabs using a passed Bundle.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_my);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        tab1 = actionBar.newTab().setText("Classes");
        tab2 = actionBar.newTab().setText("Stats");
        File dir = new File(context.getFilesDir().getPath());
        String[] values = dir.list();
        if (values.length == 0) {
            tab1.setTabListener(new MyTabListener(fT1));
            tab2.setTabListener(new MyTabListener(fragmentTab2));
        } else {
            tab1.setTabListener(new MyTabListener(fT1));
            tab2.setTabListener(new MyTabListener(fragmentTab2));
        }
        tab2.setTabListener(new MyTabListener(fragmentTab2));
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
    }

    // Take a MenuItem and then loads app's options menu including the class addition option.
    // Returns a boolean indicating the success of creating the options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Takes a MenuItem and then loads an activity for creating a new class when class addition
    // button is selected, loads a dialog box when the "about" option of the options menu is
    // selected, giving the user information on two presented options:
    // the option to view the terms of the app's license on an external website
    // and the option to access the app's documentation on an external website
    //
    // Returns a boolean indicating whether the selection has resulted in a successful execution.
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Enter name of new class:");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        CharSequence value = input.getText();
                        String fileName = value + ".txt";
                        File dirName = new File(getApplicationContext().getFilesDir().getPath(), fileName);
                        try {
                            dirName.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fT1.reload(context);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
                return true;
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage("For instructions on using this application, please tap " +
                        "'Visit Website' below.\n\n" +
                        "Participation Manager is licensed under " +
                        "the terms of the GNU General Public License v3.0.  " +
                        "To view the terms of the GNU General Public License v3.0 " +
                        "tap 'License' below.").setCancelable(true);
                builder.setPositiveButton("License", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.gnu.org/licenses/gpl-3.0.txt"));
                        startActivity(browserIntent);
                    }
                });
                builder.setNeutralButton("Visit Website", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://github.com/paytonq/ParticipationManager"));
                        startActivity(browserIntent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}