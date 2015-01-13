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
import android.support.v7.widget.SwitchCompat;
import android.widget.EditText;
import android.widget.Spinner;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.BaseActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;

/**
 * Created by MagicMicky on 13/01/2015.
 */
public class UpdatePathActivity extends BaseActivity {

    private EditText city;
    private Spinner sopralist;
    private EditText return_hour;
    private EditText departure_hour;
    private SwitchCompat isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.city = (EditText) findViewById(R.id.city);
        this.sopralist = (Spinner) findViewById(R.id.sopralist);
        this.departure_hour = (EditText) findViewById(R.id.departure_hour);
        this.return_hour = (EditText) findViewById(R.id.return_hour);
        this.isDriver = (SwitchCompat) findViewById(R.id.driver);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_update_path;
    }
}
