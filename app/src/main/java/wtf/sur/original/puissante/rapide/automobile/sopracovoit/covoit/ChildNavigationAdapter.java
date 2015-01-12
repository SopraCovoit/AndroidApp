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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;

/**
 * Created by MagicMicky on 09/01/2015.
 */
public class ChildNavigationAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<CovoitInnerFragment> mPages;
    private final String[] TITLES;
    private Bundle mArgs;

    public ChildNavigationAdapter(FragmentManager fm, Context c, Bundle args) {
        super(fm);
        mPages = new SparseArray<>();
        this.TITLES = c.getResources().getStringArray(R.array.tabs_covoit);
        mArgs = args;
    }


    @Override
    public Fragment getItem(int position) {
        CovoitInnerFragment f = new CovoitInnerFragment();
        Bundle b = new Bundle(mArgs);
        if (position == 0)
            b.putString(CovoitInnerFragment.ARG_DIRECTION, Path.getDirectionString(Path.Direction.WP));
        else
            b.putString(CovoitInnerFragment.ARG_DIRECTION, Path.getDirectionString(Path.Direction.HOME));
        Log.d("Toto2", "Item " + position + " args " + b);
        f.setArguments(b);
        mPages.put(position, f);
        return f;
    }
    public Fragment getItemAt(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (0 <= mPages.indexOfKey(position)) {
            mPages.remove(position);
        }
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
