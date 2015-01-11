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
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.CovoitServerAccessor;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.sync.CovoitServerService;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.PasswordHash;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.PlacesAutoCompleteAdapter;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils.ValidatorInput;

public class RegisterActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private String mAccountType;
    private static final int WORKPLACE_LOADER = 0;
    private SimpleCursorAdapter mWorkplaceAdapter;
    private Spinner mWorkplaceSpinner;
    private ValidatorInput validator;

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

        validator = new ValidatorInput(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    private void createAccount() {

        final TextView name = ((TextView) findViewById(R.id.name));
        final TextView surname = ((TextView) findViewById(R.id.surname));
        final TextView mail = ((TextView) findViewById(R.id.email));
        final TextView pass = ((TextView) findViewById(R.id.password));
        final TextView phone = ((TextView) findViewById(R.id.phone));
        final TextView city = ((TextView) findViewById(R.id.city));
        final TextView departureHour = ((EditText) findViewById(R.id.departure_hour));
        final TextView returnHour = ((EditText) findViewById(R.id.return_hour));
        final Boolean driver = ((SwitchCompat) findViewById(R.id.driver)).isChecked();

        boolean valid = validator.fieldRequired(name);
        valid &= validator.fieldRequired(surname);
        valid &= validator.fieldMail(mail);
        valid &= validator.fieldRequired(pass);
        valid &= validator.fieldPhone(phone);
        valid &= validator.fieldRequired(city);
        valid &= validator.fieldTime(departureHour);
        valid &= validator.fieldTime(returnHour);

        if (valid) {
            new AsyncTask<String, Void, Intent>() {


                @Override
                protected Intent doInBackground(String... params) {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Calendar calendar = Calendar.getInstance();
                    Geocoder geocoder = new Geocoder(RegisterActivity.this);

                    // Set user info
                    User register = new User();
                    register.setName(name.getText().toString().trim());
                    register.setSurname(surname.getText().toString().trim());
                    register.setMail(mail.getText().toString().trim());
                    register.setPassword(PasswordHash.getHashSHA1(pass.getText().toString().trim()));
                    register.setPhone(phone.getText().toString().trim());
                    register.setDriver(driver);

                    Path path1 = new Path(Path.Direction.HOME);
                    Cursor c = (Cursor) mWorkplaceSpinner.getSelectedItem();
                    path1.setWorkplace(new Workplace(c.getInt(c.getColumnIndex(CovoitContract.WorkplaceEntry._ID))));
                    try {
                        Date d = df.parse(departureHour.getText().toString().trim());
                        calendar.setTime(d);
                        path1.setDepartureTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    } catch (ParseException ignored) {
                    } // Checked before
                    try {
                        List<Address> list = geocoder.getFromLocationName(city.getText().toString().trim(), 1);
                        Address address = list.get(0);
                        path1.setLocation(address.getLatitude(), address.getLongitude());
                    } catch (Exception e) {
                        final Intent res = new Intent();
                        res.putExtra(AuthenticatorActivity.KEY_ERROR_MESSAGE, "Impossible to find location");
                        return res;
                    }

                    Path path2 = new Path(Path.Direction.WP);
                    path2.setWorkplace(path1.getWorkplace());
                    path2.setLocation(path1.getLocation());
                    try {
                        Date d = df.parse(returnHour.getText().toString().trim());
                        calendar.setTime(d);
                        path2.setDepartureTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    register.addPath(path1);
                    register.addPath(path2);

                    // Create intent
                    final Intent res = new Intent();
                    res.putExtras(ServerAuthenticate.userSignUp(RegisterActivity.this, register));
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
