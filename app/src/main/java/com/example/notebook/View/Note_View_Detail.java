package com.example.notebook.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.Controller.DoubleTapListener;
import com.example.notebook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Note_View_Detail extends AppCompatActivity implements DoubleTapListener.DoubleTapCallback {

    public static int REQUEST_CODE4 = 121;
    public static int RESULT_CODE4 = 122;

    TextView viewTitle, viewContent;
    String id, title, content, date, time, firstdate, lasteditdate;
    static String lastviewdate;
    String titleUpdate, contentUpdate;
    TextView firstDate, lastEditDate, lastViewDate;
    Button btnOK;

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__view__detail);

        mapping();

        receivedToMain();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void mapping () {
        viewTitle = (TextView) findViewById(R.id.view_title);
        viewContent = (TextView) findViewById(R.id.view_content);
        scrollView = (ScrollView) findViewById(R.id.scroll);

        scrollView.setOnClickListener(new DoubleTapListener(this));
        viewTitle.setOnClickListener(new DoubleTapListener(this));
        viewContent.setOnClickListener(new DoubleTapListener(this));
    }

    public void receivedToMain() {
        id = getIntent().getStringExtra("IDS");
        title = getIntent().getStringExtra("TITLES");
        content = getIntent().getStringExtra("CONTENTS");
        lasteditdate = getIntent().getStringExtra("LAST");
        firstdate = getIntent().getStringExtra("FIRSTDATE");

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
                goToNoteUpdateFromView();
                break;
            case R.id.remove:
                createDialog();
                break;
            case R.id.information:
                createInforDialog();
                break;
            case R.id.copy:
                getIntent().putExtra("titleCopy", title);
                getIntent().putExtra("contentCopy", content);
                setResult(Note_Text_Fragment.RESULT_CODE32, getIntent());
                Toast.makeText(this, "Đã tạo bản sao", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToNoteUpdateFromView() {
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

    public void sendToMain() {
        getIntent().putExtra("titleUpdate", titleUpdate);
        getIntent().putExtra("contentUpdate", contentUpdate);
        setResult(Note_Text_Fragment.RESULT_CODE3, getIntent());
        finish();
    }

    public void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setCancelable(false);
        builder.setMessage("Bạn có chắc muốn xóa ghi chú này?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Note_View_Detail.this, "Đã hủy", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Note_View_Detail.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                setResult(Note_Text_Fragment.RESULT_CODE31, getIntent());
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createInforDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View aleartInflater = inflater.inflate(R.layout.dialog_information, null);

        firstDate = (TextView) aleartInflater.findViewById(R.id.first_time);
        lastEditDate = (TextView) aleartInflater.findViewById(R.id.edit_last_time);
        lastViewDate = (TextView) aleartInflater.findViewById(R.id.view_last_time);
        btnOK = (Button) aleartInflater.findViewById(R.id.OK);

        firstDate.setText(firstdate);
        lastViewDate.setText(lastviewdate);
        lastEditDate.setText(lasteditdate);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(aleartInflater);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        date = dateFormat.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());

        lastviewdate = time + ", " + date;
        super.onBackPressed();
    }

    @Override
    public void onSingleClick(View v) {
        Toast.makeText(this, "Nhấn đúp để chỉnh sửa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleClick(View v) {
        goToNoteUpdateFromView();
    }
}