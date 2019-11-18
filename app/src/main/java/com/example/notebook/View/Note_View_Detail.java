package com.example.notebook.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.Controller.DoubleTapListener;
import com.example.notebook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Note_View_Detail extends AppCompatActivity implements DoubleTapListener.DoubleTapCallback {

    public static int REQUEST_CODE4 = 119;
    public static int RESULT_CODE4 = 120;

    TextView viewTitle, viewContent;
    String id, title, content, date, time;
    String titleUpdate, contentUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__view__detail);

        viewTitle = (TextView) findViewById(R.id.view_title);
        viewContent = (TextView) findViewById(R.id.view_content);

        viewTitle.setOnClickListener(new DoubleTapListener(this));
        viewContent.setOnClickListener(new DoubleTapListener(this));

        receivedToMain();
    }

    public void receivedToMain() {
        id = getIntent().getStringExtra("IDS");
        title = getIntent().getStringExtra("TITLES");
        content = getIntent().getStringExtra("CONTENTS");
        date = getIntent().getStringExtra("DATES");
        time = getIntent().getStringExtra("TIMES");

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

    public void goToNoteUpdateFromView () {
        Intent intent2 = new Intent(this, Note_Update_from_View.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("t", title);
        bundle2.putString("c", content);
        intent2.putExtra("note", bundle2);
        startActivityForResult(intent2, REQUEST_CODE4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE4) {
            if (resultCode == RESULT_CODE4) {
                titleUpdate = data.getStringExtra("titless");
                contentUpdate = data.getStringExtra("contentss");
                viewTitle.setText(titleUpdate);
                viewContent.setText(contentUpdate);
                sendToMain();
            }
        }
    }

    public void sendToMain () {
        getIntent().putExtra("titleUpdate", titleUpdate);
        getIntent().putExtra("contentUpdate", content);
        setResult(MainActivity.RESULT_CODE3, getIntent());
        finish();
    }

    @Override
    public void onDoubleClick(View v) {
        goToNoteUpdateFromView();
    }
}