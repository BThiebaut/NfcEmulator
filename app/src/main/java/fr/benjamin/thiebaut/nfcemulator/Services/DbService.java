package fr.benjamin.thiebaut.nfcemulator.Services;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import fr.benjamin.thiebaut.nfcemulator.BO.NfcData;

@Database(entities = {NfcData.class}, version = 1)
public abstract class DbService extends RoomDatabase{

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "NfcMemory.db";
    public static final String TABLE_NAME = "scans";

    public abstract NfcDataDao nfcDataDao();

    private static DbService instance;

    public static DbService getDatabase(final Context context) {
        if (instance == null) {
            synchronized (DbService.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        DbService.class, DbService.DB_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
