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

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * CovoitContract class
 */
public class CovoitContract {

    public static final String CONTENT_AUTHORITY = "wtf.sur.original.puissante.rapide.automobile.sopracovoit.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WORKPLACE = "workplace";
    public static final String PATH_USER = "user";
    public static final String PATH_PATH = "path";
    public static final String PATH_PATH_USER = "path_user";


    public static final class WorkplaceEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKPLACE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_WORKPLACE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_WORKPLACE;

        public static final String TABLE_NAME = "workplace";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LON = "longitude";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_MAIL = "mail";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_IS_DRIVE = "is_driver";
        public static final String COLUMN_WORKPLACE_ID = "workplace_id";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PathEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATH).build();
        public static final Uri CONTENT_URI2 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PATH_USER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_PATH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_PATH;

        public static final String TABLE_NAME = "path";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MIN = "min";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";
        public static final String COLUMN_DIRECTION = "direction";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_WORKPLACE_ID = "workplace_id";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildEmail(String email) {
            return CONTENT_URI.buildUpon().appendPath(email).build();
        }

        public static Uri buildUserId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI2, id);
        }

        public static Uri buildEmailAll(String email, String direction) {
            return CONTENT_URI.buildUpon().appendPath(email).appendPath(direction).appendPath("all").build();
        }

        public static String getEmailFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDirectionFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }
}
