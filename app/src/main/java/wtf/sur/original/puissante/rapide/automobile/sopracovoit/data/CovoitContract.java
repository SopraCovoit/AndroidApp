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

import android.provider.BaseColumns;

/**
 * CovoitContract class
 */
public class CovoitContract {
    public static final class WorkplaceEntry implements BaseColumns {
        public static final String TABLE_NAME = "workplace";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LAT = "latitude";
        public static final String COLUMN_LON = "longitude";
    }

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_MAIL = "mail";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_IS_DRIVE = "is_driver";
        public static final String COLUMN_HOME_LAT = "home_lat";
        public static final String COLUMN_HOME_LON = "home_lon";
        public static final String COLUMN_WORKPLACE_ID = "workplace_id";
    }

    public static final class PathEntry implements BaseColumns {
        public static final String TABLE_NAME = "path";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MIN = "min";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";
        public static final String COLUMN_DIRECTION = "direction";
        public static final String COLUMN_USER_ID = "user_id";
    }
}
