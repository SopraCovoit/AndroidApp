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

import java.util.Calendar;
import java.util.GregorianCalendar;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;

/**
 * Path class
 */
public class Path {


    public static enum Direction {HOME, WP}

    private int id;
    private Location location;
    private final Calendar calendar;
    private final Direction direction;
    private User user;
    private int distance;

    public Path(int id, Direction direction) {
        this.id = id;
        this.direction = direction;
        this.calendar = new GregorianCalendar();
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(double lat, double lon) {
        this.location = new Location(lat, lon);
    }

    public int getDepartureHour() {
        return calendar.get(Calendar.HOUR);
    }

    public int getDepartureMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public void setDepartureTime(int hour, int min) {
        this.calendar.set(Calendar.HOUR, hour);
        this.calendar.set(Calendar.MINUTE, min);
    }

    public Direction getDirection() {
        return direction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(CovoitContract.PathEntry._ID, this.getId());
        cv.put(CovoitContract.PathEntry.COLUMN_LAT, this.location.getLatitude());
        cv.put(CovoitContract.PathEntry.COLUMN_LON, this.location.getLongitude());
        cv.put(CovoitContract.PathEntry.COLUMN_DIRECTION, getDirectionString(this.getDirection()));
        cv.put(CovoitContract.PathEntry.COLUMN_HOUR, this.getDepartureHour());
        cv.put(CovoitContract.PathEntry.COLUMN_MIN, this.getDepartureMinute());
        if (this.getUser() != null)
            cv.put(CovoitContract.PathEntry.COLUMN_USER_ID, this.getUser().getId());
        return cv;
    }


    public static String getDirectionString(Direction d) {
        switch (d) {
            default:
            case HOME:
                return "home";
            case WP:
                return "wp";
        }
    }
}
