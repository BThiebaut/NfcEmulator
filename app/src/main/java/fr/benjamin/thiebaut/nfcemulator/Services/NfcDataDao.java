package fr.benjamin.thiebaut.nfcemulator.Services;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fr.benjamin.thiebaut.nfcemulator.BO.NfcData;

@Dao
public interface NfcDataDao {
    @Insert
    void insert(NfcData... nfcDatas);

    @Update
    void update(NfcData... nfcDatas);

    @Delete
    void delete(NfcData... nfcDatas);

    @Query("SELECT * FROM " + DbService.TABLE_NAME + " WHERE id = :dataId")
    LiveData<NfcData> select(int dataId);

    @Query("Select * FROM " + DbService.TABLE_NAME)
    LiveData<List<NfcData>> selectAll();
}
