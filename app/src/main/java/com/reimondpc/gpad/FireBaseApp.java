package com.reimondpc.gpad;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseApp extends android.app.Application {

    @Override
    public void onCreate() {        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
