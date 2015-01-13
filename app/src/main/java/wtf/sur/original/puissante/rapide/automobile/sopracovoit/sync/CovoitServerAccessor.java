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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync;

import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;

public class CovoitServerAccessor {

    private static final String URL = "http://192.168.1.6:8080/api";
    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(URL)
            .build();

    public static List<Workplace> listWorkplace() {
        CovoitServerService service = restAdapter.create(CovoitServerService.class);
        return service.listWorkplaces();
    }

    public static User createUser(User add) {
        CovoitServerService service = restAdapter.create(CovoitServerService.class);
        return service.createUser(add);
    }

    public static User connection(User connect) {
        CovoitServerService service = restAdapter.create(CovoitServerService.class);
        return service.connection(connect.getMail(), connect.getPassword());
    }

    public static List<Path> listPath() {
        CovoitServerService service = restAdapter.create(CovoitServerService.class);
        return service.listPath();
    }
}
