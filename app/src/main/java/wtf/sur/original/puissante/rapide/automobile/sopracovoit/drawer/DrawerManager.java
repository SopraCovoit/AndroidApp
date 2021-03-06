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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.MainActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.user.UpdatePathActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.user.UpdateProfileActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.OnChildClickedListener;

/**
 * Created by MagicMicky on 19/12/2014.
 */
public class DrawerManager implements OnChildClickedListener {

    private final DrawerLayout drawerLayout;
    private final RecyclerView recyclerView;
    private final MainActivity mContext;
    private final RecyclerView.LayoutManager mLayoutManager;
    private final DrawerRecyclerViewAdapter mAdapter;
    private static final String[] strings = {"Recherche de copains","Modification trajets", "Modification compte"};
    private final TextView mName;
    private final TextView mMail;

    public DrawerManager(DrawerLayout drawerLayout, MainActivity act) {
        this.mContext = act;
        this.drawerLayout = drawerLayout;
        this.recyclerView = (RecyclerView) drawerLayout.findViewById(R.id.recycler_view);
        this.mLayoutManager = new LinearLayoutManager(mContext);
        this.mAdapter = new DrawerRecyclerViewAdapter(strings);
        this.mAdapter.setOnChildClickedListener(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mName = (TextView) drawerLayout.findViewById(R.id.TV_username);
        mMail = (TextView) drawerLayout.findViewById(R.id.TV_email);
    }

    @Override
    public void onItemClicked(View v) {
        int position = this.recyclerView.getChildPosition(v);
        Log.d("Tag", "Clicked on position "+ position);
        switch(position) {
            case 0:
                mContext.reopenCovoit();
                this.drawerLayout.closeDrawers();
                break;
            case 1:
                Intent intent = new Intent(mContext, UpdateProfileActivity.class);
                mContext.startActivity(intent);
                this.drawerLayout.closeDrawers();
                break;
            case 2:
                Intent intent2 = new Intent(mContext, UpdatePathActivity.class);
                mContext.startActivity(intent2);
                this.drawerLayout.closeDrawers();
                break;

        }

    }

    public void refreshUser(String name, String surname, String mail) {
        mName.setText(surname + " " + name);
        mMail.setText(mail);
    }
}
