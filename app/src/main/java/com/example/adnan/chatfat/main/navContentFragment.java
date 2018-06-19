package com.example.adnan.chatfat.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.adnan.chatfat.R;
import com.example.adnan.chatfat.groupChannels.groupChannelListFragment;
import com.example.adnan.chatfat.openChannels.openChannelListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class navContentFragment extends Fragment {

    LinearLayout openChannel, groupChannel;


    public navContentFragment() {
        // Required empty public constructor
    }

    static navContentFragment newInstance(){
        return new navContentFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_content, container, false);

        openChannel = rootView.findViewById(R.id.layoutOpenChannels);
        openChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getActivity().getSupportFragmentManager().popBackStack(null, getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayoutContent, openChannelListFragment.newInstance())
                        .commit();

            }
        });
        groupChannel = rootView.findViewById(R.id.layoutGroupChannels);
        groupChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getActivity().getSupportFragmentManager().popBackStack(null, getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLayoutContent, groupChannelListFragment.newInstance())
                        .commit();

            }
        });

        ImageView imageView = rootView.findViewById(R.id.settingsfragment);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.itemFrames, SettingsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

}
