package com.example.notebook.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.R;

public class Note_Update_Detail extends AppCompatActivity {

    String title, content;
    EditText editTitle, editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__update__detail);

        editTitle = (EditText) findViewById(R.id.update_title);
        editContent = (EditText) findViewById(R.id.update_content);

        receivedFromMain();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void receivedFromMain() {
        title = getIntent().getStringExtra("ti");
        content = getIntent().getStringExtra("co");

        if (title.equalsIgnoreCase("")) {
            editTitle.setHeight(0);
            editContent.setText(content);
        } else {
            editTitle.setText(title);
            editContent.setText(content);
        }
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
                    Toast.makeText(Note_Update_Detail.this, "Chưa được lưu", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                } else {
                    sendToMain();
                    Toast.makeText(Note_Update_Detail.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete:
                Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.titleEdit:
                setHeightTitle();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setHeightTitle() {
        if (editTitle.getHeight() == 0) {
            editTitle.setLines(Note_Edit_Detail.height);
        } else {
            editTitle.setHeight(0);
        }
    }

    public void sendToMain() {
        getIntent().putExtra("titless", editTitle.getText().toString().trim());
        getIntent().putExtra("contentss", editContent.getText().toString().trim());
        setResult(Note_Text_Fragment.RESULT_CODE2, getIntent());
        finish();
    }

    @Override
    public void onBackPressed() {
        if (editTitle.getText().toString().trim().equalsIgnoreCase("") && editContent.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(Note_Update_Detail.this, "Chưa được lưu", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            sendToMain();
            Toast.makeText(Note_Update_Detail.this, "Đã lưu", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }
}