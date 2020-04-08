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

    private ArrayList<Notes> listNotes;

    public AdapterNotes(ArrayList<Notes> listNotes) {
        this.listNotes = listNotes;
    }

    @NonNull
    @Override
    public ViewHolderNotes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, null, false);
        return new ViewHolderNotes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNotes holder, int position) {
        holder.titleShow.setText(listNotes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class ViewHolderNotes extends RecyclerView.ViewHolder {
        TextView titleShow;
        public ViewHolderNotes(@NonNull View itemView) {
            super(itemView);
            titleShow = itemView.findViewById(R.id.listHeader);
        }
    }
}
