package com.example.adnan.chatfat.openChannels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adnan.chatfat.R;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.sendbird.android.OpenChannel;

import java.util.ArrayList;
import java.util.List;

public class openChannelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<OpenChannel> openChannels;
    private OnItemClickListener mItemClickListener;

    openChannelListAdapter(Context context){
        mContext = context;
        openChannels = new ArrayList<>();
    }

    interface OnItemClickListener {
        void onItemClick(OpenChannel channel);
    }
    void setOnItemClickListener(OnItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_open_channel, parent, false);

        return new ChannelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ChannelHolder)holder).Bind(mContext, openChannels.get(position), mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return openChannels.size();
    }

    void setOpenChannelList(List<OpenChannel> channels){
        openChannels = channels;
        notifyDataSetChanged();

    }

    void addChannel(OpenChannel channel){
        openChannels.add(channel);
        notifyDataSetChanged();
    }

    private class ChannelHolder extends RecyclerView.ViewHolder{

        private TextView channelName, recentMessage, participants;

        public ChannelHolder(View itemView) {
            super(itemView);

            channelName = itemView.findViewById(R.id.name_OpenChannel);
            recentMessage = itemView.findViewById(R.id.recent_message_open_channel);
            participants = itemView.findViewById(R.id.participants);
        }

        private void Bind(final Context context, final OpenChannel openChannel, final OnItemClickListener clickListener){

            channelName.setText(openChannel.getName());
            participants.setText(openChannel.getParticipantCount()+"");



            /*PreviousMessageListQuery listQuery = openChannel.createPreviousMessageListQuery();
            listQuery.load(1, true, new PreviousMessageListQuery.MessageListQueryResult() {
                @Override
                public void onResult(List<BaseMessage> list, SendBirdException e) {
                    if (e!=null)
                        return;

                    if (list.get(15) instanceof UserMessage)
                        recentMessage.setText(((UserMessage) list.get(15)).getMessage());
                    else if (list.get(15) instanceof AdminMessage)
                        recentMessage.setText(((AdminMessage) list.get(15)).getMessage());
                    else{
                        String lastMessage = String.format(context.getString(R.string.group_channel_list_file_message_text),
                                ((FileMessage) list.get(15)).getSender().getNickname());

                                recentMessage.setText(lastMessage);
                    }

                }
            });*/

            if (clickListener != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(openChannel);
                    }
                });
            }
        }
    }
}
