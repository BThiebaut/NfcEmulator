package fr.benjamin.thiebaut.nfcemulator.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import fr.benjamin.thiebaut.nfcemulator.BO.NfcData;
import fr.benjamin.thiebaut.nfcemulator.Services.DbService;
import fr.benjamin.thiebaut.nfcemulator.Services.NfcDataDao;

public class NfcDataRepository {

    private NfcDataDao nfcDataDao;

    public NfcDataRepository(Application application) {
        DbService db = DbService.getDatabase(application);
        nfcDataDao = db.nfcDataDao();
    }

    public LiveData<List<NfcData>> getAllDatas(){
        return nfcDataDao.selectAll();
    }


    public LiveData<NfcData> getById(int id) {
        return nfcDataDao.select(id);
    }

    public void insert(NfcData data) {
        new crudAsyncTask(nfcDataDao, "insert").execute(data);
    }

    public void update(NfcData data) {
        new crudAsyncTask(nfcDataDao, "update").execute(data);
    }

    public void delete(NfcData data) {
        new crudAsyncTask(nfcDataDao, "delete").execute(data);
    }



    private static class crudAsyncTask extends AsyncTask<NfcData, Void, Void> {

        private NfcDataDao mAsyncTaskDao;
        private String action;

        crudAsyncTask(NfcDataDao dao, String action) {
            mAsyncTaskDao = dao;
            this.action = action;
        }

        @Override
        protected Void doInBackground(final NfcData... params) {
            switch (action) {
                case "insert":
                    mAsyncTaskDao.insert(params[0]);
                    break;
                case "update":
                    mAsyncTaskDao.update(params[0]);
                    break;
                case "delete":
                    mAsyncTaskDao.delete(params[0]);
                    break;
            }
            return null;
        }
    }


}
