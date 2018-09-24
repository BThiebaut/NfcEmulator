package fr.benjamin.thiebaut.nfcemulator.Services;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import fr.benjamin.thiebaut.nfcemulator.Exceptions.NfcException;
import fr.benjamin.thiebaut.nfcemulator.Tasks.NdefReaderTask;

public class NfcReader {

    private Context context;
    NfcAdapter adapter;

    public NfcReader(Context context) throws Exception {
        this.context = context;
        adapter = NfcAdapter.getDefaultAdapter(context);
        if (adapter == null){
            throw new Exception("Nfc not supported !");
        }
        if (!adapter.isEnabled()){
            throw new NfcException("Nfc is disabled !");
        }
    }

    public NfcAdapter getAdapter(){
        return adapter;
    }

    public void startScan(Tag tag){
        new NdefReaderTask((NdefReaderTask.IReaderTask) context).execute(tag);
    }

}
