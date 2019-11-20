package com.example.notebook.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.notebook.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    boolean back = false;

    Note_Text_Fragment note_text_fragment;
    Note_Bank_Fragment note_bank_fragment;
    Note_Spice_Fragment note_spice_fragment;
    Note_BirthDay_Fragment note_birthDay_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        note_text_fragment = new Note_Text_Fragment();
        note_bank_fragment = new Note_Bank_Fragment();
        note_spice_fragment = new Note_Spice_Fragment();
        note_birthDay_fragment = new Note_BirthDay_Fragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, note_text_fragment).commit();
            navigationView.setCheckedItem(R.id.note);
        }
    }

    public void navSelected() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.note:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, note_text_fragment).commit();
                        Toast.makeText(MainActivity.this, "Ghi chú văn bản", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bankcard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, note_bank_fragment).commit();
                        getSupportActionBar().setTitle("Tài khoản ngân hàng");
                        Toast.makeText(MainActivity.this, "Tài khỏa ngân hàng", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.birthday:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, note_birthDay_fragment).commit();
                        getSupportActionBar().setTitle("Ngày sinh nhật");
                        Toast.makeText(MainActivity.this, "Ngày sinh nhật", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.spices:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, note_spice_fragment).commit();
                        getSupportActionBar().setTitle("Các đồ hương liệu");
                        Toast.makeText(MainActivity.this, "Các đồ hương liệu", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Cài đặt", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    public void mapping () {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navSelected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.setting) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (back == false) {
                Toast.makeText(this, "Nhấn back một lần nữa để thoát ứng dụng!", Toast.LENGTH_SHORT).show();
                back = true;
            } else {
                super.onBackPressed();
            }
        }
    }

}