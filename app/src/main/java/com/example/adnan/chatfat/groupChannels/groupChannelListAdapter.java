package com.example.adnan.chatfat.groupChannels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnan.chatfat.R;
import com.example.adnan.chatfat.utils.DateUtils;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.UserMessage;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class groupChannelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<GroupChannel> groupChannels;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    interface OnItemClickListener {
        void onItemClick(GroupChannel channel);
    }
    interface  OnItemLongClickListener{
        void onItemLongClick(GroupChannel channel);
    }

    void setOnItemClickListener(OnItemClickListener listener){
        mItemClickListener = listener;
    }
    void setOnItemLongClickListener(OnItemLongClickListener listener){
        mOnItemLongClickListener = listener;
    }

    groupChannelListAdapter(Context context){
        mContext = context;
        groupChannels = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group_channel, parent, false);
        return new ChannelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return groupChannels.size();
    }

    private class ChannelHolder extends RecyclerView.ViewHolder{

        ImageView groupImage;
        TextView channelName, date, recentMesssage, participants, unreadCount;
        AVLoadingIndicatorView typingStatus;

        public ChannelHolder(View itemView) {
            super(itemView);

            groupImage = itemView.findViewById(R.id.imageView_groupChannelItem);
            channelName = itemView.findViewById(R.id.topic_groupChannel);
            date = itemView.findViewById(R.id.Date);
            recentMesssage = itemView.findViewById(R.id.recent_message_group_channel);
            participants = itemView.findViewById(R.id.participants);
            unreadCount = itemView.findViewById(R.id.unreadCount);
            typingStatus = itemView.findViewById(R.id.typingIndicator);
        }

        private void Bind(Context context, final GroupChannel groupChannel, final OnItemClickListener itemClickListener,
                          final OnItemLongClickListener itemLongClickListener){

            channelName.setText(groupChannel.getName());
            participants.setText( String.valueOf(groupChannel.getMemberCount()) );

            date.setVisibility(View.INVISIBLE);
            recentMesssage.setVisibility(View.INVISIBLE);

            if (groupChannel.getLastMessage() != null){

                date.setVisibility(View.VISIBLE);
                date.setText(DateUtils.formateDate_DMY(groupChannel.getCreatedAt()));

                recentMesssage.setVisibility(View.VISIBLE);
                recentMesssage.setText(groupChannel.getLastMessage()+"");
            }

            if (groupChannel.getUnreadMessageCount() == 0){
                unreadCount.setVisibility(View.INVISIBLE);
            }
            else {
                unreadCount.setVisibility(View.VISIBLE);
                unreadCount.setText( String.valueOf(groupChannel.getUnreadMessageCount()) );
            }

            if (groupChannel.isTyping()){
                typingStatus.setVisibility(View.VISIBLE);
                recentMesssage.setVisibility(View.VISIBLE);
                recentMesssage.setText("Someone is typing...");
                typingStatus.show();
            }
            else{
                typingStatus.setVisibility(View.GONE);
                typingStatus.hide();

                if (groupChannel.getLastMessage() != null){
                    recentMesssage.setVisibility(View.VISIBLE);
                    recentMesssage.setText( ((UserMessage) groupChannel.getLastMessage()).getMessage() );
                }
                else
                    recentMesssage.setVisibility(View.INVISIBLE);
            }

            if (itemClickListener != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(groupChannel);
                    }
                });
            }

            if (itemLongClickListener != null){
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        itemLongClickListener.onItemLongClick(groupChannel);

                        // return true if the callback consumed the long click
                        return true;
                    }
                });
            }
        }
    }
}
