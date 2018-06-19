package com.example.adnan.chatfat.groupChannels;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.adnan.chatfat.R;
import com.example.adnan.chatfat.utils.preferenceUtils;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class groupChannelListFragment extends Fragment implements CreateGroupChannelActivity.OnGroupChannelCreated{

    public static final String EXTRA_GROUP_CHANNEL_URL = "GROUP_CHANNEL_URL";
    public static final int INTENT_REQUEST_GROUP_CHANNEL = 32;
    private static final int CHANNEL_LIST_LIMIT = 15;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST";

    private RecyclerView mRecyclerView;
    private groupChannelListAdapter channelListAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton actionButton;
    private GroupChannelListQuery mChannelListQuery;
    private List<User> users;


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

        mRecyclerView = rootView.findViewById(R.id.groupChannelList);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == channelListAdapter.getItemCount()-1)
                    loadMoreChannels(CHANNEL_LIST_LIMIT);
            }
        });

        channelListAdapter = new groupChannelListAdapter(getContext());
        mRecyclerView.setAdapter(channelListAdapter);

        refreshLayout = rootView.findViewById(R.id.swipe_refresh_group_channel);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshChannelList(CHANNEL_LIST_LIMIT);
            }
        });

        actionButton = rootView.findViewById(R.id.fab_addGroupChannel);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), CreateGroupChannelActivity.class));
                Log.e("Activity Call", "Call succeded");
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        SendBird.addConnectionHandler(CONNECTION_HANDLER_ID, new SendBird.ConnectionHandler() {
            @Override
            public void onReconnectStarted() {

            }

            @Override
            public void onReconnectSucceeded() {
                Toast.makeText(getContext(), "reconnected", Toast.LENGTH_SHORT).show();
                refreshChannelList(CHANNEL_LIST_LIMIT);
            }

            @Override
            public void onReconnectFailed() {

            }
        });

        if (SendBird.getConnectionState() == SendBird.getConnectionState().OPEN){
            Toast.makeText(getContext(), "already connected", Toast.LENGTH_SHORT).show();
            refreshChannelList(CHANNEL_LIST_LIMIT);
        }

        if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED){
            SendBird.connect(preferenceUtils.getUserId(), new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null) {

                        Toast.makeText(getContext(), "Error Connecting to SendBird: "+e.getCode()+" " +e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    refreshChannelList(CHANNEL_LIST_LIMIT);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Toast.makeText(getContext(), "Connection Removed: ", Toast.LENGTH_LONG).show();
        SendBird.removeConnectionHandler(CONNECTION_HANDLER_ID);
    }

    private void refreshChannelList(int numberOfChannels){

        mChannelListQuery = GroupChannel.createMyGroupChannelListQuery();
        mChannelListQuery.setLimit(numberOfChannels);
        mChannelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null){
                    Toast.makeText(getContext(), e.getCode()+": "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                channelListAdapter.setGroupChannels(list);

                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }
        });

    }

    private void loadMoreChannels(int numerOfChannels){

        if (mChannelListQuery != null){
            mChannelListQuery.setLimit(numerOfChannels);
            mChannelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                @Override
                public void onResult(List<GroupChannel> list, SendBirdException e) {
                    if (e != null){
                        Toast.makeText(getContext(), e.getCode()+": "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (GroupChannel channel: list)
                        channelListAdapter.addGroupChannel(channel);
                }
            });
        }
    }

    @Override
    public void onCreated(GroupChannel channel) {

        channelListAdapter.addGroupChannel(channel);
    }
}
