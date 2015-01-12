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

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;

/**
 * Created by MagicMicky on 08/01/2015.
 */
public class CovoitUsersRecyclerViewAdapter extends RecyclerView.Adapter {
    private static int HEADER_TYPE=1;
    private static int DATA_TYPE =2;
    private Context mContext;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    public static class CovoitViewHolder extends RecyclerView.ViewHolder {


        private final TextView userName;
        private final TextView userMessage;
        private final TextView distance;
        private final ImageView userImage;

        public CovoitViewHolder(View itemView) {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.IV_driver_icon);
            this.userName = (TextView) itemView.findViewById(R.id.TV_people_name);
            userMessage = (TextView) itemView.findViewById(R.id.TV_path_time);
            this.distance = (TextView) itemView.findViewById(R.id.TV_distance);
        }

    }

    public CovoitUsersRecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount() + 1;
        }
        return 0;
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
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {

        if (h instanceof CovoitViewHolder) {
            position--; // For header
            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }

            CovoitViewHolder holder = (CovoitViewHolder) h;
            boolean isDriver = mCursor.getInt(mCursor.getColumnIndex(CovoitContract.UserEntry.COLUMN_IS_DRIVE)) == 1;
            if (isDriver) {
                holder.userImage.setImageResource(R.drawable.ic_car_black);
            } else {
                holder.userImage.setImageResource(R.drawable.ic_walk_black);
            }
            holder.userName.setText(mCursor.getString(mCursor.getColumnIndex(CovoitContract.UserEntry.COLUMN_SURNAME)) + " " + mCursor.getString(mCursor.getColumnIndex(CovoitContract.UserEntry.COLUMN_NAME)));
            holder.userMessage.setText(holder.userMessage.getContext().getString(R.string.time, mCursor.getString(mCursor.getColumnIndex(CovoitContract.PathEntry.COLUMN_HOUR)), mCursor.getString(mCursor.getColumnIndex(CovoitContract.PathEntry.COLUMN_MIN))));
            holder.distance.setText(holder.distance.getContext().getString(R.string.distance, mCursor.getString(mCursor.getColumnIndex(CovoitContract.PathEntry.COLUMN_DISTANCE)) + ""));
        }

    }


    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}
