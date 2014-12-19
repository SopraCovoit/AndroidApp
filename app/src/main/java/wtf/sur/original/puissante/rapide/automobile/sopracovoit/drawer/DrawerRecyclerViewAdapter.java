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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;

/**
 * Created by MagicMicky on 19/12/2014.
 */
public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.DrawerViewHolder> {

    private String[] mData;
    public static class DrawerViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView;
        }
    }

    public DrawerRecyclerViewAdapter(String[] data) {
        this.mData = data;
    }


    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_recycler_single_item, parent, false);
        return new DrawerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        holder.textView.setText(mData[position]);

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


}
