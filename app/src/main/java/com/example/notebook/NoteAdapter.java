package com.example.notebook;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.AMViewHolder>  {

    private List<Note> notes;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        mLongListener = longClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public NoteAdapter(List<Note> notes){
        this.notes = notes;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }

    @NonNull
    @Override
    public AMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        AMViewHolder amViewHolder = new AMViewHolder(view, mListener, mLongListener);
        return amViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AMViewHolder holder, int position) {
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


    public static class AMViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time;
        CardView cardView;

        AMViewHolder(final View itemView, final OnItemClickListener listener, final OnItemLongClickListener longClickListener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(position, cardView);
                        }
                    }
                    return true;
                }
            });
        }

    }


}
