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

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;

public class CovoitProviderHelper {
    public static void insertOrUpdateWorkplace(ContentResolver contentResolver, Workplace wp) {
        Cursor c = contentResolver.query(CovoitContract.WorkplaceEntry.buildUri(wp.getId()), null, null, null, null);
        if (c.getCount() == 0) {
            contentResolver.insert(CovoitContract.WorkplaceEntry.CONTENT_URI, wp.getContentValues());
        } else {
            contentResolver.update(CovoitContract.WorkplaceEntry.CONTENT_URI,
                    wp.getContentValues(),
                    CovoitContract.WorkplaceEntry._ID + " = " + wp.getId(), null);
        }
    }

    public static void insertOrUpdateUser(ContentResolver contentResolver, User u) {
        Cursor c = contentResolver.query(CovoitContract.UserEntry.buildUri(u.getId()), null, null, null, null);
        if (c.getCount() == 0) {
            contentResolver.insert(CovoitContract.UserEntry.CONTENT_URI, u.getContentValues());
        } else {
            contentResolver.update(CovoitContract.UserEntry.CONTENT_URI,
                    u.getContentValues(),
                    CovoitContract.UserEntry._ID + " = " + u.getId(), null);
        }
    }

    public static void insertOrUpdatePath(ContentResolver contentResolver, Path u) {
        Cursor c = contentResolver.query(CovoitContract.PathEntry.buildUri(u.getId()), null, null, null, null);
        if (c.getCount() == 0) {
            contentResolver.insert(CovoitContract.PathEntry.CONTENT_URI, u.getContentValues());
        } else {
            contentResolver.update(CovoitContract.PathEntry.CONTENT_URI,
                    u.getContentValues(),
                    CovoitContract.PathEntry._ID + " = " + u.getId(), null);
        }
    }
}
