package com.example.notebook.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.R;

public class Note_Edit_Detail extends AppCompatActivity {

    EditText editTitle, editContent;
    boolean deHeight = true;
    public static final int height = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__edit__detail);

        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_edit_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save:
                if (editTitle.getText().toString().trim().equalsIgnoreCase("") && editContent.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(Note_Edit_Detail.this, "Chưa được lưu", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                } else {
                    sendToMain();
                    Toast.makeText(Note_Edit_Detail.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete:
                Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.titleEdit:
                setHeightTitle();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setHeightTitle() {
        if (deHeight == true) {
            deHeight = false;
            editTitle.setHeight(0);
        } else {
            deHeight = true;
            editTitle.setLines(height);
        }
    }

    @Override
    public void onBackPressed() {
        if (editTitle.getText().toString().trim().equalsIgnoreCase("") && editContent.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(Note_Edit_Detail.this, "Chưa được lưu", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            sendToMain();
            Toast.makeText(Note_Edit_Detail.this, "Đã lưu", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    public void sendToMain() {
        Bundle bundle = new Bundle();
        bundle.putString("title", editTitle.getText().toString().trim());
        bundle.putString("content", editContent.getText().toString().trim());
        getIntent().putExtra("data", bundle);
        setResult(Note_Text_Fragment.RESULT_CODE, getIntent());
        finish();
    }

}
