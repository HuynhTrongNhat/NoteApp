package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Note_View_Detail extends AppCompatActivity {

    TextView viewTitle, viewContent;
    String id, title, content, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__view__detail);

        viewTitle = (TextView) findViewById(R.id.view_title);
        viewContent = (TextView) findViewById(R.id.view_content);

        receivedToMain();
    }

    public void receivedToMain() {
        Intent intent = getIntent();
        id = getIntent().getStringExtra("IDS");
        title = getIntent().getStringExtra("TITLES");
        content = intent.getStringExtra("CONTENTS");
        date = intent.getStringExtra("DATES");
        time = intent.getStringExtra("TIMES");

        if (title.equalsIgnoreCase("")) {
            viewTitle.setHeight(0);
            viewContent.setText(content);
        } else {
            viewTitle.setText(title);
            viewContent.setText(content);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_view_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit:
                Toast.makeText(this, "Chỉnh sửa", Toast.LENGTH_LONG).show();
                break;
            case R.id.remove:
                Toast.makeText(this, "Xóa", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.information:
                Toast.makeText(this, "Thông tin", Toast.LENGTH_LONG).show();
                finish();
                break;

            case R.id.copy:
                Toast.makeText(this, "Copy", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void goToNoteEditUpdate () {
        Intent intent2 = new Intent(this, Note_Edit_Detail.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("i", id);
        bundle2.putString("t", title);
        bundle2.putString("c", content);
        bundle2.putString("d", date);
        bundle2.putString("tm", time);
        intent2.putExtra("note", bundle2);
        startActivity(intent2);
    }*/
}