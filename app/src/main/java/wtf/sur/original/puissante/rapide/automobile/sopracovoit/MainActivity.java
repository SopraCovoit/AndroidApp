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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator.AccountGeneral;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.covoit.CovoitFragment;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.covoit.detailed.CovoitDetailedFragment;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.drawer.DrawerManager;


public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DrawerLayout drawer_container;
    private DrawerManager drawerManager;
    private AccountManager mAccountManager;
    private Account mConnectedAccount;
    private String mAuthToken;
    private static final int CURRENT_USER_LOADER = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountManager = AccountManager.get(this);

        this.drawer_container = (DrawerLayout) this.findViewById(R.id.drawer_container);
        this.drawerManager = new DrawerManager(drawer_container,this);
        // Check account, get token or create new account
        mAccountManager.getAuthTokenByFeatures(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Log.d("Toto", "mAccountManager ok init loader");
                            Bundle b = future.getResult();
                            getLoaderManager().initLoader(MainActivity.CURRENT_USER_LOADER, b, MainActivity.this);
                            if (!b.getString(AccountManager.KEY_AUTHTOKEN).equals("")) {
                                mConnectedAccount = new Account(b.getString(AccountManager.KEY_ACCOUNT_NAME), AccountGeneral.ACCOUNT_TYPE);
                                mAuthToken = b.getString(AccountManager.KEY_AUTHTOKEN);
                            }
                            if (savedInstanceState == null) {
//                                CovoitFragment cf = new CovoitFragment();
//                                cf.setArguments(b);
//                                getSupportFragmentManager().beginTransaction()
//                                        .add(R.id.fragment_container, cf)
//                                        .commit();
                                openCovoit(b);
                            }

                        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                            e.printStackTrace();
                            //TODO manage exception
                        }

                    }
                }
                , null);

        this.drawer_container = (DrawerLayout) this.findViewById(R.id.drawer_container);
        this.drawerManager = new DrawerManager(drawer_container, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                drawer_container.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("Toto", "Loader create");
        return new CursorLoader(
                this,
                CovoitContract.UserEntry.CONTENT_URI,
                null,
                CovoitContract.UserEntry.COLUMN_MAIL + " = '" + args.getString(AccountManager.KEY_ACCOUNT_NAME) + "'",
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("Toto", "Loader finish count =" + data.getCount());
        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_NAME));
            String surname = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_SURNAME));
            String mail = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_MAIL));
            drawerManager.refreshUser(name, surname, mail);
            data.close();
        } else if (data.getCount() == 0) {
            mAccountManager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, mAuthToken);
            mAccountManager.getAuthTokenByFeatures(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE, null, this, null, null,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Log.d("Toto", "mAccountManager ok restart loader");
                                Bundle b = future.getResult();
                                getLoaderManager().restartLoader(MainActivity.CURRENT_USER_LOADER, b, MainActivity.this);
                                if (!b.getString(AccountManager.KEY_AUTHTOKEN).equals("")) {
                                    mConnectedAccount = new Account(b.getString(AccountManager.KEY_ACCOUNT_NAME), AccountGeneral.ACCOUNT_TYPE);
                                    mAuthToken = b.getString(AccountManager.KEY_AUTHTOKEN);
                                }
                            } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                                e.printStackTrace();
                                //TODO manage exception
                            }

                        }
                    }
                    , null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        drawerManager.refreshUser("", "", "");
    }

    public Account getConnectedAccount() {
        return mConnectedAccount;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    public void openCovoit(Bundle b) {
        Fragment f = new CovoitFragment();
        f.setArguments(b);
        this.changeFragment(f,CovoitFragment.COVOIT_TAG, false);
    }
    public void reopenCovoit() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0)
            getSupportFragmentManager().popBackStack();
    }
    public void openCovoitDetailed(long id) {
        Fragment f = CovoitDetailedFragment.newInstance(id);
        this.changeFragment(f, CovoitDetailedFragment.COVOIT_DET_TAG, true);
    }

    public void changeFragment(Fragment f, String tag, boolean shouldAddToBackstack) {
        Fragment fragInStack = this.getSupportFragmentManager().findFragmentByTag(tag);
        if(fragInStack != null) {
            this.getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            if(shouldAddToBackstack)
                ft.addToBackStack(tag);
            ft.replace(R.id.fragment_container, f, tag).commit();
        }
    }
}
