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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class CovoitFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private CovoitUsersRecyclerViewAdapter covoitAdapter;
    private SwipeRefreshLayout swipeLayout;

    public CovoitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.swipeLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_covoit,container,false);
        swipeLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = (RecyclerView) swipeLayout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.covoitAdapter = new CovoitUsersRecyclerViewAdapter();
        recyclerView.setAdapter(covoitAdapter);

        this.updateRecyclerView(testData());
        return swipeLayout;
    }


    void updateRecyclerView(List<Path> paths) {
        this.covoitAdapter.setPaths(paths);
    }

    @Override
    public void onRefresh() {
        //TODO
//        this.swipeLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
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
