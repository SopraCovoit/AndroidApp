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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.user;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.TextView;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.BaseActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;

/**
 * Created by MagicMicky on 13/01/2015.
 */
public class UpdateProfileActivity extends BaseActivity {

    private EditText name;
    private EditText surname;
    private EditText mail;
    private EditText phone;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.name = (EditText) findViewById(R.id.name);
        this.surname = (EditText) findViewById(R.id.surname);
        this.mail = (EditText) findViewById(R.id.email);
        this.phone = (EditText) findViewById(R.id.phone);
        this.password = (EditText) findViewById(R.id.password);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_update_user;
    }
}
