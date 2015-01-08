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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;

/**
 * Created by MagicMicky on 08/01/2015.
 */
public class CovoitUsersRecyclerViewAdapter extends RecyclerView.Adapter {
    private static int HEADER_TYPE=1;
    private static int DATA_TYPE =2;
    private List<User> mUsers;

    public static class CovoitViewHolder extends RecyclerView.ViewHolder {


        private final TextView userName;
        private final TextView userMessage;
        private final ImageView userImage;

        public CovoitViewHolder(View itemView) {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.IV_driver_icon);
            this.userName = (TextView) itemView.findViewById(R.id.TV_people_name);
            userMessage = (TextView) itemView.findViewById(R.id.TV_people_desc);
        }

    }
    public CovoitUsersRecyclerViewAdapter() {
        this(new ArrayList<User>());
    }
    public CovoitUsersRecyclerViewAdapter(List<User> users) {
        this.mUsers =users;
    }

    public void setUsers(List<User> users) {
        this.mUsers=users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==HEADER_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header,parent, false);
            return new HeaderViewHolder(v);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_view, parent, false);

        return new CovoitViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) return HEADER_TYPE;
        return DATA_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if(position>0) {
            CovoitViewHolder holder = (CovoitViewHolder) h;
            User user = this.mUsers.get(position-1);
            if(user.isDriver()) {
                holder.userImage.setImageResource(R.drawable.ic_car_black);
            } else {
                holder.userImage.setImageResource(R.drawable.ic_walk_black);
            }
            holder.userName.setText(user.getName());
            holder.userMessage.setText(user.getMail() + " - " + user.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
