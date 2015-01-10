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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.authenticator;


import android.content.Context;
import android.database.Cursor;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.CovoitServerService;

public class ServerAuthenticate {

    public User userSignIn(Context context, String email, String pass) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://etud.insa-toulouse.fr/~livet")
                .build();

        CovoitServerService service = restAdapter.create(CovoitServerService.class);
        User u = service.connexion();

        assert (u.getWorkplace() != null);

        // Check if workplace exists
        Cursor c = context.getContentResolver().query(CovoitContract.WorkplaceEntry.buildUri(u.getWorkplace().getId()), null, null, null, null);
        if (c.getCount() == 0) {
            context.getContentResolver().insert(CovoitContract.WorkplaceEntry.CONTENT_URI, u.getWorkplace().getContentValues());
        } else {
            context.getContentResolver().update(CovoitContract.WorkplaceEntry.CONTENT_URI,
                    u.getWorkplace().getContentValues(),
                    CovoitContract.WorkplaceEntry._ID + " = " + u.getWorkplace().getId(), null);
        }

        // Check if user exists
        c = context.getContentResolver().query(CovoitContract.UserEntry.buildUri(u.getId()), null, null, null, null);
        if (c.getCount() == 0) {
            context.getContentResolver().insert(CovoitContract.UserEntry.CONTENT_URI, u.getContentValues());
        } else {
            context.getContentResolver().update(CovoitContract.UserEntry.CONTENT_URI,
                    u.getContentValues(),
                    CovoitContract.UserEntry._ID + " = " + u.getId(), null);
        }

        return u;
    }
}
