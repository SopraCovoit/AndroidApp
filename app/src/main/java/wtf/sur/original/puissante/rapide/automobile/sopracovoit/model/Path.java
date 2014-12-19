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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Path class
 */
public class Path {
    public enum Direction {HOME, WP}

    private Location location;
    private final Calendar calendar;
    private final Direction direction;
    private User user;

    public Path(Direction direction) {
        this.direction = direction;
        this.calendar = new GregorianCalendar();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
}
