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
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.MainActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator.AccountGeneral;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;

/**
 * Created by MagicMicky on 09/01/2015.
 */
public class CovoitInnerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final java.lang.String ARG_DIRECTION = "direction";
    private CovoitUsersRecyclerViewAdapter covoitAdapter;
    private View root;
    private static final int PATH_LOADER = 1;

    public CovoitInnerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_inner_covoit,container,false);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.covoitAdapter = new CovoitUsersRecyclerViewAdapter(getActivity(), null);
        recyclerView.setAdapter(covoitAdapter);
        Log.d("Toto2", "Args : " + getArguments());
        getLoaderManager().initLoader(PATH_LOADER, getArguments(), CovoitInnerFragment.this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("Toto2", "Direction : " + args.getString(ARG_DIRECTION));
        return new CursorLoader(
                getActivity(),
                CovoitContract.PathEntry.buildEmailAll(args.getString(AccountManager.KEY_ACCOUNT_NAME), args.getString(ARG_DIRECTION)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("Toto2", "Loader finish count =" + data.getCount());
        covoitAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        covoitAdapter.changeCursor(null);
    }
}
