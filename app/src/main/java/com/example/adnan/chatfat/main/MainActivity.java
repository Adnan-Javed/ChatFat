package com.example.adnan.chatfat.main;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.adnan.chatfat.R;
import com.example.adnan.chatfat.openChannels.openChannelListFragment;
import com.github.florent37.awesomebar.AwesomeBar;

public class MainActivity extends AppCompatActivity {

    NavigationView navView;
    DrawerLayout mDrawer;
    AwesomeBar awesomeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = findViewById(R.id.drawer);
        awesomeBar = findViewById(R.id.awesomebar);
        awesomeBar.displayHomeAsUpEnabled(false);

        awesomeBar.setOnMenuClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.START);
            }
        });
        //awesomeBar.addOverflowItem("Open Channels");


        Fragment fragment = openChannelListFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragmentLayoutContent, fragment)
                .commit();
        awesomeBar.addAction(null, "Open Channels");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.itemFrames, navContentFragment.newInstance())
                .commit();

    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)){
            if(getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            else
                mDrawer.closeDrawer(Gravity.LEFT);
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == android.R.id.home){
            if(mDrawer.isDrawerOpen(GravityCompat.START)){
                getSupportFragmentManager().popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
