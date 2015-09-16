package com.example.jose.mymavmobile;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


public class ClassManagementFragment extends Fragment {

    private FragmentTabHost mTabHost;
    private ClassManagementFragment context = this;
    private Drawable bgBlue;

    public ClassManagementFragment() {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bgBlue = getResources().getDrawable(R.drawable.mymav_titlebar_blue_bg);

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_class_management);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        mTabHost.addTab(mTabHost.newTabSpec("view").setIndicator("View Class"),
                TabViewClass.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        mTabHost.addTab(mTabHost.newTabSpec("add").setIndicator("Add Class"),
                TabAddClass.class, arg2);
        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag3", 3);
        mTabHost.addTab(mTabHost.newTabSpec("drop").setIndicator("Drop Class"),
                TabDropClass.class, arg3);

        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
            if (mTabHost.getCurrentTab() == i) {
                mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
            } else {
                mTabHost.getTabWidget().getChildTabViewAt(i).setBackground(bgBlue);
            }
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabChanged(String onTabChanged) {
                for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
                    if (mTabHost.getCurrentTab() == i) {
                        mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
                    } else {
                        mTabHost.getTabWidget().getChildTabViewAt(i).setBackground(bgBlue);
                    }
                }
            }
        });
        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;

    }
}
