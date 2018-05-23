package com.shra1.sendbird;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sendbird.android.UserMessage;
import com.shra1.sendbird.utils.SharedPreferencesManager;

import java.util.List;

import static com.shra1.sendbird.GuestActivity.ADMIN;

/**
 * Created by Shrawan WABLE on 3/20/2018.
 */

public class AdminMessagesRecyclerViewAdapter extends RecyclerView.Adapter<AdminMessagesRecyclerViewAdapter.GMRVAViewHolder> {

    List<UserMessage> l;
    SharedPreferencesManager s;

    public AdminMessagesRecyclerViewAdapter(Context context, List<UserMessage> l) {
        this.l = l;
        s=SharedPreferencesManager.getInstance(context);
    }

    @Override
    public GMRVAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_row_layout, parent, false);
        return new GMRVAViewHolder(row);
    }

    @Override
    public void onBindViewHolder(GMRVAViewHolder holder, int position) {
        UserMessage um = l.get(position);
        if (um.getSender().getUserId().equals(ADMIN)){
            //this is sent message
            holder.tvSentMessage.setVisibility(View.VISIBLE);
            holder.tvSentMessage.setText(um.getMessage());
            holder.tvRecievedMessage.setVisibility(View.GONE);
        }else{
            //this is recieved message
            holder.tvRecievedMessage.setVisibility(View.VISIBLE);
            holder.tvRecievedMessage.setText(um.getMessage());
            holder.tvSentMessage.setVisibility(View.GONE);
        }
    }

    public void addMessage(UserMessage m){
        l.add(m);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    static class GMRVAViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSentMessage;
        private TextView tvRecievedMessage;

        public GMRVAViewHolder(View itemView) {
            super(itemView);
            tvSentMessage = (TextView) itemView.findViewById(R.id.tvSentMessage);
            tvRecievedMessage = (TextView) itemView.findViewById(R.id.tvRecievedMessage);
        }
    }

}
