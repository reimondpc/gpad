package com.reimondpc.gpad.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reimondpc.gpad.Notes;
import com.reimondpc.gpad.R;

import java.util.ArrayList;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.NoteViewHolder> {
    private static final String TAG = AdapterNotes.class.getSimpleName();

    public interface NoteModifier {
        public void onNoteSelected(int position);
    }

    private ArrayList<Notes> listNotes;
    private Context context;
    private NoteModifier noteModifier;

    public AdapterNotes(Context context, ArrayList<Notes> mlistNotes) {
        this.listNotes = mlistNotes;
        this.context = context;
    }

    public void setNoteModifier(NoteModifier noteModifier){
        this.noteModifier = noteModifier;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false);
        final NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                int position = noteViewHolder.getAdapterPosition();
                Toast.makeText(parent.getContext(), "Item at Position " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteModifier != null){
                    noteModifier.onNoteSelected(noteViewHolder.getAdapterPosition());
                }
            }
        });
        Log.i(TAG, "onCreateViewHolder invoked");
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final Notes notes = listNotes.get(position);
        holder.titleShow.setText(notes.getTitle());
        holder.timestamp.setText(notes.getTimestamp());

        Log.i(TAG, "onBindViewHolder invoked: " + position);
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleShow, timestamp;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleShow = itemView.findViewById(R.id.listHeader);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
