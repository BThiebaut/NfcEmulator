package fr.benjamin.thiebaut.nfcemulator.BO;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import fr.benjamin.thiebaut.nfcemulator.Services.DbService;

@Entity(
        tableName = DbService.TABLE_NAME
)
public class NfcData {

    @PrimaryKey
    private int id;

    @NonNull
    private String title;

    @NonNull
    private String data;

    public NfcData(int id, String title, String data) {
        this.id = id;
        this.title = title;
        this.data = data;
    }

    public NfcData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
