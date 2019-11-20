package com.example.notebook.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notebook.Adapter.NoteAdapter;
import com.example.notebook.Database.Database;
import com.example.notebook.Module.Note;
import com.example.notebook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class Note_Text_Fragment extends Fragment {

    public static final int REQUEST_CODE = 113;
    public static final int RESULT_CODE = 114;
    public static final int REQUEST_CODE2 = 115;
    public static final int RESULT_CODE2 = 116;
    public static final int REQUEST_CODE3 = 117;
    public static final int RESULT_CODE3 = 118;
    public static final int RESULT_CODE31 = 119;
    public static final int RESULT_CODE32 = 120;

    private String firstDate, lastEditDate, firstTimeCopy, idCopy, titleCopy, contentCopy, dateCopy, timeCopy;
    private String getIdCopy, getDateCopy, getTimeCopy, getFirstDateCopy;

    private NoteAdapter noteAdapter;
    private List<Note> notes;
    private RecyclerView recyclerView;
    private String id, title, content, date, time;
    private String titleUpdate, contentUpdate, dateUpdate, timeUpdate;
    private String titleUpdate2, contentUpdate2, dateUpdate2, timeUpdate2;
    private int position1;
    private int position2;

    Database database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapping();
        floatButtonSelected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_text, container, false);
    }


    public void floatButtonSelected() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNoteEditDetail();
            }
        });
    }

    public void mapping() {

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
        notes = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(getContext(), notes);
        recyclerView.setAdapter(noteAdapter);

        createDatabase();

        noteAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new NoteAdapter.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                position1 = position;
                goToNoteViewDetail();
            }

            @Override
            public void onLongClick(View view, int position) {
                position2 = position;
                registerForContextMenu(view);
            }
        }));
    }

    public void delete(int index) {
        String idNt = notes.get(index).getId();
        database.QueryData("DELETE FROM NOTES WHERE ID = '" + idNt + "' ");
        notes.remove(notes.get(index));
        noteAdapter.notifyDataSetChanged();
    }

    public void createDialog(final int index) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận xóa");
        builder.setCancelable(false);
        builder.setMessage("Bạn có chắc muốn xóa ghi chú này?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(index);
                Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createDatabase() {
        database = new Database(getActivity(), "note.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS NOTES(ID VARCHAR(100) PRIMARY KEY, TITLE VARCHAR(200), CONTENT TEXT, DATE VARCHAR(20), TIME VARCHAR(20), FIRSTDATE VARCHAR(40))");

        Cursor cursor = database.GetData("SELECT * FROM NOTES");
        notes.clear();
        while (cursor.moveToNext()) {
            String idNote = cursor.getString(0);
            String titleNote = cursor.getString(1);
            String contentNote = cursor.getString(2);
            String dateNote = cursor.getString(3);
            String timeNote = cursor.getString(4);
            String firstDateNote = cursor.getString(5);
            notes.add(new Note(idNote, titleNote, contentNote, dateNote, timeNote, firstDateNote));
        }
    }


    public void goToNoteEditDetail() {
        Intent intentNoteEdit = new Intent(getContext(), Note_Edit_Detail.class);
        startActivityForResult(intentNoteEdit, REQUEST_CODE);
    }

    public void goToNoteViewDetail() {
        Intent intent2 = new Intent(getContext(), Note_View_Detail.class);
        intent2.putExtra("IDS", notes.get(position1).getId());
        intent2.putExtra("TITLES", notes.get(position1).getTitle());
        intent2.putExtra("CONTENTS", notes.get(position1).getContent());
        intent2.putExtra("LAST", lastEditDate);
        intent2.putExtra("FIRSTDATE", notes.get(position1).getFirstDate());
        startActivityForResult(intent2, REQUEST_CODE3);
    }

    public void goToNoteUpdateDetail() {
        Intent intent3 = new Intent(getContext(), Note_Update_Detail.class);
        intent3.putExtra("ti", notes.get(position2).getTitle());
        intent3.putExtra("co", notes.get(position2).getContent());
        startActivityForResult(intent3, REQUEST_CODE2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                Bundle bundle = data.getBundleExtra("data");
                title = bundle.getString("title");
                content = bundle.getString("content");

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                date = dateFormat.format(calendar.getTime());
                time = timeFormat.format(calendar.getTime());

                firstDate = time + ", " + date;

                UUID uuid = UUID.randomUUID();
                id = uuid.toString();

                addObjectDatabase(id, title, content, date, time, firstDate);
            }
        }
        if (requestCode == REQUEST_CODE2) {
            if (resultCode == RESULT_CODE2) {
                titleUpdate = data.getStringExtra("titless");
                contentUpdate = data.getStringExtra("contentss");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                dateUpdate = dateFormat.format(calendar.getTime());
                timeUpdate = timeFormat.format(calendar.getTime());

                lastEditDate = timeUpdate + ", " + dateUpdate;

                updateObjectDatabase(titleUpdate, contentUpdate, timeUpdate, dateUpdate, position2);
            }
        }
        if (requestCode == REQUEST_CODE3) {
            if (resultCode == RESULT_CODE3) {
                titleUpdate2 = data.getStringExtra("titleUpdate");
                contentUpdate2 = data.getStringExtra("contentUpdate");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                dateUpdate2 = dateFormat.format(calendar.getTime());
                timeUpdate2 = timeFormat.format(calendar.getTime());

                lastEditDate = timeUpdate2 + ", " + dateUpdate2;

                updateObjectDatabase(titleUpdate2, contentUpdate2, timeUpdate2, dateUpdate2, position1);
            }
            if (resultCode == RESULT_CODE31) {
                delete(position1);
            }

            if (resultCode == RESULT_CODE32) {
                titleCopy = data.getStringExtra("titleCopy");
                contentCopy = data.getStringExtra("contentCopy");

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                dateCopy = dateFormat.format(calendar.getTime());
                timeCopy = timeFormat.format(calendar.getTime());

                UUID uuid = UUID.randomUUID();
                idCopy = uuid.toString();

                firstTimeCopy = dateCopy + ", " + timeCopy;

                addObjectDatabase(idCopy, titleCopy, contentCopy, dateCopy, timeCopy, firstTimeCopy);
            }
        }
    }

    public void updateObjectDatabase(String titleUpdate, String contentUpdate, String dateUpdate, String timeUpdate, int index) {
        Note note = notes.get(index);

        note.setTitle(titleUpdate);
        note.setContent(contentUpdate);
        note.setDate(dateUpdate);
        note.setTime(timeUpdate);

        database.QueryData("UPDATE NOTES SET TITLE = '" + titleUpdate + "', CONTENT = '" + contentUpdate + "', DATE = '" + dateUpdate + "', TIME = '" + timeUpdate + "' WHERE ID = '" + note.getId() + "'");
        noteAdapter.notifyDataSetChanged();
    }

    public void addObjectDatabase(String id, String title, String content, String date, String time, String firstDate) {
        database.QueryData("INSERT INTO NOTES VALUES('" + id + "', '" + title + "', '" + content + "', '" + date + "', '" + time + "', '" + firstDate + "')");

        Cursor cursor = database.GetData("SELECT * FROM NOTES");
        notes.clear();
        while (cursor.moveToNext()) {
            String idNote = cursor.getString(0);
            String titleNote = cursor.getString(1);
            String contentNote = cursor.getString(2);
            String dateNote = cursor.getString(3);
            String timeNote = cursor.getString(4);
            String firstDateNote = cursor.getString(5);
            notes.add(new Note(idNote, titleNote, contentNote, dateNote, timeNote, firstDateNote));
        }
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_item_edit:
                goToNoteUpdateDetail();
                return true;
            case R.id.context_item_delete:
                createDialog(position2);
                return true;
            case R.id.context_item_copy:
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                getDateCopy = dateFormat.format(calendar.getTime());

                getTimeCopy = timeFormat.format(calendar.getTime());

                UUID uuid = UUID.randomUUID();
                getIdCopy = uuid.toString();

                getFirstDateCopy = getDateCopy + ", " + getTimeCopy;

                addObjectDatabase(getIdCopy, notes.get(position2).getTitle(), notes.get(position2).getContent(), getDateCopy, getTimeCopy, getFirstDateCopy);

                Toast.makeText(getContext(), "Copy", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context_item, menu);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private NoteAdapter.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final NoteAdapter.ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}

