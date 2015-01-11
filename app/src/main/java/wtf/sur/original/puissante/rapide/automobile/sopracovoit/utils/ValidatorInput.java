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

package wtf.sur.original.puissante.rapide.automobile.sopracovoit.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wtf.sur.original.puissante.rapide.automobile.sopracovoit.R;

/**
 * Created by david on 11/01/15.
 */
public class ValidatorInput {

    private final Context mContext;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    public ValidatorInput(Context context) {
        mContext = context;
    }

    public boolean fieldRequired(TextView e) {
        if (e.getText().toString().trim().length() == 0) {
            e.setError(mContext.getString(R.string.field_required));
            return false;
        }
        return true;
    }

    public boolean fieldMail(TextView e) {
        if (e.getText().toString().trim().length() == 0) {
            e.setError(mContext.getString(R.string.field_required));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(e.getText().toString().trim()).matches()) {
            e.setError(mContext.getString(R.string.field_bad_email));
            return false;
        }
        return true;
    }

    public boolean fieldPhone(TextView e) {
        if (e.getText().toString().trim().length() == 0) {
            e.setError(mContext.getString(R.string.field_required));
            return false;
        } else if (!Patterns.PHONE.matcher(e.getText().toString().trim()).matches()) {
            e.setError(mContext.getString(R.string.field_bad_phone));
            return false;
        }
        return true;
    }

    public boolean fieldTime(TextView e) {
        if (e.getText().toString().trim().length() == 0) {
            e.setError(mContext.getString(R.string.field_required));
            return false;
        } else {
            try {
                df.parse(e.getText().toString().trim());
                return true;
            } catch (ParseException exception) {
                e.setError(mContext.getString(R.string.field_bad_time));
                return false;
            }
        }
    }
}
