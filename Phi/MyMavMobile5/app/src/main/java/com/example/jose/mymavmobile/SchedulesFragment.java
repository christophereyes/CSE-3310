package com.example.jose.mymavmobile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;


public class SchedulesFragment extends Fragment {

    private FragmentTabHost mTabHost;

    public SchedulesFragment() {
        //empty constructor
    }

    @SuppressLint("NewApi")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstantState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_schedules);
        LinearLayout linearLayout = (LinearLayout) mTabHost.getTabWidget().getParent();
        HorizontalScrollView hScrollView = new HorizontalScrollView(mTabHost.getContext());
        hScrollView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        linearLayout.addView(hScrollView, 0);
        linearLayout.removeView(mTabHost.getTabWidget());
        hScrollView.addView(mTabHost.getTabWidget());
        hScrollView.setHorizontalScrollBarEnabled(false);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        mTabHost.addTab(mTabHost.newTabSpec("view").setIndicator("View Schedules"),
                TabViewSchedules.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        mTabHost.addTab(mTabHost.newTabSpec("create").setIndicator("Create a Schedule"),
                TabCreateSchedule.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag3", 3);
        mTabHost.addTab(mTabHost.newTabSpec("drop").setIndicator("Delete a Schedule"),
                TabDeleteSchedule.class, arg3);

        Bundle arg4 = new Bundle();
        arg4.putInt("Arg for Frag4", 4);
        mTabHost.addTab(mTabHost.newTabSpec("verify").setIndicator("Verify Schedule"),
                TabVerifySchedule.class, arg4);

        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
//            mTabHost.getTabWidget().getChildTabViewAt(i).getLayoutParams().width = 220;
            if (mTabHost.getCurrentTab() == i) {
                mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
            } else {
                mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.parseColor("#0064b1"));
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
                        mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.parseColor("#0064b1"));
                    }
                }
            }
        });
        return mTabHost;
    }
}
