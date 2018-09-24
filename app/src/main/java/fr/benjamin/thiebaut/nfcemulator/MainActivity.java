package fr.benjamin.thiebaut.nfcemulator;

import android.app.Activity;
import android.app.PendingIntent;
import android.arch.core.util.Function;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.benjamin.thiebaut.nfcemulator.Exceptions.NfcException;
import fr.benjamin.thiebaut.nfcemulator.Services.NfcReader;
import fr.benjamin.thiebaut.nfcemulator.Tasks.NdefReaderTask;
import fr.benjamin.thiebaut.nfcemulator.Utils.Util;

public class MainActivity extends AppCompatActivity implements NdefReaderTask.IReaderTask {

    Button btnRead;
    Button btnEmulate;
    TextView txtResult;

    private static boolean RECREATED;
    private static final int REQUEST_CODE_PERM = 1;
    private static final String MIME_TEXT_PLAIN = "text/plain";
    private NfcReader nfcReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permission and nfc
        try {
            nfcReader = new NfcReader(this);
        } catch (NfcException n) {
            // Nfc disabled, ask for activation
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } catch (Exception e) {
            // Nfc module not available
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Util.requestPermissions(this, REQUEST_CODE_PERM);

        btnRead = findViewById(R.id.btn_read_nfc);
        btnEmulate = findViewById(R.id.btn_emulate_nfc);
        txtResult = findViewById(R.id.txt_nfc_result);

        btnRead.setOnClickListener(l -> readNfcAction());
        btnEmulate.setOnClickListener(l -> emulateNfcAction());
    }

    public void readNfcAction(){

    }

    public void emulateNfcAction(){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allOk = true;
        for (int res : grantResults){
            if (allOk && res != RESULT_OK){
                allOk = false;
            }
        }
        if (allOk){
            if (!RECREATED){
                Handler handler = new Handler();
                Activity ctx = this;
                handler.postDelayed(() -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
                    {
                        ctx.finish();
                        ctx.startActivity(ctx.getIntent());
                    } else ctx.recreate();
                    RECREATED = true;
                }, 1);
            }
        }else {
            // Permission required for application
            Util.requestPermissions(this, REQUEST_CODE_PERM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcReader != null){
            setupForegroundDispatch(this, nfcReader.getAdapter());
        }
    }

    @Override
    protected void onPause() {
        if (nfcReader != null){
            stopForegroundDispatch(this, nfcReader.getAdapter());
        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                nfcReader.startScan(tag);
            } else {
                Log.d(String.valueOf(this.getClass()), "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    nfcReader.startScan(tag);
                    break;
                }
            }
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    @Override
    public void onReadTaskEnd(String result) {
        this.runOnUiThread(() -> {
            txtResult.setText(result);
        });
    }
}
