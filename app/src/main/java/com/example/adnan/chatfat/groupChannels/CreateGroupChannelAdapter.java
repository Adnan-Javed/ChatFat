package com.example.adnan.chatfat.groupChannels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnan.chatfat.R;
import com.sendbird.android.User;

import java.util.List;
import java.util.zip.Inflater;

public class CreateGroupChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<User> mUsers;
    private OnItemClickListener mListener;
    //private CheckBox.OnCheckedChangeListener checkedChangeListener;

    interface OnItemClickListener{
        void onItemClick(User user);
    }

    void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    CreateGroupChannelAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item_selectable_user, parent, false);
        return new ChannelHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ChannelHolder)holder).Bind(mContext, mUsers.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setUserList(List<User> users){
        mUsers = users;
        Log.e("SetList# ", "User List Set");
        notifyDataSetChanged();
    }

    public void addUser(User user){
        mUsers.add(user);
        notifyDataSetChanged();
    }

    private class ChannelHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ImageView profileImage;
        TextView userName;

        public ChannelHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox_selectUser);
            profileImage = itemView.findViewById(R.id.imageView_selectableImage_group_chat);
            userName = itemView.findViewById(R.id.textView_SelectableUserName);
        }

        private void Bind(Context context, final User user, final OnItemClickListener listener){

            userName.setText(user.getNickname());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked)
                        listener.onItemClick(user);

                }
            });

            if (listener != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkBox.isChecked()){
                            checkBox.setChecked(false);
                        }
                        else {
                            checkBox.setChecked(true);
                            listener.onItemClick(user);
                        }
                    }
                });
            }
        }
    }
}
