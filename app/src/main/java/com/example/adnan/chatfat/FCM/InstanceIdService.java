package com.example.adnan.chatfat.FCM;

import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

public class InstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationTokenToServer(refreshedToken);
    }

    private void sendRegistrationTokenToServer(String refreshedToken) {
        SendBird.registerPushTokenForCurrentUser(refreshedToken, new SendBird.RegisterPushTokenWithStatusHandler() {
            @Override
            public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                if (e != null){
                    Toast.makeText(InstanceIdService.this, e.getCode()+": "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pushTokenRegistrationStatus == SendBird.PushTokenRegistrationStatus.PENDING)
                    Toast.makeText(InstanceIdService.this, "Connection required to register Push Token", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
