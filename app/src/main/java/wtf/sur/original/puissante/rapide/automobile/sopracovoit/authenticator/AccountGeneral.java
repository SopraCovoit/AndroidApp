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

public class AccountGeneral {
    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync";

    public static final String AUTHTOKEN_TYPE = "Authtoken";
    public static final String AUTHTOKEN_TYPE_LABEL = "Authenticator token";

    public static final ServerAuthenticate sServerAuthenticate = new ServerAuthenticate();

}
