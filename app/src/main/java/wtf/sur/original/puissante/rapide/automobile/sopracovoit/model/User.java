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

import java.util.ArrayList;
import java.util.List;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.PasswordHash;

/**
 * User class
 */
public class User {
    private int id;
    private String name;
    private String surname;
    private String mail;
    private String phone;
    private boolean isDriver;
    private String password;

    private Workplace workplace;
    private String token;
    private List<Path> path = new ArrayList<>();

    public User(int id) {
        this.id = id;
    }

    public User() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean isDriver) {
        this.isDriver = isDriver;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(CovoitContract.UserEntry._ID, id);
        cv.put(CovoitContract.UserEntry.COLUMN_NAME, name);
        cv.put(CovoitContract.UserEntry.COLUMN_SURNAME, surname);
        cv.put(CovoitContract.UserEntry.COLUMN_MAIL, mail);
        cv.put(CovoitContract.UserEntry.COLUMN_PHONE, phone);
        cv.put(CovoitContract.UserEntry.COLUMN_IS_DRIVE, isDriver ? 1 : 0);
        if (workplace != null)
            cv.put(CovoitContract.UserEntry.COLUMN_WORKPLACE_ID, workplace.getId());
        return cv;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PasswordHash.getHashSHA1(password);
    }

    public List<Path> getPath() {
        return path;
    }

    public void addPath(Path path) {
        this.path.add(path);
    }
}
