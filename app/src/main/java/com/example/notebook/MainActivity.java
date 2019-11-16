package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 113;
    public static final int RESULT_CODE = 114;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    NoteAdapter noteAdapter;
    List<Note> notes;
    RecyclerView recyclerView;
    String id, title, content, date, time;
    int position;
    int position2;

    boolean back = false;

    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        floatButtonSelected();
        navSelected();
    }

    public void navSelected() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //do something
                switch (menuItem.getItemId()) {
                    case R.id.note:
                        Toast.makeText(MainActivity.this, "Ghi chú văn bản", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bankcard:
                        Toast.makeText(MainActivity.this, "Tài khỏa ngân hàng", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.spices:
                        Toast.makeText(MainActivity.this, "Các đồ hương liệu", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Cài đặt", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    public void floatButtonSelected() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNoteEditDetail();
            }
        });
    }

    public void mapping() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        notes = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(notes);
        recyclerView.setAdapter(noteAdapter);

        createDatabase();

        noteAdapter.notifyDataSetChanged();

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positions) {
                position = positions;
                goToNoteViewDetail();
            }
        });

        noteAdapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view) {
                position2 = position;
                registerForContextMenu(view);
            }
        });
    }

    public void delete() {
        String idNt = notes.get(position2).getId();
        database.QueryData("DELETE FROM NOTES WHERE ID = '" + idNt + "' ");
        notes.remove(notes.get(position2));
        noteAdapter.notifyDataSetChanged();
    }

    public void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setCancelable(false);
        builder.setMessage("Bạn có chắc muốn xóa ghi chú này?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Đã hủy", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
                Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createDatabase() {
        database = new Database(this, "note.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS NOTES(ID VARCHAR(100) PRIMARY KEY, TITLE VARCHAR(200), CONTENT TEXT, DATE VARCHAR(20), TIME VARCHAR(20))");

        Cursor cursor = database.GetData("SELECT * FROM NOTES");
        notes.clear();
        while (cursor.moveToNext()) {
            String idNote = cursor.getString(0);
            String titleNote = cursor.getString(1);
            String contentNote = cursor.getString(2);
            String dateNote = cursor.getString(3);
            String timeNote = cursor.getString(4);
            notes.add(new Note(idNote, titleNote, contentNote, dateNote, timeNote));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            //do something
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToNoteEditDetail() {
        Intent intent = new Intent(this, Note_Edit_Detail.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                Bundle bundle = data.getBundleExtra("data");
                title = bundle.getString("title");
                content = bundle.getString("content");
                Note note = new Note(title, content);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                date = dateFormat.format(calendar.getTime());
                time = timeFormat.format(calendar.getTime());
                UUID uuid = UUID.randomUUID();
                id = uuid.toString();

                addObjectDatabase(id, title, content, date, time);
            }
        }
    }

    public void addObjectDatabase(String id, String title, String content, String date, String time) {
        database.QueryData("INSERT INTO NOTES VALUES('" + id + "', '" + title + "', '" + content + "', '" + date + "', '" + time + "')");

        Cursor cursor = database.GetData("SELECT * FROM NOTES");
        notes.clear();
        while (cursor.moveToNext()) {
            String idNote = cursor.getString(0);
            String titleNote = cursor.getString(1);
            String contentNote = cursor.getString(2);
            String dateNote = cursor.getString(3);
            String timeNote = cursor.getString(4);
            notes.add(new Note(idNote, titleNote, contentNote, dateNote, timeNote));
        }
        noteAdapter.notifyDataSetChanged();
    }

    public void goToNoteViewDetail() {
        Intent intent2 = new Intent(MainActivity.this, Note_View_Detail.class);
        intent2.putExtra("IDS", notes.get(position).getId());
        intent2.putExtra("TITLES", notes.get(position).getTitle());
        intent2.putExtra("CONTENTS", notes.get(position).getContent());
        intent2.putExtra("DATES", notes.get(position).getDate());
        intent2.putExtra("TIMES", notes.get(position).getTime());
        startActivity(intent2);
    }

    @Override
    public void onBackPressed() {
        if (back == false) {
            Toast.makeText(this, "Nhấn back một lần nữa để thoát ứng dụng!", Toast.LENGTH_LONG).show();
            back = true;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_item_edit:
                Toast.makeText(this, "Chỉnh sửa", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.context_item_delete:
                createDialog();
                return true;
            case R.id.context_item_copy:
                Toast.makeText(this, "Copy", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_item, menu);
    }
}
