package com.reimondpc.gpad.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reimondpc.gpad.Notes;
import com.reimondpc.gpad.R;

import java.util.ArrayList;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.ViewHolderNotes> {

    ArrayList<Notes> listNotes;
    private OnNoteListener mOnNoteListener;

    public AdapterNotes(ArrayList<Notes> mlistNotes, OnNoteListener onNoteListener) {
        this.listNotes = mlistNotes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolderNotes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, null, false);
        return new ViewHolderNotes(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotes holder, final int position) {
        holder.titleShow.setText(listNotes.get(position).getTitle());
        holder.timestamp.setText(listNotes.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class ViewHolderNotes extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleShow, timestamp;
        OnNoteListener onNoteListener;

        public ViewHolderNotes(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            titleShow = itemView.findViewById(R.id.listHeader);
            timestamp = itemView.findViewById(R.id.timestamp);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
