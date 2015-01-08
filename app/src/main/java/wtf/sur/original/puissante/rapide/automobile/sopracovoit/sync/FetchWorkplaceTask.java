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


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.data.CovoitContract;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Location;
import wtf.sur.original.puissante.rapide.automobile.sopracovoit.model.Workplace;

public class FetchWorkplaceTask extends AsyncTask<String, Void, Void> {

    private final Context mContext;

    public FetchWorkplaceTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://etud.insa-toulouse.fr/~livet")
                .build();

        CovoitServerService service = restAdapter.create(CovoitServerService.class);
        List<Workplace> workplaces = service.listWorkplaces();
        ContentValues cv[] = new ContentValues[workplaces.size()];
        int i = 0;
        for (Workplace w : workplaces) {
            cv[i] = w.getContentValues();
            i++;
        }
        mContext.getContentResolver().delete(CovoitContract.WorkplaceEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().bulkInsert(CovoitContract.WorkplaceEntry.CONTENT_URI, cv);

        return null;
    }
}
