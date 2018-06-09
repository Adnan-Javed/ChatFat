package com.example.adnan.chatfat.openChannels;

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
import com.example.adnan.chatfat.utils.ImageUtils;
import com.example.adnan.chatfat.utils.preferenceUtils;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class openChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER_MESSAGE_ME = 10;
    private static final int VIEW_TYPE_USER_MESSAGE_Other = 20;

    Context mContext;
    List<BaseMessage> messageList;

    openChatAdapter(Context context){
        mContext = context;
        messageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View userMessageView;

        if (viewType == VIEW_TYPE_USER_MESSAGE_ME){
            userMessageView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_open_chat_me, parent, false);

            return new messageHolder_Me(userMessageView);
        }
        else {
            userMessageView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_open_chat_other, parent, false);

            return new messageHolder_Other(userMessageView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        BaseMessage message = messageList.get(position);

        switch (holder.getItemViewType()){
            case VIEW_TYPE_USER_MESSAGE_ME:
                ((messageHolder_Me)holder).Bind(mContext, (UserMessage) message);
                break;
            case VIEW_TYPE_USER_MESSAGE_Other:
                ((messageHolder_Other)holder).Bind(mContext, (UserMessage) message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (messageList.get(position) instanceof UserMessage){
            if (((UserMessage) messageList.get(position)).getSender().getNickname().equals(preferenceUtils.getNickName()))
                return VIEW_TYPE_USER_MESSAGE_ME;
            else
                return VIEW_TYPE_USER_MESSAGE_Other;
        }

        return super.getItemViewType(position);
    }

    public void setMessageList(List<BaseMessage> messages){
        messageList = messages;
        notifyDataSetChanged();
    }

    public void addFirstMessage(BaseMessage message){
        messageList.add(0, message);
        notifyDataSetChanged();
    }

    public void addMessage(BaseMessage message){
        messageList.add(message);
        notifyDataSetChanged();
    }

    private class messageHolder_Me extends RecyclerView.ViewHolder{

        TextView date, nickName, messageBody;
        ImageView profileImage;

        public messageHolder_Me(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.textView_date_open_chat_me);
            nickName = itemView.findViewById(R.id.textView_nickname_open_chat_me);
            messageBody = itemView.findViewById(R.id.textView_message_open_chat_me);
            profileImage = itemView.findViewById(R.id.imageView_open_chat_me);
        }

        void Bind(Context context, UserMessage message){

            date.setText(DateUtils.formatDate(message.getCreatedAt()));
            nickName.setText(message.getSender().getNickname());
            messageBody.setText(message.getMessage());
            //ImageUtils.displayRoundImageFromUrl(context, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private class messageHolder_Other extends RecyclerView.ViewHolder{

        TextView date, nickName, messageBody;
        ImageView profileImage;

        public messageHolder_Other(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.textView_date_open_chat_other);
            nickName = itemView.findViewById(R.id.textView_nickname_open_chat_other);
            messageBody = itemView.findViewById(R.id.textView_message_open_chat_other);
            profileImage = itemView.findViewById(R.id.imageView_open_chat_other);
        }

        void Bind(Context context, UserMessage message){
            date.setText(DateUtils.formatDate(message.getCreatedAt()));
            nickName.setText(message.getSender().getNickname());
            messageBody.setText(message.getMessage());
            //ImageUtils.displayRoundImageFromUrl(context, message.getSender().getProfileUrl(), profileImage);
        }
    }
}