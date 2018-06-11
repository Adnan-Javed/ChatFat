package com.example.adnan.chatfat.main;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adnan.chatfat.R;
import com.example.adnan.chatfat.utils.preferenceUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;


public class Login extends AppCompatActivity {

    AVLoadingIndicatorView mLoadingindicator;
    Button mloginButton;
    TextInputLayout mUserIDWrapper, mNicknameWrapper;
    TextInputEditText mUserIdEditText, mNickNameEditText;

    @Override
    protected void onStart() {
        super.onStart();

        if (preferenceUtils.getConnectionStatus()){

            mUserIdEditText.setText(preferenceUtils.getUserId());
            mUserIdEditText.setFocusable(false);
            mNickNameEditText.setText(preferenceUtils.getNickName());
            mNickNameEditText.setFocusable(false);

            connectToSendBird(preferenceUtils.getUserId(), preferenceUtils.getNickName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mloginButton = findViewById(R.id.login_button);
        mLoadingindicator = findViewById(R.id.loading_indicator);
        mUserIDWrapper = findViewById(R.id.textInputLayout_userIdWrapper);
        mNicknameWrapper = findViewById(R.id.textInputLayout_nicknameWrapper);
        mUserIdEditText = findViewById(R.id.textInputEditText_userId);
        mNickNameEditText = findViewById(R.id.textInputEditText_nickname);

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = mUserIdEditText.getText().toString();
                String nickName = mNickNameEditText.getText().toString();

                if (userId.isEmpty()){
                    mUserIDWrapper.setError("Field is required!");
                    mUserIDWrapper.setErrorEnabled(true);
                    return;
                }
                if (nickName.isEmpty()){
                    mNicknameWrapper.setError("Field is required!");
                    mNicknameWrapper.setErrorEnabled(true);
                    return;
                }

                userId = userId.replaceAll("\\s+", "");

                mUserIdEditText.setSelectAllOnFocus(true);
                mNickNameEditText.setSelectAllOnFocus(true);

                connectToSendBird(userId, nickName);
                //mLoadingindicator.show();
                //startActivity(new Intent(Login.this, MainActivity.class));
                //finish();
            }
        });

        mUserIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mUserIDWrapper.isErrorEnabled()){
                    mUserIDWrapper.setErrorEnabled(false);
                    mUserIDWrapper.setError("");
                }
                if (mNicknameWrapper.isErrorEnabled()){
                    mNicknameWrapper.setErrorEnabled(false);
                    mNicknameWrapper.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void connectToSendBird(final String id, final String nickname){

        mLoadingindicator.show();
        mloginButton.setEnabled(false);


        SendBird.connect(id, nickname, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {

                if (e != null){

                    Toast.makeText(Login.this, e.getCode()+": "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.rootLayout_login),"Login to sendBird Failed!", Snackbar.LENGTH_SHORT).show();

                    mLoadingindicator.hide();
                    mloginButton.setEnabled(true);

                    preferenceUtils.setConnectionStatus(false);
                    return;
                }

                preferenceUtils.setUserId(id);
                preferenceUtils.setNickName(nickname);
                preferenceUtils.setProfileURL(user.getProfileUrl());
                preferenceUtils.setConnectionStatus(true);

                SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), new SendBird.RegisterPushTokenWithStatusHandler() {
                    @Override
                    public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                        if (e != null){
                            Toast.makeText(Login.this, e.getCode()+": "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
        });

    }
}
