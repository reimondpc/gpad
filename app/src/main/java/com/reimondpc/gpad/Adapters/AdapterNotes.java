package com.reimondpc.gpad.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reimondpc.gpad.Notes;
import com.reimondpc.gpad.R;

import java.util.ArrayList;

public class AdapterNotes
        extends RecyclerView.Adapter<AdapterNotes.ViewHolderNotes>
        implements View.OnClickListener {

    ArrayList<Notes> listNotes;
    private View.OnClickListener listener;

    public AdapterNotes(ArrayList<Notes> listNotes) {
        this.listNotes = listNotes;
    }

    @NonNull
    @Override
    public ViewHolderNotes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, null, false);
        view.setOnClickListener(this);
        return new ViewHolderNotes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotes holder, int position) {
        holder.titleShow.setText(listNotes.get(position).getTitle());
        holder.timestamp.setText(listNotes.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolderNotes extends RecyclerView.ViewHolder {
        TextView titleShow, timestamp;
        public ViewHolderNotes(@NonNull View itemView) {
            super(itemView);
            titleShow = itemView.findViewById(R.id.listHeader);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
