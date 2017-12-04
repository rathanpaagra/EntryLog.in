package in.entrylog.entrylog.main.apponitments;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.main.bluetooth.AddVisitor_Bluetooth;
import in.entrylog.entrylog.main.el101_102.AddVisitors_EL101;
import in.entrylog.entrylog.main.el201.AddVisitors_EL201;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.FunctionCalls;

public class Appointment_Details extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";

    String Appointment_Name, Appointment_Mobile, Appointment_Email, Appointment_Tomeet, Appointment_Date,
            Appointment_Time, Appointment_Purpose, Appointment_Location, Appointment_From;
    TextView tv_appointment_name, tv_appointment_mobile, tv_appointment_email, tv_appointment_tomeet,
            tv_appointment_date, tv_appointment_time, tv_appointment_purpose, tv_appointment_location, tv_appointment_from;

    String Organization_ID, CheckingUser, Device, PrinterType;

    String OrganizationID, GuardID, OrganizationName, USER;


    SharedPreferences.Editor editor;
    SharedPreferences settings;

    Thread appointmentsthread;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;
    static ProgressDialog dialog = null;

    ConnectingTask task;
    DetailsValue detailsValue;
    FunctionCalls functionCalls;


    Button Check_in;
    Context context;
    DetailsValue details;
    private static final int REQUEST_FOR_ACTIVITY_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Appointment_Name = bundle.getString("NAME");
        Appointment_Mobile = bundle.getString("MOBILE");
        Appointment_Email = bundle.getString("EMAIL");
        Appointment_Tomeet = bundle.getString("TOMEET");
        Appointment_Date = bundle.getString("DATE");
        Appointment_Time = bundle.getString("TIME");
        Appointment_Purpose = bundle.getString("PURPOSE");
        Appointment_Location = bundle.getString("LOCATION");
        Appointment_From=bundle.getString("FROM");

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        detailsValue = new DetailsValue();
        task = new ConnectingTask();
        functionCalls = new FunctionCalls();

        Organization_ID = settings.getString("OrganizationID", "");
        CheckingUser = settings.getString("GuardID", "");
        Device = settings.getString("Device", "");
        PrinterType = settings.getString("Printertype", "");

        tv_appointment_name = (TextView) findViewById(R.id.appointment_name);
        tv_appointment_mobile = (TextView) findViewById(R.id.appointment_mobile);
        tv_appointment_email = (TextView) findViewById(R.id.appointment_email);
        tv_appointment_from= (TextView) findViewById(R.id.appointment_from);
        tv_appointment_tomeet = (TextView) findViewById(R.id.appointment_tomeet);
        tv_appointment_date = (TextView) findViewById(R.id.appointment_date);
        tv_appointment_time = (TextView) findViewById(R.id.appointment_time);
        tv_appointment_purpose = (TextView) findViewById(R.id.appointment_purpose);
        tv_appointment_location = (TextView) findViewById(R.id.appointment_location);


        tv_appointment_name.setText(Appointment_Name);
        tv_appointment_mobile.setText(Appointment_Mobile);
        tv_appointment_email.setText(Appointment_Email);
        tv_appointment_from.setText(Appointment_From);
        tv_appointment_tomeet.setText(Appointment_Tomeet);
        tv_appointment_date.setText(Appointment_Date);
        tv_appointment_time.setText(Appointment_Time);
        tv_appointment_purpose.setText(Appointment_Purpose);
        tv_appointment_location.setText(Appointment_Location);

        appointmentsthread = null;
        Runnable runnable = new AppointmentsTimer();
        appointmentsthread = new Thread(runnable);
        appointmentsthread.start();

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }

        Check_in = (Button) findViewById(R.id.button_checkin);


        Check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionCalls.isInternetOn(Appointment_Details.this)) {
                    if (settings.getString("Printertype", "").equals("Bluetooth")) {
                        Toast.makeText(Appointment_Details.this, "Bluetooth with " + settings.getString("Device", ""),
                                Toast.LENGTH_SHORT).show();
                        addVisitors(AddVisitor_Bluetooth.class);
                    } else if (!settings.getString("Printertype", "").equals("Bluetooth")) {
                        if (settings.getString("Device", "").equals("EL101")) {
                            Toast.makeText(Appointment_Details.this, "EL101", Toast.LENGTH_SHORT).show();
                            addVisitors(AddVisitors_EL101.class);
                            /*if (el101_enabled) {
                                Toast.makeText(Appointment_Details.this, "EL101", Toast.LENGTH_SHORT).show();
                                addVisitors(AddVisitors_EL101.class);
                            } else {
                                Toast.makeText(Appointment_Details.this, "EL101/102 device will not support for your device..",
                                        Toast.LENGTH_SHORT).show();
                            }*/
                        } else if (settings.getString("Device", "").equals("EL201")) {
                            addVisitors(AddVisitors_EL201.class);
                            /*if (Build.MANUFACTURER.equals("LS888")) {
                                Toast.makeText(Appointment_Details.this, "EL201", Toast.LENGTH_SHORT).show();
                                addVisitors(AddVisitors_EL201.class);
                            } else {
                                Toast.makeText(Appointment_Details.this, "EL201 device will not support for your device..",
                                        Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    }
                } else {
                    Toast.makeText(Appointment_Details.this, "Please Turn On Internet", Toast.LENGTH_SHORT).show();
                }
                finish();

            }
        });


    }

    class AppointmentsTimer implements Runnable {

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
                    if (detailsValue.isSmartIn()) {
                        appointmentsthread.interrupt();
                        detailsValue.setSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(Appointment_Details.this, Message);
                    }
                    if (detailsValue.isSmartOut()) {
                        appointmentsthread.interrupt();
                        detailsValue.setSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(Appointment_Details.this, Message);
                    }
                    if (detailsValue.isSmartError()) {
                        appointmentsthread.interrupt();
                        detailsValue.setSmartError(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Please check again...";
                        functionCalls.smartCardStatus(Appointment_Details.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    private void addVisitors(Class startclass) {
        Intent intent = new Intent(Appointment_Details.this, startclass);
        intent.putExtra("MOBILE", Appointment_Mobile);
        intent.putExtra("ID", settings.getString("OrganizationID", ""));
        intent.putExtra("GuardID", settings.getString("GuardID", ""));
        intent.putExtra("OrganizationName", settings.getString("OrganizationName", ""));
        intent.putExtra("User", settings.getString("User", ""));
        startActivity(intent);
    }

    /*  private void addVisitors(Class startclass) {
          Intent intent = new Intent(Appointment_Details.this, startclass);
          intent.putExtra("ID", OrganizationID);
          intent.putExtra("GuardID", GuardID);
          intent.putExtra("OrganizationName", OrganizationName);
          intent.putExtra("User", USER);
          startActivity(intent);
      }
      */
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Appointment_Details.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(Appointment_Details.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(Appointment_Details.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Appointment_Details.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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
        ConnectingTask.SmartCheckinout checkOut = task.new SmartCheckinout(detailsValue, result, Organization_ID, CheckingUser);
        checkOut.execute();
        dialog = ProgressDialog.show(Appointment_Details.this, "", "Checking...", true);
        appointmentsthread = null;
        Runnable runnable = new AppointmentsTimer();
        appointmentsthread = new Thread(runnable);
        appointmentsthread.start();
    }
}
