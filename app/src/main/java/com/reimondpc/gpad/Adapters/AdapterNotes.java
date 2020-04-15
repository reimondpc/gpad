package com.reimondpc.gpad.Adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reimondpc.gpad.Notes;
import com.reimondpc.gpad.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterNotes extends SelectableAdapter<AdapterNotes.NoteViewHolder> {
    private static final String TAG = AdapterNotes.class.getSimpleName();

    private ArrayList<Notes> listNotes;
    private NoteViewHolder.ClickListener clickListener;

    public AdapterNotes(ArrayList<Notes> listNotes, NoteViewHolder.ClickListener clickListener) {
        this.listNotes = listNotes;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false);
        Log.i(TAG, "onCreateViewHolder invoked");
        return new NoteViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final Notes notes = listNotes.get(position);
        holder.titleShow.setText(notes.getTitle());
        holder.timestamp.setText(notes.getTimestamp());

        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

        Log.i(TAG, "onBindViewHolder invoked: " + position);
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public void removeItem(int position) {
        listNotes.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            listNotes.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private static final String TAG = RecyclerView.ViewHolder.class.getSimpleName();

        TextView titleShow, timestamp;
        View selectedOverlay;
        private ClickListener listener;

        public NoteViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            titleShow = itemView.findViewById(R.id.listHeader);
            timestamp = itemView.findViewById(R.id.timestamp);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClicked(getPosition());
            }
            Log.d(TAG, "Item clicked at position " + getPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null){
                return listener.onItemLongClicked(getPosition());
            }
            Log.d(TAG, "Item long-clicked at position " + getPosition());
            return true;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }
    }
}
