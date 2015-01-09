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


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.SlidingTabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class CovoitFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private LinearLayout rootView;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mPager;
    private ChildNavigationAdapter mPagerAdapter;

    public CovoitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = (LinearLayout) inflater.inflate(R.layout.fragment_covoit,container,false);
//        rootView.setOnRefreshListener(this);
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        this.mPagerAdapter = new ChildNavigationAdapter(getChildFragmentManager(),getActivity());
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
                /*for(int i=0; i <mPagerAdapter.getCount();i++){
                    // Skip current item
                    if (i == mPager.getCurrentItem()) {
                        continue;
                    }

                    // Skip destroyed or not created item
                    Fragment f = mPagerAdapter.getItemAt(i);
                    if (f == null) {
                        continue;
                    }
                }*/

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    @Override
    public void onRefresh() {
        //TODO
//        this.rootView.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                rootView.setRefreshing(false);
            }
        }, 5000);
    }


    public List<Path> testData() {
        List<Path> paths = new ArrayList<>();
        User user = new User(1);
        user.setDriver(false);
        user.setName("Mme Djemaa");
        user.setPhone("0612345678");
        user.setMail("djemaa@fac.ile");
        Path path = new Path(1, Path.Direction.HOME);
        path.setDepartureTime(10,50);
        path.setLocation(10, 10);
        path.setUser(user);
        path.setDistance(100);
        paths.add(path);

        user = new User(2);
        user.setDriver(true);
        user.setName("Test");
        user.setPhone("phonetest");
        user.setMail("mailtest");
        path = new Path(2, Path.Direction.HOME);
        path.setDepartureTime(11, 20);
        path.setLocation(20, 20);
        path.setUser(user);
        path.setDistance(150);

        paths.add(path);

        return paths;
    }


}
