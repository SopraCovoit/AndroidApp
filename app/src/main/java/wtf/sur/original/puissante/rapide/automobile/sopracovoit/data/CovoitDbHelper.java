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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract.*;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;

/**
 * CovoitDbHelper class
 */
public class CovoitDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "covoit.db";

    public CovoitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WORKPLACE_TABLE = "CREATE TABLE " + WorkplaceEntry.TABLE_NAME + " (" +
                WorkplaceEntry._ID + " INTEGER PRIMARY KEY, " +
                WorkplaceEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                WorkplaceEntry.COLUMN_LAT + " REAL NOT NULL, " +
                WorkplaceEntry.COLUMN_LON + " REAL NOT NULL); ";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY, " +
                UserEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_SURNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_MAIL + " TEXT NOT NULL, " +
                UserEntry.COLUMN_IS_DRIVE + " BOOLEAN, " +
                UserEntry.COLUMN_PHONE + " TEXT, " +
                UserEntry.COLUMN_HOME_LAT + " REAL, " +
                UserEntry.COLUMN_HOME_LON + " REAL, " +
                UserEntry.COLUMN_WORKPLACE_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + UserEntry.COLUMN_WORKPLACE_ID + ") REFERENCES " + WorkplaceEntry.TABLE_NAME + " (" + WorkplaceEntry._ID + "));";

        final String SQL_CREATE_PATH_TABLE = "CREATE TABLE " + PathEntry.TABLE_NAME + " (" +
                PathEntry._ID + " INTEGER PRIMARY KEY, " +
                PathEntry.COLUMN_HOUR + " INTEGER NOT NULL, " +
                PathEntry.COLUMN_MIN + " INTEGER NOT NULL, " +
                PathEntry.COLUMN_LAT + " REAL NOT NULL, " +
                PathEntry.COLUMN_LON + " REAL NOT NULL, " +
                PathEntry.COLUMN_DIRECTION + " TEXT NOT NULL, " +
                PathEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + PathEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + " (" + UserEntry._ID + "));";

        db.execSQL(SQL_CREATE_WORKPLACE_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_PATH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PathEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WorkplaceEntry.TABLE_NAME);
        onCreate(db);
    }
}
