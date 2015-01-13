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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Location;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.PasswordHash;

public class CovoitProviderTest extends AndroidTestCase {
    public static final String LOG_TAG = CovoitProviderTest.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(CovoitDbHelper.DATABASE_NAME);
    }

    public void testInsertReadDb() {

        CovoitDbHelper dbHelper = new CovoitDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Test insert workplace
        Workplace w1 = new Workplace(1, "Toulouse", new Location(0.1, 0.2));
        ContentValues testValues = w1.getContentValues();

        long workplaceRowId;
        workplaceRowId = db.insert(CovoitContract.WorkplaceEntry.TABLE_NAME, null, testValues);

        assertTrue(workplaceRowId != -1);
        assertEquals(workplaceRowId, 1);
        Log.d(LOG_TAG, "New row id: " + workplaceRowId);

        // Test query workplace
        Cursor cursor = mContext.getContentResolver().query(
                CovoitContract.WorkplaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        CovoitDbHelperTest.validateCursor(cursor, testValues);


        // Test insert User
        User u = new User(1);
        u.setDriver(true);
        u.setMail("toto@toto.fr");
        u.setName("Race");
        u.setSurname("Ta");
        u.setPhone("0612345678");
        u.setWorkplace(w1);
        testValues = u.getContentValues();

        long userRowId = db.insert(CovoitContract.UserEntry.TABLE_NAME, null, testValues);

        assertTrue(userRowId != -1);
        assertEquals(userRowId, 1);
        Log.d(LOG_TAG, "New row id: " + userRowId);

        // Test query user
        cursor = mContext.getContentResolver().query(
                CovoitContract.UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        CovoitDbHelperTest.validateCursor(cursor, testValues);


        // Test path insert
        Path p = new Path(1, Path.Direction.HOME);
        p.setDepartureTime(12, 12);
        p.setLocation(02.2, 23.2);
        p.setWorkplace(w1);
        p.setDistance(0);
        p.setUser(u);
        testValues = p.getContentValues();

        long pathRowId = db.insert(CovoitContract.PathEntry.TABLE_NAME, null, testValues);

        assertTrue(pathRowId != -1);
        assertEquals(pathRowId, 1);
        Log.d(LOG_TAG, "New row id: " + pathRowId);

        // Test query user
        cursor = mContext.getContentResolver().query(
                CovoitContract.PathEntry.buildUserId(1),
                null,
                null,
                null,
                null
        );

        assertEquals(cursor.getCount(), 1);

        dbHelper.close();

        System.out.println(PasswordHash.getHashSHA1("david"));
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(CovoitContract.WorkplaceEntry.CONTENT_URI);
        assertEquals(CovoitContract.WorkplaceEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(CovoitContract.UserEntry.CONTENT_URI);
        assertEquals(CovoitContract.UserEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(CovoitContract.PathEntry.CONTENT_URI);
        assertEquals(CovoitContract.PathEntry.CONTENT_TYPE, type);

    }
}