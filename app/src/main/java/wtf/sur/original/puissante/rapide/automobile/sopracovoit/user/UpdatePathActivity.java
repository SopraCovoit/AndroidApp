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

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.BaseActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;

/**
 * Created by MagicMicky on 13/01/2015.
 */
public class UpdatePathActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WORKPLACE_LOADER = 0;
    private SimpleCursorAdapter mWorkplaceAdapter;
    private EditText city;
    private Spinner sopralist;
    private EditText return_hour;
    private EditText departure_hour;
    private SwitchCompat isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.city = (EditText) findViewById(R.id.city);

        sopralist = (Spinner) findViewById(R.id.sopralist);
        sopralist.setEnabled(false);
        mWorkplaceAdapter = new SimpleCursorAdapter(this, R.layout.simple_text_view, null, new String[]{CovoitContract.WorkplaceEntry.COLUMN_NAME}, new int[]{R.id.textView}, 0);
        sopralist.setAdapter(mWorkplaceAdapter);

        this.departure_hour = (EditText) findViewById(R.id.departure_hour);
        this.return_hour = (EditText) findViewById(R.id.return_hour);
        this.isDriver = (SwitchCompat) findViewById(R.id.driver);
        getLoaderManager().initLoader(WORKPLACE_LOADER, null, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_register:
                //TODO
                return true;
            default:
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                CovoitContract.WorkplaceEntry.CONTENT_URI,
                null,
                null,
                null,
                CovoitContract.WorkplaceEntry.COLUMN_NAME + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWorkplaceAdapter.swapCursor(data);
        if (data.getCount() > 0) {
            sopralist.setEnabled(true);
        } else {
            sopralist.setEnabled(false);
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_update_path;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWorkplaceAdapter.swapCursor(null);
    }

}
