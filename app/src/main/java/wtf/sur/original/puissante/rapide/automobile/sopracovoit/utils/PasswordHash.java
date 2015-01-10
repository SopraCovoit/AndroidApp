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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by david on 10/01/15.
 */
public class PasswordHash {

    private static final String TAG = PasswordHash.class.getSimpleName();

    public static String getHashSHA1(String password) {
        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
            mdSha1.update(password.getBytes("ASCII"));
            byte[] data = mdSha1.digest();
            return Base64.encodeToString(data, 0, data.length, 0);

        } catch (NoSuchAlgorithmException e1) {
            Log.e(TAG, "Error initializing SHA1 message digest");

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding SHA1 message digest");
        }
        return null;
    }

}
