package com.example.adnan.chatfat.openChannels;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sendbird.android.OpenChannel;
import com.sendbird.android.OpenChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class openChannelListFragment extends Fragment {

    public static final String EXTRA_OPEN_CHANNEL_URL = "OPEN_CHANNEL_URL";
    private static final int CHANNEL_LIST_LIMIT = 15;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_OPEN_CHANNEL_LIST";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_OPEN_CHANNEL_LIST";

    private RecyclerView mRecyclerView;
    private openChannelListAdapter channelListAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton actionButton;
    private OpenChannelListQuery mChannelListQuery;
    private AVLoadingIndicatorView channelLoadingIndicator;
    CFAlertDialog.Builder dialog;
    View rootView;

    private TextInputEditText editText_ChannelName;

    View CFAlertDialogue_footerView;

    public openChannelListFragment() {
        // Required empty public constructor
    }

    public static openChannelListFragment newInstance(){
        return new openChannelListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_open_channel_list, null);

        CFAlertDialogue_footerView = inflater.inflate(R.layout.cfalertdialog_footer_view_open_channel, null);
        editText_ChannelName = CFAlertDialogue_footerView.findViewById(R.id.textInputEditText_channelName);

        setRetainInstance(true);
        channelLoadingIndicator = rootView.findViewById(R.id.loading_indicator);

        mRecyclerView = rootView.findViewById(R.id.recyclerView_openChannelList);
        channelListAdapter = new openChannelListAdapter(getContext());

        refreshLayout = rootView.findViewById(R.id.swipe_refresh_open_channel_list);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                refreshChannelList(CHANNEL_LIST_LIMIT);
            }
        });

        actionButton = rootView.findViewById(R.id.fab_addOpenChannel);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup parent = (ViewGroup) CFAlertDialogue_footerView.getParent();
                if (parent != null)
                    parent.removeView(CFAlertDialogue_footerView);

                dialog = new CFAlertDialog.Builder(getActivity());
                dialog.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                dialog.setTitle("Create Open Channel");
                dialog.setTextGravity(Gravity.CENTER_HORIZONTAL);
                dialog.setCornerRadius(5);
                dialog.setCancelable(true);
                dialog.setFooterView(CFAlertDialogue_footerView);
                dialog.addButton("Cancel", Color.parseColor("#000000"), Color.parseColor("#f8f8ff"),
                                CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.addButton("Create", Color.parseColor("#000000"), Color.parseColor("#8b3a3a"),
                                CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {

                                        OpenChannel.createChannelWithOperatorUserIds(editText_ChannelName.getText().toString(),
                                                null, null, null, null, new OpenChannel.OpenChannelCreateHandler() {
                                                    @Override
                                                    public void onResult(OpenChannel openChannel, SendBirdException e) {

                                                        if ( e != null){
                                                            Toast.makeText(getContext(), "Error Creating Channel: "+e.getCode()+" "
                                                                    +e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        refreshChannelList(CHANNEL_LIST_LIMIT);
                                                        dialog.dismiss();
                                                        editText_ChannelName.setText("");
                                                    }
                                                });
                                    }
                                });
                dialog.show();
            }
        });

        setUpRecyclerViewAndAdapter();

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

                refreshChannelList(CHANNEL_LIST_LIMIT);
                Toast.makeText(getContext(), "Reconnected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReconnectFailed() {

            }
        });

        if (SendBird.getConnectionState() == SendBird.getConnectionState().OPEN)
            refreshChannelList(CHANNEL_LIST_LIMIT);

        if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED){

            SendBird.connect(preferenceUtils.getUserId(), new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null)
                    {
                        Toast.makeText(getContext(), "Error Connecting to channel: "+e.getCode()+" " +e.getMessage(),
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

        SendBird.removeConnectionHandler(CONNECTION_HANDLER_ID);
    }

    private void setUpRecyclerViewAndAdapter(){

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(channelListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (mLayoutManager.findLastVisibleItemPosition() == channelListAdapter.getItemCount()-1)
                    loadMoreChannels();
            }
        });

        channelListAdapter.setOnItemClickListener(new openChannelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OpenChannel channel) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayoutContent, openChatFragment.newInstance(channel.getUrl()))
                        .addToBackStack(null)
                        .commit();
            }
        });

    }


    private void loadMoreChannels(){

        if (mChannelListQuery != null){
            mChannelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
                @Override
                public void onResult(List<OpenChannel> list, SendBirdException e) {
                    if (e != null){
                        /*Toast.makeText(getContext(), "Channels Load Failure: "+e.getCode()+" "+e.getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();*/
                        channelLoadingIndicator.show();
                        return;
                    }

                    for (OpenChannel channel : list)
                        channelListAdapter.addChannel(channel);
                }
            });
            channelLoadingIndicator.hide();
        }
    }

    private void refreshChannelList(int numberOfChannels){

        mChannelListQuery = OpenChannel.createOpenChannelListQuery();
        mChannelListQuery.setLimit(numberOfChannels);
        mChannelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
            @Override
            public void onResult(List<OpenChannel> list, SendBirdException e) {
                if (e != null){
                    /*Toast.makeText(getContext(), "Channels Load Failure: "+e.getCode()+" "+e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();*/
                    return;
                }
                channelListAdapter.setOpenChannelList(list);

                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }
        });
    }
}
