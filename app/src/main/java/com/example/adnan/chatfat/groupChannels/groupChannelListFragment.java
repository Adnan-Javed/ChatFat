package com.example.adnan.chatfat.groupChannels;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.adnan.chatfat.R;
import com.sendbird.android.GroupChannelListQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class groupChannelListFragment extends Fragment {

    public static final String EXTRA_GROUP_CHANNEL_URL = "GROUP_CHANNEL_URL";
    private static final int CHANNEL_LIST_LIMIT = 15;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST";

    private RecyclerView mRecyclerView;
    //private groupChannelListFragment channelListAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton actionButton;
    private GroupChannelListQuery mChannelListQuery;

    private TextInputEditText eChannelName;
    private CheckBox cChannelDistinct;


    public groupChannelListFragment() {
        // Required empty public constructor
    }

    public static groupChannelListFragment newInstance(){
        return new groupChannelListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group_channel_list, container, false);
        View CFAlertDialog = inflater.inflate(R.layout.cfalertdialog_footer_view_group_channel, null);

        eChannelName = CFAlertDialog.findViewById(R.id.textInputEditText_channelName);
        cChannelDistinct = CFAlertDialog.findViewById(R.id.checkbox_channelDistinct);


        return rootView;
    }

}
