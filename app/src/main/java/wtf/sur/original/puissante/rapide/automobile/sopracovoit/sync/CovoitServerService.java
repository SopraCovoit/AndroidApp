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

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;

public interface CovoitServerService {
    @GET("/workplace")
    List<Workplace> listWorkplaces();

    @GET("/user/token")
    User connection(@retrofit.http.Path("mail") String mail, @retrofit.http.Path("password") String password);

    @POST("/newuser.php")
    User createUser(@Body User user);

    @GET("/path.json")
    List<Path> listPath();
}
