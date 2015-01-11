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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator.AccountGeneral;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;

public class PathSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = PathSyncAdapter.class.getSimpleName();
    private final AccountManager mAccountManager;
    private final ContentResolver mContentResolver;

    public PathSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync");
        Log.d(TAG, "Account : " + account.name + ", " + account.type);

        try {
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE, true);
            Cursor cUser = mContentResolver.query(CovoitContract.UserEntry.CONTENT_URI, null, CovoitContract.UserEntry.COLUMN_MAIL + " = '" + account.name + "'", null, null);
            if (cUser.moveToFirst()) {
                Integer _userId = cUser.getInt(cUser.getColumnIndex(CovoitContract.UserEntry._ID));
                Integer _workplaceId = cUser.getInt(cUser.getColumnIndex(CovoitContract.UserEntry.COLUMN_WORKPLACE_ID));
                Cursor cPath = mContentResolver.query(CovoitContract.PathEntry.CONTENT_URI, null, CovoitContract.PathEntry.COLUMN_USER_ID + " = '" + _userId + "' AND " + CovoitContract.PathEntry.COLUMN_WORKPLACE_ID + " = '" + _workplaceId + "'", null, null);
                if (cPath.moveToFirst()) {
                    double _lat = cPath.getDouble(cPath.getColumnIndex(CovoitContract.PathEntry.COLUMN_LAT));
                    double _lon = cPath.getDouble(cPath.getColumnIndex(CovoitContract.PathEntry.COLUMN_LON));
                    Log.d(TAG, "Token : " + authToken + ", user : " + _userId + ", workplace : " + _workplaceId + ", lat : " + _lat + ", lon : " + _lon);
                } else {
                    // No path for user
                }
            } else {
                // No user for account
            }
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }
    }

    public static void syncImmediately(Account a) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(a, AccountGeneral.ACCOUNT_TYPE, bundle);
    }
}
