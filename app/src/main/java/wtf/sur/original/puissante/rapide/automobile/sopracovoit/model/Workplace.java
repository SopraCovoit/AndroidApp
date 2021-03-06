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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.model;

import android.content.ContentValues;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;

/**
 * Workplace class
 */
public class Workplace {
    private final int id;
    private String name;
    private Location location;

    public Workplace(int id) {
        this.id = id;
    }

    public Workplace(int id, String name, Location location) {
        this(id);
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(CovoitContract.WorkplaceEntry._ID, id);
        cv.put(CovoitContract.WorkplaceEntry.COLUMN_NAME, name);
        cv.put(CovoitContract.WorkplaceEntry.COLUMN_LAT, location.getLatitude());
        cv.put(CovoitContract.WorkplaceEntry.COLUMN_LON, location.getLongitude());
        return cv;
    }
}
