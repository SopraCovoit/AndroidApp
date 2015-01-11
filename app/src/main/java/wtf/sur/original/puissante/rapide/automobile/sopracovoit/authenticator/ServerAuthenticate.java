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


import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitProvider;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitProviderHelper;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.CovoitServerAccessor;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.CovoitServerService;

public class ServerAuthenticate {

    public static Bundle userSignIn(Context context, User login) {
        User u = CovoitServerAccessor.connection();
        u.setPassword(login.getPassword());
        return finishLogin(context, u);
    }

    public static Bundle userSignUp(Context context, User register) {
        User newUser = CovoitServerAccessor.createUser(register);
        newUser.setPassword(register.getPassword());
        return finishLogin(context, newUser);
    }

    private static Bundle finishLogin(Context context, User u) {
        assert (u.getWorkplace() != null);

        // Check if workplace exists or add
        CovoitProviderHelper.insertOrUpdateWorkplace(context.getContentResolver(), u.getWorkplace());

        // Check if user exists or add
        CovoitProviderHelper.insertOrUpdateUser(context.getContentResolver(), u);

        // Check if path exists or add
        for (Path p : u.getPath()) {
            p.setUser(u);
            CovoitProviderHelper.insertOrUpdateWorkplace(context.getContentResolver(), p.getWorkplace());
            CovoitProviderHelper.insertOrUpdatePath(context.getContentResolver(), p);
        }

        // Create bundle
        Bundle data = new Bundle();
        data.putString(AccountManager.KEY_ACCOUNT_NAME, u.getMail());
        data.putString(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);
        data.putString(AccountManager.KEY_AUTHTOKEN, u.getToken());
        data.putString(AuthenticatorActivity.PARAM_USER_PASS, u.getPassword());
        return data;
    }
}
