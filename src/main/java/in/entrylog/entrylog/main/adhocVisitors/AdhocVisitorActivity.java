package in.entrylog.entrylog.main.adhocVisitors;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.main.BlocksActivity;
import in.entrylog.entrylog.main.adhocVisitors.adhoc_el201.AddAdhocVisitors_EL201;
import in.entrylog.entrylog.main.adhocVisitors.adhocviewvisitor.ViewAdhocVisitors;
import in.entrylog.entrylog.main.services.AdhocFieldService;
import in.entrylog.entrylog.main.services.ContractorService;
import in.entrylog.entrylog.serialprinter.SerialPrinter;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.EL101_102;
import in.entrylog.entrylog.values.FunctionCalls;
import in.entrylog.entrylog.values.IMEIFunctionCalls;

public class AdhocVisitorActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int REQUEST_FOR_ACTIVITY_CODE = 1;

    Button addAdhocVisitor;
    Button viewAdhocVisitor;

    FunctionCalls functionCalls;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    ConnectingTask task;
    SerialPrinter printer;

    DetailsValue detailsValue;
    IMEIFunctionCalls imeiFunctionCalls;
    EL101_102 el101_102device;
    String OrganizationID, OrganizationName, GuardID, User, UpdateApkURL = "", Apkfile = "", Serverapkversion = "", Appversion = "",
            OverNightTime = "", ContextView;

    //RFID Check in Check out
    Thread adhocvisitorthread;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;
    static ProgressDialog dialog = null;
    boolean writeNFC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc_visitor);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();

        OrganizationID = settings.getString("OrganizationID", "");
        GuardID = settings.getString("GuardID", "");
        OrganizationName = settings.getString("OrganizationName", "");
        User = settings.getString("User", "");
        OverNightTime = settings.getString("OverNightTime", "");

        task = new ConnectingTask();
        printer = new SerialPrinter();
        detailsValue = new DetailsValue();
        functionCalls = new FunctionCalls();
        imeiFunctionCalls = new IMEIFunctionCalls();
        el101_102device = new EL101_102();

        adhocvisitorthread = null;
        Runnable runnable = new AdhocVisitorActivity.AdhocVisitorTimer();
        adhocvisitorthread = new Thread(runnable);
        adhocvisitorthread.start();

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }

        addAdhocVisitor = (Button) findViewById(R.id.add_adhoc_visitors_btn);
        viewAdhocVisitor = (Button) findViewById(R.id.adhoc_visitors_btn);

        Intent intent = new Intent(AdhocVisitorActivity.this, AdhocFieldService.class);
        startService(intent);

        Intent service4 = new Intent(AdhocVisitorActivity.this, ContractorService.class);
        startService(service4);

        addAdhocVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(AdhocVisitorActivity.this)) {
                    if (settings.getString("Device", "").equals("EL201")) {
                        addVisitors(AddAdhocVisitors_EL201.class);
                    }
                } else {
                    Toast.makeText(AdhocVisitorActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewAdhocVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(AdhocVisitorActivity.this)) {
                    Intent intent = new Intent(AdhocVisitorActivity.this, ViewAdhocVisitors.class);
                    intent.putExtra("ID", OrganizationID);
                    intent.putExtra("VIEW", ContextView);
                    startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
                } else {
                    Toast.makeText(AdhocVisitorActivity.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adhoc, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean handled = false;
        switch (item.getItemId()) {
            case R.id.action_adhoc_settings:
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                //Toast.makeText(AdhocVisitorActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                handled = true;
                break;
            case R.id.action_adhoc_Logout:
                Intent i = new Intent(AdhocVisitorActivity.this, BlocksActivity.class);
                startActivity(i);
                // handled = true;
                finish();
                break;
        }

        return handled;
    }

    private void addVisitors(Class startclass) {
        Intent intent = new Intent(AdhocVisitorActivity.this, startclass);
        intent.putExtra("ID", OrganizationID);
        intent.putExtra("GuardID", GuardID);
        intent.putExtra("OrganizationName", OrganizationName);
        intent.putExtra("User", User);
        startActivity(intent);
    }


    private void visitors(Class startclass, String view) {
        Intent visitors = new Intent(AdhocVisitorActivity.this, startclass);
        visitors.putExtra("VIEW", view);
        startActivity(visitors);
    }

    private void addVisitors1(Class startclass) {
        Intent intent = new Intent(AdhocVisitorActivity.this, startclass);
        intent.putExtra("ID", OrganizationID);
        intent.putExtra("GuardID", GuardID);
        intent.putExtra("OrganizationName", OrganizationName);
        intent.putExtra("User", User);
        startActivity(intent);
    }

    class AdhocVisitorTimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    visiting();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void visiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String Message = "";
                    if (detailsValue.isAdhocSmartIn()) {
                        adhocvisitorthread.interrupt();
                        detailsValue.setAdhocSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(AdhocVisitorActivity.this, Message);
                    }
                    if (detailsValue.isAdhocSmartOut()) {
                        adhocvisitorthread.interrupt();
                        detailsValue.setAdhocSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(AdhocVisitorActivity.this, Message);
                    }
                    if (detailsValue.isAdhocSmartError()) {
                        adhocvisitorthread.interrupt();
                        detailsValue.setAdhocSmartError(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Adhoc Visitor is Expired......";
                        functionCalls.smartCardStatus(AdhocVisitorActivity.this, Message);
                    }

                    if (detailsValue.isAdhocSmartInvalidCard()) {
                        adhocvisitorthread.interrupt();
                        detailsValue.setAdhocSmartInvalidCard(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Please Check in Again......";
                        functionCalls.smartCardStatus(AdhocVisitorActivity.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, AdhocVisitorActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(AdhocVisitorActivity.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(AdhocVisitorActivity.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagcontent = getTextfromNdefRecord(ndefRecord);
            checkingout(tagcontent);
        } else {
            Toast.makeText(AdhocVisitorActivity.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
        }
    }

    public String getTextfromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {

        }
        return tagContent;
    }

    public void checkingout(String result) {
        ConnectingTask.AdhocSmartCheckInOut checkOut = task.new AdhocSmartCheckInOut(detailsValue, result, OrganizationID, GuardID);
        checkOut.execute();
        dialog = ProgressDialog.show(AdhocVisitorActivity.this, "", "Checking...", true);
        dialog.setCancelable(true);
        adhocvisitorthread = null;
        Runnable runnable = new AdhocVisitorActivity.AdhocVisitorTimer();
        adhocvisitorthread = new Thread(runnable);
        adhocvisitorthread.start();
    }

}