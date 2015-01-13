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
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator.AccountGeneral;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.covoit.CovoitFragment;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitProviderHelper;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Location;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;

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
            Cursor cursor = mContentResolver.query(CovoitContract.PathEntry.buildEmail(account.name), null, null, null, null);
            if (cursor.moveToFirst()) {
                int _userId = cursor.getInt(cursor.getColumnIndex(CovoitContract.UserEntry._ID));
                int _workplaceId = cursor.getInt(cursor.getColumnIndex(CovoitContract.UserEntry.COLUMN_WORKPLACE_ID));
                double _lat = cursor.getDouble(cursor.getColumnIndex(CovoitContract.PathEntry.COLUMN_LAT));
                double _lon = cursor.getDouble(cursor.getColumnIndex(CovoitContract.PathEntry.COLUMN_LON));
                Log.d(TAG, "Token : " + authToken + ", user : " + _userId + ", workplace : " + _workplaceId + ", lat : " + _lat + ", lon : " + _lon);

                int _count = 0;
                List<Path> pathList = CovoitServerAccessor.listPath(_workplaceId, new Location(_lat, _lon), authToken);
                StringBuilder ids = new StringBuilder();
                ids.append('(');
                for (Path p : pathList) {

                    ids.append(p.getId());
                    if (_count != pathList.size() - 1)
                        ids.append(',');

                    CovoitProviderHelper.insertOrUpdateWorkplace(getContext().getContentResolver(), p.getWorkplace());
                    CovoitProviderHelper.insertOrUpdateWorkplace(getContext().getContentResolver(), p.getUser().getWorkplace());

                    CovoitProviderHelper.insertOrUpdateUser(getContext().getContentResolver(), p.getUser());

                    CovoitProviderHelper.insertOrUpdatePath(getContext().getContentResolver(), p);
                    _count++;
                }
                ids.append(')');
                getContext().getContentResolver().delete(CovoitContract.PathEntry.CONTENT_URI, CovoitContract.PathEntry._ID + " NOT IN " + ids.toString(), null);

                Log.d(TAG, "Add path " + _count);

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
        Intent i = new Intent(CovoitFragment.SYNC_FINISHED);
        getContext().sendBroadcast(i);
    }

}
