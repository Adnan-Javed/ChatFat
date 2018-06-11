package com.example.adnan.chatfat.openChannels;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adnan.chatfat.R;
import com.example.adnan.chatfat.utils.preferenceUtils;
import com.example.adnan.chatfat.main.MainActivity;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class openChatFragment extends Fragment {

    private static final int MESSAGE_LIST_LIMIT = 30;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_OPEN_CHAT";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_OPEN_CHAT";

    private ImageButton button_attachment, button_send;
    private EditText inputField;
    private TextView textView_typingUsers;
    private AVLoadingIndicatorView typingIndicator;
    private LinearLayout rootView_typingStatus;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private openChatAdapter mChatAdapter;

    private OpenChannel mOpenChannel;
    private String mChannelUrl;
    private List<BaseMessage> mList;
    private PreviousMessageListQuery messageListQuery;


    public openChatFragment() {
        // Required empty public constructor
    }

    public static openChatFragment newInstance(String ChannelUrl){

        openChatFragment fragment = new openChatFragment();

        Bundle args = new Bundle();
        args.putString(openChannelListFragment.EXTRA_OPEN_CHANNEL_URL, ChannelUrl);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_open_chat, container, false);

        button_attachment = rootView.findViewById(R.id.imageButton_attachment_open_chat);
        inputField = rootView.findViewById(R.id.edittext_messageBody_open_chat);
        button_send = rootView.findViewById(R.id.imageButton_send_open_chat);

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    button_send.setVisibility(View.VISIBLE);
                else
                    button_send.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_send.setVisibility(View.INVISIBLE);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = inputField.getText().toString();
                if (message.length() > 0){
                    sendUserMessage(message);
                    inputField.setText("");
                }

            }
        });

        rootView_typingStatus = rootView.findViewById(R.id.layout_open_chat_current_event);
        textView_typingUsers = rootView.findViewById(R.id.textView_typingStatus_open_chat);
        typingIndicator = rootView.findViewById(R.id.typingIndicator_open_chat);

        mChannelUrl = getArguments().getString(openChannelListFragment.EXTRA_OPEN_CHANNEL_URL);

        mRecyclerView = rootView.findViewById(R.id.recyclerView_chatBody_open_chat);
        mChatAdapter = new openChatAdapter(getActivity());
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

                loadInitialMessageList(MESSAGE_LIST_LIMIT);
            }

            @Override
            public void onReconnectFailed() {

            }
        });

        if (SendBird.getConnectionState() == SendBird.ConnectionState.CLOSED ||
                SendBird.getConnectionState() == SendBird.ConnectionState.OPEN  ){

            SendBird.connect(preferenceUtils.getUserId(), new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {
                    if (e != null)
                    {
                        Toast.makeText(getContext(), "Error Connecting to channel: "+e.getCode()+" " +e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    EnterChannel(mChannelUrl);
                }
            });
        }

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(mChannelUrl))
                    mChatAdapter.addFirstMessage(baseMessage);
            }
        });
    }

    @Override
    public void onPause() {

        SendBird.removeConnectionHandler(CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onDestroyView() {

        if (mOpenChannel != null){
            mOpenChannel.exit(new OpenChannel.OpenChannelExitHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null)
                    {
                        Toast.makeText(getContext(), "Error Exiting Channel: "+e.getCode()+" " +e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }
        super.onDestroyView();
    }

    private void setUpRecyclerViewAndAdapter(){

        mLayoutManager = new LinearLayoutManager(getContext());
        //mLayoutManager.scrollToPosition(mList.size()-1);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount()-1)
                    loadMoreMessageList(MESSAGE_LIST_LIMIT);
            }
        });
    }

    private void EnterChannel(String channelUrl){

        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null)
                {
                    Toast.makeText(getContext(), "Error Getting Channel: "+e.getCode()+" "
                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null)
                        {
                            Toast.makeText(getContext(), "Error Entering Channel: "+e.getCode()+" "
                                    +e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mOpenChannel = openChannel;

                        /*if (getActivity() != null)
                            ((MainActivity)getActivity()).setActionBarTitle(mOpenChannel.getName());*/

                        loadInitialMessageList(MESSAGE_LIST_LIMIT);
                    }
                });
            }
        });
    }

    private void loadInitialMessageList(int numberOfMessages){

        messageListQuery = mOpenChannel.createPreviousMessageListQuery();
        messageListQuery.load(numberOfMessages, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null)
                {
                    Toast.makeText(getContext(), "Error Loading Messages: "+e.getCode()+" "
                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.setMessageList(list);
                //mList = list;
                //mLayoutManager.scrollToPosition(mList.size()-1);
            }
        });
    }

    private void loadMoreMessageList(int numberOfMessages) throws NullPointerException{

        if (mOpenChannel == null)
            throw new NullPointerException("Current Channel Instance is null!");

        if (messageListQuery == null)
            throw new NullPointerException("Current Query Instance is null");

        messageListQuery.load(numberOfMessages, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null)
                {
                    Toast.makeText(getContext(), "Error Loading Messages: "+e.getCode()+" "
                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                for (BaseMessage message: list)
                    mChatAdapter.addMessage(message);

                //mList = list;
                ///mLayoutManager.scrollToPosition(mList.size()-1);
            }
        });
    }

    private void sendUserMessage(String message){

        if (mOpenChannel == null){
            Log.e("CHANNEL# ", "Null");
            return;
        }

        mOpenChannel.sendUserMessage(message,null, null, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null)
                {
                    Toast.makeText(getContext(), "Error Sending Messages: "+e.getCode()+" "
                            +e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.addFirstMessage(userMessage);
                //mLayoutManager.scrollToPosition(mList.size()-1);

            }
        });
    }

}
