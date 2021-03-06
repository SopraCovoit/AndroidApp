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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.model;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private final Direction direction;
    private User user;
    private int distance;
    private Workplace workplace;
    private int hour, minute;
    private String departure_hour;

    public Path(int id, Direction direction) {
        this(direction);
        this.id = id;
    }

    public Path(Direction direction) {
        this.direction = direction;
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

    public void setDeparture_hour(String hour) {
        departure_hour = hour;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        try {
            c.setTime(s.parse(hour));
            this.hour = c.get(Calendar.HOUR_OF_DAY);
            this.minute = c.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDepartureTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        departure_hour = hour + ":" + minute;
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


    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    public ContentValues getContentValues() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        try {
            c.setTime(s.parse(departure_hour));
            this.hour = c.get(Calendar.HOUR_OF_DAY);
            this.minute = c.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContentValues cv = new ContentValues();
        cv.put(CovoitContract.PathEntry._ID, this.getId());
        cv.put(CovoitContract.PathEntry.COLUMN_LAT, this.location.getLatitude());
        cv.put(CovoitContract.PathEntry.COLUMN_LON, this.location.getLongitude());
        cv.put(CovoitContract.PathEntry.COLUMN_DIRECTION, getDirectionString(this.getDirection()));
        cv.put(CovoitContract.PathEntry.COLUMN_HOUR, this.hour);
        cv.put(CovoitContract.PathEntry.COLUMN_MIN, this.minute);
        cv.put(CovoitContract.PathEntry.COLUMN_DISTANCE, this.getDistance());
        if (this.getUser() != null)
            cv.put(CovoitContract.PathEntry.COLUMN_USER_ID, this.getUser().getId());
        if (this.getWorkplace() != null)
            cv.put(CovoitContract.PathEntry.COLUMN_WORKPLACE_ID, this.getWorkplace().getId());
        return cv;
    }


    public static String getDirectionString(Direction d) {
        switch (d) {
            default:
            case HOME:
                return "HOME";
            case WP:
                return "WP";
        }
    }

    public static Direction getStringDirection(String d) {
        switch (d) {
            default:
            case "HOME":
                return Direction.HOME;
            case "WP":
                return Direction.WP;
        }
    }
}
