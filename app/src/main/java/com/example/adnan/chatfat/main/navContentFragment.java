package com.example.adnan.chatfat.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adnan.chatfat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class navContentFragment extends Fragment {


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
