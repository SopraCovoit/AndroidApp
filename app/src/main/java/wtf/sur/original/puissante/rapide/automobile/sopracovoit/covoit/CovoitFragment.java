/*
 * Copyright 2015 Jérémie Boutoille, Jules Cantegril, Hugo Djemaa, Mickael Goubin, David Livet
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.covoit;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.MainActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator.AccountGeneral;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.PathSyncAdapter;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.SlidingTabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class CovoitFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public static final String SYNC_FINISHED = "sync_finished";
    public static final String COVOIT_TAG = "covoit_tag";
    private LinearLayout rootView;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mPager;
    private ChildNavigationAdapter mPagerAdapter;
    private SwipeRefreshLayout swipeView;

    public CovoitFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(syncFinishedReceiver, new IntentFilter(SYNC_FINISHED));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(syncFinishedReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = (LinearLayout) inflater.inflate(R.layout.fragment_covoit,container,false);
        this.swipeView = ((SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout));
        swipeView.setOnRefreshListener(this);
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        this.mPagerAdapter = new ChildNavigationAdapter(getChildFragmentManager(), getActivity(), getArguments());
        mPager.setAdapter(mPagerAdapter);
        this.setupTabs(rootView);
        return rootView;
    }
    private void setupTabs(View rootLayout) {
        mSlidingTabLayout = (SlidingTabLayout) rootLayout.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setSelectedIndicatorColors(this.getResources().getColor(R.color.accent));
        mSlidingTabLayout.setDistributeEvenly(true);

        mSlidingTabLayout.setViewPager(mPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    @Override
    public void onRefresh() {
        Log.d("Sync", "Begin");
        Account a = ((MainActivity) getActivity()).getConnectedAccount();
        if (a != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(a, "wtf.sur.original.puissante.rapide.automobile.sopracovoit.app", bundle);
        }
    }

    private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            CovoitFragment.this.swipeView.setRefreshing(false);
            Log.d("Sync", "Finished");
        }
    };

    public void notifyClickOnPath(long id) {
        ((MainActivity) this.getActivity()).openCovoitDetailed(id);
    }
}
