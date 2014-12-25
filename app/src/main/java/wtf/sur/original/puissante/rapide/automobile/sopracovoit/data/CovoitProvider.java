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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Content Provider
 */
public class CovoitProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CovoitDbHelper mOpenHelper;

    private static final int WORKPLACE = 100;
    private static final int USER = 200;
    private static final int PATH = 300;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CovoitContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CovoitContract.PATH_WORKPLACE, WORKPLACE);
        matcher.addURI(authority, CovoitContract.PATH_PATH, PATH);
        matcher.addURI(authority, CovoitContract.PATH_USER, USER);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CovoitDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "workplace"
            case WORKPLACE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CovoitContract.WorkplaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "user"
            case USER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CovoitContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "path"
            case PATH: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CovoitContract.PathEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WORKPLACE:
                return CovoitContract.WorkplaceEntry.CONTENT_TYPE;
            case PATH:
                return CovoitContract.PathEntry.CONTENT_TYPE;
            case USER:
                return CovoitContract.UserEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
