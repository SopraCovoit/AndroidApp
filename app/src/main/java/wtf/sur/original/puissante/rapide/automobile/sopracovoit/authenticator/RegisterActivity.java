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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit.RestAdapter;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.BaseActivity;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.User;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.CovoitServerService;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.PasswordHash;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.PlacesAutoCompleteAdapter;

public class RegisterActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private String mAccountType;
    private static final int WORKPLACE_LOADER = 0;
    private SimpleCursorAdapter mWorkplaceAdapter;
    private Spinner mWorkplaceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getLoaderManager().initLoader(WORKPLACE_LOADER, null, this);

        mAccountType = getIntent().getStringExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE);

        mWorkplaceSpinner = (Spinner) findViewById(R.id.sopralist);
        mWorkplaceSpinner.setEnabled(false);
        mWorkplaceAdapter = new SimpleCursorAdapter(this, R.layout.simple_text_view, null, new String[]{CovoitContract.WorkplaceEntry.COLUMN_NAME}, new int[]{R.id.textView}, 0);
        mWorkplaceSpinner.setAdapter(mWorkplaceAdapter);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.city);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        final EditText departureTime = (EditText) findViewById(R.id.departure_hour);
        final TimePickerDialog departureTimePickerDialog = new TimePickerDialog(RegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                departureTime.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, 8, 0, true);
        departureTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    departureTimePickerDialog.show();
                }
            }
        });

        final EditText returnTime = (EditText) findViewById(R.id.return_hour);
        final TimePickerDialog returnTimePickerDialog = new TimePickerDialog(RegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                returnTime.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, 18, 0, true);
        returnTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    returnTimePickerDialog.show();
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    private void createAccount() {

        new AsyncTask<String, Void, Intent>() {

            String name = ((TextView) findViewById(R.id.name)).getText().toString().trim();
            String surname = ((TextView) findViewById(R.id.surname)).getText().toString().trim();
            String mail = ((TextView) findViewById(R.id.email)).getText().toString().trim();
            String pass = PasswordHash.getHashSHA1(((TextView) findViewById(R.id.password)).getText().toString().trim());
            String phone = ((TextView) findViewById(R.id.phone)).getText().toString().trim();
            String city = ((TextView) findViewById(R.id.city)).getText().toString().trim();
            String departureHour = ((EditText) findViewById(R.id.departure_hour)).getText().toString().trim();
            String returnHour = ((EditText) findViewById(R.id.return_hour)).getText().toString().trim();
            Boolean driver = ((SwitchCompat) findViewById(R.id.driver)).isChecked();
            final String accountType = getIntent().getStringExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE);

            @Override
            protected Intent doInBackground(String... params) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Calendar calendar = Calendar.getInstance();
                Geocoder geocoder = new Geocoder(RegisterActivity.this);

                User register = new User();
                register.setName(name);
                register.setSurname(surname);
                register.setMail(mail);
                register.setPassword(pass);
                register.setPhone(phone);
                register.setDriver(driver);

                Path path1 = new Path(Path.Direction.HOME);
                Cursor c = (Cursor) mWorkplaceSpinner.getSelectedItem();
                path1.setWorkplace(new Workplace(c.getInt(c.getColumnIndex(CovoitContract.WorkplaceEntry._ID))));
                try {
                    Date d = df.parse(departureHour);
                    calendar.setTime(d);
                    path1.setDepartureTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    List<Address> list = geocoder.getFromLocationName(city, 1);
                    Address address = list.get(0);
                    path1.setLocation(address.getLatitude(), address.getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Path path2 = new Path(Path.Direction.WP);
                path2.setWorkplace(path1.getWorkplace());
                path2.setLocation(path1.getLocation());
                try {
                    Date d = df.parse(returnHour);
                    calendar.setTime(d);
                    path2.setDepartureTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                register.addPath(path1);
                register.addPath(path2);

                Bundle data = new Bundle();
                try {
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint("http://etud.insa-toulouse.fr/~livet")
                            .build();

                    CovoitServerService service = restAdapter.create(CovoitServerService.class);

                    User newUser = service.createUser(register);

                    // Check if workplace exists
                    c = getContentResolver().query(CovoitContract.WorkplaceEntry.buildUri(newUser.getWorkplace().getId()), null, null, null, null);
                    if (c.getCount() == 0) {
                        getContentResolver().insert(CovoitContract.WorkplaceEntry.CONTENT_URI, newUser.getWorkplace().getContentValues());
                    } else {
                        getContentResolver().update(CovoitContract.WorkplaceEntry.CONTENT_URI,
                                newUser.getWorkplace().getContentValues(),
                                CovoitContract.WorkplaceEntry._ID + " = " + newUser.getWorkplace().getId(), null);
                    }

                    // Check if user exists
                    c = getContentResolver().query(CovoitContract.UserEntry.buildUri(newUser.getId()), null, null, null, null);
                    if (c.getCount() == 0) {
                        getContentResolver().insert(CovoitContract.UserEntry.CONTENT_URI, newUser.getContentValues());
                    } else {
                        getContentResolver().update(CovoitContract.UserEntry.CONTENT_URI,
                                newUser.getContentValues(),
                                CovoitContract.UserEntry._ID + " = " + newUser.getId(), null);
                    }

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, newUser.getMail());
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, newUser.getToken());
                    data.putString(AuthenticatorActivity.PARAM_USER_PASS, newUser.getPassword());
                } catch (Exception e) {
                    data.putString(AuthenticatorActivity.KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(AuthenticatorActivity.KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(AuthenticatorActivity.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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
            mWorkplaceSpinner.setEnabled(true);
        } else {
            mWorkplaceSpinner.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registration,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_register:
                createAccount();
                return true;
            default:
                //TODO
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWorkplaceAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
