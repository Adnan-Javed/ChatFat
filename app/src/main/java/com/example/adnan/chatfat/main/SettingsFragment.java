package com.example.adnan.chatfat.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;


import com.example.adnan.chatfat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    SwitchCompat groupNotificationSwitch;
    LinearLayout groupChannelNotificationsChild;


    public SettingsFragment() {
        // Required empty public constructor
    }

    static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar_settings);
        groupChannelNotificationsChild = rootView.findViewById(R.id.childs_groupChannelNotification);
        groupNotificationSwitch = rootView.findViewById(R.id.switch_groupChannelNotification);
        groupNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    groupChannelNotificationsChild.setVisibility(View.VISIBLE);
                else
                    groupChannelNotificationsChild.setVisibility(View.GONE);
            }
        });

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setHasOptionsMenu(true);

        toolbar.setTitle("Settings");


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }
}
