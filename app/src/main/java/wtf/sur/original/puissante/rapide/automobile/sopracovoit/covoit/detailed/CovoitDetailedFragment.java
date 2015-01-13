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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.covoit.detailed;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Location;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Path;

/**
 * Created by MagicMicky on 12/01/2015.
 */
public class CovoitDetailedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ID_PATH_KEY = "id_path_key";
    public static final String COVOIT_DET_TAG = "covoit_detailed";
    private static final int DETAIL_LOADER = 0;
    private long mId;
    private TextView mName;
    private TextView mFrom;
    private TextView mTo;
    private TextView mTime;
    private TextView mContactMail;
    private TextView mContactPhone;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mId = arguments.getLong(ID_PATH_KEY);
        }

        View root = inflater.inflate(R.layout.covoit_detailed_fragment, container,false);
        mName = (TextView) root.findViewById(R.id.TV_people_name);
        mFrom = (TextView) root.findViewById(R.id.TV_from);
        mTo = (TextView) root.findViewById(R.id.TV_to);
        mTime = (TextView) root.findViewById(R.id.TV_time);
        mContactMail = (TextView) root.findViewById(R.id.TV_mail);
        mContactPhone = (TextView) root.findViewById(R.id.TV_phone);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ID_PATH_KEY)) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                CovoitContract.PathEntry.buildUserId(mId),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_NAME));
            String surname = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_SURNAME));
            String mail = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_MAIL));
            String phone = data.getString(data.getColumnIndex(CovoitContract.UserEntry.COLUMN_PHONE));
            String sworkplace = data.getString(data.getColumnIndex(CovoitContract.WorkplaceEntry.COLUMN_NAME));
            Location l = new Location(data.getDouble(data.getColumnIndex(CovoitContract.PathEntry.COLUMN_LAT)), data.getDouble(data.getColumnIndex(CovoitContract.PathEntry.COLUMN_LON)));
            int hour = data.getInt(data.getColumnIndex(CovoitContract.PathEntry.COLUMN_HOUR));
            int minute = data.getInt(data.getColumnIndex(CovoitContract.PathEntry.COLUMN_MIN));
            int distance = data.getInt(data.getColumnIndex(CovoitContract.PathEntry.COLUMN_DISTANCE));
            Path.Direction dir = Path.getStringDirection(data.getString(data.getColumnIndex(CovoitContract.PathEntry.COLUMN_DIRECTION)));
            mName.setText(surname + " " + name);

            TextView location;
            TextView workplace;
            if (dir.equals(Path.Direction.HOME)) {
                location = mTo;
                workplace = mFrom;
            } else {
                location = mFrom;
                workplace = mTo;
            }

            workplace.setText(sworkplace);
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                Address address = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1).get(0);
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                location.setText(addressText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mContactMail.setText(mail);
            mContactPhone.setText(phone);
            mTime.setText(hour + ":" + minute);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static Fragment newInstance(long id) {
        Fragment f = new CovoitDetailedFragment();
        Bundle args = new Bundle();
        args.putLong(ID_PATH_KEY, id);
        f.setArguments(args);
        return f;
    }
}
