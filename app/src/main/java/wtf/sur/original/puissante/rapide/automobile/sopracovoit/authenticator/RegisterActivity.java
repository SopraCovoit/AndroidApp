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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.BaseActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;

public class RegisterActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String mAccountType;
    private static final int WORKPLACE_LOADER = 0;
    private SimpleCursorAdapter mWorkplaceAdapter;
    private Spinner mWorkplaceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getLoaderManager().initLoader(WORKPLACE_LOADER, null, this);

        mAccountType = getIntent().getStringExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE);

        mWorkplaceSpinner = (Spinner) findViewById(R.id.sopralist);
        mWorkplaceSpinner.setEnabled(false);
        mWorkplaceAdapter = new SimpleCursorAdapter(this, R.layout.simple_text_view, null, new String[]{CovoitContract.WorkplaceEntry.COLUMN_NAME}, new int[]{R.id.textView}, 0);
        mWorkplaceSpinner.setAdapter(mWorkplaceAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    private void createAccount() {

        new AsyncTask<String, Void, Intent>() {

            String name = ((TextView) findViewById(R.id.name)).getText().toString().trim();

            @Override
            protected Intent doInBackground(String... params) {

                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    /*authtoken = AccountGeneral.sServerAuthenticate.userSignUp(name, accountName, accountPassword, AccountGeneral.AUTHTOKEN_TYPE);

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(AuthenticatorActivity.PARAM_USER_PASS, accountPassword);*/
                } catch (Exception e) {
                    data.putString(AuthenticatorActivity.KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(AuthenticatorActivity.KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(AuthenticatorActivity.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                CovoitContract.WorkplaceEntry.CONTENT_URI,
                null,
                null,
                null,
                CovoitContract.WorkplaceEntry.COLUMN_NAME + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWorkplaceAdapter.swapCursor(data);
        if (data.getCount() > 0) {
            mWorkplaceSpinner.setEnabled(true);
        } else {
            mWorkplaceSpinner.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registration,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_register:
                createAccount();
                return true;
            default:
                //TODO
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWorkplaceAdapter.swapCursor(null);
    }
}
