package com.example.notebook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.Module.Note;
import com.example.notebook.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.AMViewHolder> {

    Context context;
    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    @NonNull
    @Override
    public AMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        AMViewHolder amViewHolder = new AMViewHolder(view);
        return amViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AMViewHolder holder, final int position) {
        Note note = notes.get(position);

        String string = notes.get(position).getContent();
        if (notes.get(position).getTitle().trim().equalsIgnoreCase("")) {
            int length = string.length();
            if (length > 27) {
                Double x = length * 0.8;
                length = x.intValue();
            }
            holder.title.setText(string.substring(0, length));
        } else {
            holder.title.setText(notes.get(position).getTitle());
        }
        holder.date.setText(notes.get(position).getDate());
        holder.time.setText(notes.get(position).getTime());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class AMViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time;
        CardView cardView;

        public AMViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

}
