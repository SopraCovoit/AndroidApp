/*
 * Copyright 2014 Jérémie Boutoille, Jules Cantegril, Hugo Djemaa, Mickael Goubin, David Livet
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

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;

/**
 * Created by MagicMicky on 19/12/2014.
 */
public class DrawerManager {

    private final DrawerLayout drawerLayout;
    private final RecyclerView recyclerView;
    private final Context mContext;
    private final RecyclerView.LayoutManager mLayoutManager;
    private final DrawerRecyclerViewAdapter mAdapter;
    private static final String[] strings = {};
    public DrawerManager(DrawerLayout drawerLayout, Context context) {
        this.mContext = context;
        this.drawerLayout = drawerLayout;
        this.recyclerView = (RecyclerView) drawerLayout.findViewById(R.id.recycler_view);
        this.mLayoutManager = new LinearLayoutManager(mContext);
        this.mAdapter = new DrawerRecyclerViewAdapter(strings);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }
}
