package com.example.pushuttletracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.*;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private Button button1;
    public CharSequence itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(HomePage.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                HomePage.this,
                                "You Clicked : " + item.getItemId(),
                                Toast.LENGTH_SHORT
                        ).show();
                        String selectedItemId = Integer.toString(item.getItemId()) ;
                        String  selectedItemString = item.getTitle().toString();
                        // CharSequence itemId = item.getTitle();
                        Intent intent = new Intent(HomePage.this,MapActivity.class);
                        intent.putExtra("itemId",selectedItemId);
                        intent.putExtra("itemString",selectedItemString);
                        startActivity(intent);

                        return true;

                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
        // setContentView();
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();

                if(id == R.id.myprofile)
                {
                    Toast.makeText(HomePage.this,"MapTracking", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomePage.this,MapActivity.class);
                    startActivity(intent);
                }
                 else if(id == R.id.settings){
                    Toast.makeText(HomePage.this,"Settings", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.editprofile){
                    Toast.makeText(HomePage.this,"EditProfile", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return  abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}


