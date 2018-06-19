package com.example.adnan.chatfat.groupChannels;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adnan.chatfat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class groupChatFragment extends Fragment {


    public groupChatFragment() {
        // Required empty public constructor
    }

    public static groupChatFragment newInstance(String channelUrl){

        groupChatFragment fragment = new groupChatFragment();

        Bundle arg = new Bundle();
        arg.putString(groupChannelListFragment.EXTRA_GROUP_CHANNEL_URL, channelUrl);

        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

}
