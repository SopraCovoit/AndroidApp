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

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.RetrofitError;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitDbHelper;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.FetchWorkplaceTask;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.PathSyncAdapter;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.ValidatorInput;

public class AuthenticatorActivity extends BaseAuthenticatorActivity {
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";
    private String mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE;

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();

    private AccountManager mAccountManager;
    private ValidatorInput validator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        if (accountName != null) {
            ((TextView) findViewById(R.id.accountName)).setText(accountName);
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(getBaseContext(), RegisterActivity.class);
                signup.putExtras(getIntent().getExtras());
                startActivityForResult(signup, REQ_SIGNUP);
            }
        });

        (new FetchWorkplaceTask(this)).execute();

        validator = new ValidatorInput(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void submit() {

        final TextView userName = ((TextView) findViewById(R.id.accountName));
        final TextView userPass = ((TextView) findViewById(R.id.accountPassword));

        boolean valid = validator.fieldRequired(userPass);
        valid &= validator.fieldMail(userName);
        if (valid) {
            new AsyncTask<String, Void, Intent>() {

                @Override
                protected Intent doInBackground(String... params) {
                    // Set User info
                    User currentUser = new User();
                    currentUser.setMail(userName.getText().toString());
                    currentUser.setPassword(userPass.getText().toString());

                    // Create intent
                    final Intent res = new Intent();
                    try {
                        res.putExtras(ServerAuthenticate.userSignIn(AuthenticatorActivity.this, currentUser));
                    } catch (RetrofitError e) {
                        res.putExtra(KEY_ERROR_MESSAGE, e.getLocalizedMessage());
                    }
                    return res;
                }

                @Override
                protected void onPostExecute(Intent intent) {
                    if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                        Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        finishLogin(intent);
                    }
                }
            }.execute();
        }
    }

    private void finishLogin(Intent intent) {
        Log.d(TAG, "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(TAG, "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = AccountGeneral.AUTHTOKEN_TYPE;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d(TAG, "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        ContentResolver.setSyncAutomatically(account, "wtf.sur.original.puissante.rapide.automobile.sopracovoit.app", true);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
