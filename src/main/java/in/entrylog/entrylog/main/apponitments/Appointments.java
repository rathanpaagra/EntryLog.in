package in.entrylog.entrylog.main.apponitments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.adapters.AppointmentAdapters;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.dataposting.ConnectingTask.AllAppointments;
import in.entrylog.entrylog.main.Visitors;
import in.entrylog.entrylog.main.services.StaffService;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.FunctionCalls;

public class Appointments extends AppCompatActivity implements View.OnClickListener {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int REQUEST_FOR_ACTIVITY_CODE = 1;
    public static final int APPOINTMENTS_DLG = 3;
    public static final int DATE_DLG = 2;

    //region Declaration
    RecyclerView Appointmentview;
    ArrayList<DetailsValue> AppointmentsList;
    AppointmentAdapters Appointmentadapter;
    RecyclerView.LayoutManager layoutManager;
    ConnectingTask task;
    DetailsValue detailsValue;
    String Organization_ID, ContextView, CheckingUser, SearchName="", SearchMobile="", SearchTomeet="",
            Device="", PrinterType = "",  CheckinDate="", StaffTomeetId="", StaffTomeet="";
    Button Search_btn, SearchByName_btn, SearchByMobile_btn, Checkin_btn, SearchByToMeet_btn, Reset_btn;
    boolean searchname = false, searchmobile = false, searchtomeet = false, searchcheckin = false,
           result = false, checkindate = false;
    EditText et_SearchName, et_SearchMobile;
    AutoCompleteTextView et_SearchTomeet;
    TextInputLayout Til_SearchName, Til_SearchMobile, Til_SearchTomeet;
    LinearLayout CheckinLayout;
    ImageView Edit_Checkin;
    TextView tv_CheckIndate;
    int year, month, date;
    SharedPreferences settings;
    Thread appointmentsthread;
    FunctionCalls functionCalls;
    static ProgressDialog dialog = null;
    StaffService staffService;
    ArrayAdapter<String> Staffadapter;
    static ArrayList<String> stafflist;
    HashMap<String, String> listid;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_entrylog_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        detailsValue = new DetailsValue();
        task = new ConnectingTask();
        functionCalls = new FunctionCalls();
        staffService = new StaffService();

        functionCalls.OrientationView(Appointments.this);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //region Get Intent results
        Organization_ID = settings.getString("OrganizationID", "");
        CheckingUser = settings.getString("GuardID", "");
        Device = settings.getString("Device", "");
        PrinterType = settings.getString("Printertype", "");
        //endregion

        //region Buttons Initialization
        Search_btn = (Button) findViewById(R.id.search_btn);
        Search_btn.setOnClickListener(this);
        Reset_btn = (Button) findViewById(R.id.reset_btn);
        Reset_btn.setOnClickListener(this);
        SearchByName_btn = (Button) findViewById(R.id.searchvisitor_name_btn);
        SearchByName_btn.setOnClickListener(this);
        SearchByMobile_btn = (Button) findViewById(R.id.searchvisitor_mobile_btn);
        SearchByMobile_btn.setOnClickListener(this);

        SearchByToMeet_btn = (Button) findViewById(R.id.searchvisitor_tomeet_btn);
        SearchByToMeet_btn.setOnClickListener(this);

        Checkin_btn = (Button) findViewById(R.id.checkindate_btn);
        Checkin_btn.setOnClickListener(this);
        //endregion

        //region Text Input Layout Initialization
        Til_SearchName = (TextInputLayout) findViewById(R.id.searchvisitor_name_Til);
        Til_SearchMobile = (TextInputLayout) findViewById(R.id.searchvisitor_mobile_Til);
        Til_SearchTomeet = (TextInputLayout) findViewById(R.id.searchvisitor_tomeet_Til);
        //endregion

        //region CheckDates Linear Layout Initialization
        CheckinLayout = (LinearLayout) findViewById(R.id.checkin_layout);
        //endregion

        //region Check Date Edit Initialization
        Edit_Checkin = (ImageView) findViewById(R.id.checkindate_edit);
        Edit_Checkin.setOnClickListener(this);

        //region TextView Initialization
        tv_CheckIndate = (TextView) findViewById(R.id.checkindate_Txt);

        //region Edit Text Initialization
        et_SearchName = (EditText) findViewById(R.id.searchvisitor_name);
        et_SearchMobile = (EditText) findViewById(R.id.searchvisitor_mobile);
        et_SearchTomeet = (AutoCompleteTextView) findViewById(R.id.searchvisitor_tomeet);
        //endregion

        //region RecyclerView with Adapter
        StaggeredRotationChanged();
        Appointmentview = (RecyclerView) findViewById(R.id.appointmentview);
        AppointmentsList = new ArrayList<DetailsValue>();
        Appointmentadapter = new AppointmentAdapters(Appointments.this, AppointmentsList, ContextView, Organization_ID,
                Device, PrinterType);
        Appointmentview.setHasFixedSize(true);
        Appointmentview.setLayoutManager(layoutManager);
        Appointmentview.setAdapter(Appointmentadapter);
        //endregion

        AllAppointments checkAppointments = task.new AllAppointments(AppointmentsList, Appointmentadapter, detailsValue,
                Organization_ID);
        checkAppointments.execute();
        dialog = ProgressDialog.show(Appointments.this, "", "Searching for a appointments..", true);
        appointmentsthread = null;
        Runnable runnable = new AppointmentsTimer();
        appointmentsthread = new Thread(runnable);
        appointmentsthread.start();

        et_SearchMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                int test = number.length();
                if (test >= 1) {
                    String trimnumber = number.substring(0, 1);
                    int num = Integer.parseInt(trimnumber);
                    if (num == 7 || num == 8 || num == 9) {
                    } else {
                        et_SearchMobile.setText("");
                    }
                }
            }
        });

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }
    }

    private void StaggeredRotationChanged() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                switch (rotation) {
                    case Surface.ROTATION_0:
                        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                        break;
                    case Surface.ROTATION_90:
                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        break;
                    case Surface.ROTATION_270:
                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        break;
                }
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                break;
            default:
                layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                reload();
            }
        }
    }

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void searchbtn() {
        if (Search_btn.getVisibility() != View.VISIBLE) {
            Search_btn.setVisibility(View.VISIBLE);
            Reset_btn.setVisibility(View.VISIBLE);
            Appointmentview.setVisibility(View.GONE);
        }
    }

    private void tomeetcontent() {
        functionCalls.LogStatus("Staff field Started");
        HashSet<String> StaffSet = new HashSet<>();
        StaffSet = staffService.staffset;
        ArrayList<String> list = new ArrayList<>();
        listid = new HashMap<>();
        list.addAll(StaffSet);
        stafflist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String liststaff = list.get(i);
            String staff = liststaff.substring(0, liststaff.lastIndexOf(','));
            String staffid = liststaff.substring(liststaff.lastIndexOf(',')+1, liststaff.length());
            stafflist.add(staff);
            listid.put(staff.toLowerCase(), staffid);
        }
        if (stafflist.size() > 0) {
            functionCalls.LogStatus("Staff list Available");
            Staffadapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stafflist);
            et_SearchTomeet.setAdapter(Staffadapter);
            Collections.sort(stafflist);
            Staffadapter.notifyDataSetChanged();
            et_SearchTomeet.setThreshold(1);
            et_SearchTomeet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StaffTomeet = parent.getItemAtPosition(position).toString();
                    StaffTomeetId = listid.get(parent.getItemAtPosition(position).toString().toLowerCase());
                }
            });
        } else {
            functionCalls.LogStatus("Staff list not Available");
            Toast.makeText(this, "Staff list not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkindate_btn:
                CheckinDate = "";
                searchbtn();
                checkindate = true;
                showdialog(DATE_DLG);
                break;

            case R.id.checkindate_edit:
                checkindate = true;
                showdialog(DATE_DLG);
                break;

            case R.id.searchvisitor_name_btn:
                SearchName = "";
                SearchByName_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchname = true;
                if (Til_SearchName.getVisibility() != View.VISIBLE) {
                    Til_SearchName.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.searchvisitor_mobile_btn:
                SearchMobile = "";
                SearchByMobile_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchmobile = true;
                if (Til_SearchMobile.getVisibility() != View.VISIBLE) {
                    Til_SearchMobile.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.searchvisitor_tomeet_btn:
                SearchTomeet = "";
                SearchByToMeet_btn.setVisibility(View.GONE);
                searchbtn();
                reset();
                searchtomeet = true;
                if (Til_SearchTomeet.getVisibility() != View.VISIBLE) {
                    Til_SearchTomeet.setVisibility(View.VISIBLE);
                }
                tomeetcontent();
                break;

            case R.id.reset_btn:
                fullreset();
                break;

            case R.id.search_btn:
                for (int i = 0; i < 6; i++) {
                    if (i == 0) {
                        if (searchname) {
                            if (!et_SearchName.getText().toString().equals("")) {
                                SearchName = et_SearchName.getText().toString();
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(Appointments.this, "Please Enter Search Name", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 1) {
                        if (searchmobile) {
                            if (!et_SearchMobile.getText().toString().equals("")) {
                                SearchMobile = et_SearchMobile.getText().toString();
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(Appointments.this, "Please Enter Search Mobile", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    if (i == 2) {
                        if (searchtomeet) {
                            if (!et_SearchTomeet.getText().toString().equals("")) {
                                if (!StaffTomeetId.equals("")) {
                                    SearchTomeet = StaffTomeetId;
                                } else {
                                    SearchTomeet = listid.get(et_SearchTomeet.getText().toString().toLowerCase());
                                }
                                if (!SearchTomeet.equals("")) {
                                    result = true;
                                } else {
                                    result = false;
                                    Toast.makeText(Appointments.this, "Please Enter Correct Search ToMeet", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            } else {
                                result = false;
                                Toast.makeText(Appointments.this, "Please Enter Search ToMeet", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    if (i == 3) {
                        if (searchcheckin) {
                            if (!CheckinDate.equals("")) {
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(Appointments.this, "Please Select Appointment in Date", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                }
                if (result) {
                    result = false;
                    Intent intent = new Intent(Appointments.this, Search_Appointment_Details.class);
                    intent.putExtra("ID", Organization_ID);
                    intent.putExtra("VIEW", ContextView);
                    intent.putExtra("ACHECKINDATE", CheckinDate);
                    intent.putExtra("ASEARCHNAME", SearchName);
                    intent.putExtra("ASEARCHMOBILE", SearchMobile);
                    intent.putExtra("ASEARCHTOMEET", StaffTomeet);
                    intent.putExtra("ASEARCHTOMEETID", SearchTomeet);
                    startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
                }
                break;
        }
    }

    private void reset() {
        if (searchname) {
            if (et_SearchName.getText().toString().equals("")) {
                searchname = false;
                Til_SearchName.setVisibility(View.GONE);
                SearchByName_btn.setVisibility(View.VISIBLE);
                SearchName = "";
            }
        }
        if (searchmobile) {
            if (et_SearchMobile.getText().toString().equals("")) {
                searchmobile = false;
                Til_SearchMobile.setVisibility(View.GONE);
                SearchByMobile_btn.setVisibility(View.VISIBLE);
                SearchMobile = "";
            }
        }
        if (searchtomeet) {
            if (et_SearchTomeet.getText().toString().equals("")) {
                searchtomeet = false;
                Til_SearchTomeet.setVisibility(View.GONE);
                SearchByToMeet_btn.setVisibility(View.VISIBLE);
                SearchTomeet = "";
            }
        }

    }

    private void fullreset() {
        Search_btn.setVisibility(View.GONE);
        Reset_btn.setVisibility(View.GONE);
        Appointmentview.setVisibility(View.VISIBLE);
        if (searchcheckin) {
            searchcheckin = false;
            CheckinLayout.setVisibility(View.GONE);
            Checkin_btn.setVisibility(View.VISIBLE);
            CheckinDate = "";
        }
        if (searchname) {
            searchname = false;
            Til_SearchName.setVisibility(View.GONE);
            et_SearchName.setText("");
            SearchByName_btn.setVisibility(View.VISIBLE);
            SearchName = "";
        }
        if (searchmobile) {
            searchmobile = false;
            Til_SearchMobile.setVisibility(View.GONE);
            et_SearchMobile.setText("");
            SearchByMobile_btn.setVisibility(View.VISIBLE);
            SearchMobile = "";
        }
        if (searchtomeet) {
            searchtomeet = false;
            Til_SearchTomeet.setVisibility(View.GONE);
            et_SearchTomeet.setText("");
            SearchByToMeet_btn.setVisibility(View.VISIBLE);
            SearchTomeet = "";
        }

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
                    if (detailsValue.isAppointmentsFound()) {
                        detailsValue.setAppointmentsFound(false);
                        dialog.dismiss();
                        appointmentsthread.interrupt();
                    }
                    if (detailsValue.isAppointmentsNotFound()) {
                        detailsValue.setAppointmentsNotFound(false);
                        dialog.dismiss();
                        appointmentsthread.interrupt();
                        showdialog(APPOINTMENTS_DLG);
                    }
                    String Message = "";
                    if (detailsValue.isSmartIn()) {
                        appointmentsthread.interrupt();
                        detailsValue.setSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(Appointments.this, Message);
                    }
                    if (detailsValue.isSmartOut()) {
                        appointmentsthread.interrupt();
                        detailsValue.setSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(Appointments.this, Message);
                    }
                    if (detailsValue.isSmartError()) {
                        appointmentsthread.interrupt();
                        detailsValue.setSmartError(false);
                        dialog.dismiss();
                        /*Message = "Checking Error.. Please swipe again..";*/
                        Message = "Please check again...";
                        functionCalls.smartCardStatus(Appointments.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date Starttime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Starttime = new SimpleDateFormat("dd/MM/yyyy").parse((""+ dayOfMonth + "/" + ""+ (monthOfYear + 1) + "/" + ""+year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateselected = sdf.format(Starttime);
            if (checkindate) {
                checkindate = false;
                Checkin_btn.setVisibility(View.GONE);
                CheckinLayout.setVisibility(View.VISIBLE);
                CheckinDate = dateselected;
                tv_CheckIndate.setText(dateselected);
                searchcheckin = true;
            } /*else if (checkoutdate) {
                checkoutdate = false;
                Checkout_btn.setVisibility(View.GONE);
                CheckoutLayout.setVisibility(View.VISIBLE);
                CheckoutDate = dateselected;
                tv_CheckOutdate.setText(dateselected);
                searchcheckout = true;
            }*/
        }
    };
    protected void showdialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DLG:
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                date = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dp = new DatePickerDialog(Appointments.this, dpd, year, month, date);
                dp.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog = dp;
                dialog.show();
                break;

            case APPOINTMENTS_DLG:
                AlertDialog.Builder novisitors = new AlertDialog.Builder(this);
                novisitors.setTitle("Appointment Details");
                novisitors.setCancelable(false);
                novisitors.setMessage("No Appointments Found to display..");
                novisitors.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertdialog = novisitors.create();
                alertdialog.show();
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Appointments.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(Appointments.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(Appointments.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Appointments.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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
        dialog = ProgressDialog.show(Appointments.this, "", "Checking...", true);
        appointmentsthread = null;
        Runnable runnable = new AppointmentsTimer();
        appointmentsthread = new Thread(runnable);
        appointmentsthread.start();
    }
}
