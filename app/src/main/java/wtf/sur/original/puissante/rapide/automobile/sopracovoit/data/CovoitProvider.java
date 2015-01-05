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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Content Provider
 */
public class CovoitProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CovoitDbHelper mOpenHelper;

    private static final int WORKPLACE = 100;
    private static final int USER = 200;
    private static final int USER_ID = 201;
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
            // "user/*"
            case USER_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CovoitContract.UserEntry.TABLE_NAME,
                        projection,
                        CovoitContract.UserEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
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
            case USER_ID:
                return CovoitContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case WORKPLACE: {
                long _id = db.insert(CovoitContract.WorkplaceEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CovoitContract.WorkplaceEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PATH: {
                long _id = db.insert(CovoitContract.PathEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CovoitContract.PathEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER: {
                long _id = db.insert(CovoitContract.UserEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CovoitContract.UserEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case WORKPLACE:
                rowsDeleted = db.delete(
                        CovoitContract.WorkplaceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PATH:
                rowsDeleted = db.delete(
                        CovoitContract.PathEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(
                        CovoitContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WORKPLACE:
                rowsUpdated = db.update(CovoitContract.WorkplaceEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PATH:
                rowsUpdated = db.update(CovoitContract.PathEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(CovoitContract.UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WORKPLACE:
                int returnCount = 0;
                    for (ContentValues value : values) {
                        try {
                            long _id = db.insertOrThrow(CovoitContract.WorkplaceEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        } catch (SQLiteConstraintException e) {
                            long _id = db.update(CovoitContract.WorkplaceEntry.TABLE_NAME, value, CovoitContract.WorkplaceEntry._ID + " = " + value.getAsInteger(CovoitContract.WorkplaceEntry._ID), null);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
