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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class CovoitFragment extends Fragment {


    private CovoitUsersRecyclerViewAdapter covoitAdapter;

    public CovoitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_covoit,container,false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.covoitAdapter = new CovoitUsersRecyclerViewAdapter();
        recyclerView.setAdapter(covoitAdapter);
        List<User> users = new ArrayList<User> ();
        User user = new User(1);
        user.setDriver(false);
        user.setName("Valerie Daras");
        user.setPhone("0612345678");
        user.setMail("valerie@dar.as");
        users.add(user);
        user = new User(2);
        user.setDriver(true);
        user.setName("Test");
        user.setPhone("phonetest");
        user.setMail("mailtest");
        users.add(user);
        this.updateRecyclerView(users);
        return root;
    }


    void updateRecyclerView(List<User> userList) {
        this.covoitAdapter.setUsers(userList);
    }

}
