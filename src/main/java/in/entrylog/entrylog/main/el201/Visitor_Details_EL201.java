package in.entrylog.entrylog.main.el201;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.POSD.controllers.PrinterController;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.dataposting.ConnectingTask.SmartCheckinout;
import in.entrylog.entrylog.dataposting.ConnectingTask.VisitorManualCheckout;
import in.entrylog.entrylog.main.CustomVolleyRequest;
import in.entrylog.entrylog.main.services.FieldsService;
import in.entrylog.entrylog.main.services.PrintingService;
import in.entrylog.entrylog.util.ImageProcessing;
import in.entrylog.entrylog.values.DataPrinting;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.EL201;
import in.entrylog.entrylog.values.FunctionCalls;

public class Visitor_Details_EL201 extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int END_DLG = 6;
    private static final int ERROR_DLG = 7;
    private PrinterController printerController = null;
    LinearLayout maincontent, checkoutlayout, checkoutuserlayout, emailayout,vehiclelayout, designationlayout, departmentlayout,
            purposelayout, housenolayout, flatnolayout, blocklayout, noofvisitorlayout, classlayout, sectionlayout,
            studentnamelayout, idcardlayout,bloodgrouplayout,timebound_layout;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String Visitor_Name, Visitor_Mobile, Visitor_Fromaddress, Visitor_ToMeet, Visitor_CheckinTime, Visitor_CheckoutTime,
            Visitor_VehicleNo, Visitor_EntryGate, Visitor_Photo, ContextView, Visitor_id, Organization_ID, CheckingUser,
            HeaderPath, DataPath, OrganizationPath, OrganizationName, BarCodeValue, CheckinUser="", CheckoutUser="",
            Visitor_Designation, Department, Purpose, House_number, Flat_number, Block, No_Visitor, aClass, Section,
            Student_Name, ID_Card, Email, VehicleNo, BuildManu, SecurityID,Blood_Group,TimeBound;
    NetworkImageView Visitor_image;
    ImageLoader imageLoader;
    TextView tv_name, tv_mobile, tv_address, tv_tomeet, tv_checkintime, tv_checkouttime, tv_vehicleno, tv_entry, tv_exit,
            tv_email, tv_designation, tv_department, tv_purpose, tv_houseno, tv_flatno, tv_block, tv_noofvisitor, tv_class,
            tv_section, tv_studentname, tv_idcardno,tv_bloodgroup,tv_timebound;
    Button Checkout_btn, Print_btn;
    ConnectingTask task;
    DetailsValue detailsValue;
    ProgressDialog dialog = null;
    Thread checkingoutthread,timeboundthread;
    SharedPreferences settings;
    int flag;
    FunctionCalls functionCalls;
    DataPrinting dataPrinting;
    boolean imageprinting = false, barcodeprinting = false;
    PrintingService printingService;
    FieldsService fieldsService;
    EL201 el201device;
    static ArrayList<String> printingdisplay;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    boolean nfcavailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenSize1 = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        int rotation1 = this.getWindowManager().getDefaultDisplay().getRotation();
        switch(screenSize1) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                setTheme(R.style.AppTheme);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                switch (rotation1) {
                    case Surface.ROTATION_0:
                        setTheme(R.style.MyTheme);
                        break;
                    case Surface.ROTATION_90:
                        setTheme(R.style.AppTheme);
                        break;
                    case Surface.ROTATION_270:
                        setTheme(R.style.AppTheme);
                        break;
                }
                break;
        }
        setContentView(R.layout.activity_visitor_details);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        task = new ConnectingTask();
        detailsValue = new DetailsValue();
        functionCalls = new FunctionCalls();
        dataPrinting = new DataPrinting();

        fieldsService = new FieldsService();
        printingService = new PrintingService();

        functionCalls.OrientationView(Visitor_Details_EL201.this);

        OrganizationPath = functionCalls.filepath("Textfile") + File.separator + "Organization.txt";
        HeaderPath = functionCalls.filepath("Textfile") + File.separator + "Header.txt";
        DataPath = functionCalls.filepath("Textfile") + File.separator + "Data.txt";

        OrganizationName = settings.getString("OrganizationName", "");
        SecurityID = settings.getString("GuardID", "");

        Intent intent = getIntent();
        Bundle bnd = intent.getExtras();
        //region Intent Values
        ContextView = bnd.getString("View");
        Visitor_Photo = bnd.getString("Image");
        Organization_ID = bnd.getString("OrganizationID");
        Visitor_id = bnd.getString("VisitorID");
        Visitor_Name = bnd.getString("Name");
        Visitor_Mobile = bnd.getString("Mobile");
        Visitor_Fromaddress = bnd.getString("From");
        Visitor_ToMeet = bnd.getString("ToMeet");
        Visitor_CheckinTime = bnd.getString("CheckinTime");
        Visitor_CheckoutTime = bnd.getString("CheckoutTime");
        BarCodeValue = bnd.getString("BarCode");
        Visitor_VehicleNo = bnd.getString("VehicleNo");
        Visitor_EntryGate = bnd.getString("Entry");
        CheckingUser = bnd.getString("CheckingUser");
        CheckinUser = bnd.getString("CheckinUser");
        CheckoutUser = bnd.getString("CheckoutUser");
        Email = bnd.getString("Email");
        VehicleNo = bnd.getString("VehicleNo");
        if (CheckoutUser.equals("null")) {
            CheckoutUser = "";
        }
        Visitor_Designation = bnd.getString("visitor_designation");
        Department = bnd.getString("department");
        Purpose = bnd.getString("purpose");
        House_number = bnd.getString("house_number");
        Flat_number = bnd.getString("flat_number");
        Block = bnd.getString("block");
        No_Visitor = bnd.getString("no_visitor");
        aClass = bnd.getString("class");
        Section = bnd.getString("section");
        Student_Name = bnd.getString("student_name");
        ID_Card = bnd.getString("id_card_number");
        Blood_Group=bnd.getString("blood_group");
        TimeBound=bnd.getString("timebound_time");
        //endregion

        //region Linear Layout Initialization
        checkoutlayout = (LinearLayout) findViewById(R.id.detailscheckoutdatelayout);
        checkoutuserlayout = (LinearLayout) findViewById(R.id.exitgate_layout);
        vehiclelayout = (LinearLayout) findViewById(R.id.detailsvehiclel_layout);
        emailayout = (LinearLayout) findViewById(R.id.detailsemaillayout);
        designationlayout = (LinearLayout) findViewById(R.id.detailsvisitordesignation_layout);
        departmentlayout = (LinearLayout) findViewById(R.id.detailsdepartmentlayout);
        purposelayout = (LinearLayout) findViewById(R.id.detailspurposelayout);
        housenolayout = (LinearLayout) findViewById(R.id.detailshousenolayout);
        flatnolayout = (LinearLayout) findViewById(R.id.detailsflatnolayout);
        blocklayout = (LinearLayout) findViewById(R.id.detailsblocklayout);
        noofvisitorlayout = (LinearLayout) findViewById(R.id.detailsnoofvisitorlayout);
        classlayout = (LinearLayout) findViewById(R.id.detailsclasslayout);
        sectionlayout = (LinearLayout) findViewById(R.id.detailssectionlayout);
        studentnamelayout = (LinearLayout) findViewById(R.id.detailsstudentnamelayout);
        idcardlayout = (LinearLayout) findViewById(R.id.detailsidcardnolayout);
        bloodgrouplayout= (LinearLayout) findViewById(R.id.detailsblood_group_layout);
        timebound_layout= (LinearLayout) findViewById(R.id.details_timebound_layout);
        //endregion

        //region TextView Initialization
        tv_name = (TextView) findViewById(R.id.visitor_name);
        tv_mobile = (TextView) findViewById(R.id.visitor_mobile);
        tv_address = (TextView) findViewById(R.id.visitor_fromaddress);
        tv_tomeet = (TextView) findViewById(R.id.visitor_tomeet);
        tv_checkintime = (TextView) findViewById(R.id.visitorcheckin_date);
        tv_checkouttime = (TextView) findViewById(R.id.visitorcheckout_date);
        tv_vehicleno = (TextView) findViewById(R.id.visitor_vehicleno);
        tv_entry = (TextView) findViewById(R.id.entry_gate);
        tv_exit = (TextView) findViewById(R.id.exit_gate);
        tv_email = (TextView) findViewById(R.id.visitor_email);
        tv_designation = (TextView) findViewById(R.id.visitor_designation);
        tv_department = (TextView) findViewById(R.id.visitor_department);
        tv_purpose = (TextView) findViewById(R.id.visitor_purpose);
        tv_houseno = (TextView) findViewById(R.id.visitor_houseno);
        tv_flatno = (TextView) findViewById(R.id.visitor_flatno);
        tv_block = (TextView) findViewById(R.id.visitor_block);
        tv_noofvisitor = (TextView) findViewById(R.id.noofvisitor);
        tv_class = (TextView) findViewById(R.id.visitor_class);
        tv_section = (TextView) findViewById(R.id.visitor_section);
        tv_studentname = (TextView) findViewById(R.id.visitor_section);
        tv_idcardno = (TextView) findViewById(R.id.visitor_idcardno);
        tv_bloodgroup= (TextView) findViewById(R.id.visitor_blood_group);
        tv_timebound= (TextView) findViewById(R.id.visitor_timebound);
        //endregion

        Checkout_btn = (Button) findViewById(R.id.checkout_btn);
        Print_btn = (Button) findViewById(R.id.detailsprint_btn);

        Visitor_image = (NetworkImageView) findViewById(R.id.visitor_image);
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();


        BuildManu = Build.MANUFACTURER;
        //Time Bound Layout
        TimeBoundThread();

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                switch (rotation) {
                    case Surface.ROTATION_0:
                        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_content);
                        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
                        collapsingToolbarLayout.setTitle(Visitor_Name);
                        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                        break;
                    case Surface.ROTATION_90:
                        maincontent = (LinearLayout) findViewById(R.id.main_content);
                        break;
                    case Surface.ROTATION_270:
                        maincontent = (LinearLayout) findViewById(R.id.main_content);
                        break;
                }
                break;
        }

        if (ContextView.equals("Manually Checkout")) {
            Checkout_btn.setVisibility(View.VISIBLE);
            checkoutlayout.setVisibility(View.GONE);
            checkoutuserlayout.setVisibility(View.GONE);
        } else if (ContextView.equals("Visitors")) {
            Print_btn.setVisibility(View.VISIBLE);
            if (Visitor_CheckoutTime.equals("")) {
                Checkout_btn.setVisibility(View.VISIBLE);
            }
            if (BuildManu.equals("LS888")) {
                printerController = PrinterController.getInstance(this);
                flag = printerController.PrinterController_Open();
                if (flag == 0) {
                    Toast.makeText(Visitor_Details_EL201.this, "connect_Success", Toast.LENGTH_SHORT).show();
                } else if (flag == -1){
                    Toast.makeText(Visitor_Details_EL201.this, "Will not Connect to this Device", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Visitor_Details_EL201.this, "connect_Failure", Toast.LENGTH_SHORT).show();
                }
                el201device = new EL201(printerController, settings);
            }
        } else {
        }

        /*Picasso.with(Visitor_Details_Bluetooth.this).load(Visitor_Photo).error(R.drawable.blankperson).into(Visitor_image);*/
        imageLoader.get(Visitor_Photo, ImageLoader.getImageListener(Visitor_image, R.drawable.blankperson,
                R.drawable.blankperson));
        Visitor_image.setImageUrl(Visitor_Photo, imageLoader);
        tv_name.setText(Visitor_Name);
        tv_mobile.setText(Visitor_Mobile);
        tv_address.setText(Visitor_Fromaddress);
        tv_tomeet.setText(Visitor_ToMeet);
        tv_checkintime.setText(Visitor_CheckinTime);
        tv_checkouttime.setText(Visitor_CheckoutTime);
        //tv_vehicleno.setText(Visitor_VehicleNo);
        tv_entry.setText(CheckinUser);
        tv_exit.setText(CheckoutUser);

        TimeBoundFields();
        TimeboundFieldsLayout();
        DisplayFields();

        Checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisitorManualCheckout visitorManualCheckout = task.new VisitorManualCheckout(detailsValue, Organization_ID,
                        Visitor_id, CheckingUser, Visitor_Details_EL201.this);
                dialog = ProgressDialog.show(Visitor_Details_EL201.this, "", "Checking Out Please wait...", true);
                checkingoutthread = null;
                Runnable runnable = new TestCheckOut();
                checkingoutthread = new Thread(runnable);
                checkingoutthread.start();
                visitorManualCheckout.execute();
            }
        });

        Print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildManu.equals("LS888")) {
                    PrintingData();
                } else {
                    showdialog(ERROR_DLG);
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

    private void PrintingData() {
        try {
            dialog = ProgressDialog.show(Visitor_Details_EL201.this, "", "Printing Data...", true);
            Log.d("debug", "Saving Text");
            dataPrinting.SaveOrganization(OrganizationName);
            dataPrinting.SaveHeader();
            SaveData();
            Log.d("debug", "Printing Header");
            printerController.PrinterController_PrinterLanguage(0);
            printerController.PrinterController_Font_Times();
            printerController.PrinterController_Set_Center();
            el201device.printString(OrganizationPath);
            printerController.PrinterController_Font_Normal_mode();
            el201device.printString(HeaderPath);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "Printing Image");
                    imageprinting = true;
                    Bitmap actualbitmap = ((BitmapDrawable) Visitor_image.getDrawable()).getBitmap();
                    int width=256;
                    int height=192;
                    Bitmap bitMap = Bitmap.createScaledBitmap(actualbitmap, width, height, true);
                    Bitmap bb = ImageProcessing.bitMaptoGrayscale(bitMap);
                    Bitmap bitmap = ImageProcessing.convertGreyImgByFloyd(bb);
                    printerController.PrinterController_Bitmap(bitmap);
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "Printing BarCode");
                    if (imageprinting) {
                        imageprinting = false;
                    }
                    barcodeprinting = true;
                    new Thread() {
                        public void run() {
                            el201device.stringtocode(BarCodeValue);
                        };
                    }.start();
                }
            }, 2000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("debug", "Printing Header");
                    if (barcodeprinting) {
                        barcodeprinting = false;
                    }
                    printerController.PrinterController_PrinterLanguage(0);
                    printerController.PrinterController_Set_Left();
                    printerController.PrinterController_Font_Normal_mode();
                    el201device.printString(DataPath);
                    printerController.PrinterController_Take_The_Paper(1);
                }
            }, 4000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    showdialog(END_DLG);
                }
            }, 5500);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Visitor_Details_EL201.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveData() {

        String path = functionCalls.filepath("Textfile");
        String filename = "Data.txt";
        try {
            File f = new File(path + File.separator + filename);
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            HashSet<String> Printdisplay = new HashSet<>();
            Printdisplay = printingService.printingset;
            printingdisplay = new ArrayList<>();
            printingdisplay.addAll(Printdisplay);
            Collections.sort(printingdisplay);
            if (printingdisplay.size() > 0) {
                functionCalls.LogStatus("Printing Display Size: "+printingdisplay.size());
                for (int i = 0; i < printingdisplay.size(); i++) {
                    String PrintOrder = printingdisplay.get(i).toString();
                    functionCalls.LogStatus("Print Order: "+PrintOrder);
                    String Display = PrintOrder.substring(2, PrintOrder.length());
                    functionCalls.LogStatus("Display: "+Display);
                    myOutWriter.append(Display+": ");
                    if (Display.equals("Name")) {
                        myOutWriter.append(Visitor_Name + "\r\n");
                    }
                    if (Display.equals("Mobile")) {
                        myOutWriter.append(Visitor_Mobile + "\r\n");
                    }
                    if (Display.equals("From")) {
                        myOutWriter.append(Visitor_Fromaddress + "\r\n");
                    }
                    if (Display.equals("To Meet")) {
                        myOutWriter.append(Visitor_ToMeet + "\r\n");
                    }
                    if (Display.equals("Date")) {
                        myOutWriter.append(Visitor_CheckinTime + "\r\n");
                    }
                    if (Display.equals("Designation")) {
                        myOutWriter.append(Visitor_Designation + "\r\n");
                    }
                    if (Display.equals("Department")) {
                        myOutWriter.append(Department + "\r\n");
                    }
                    if (Display.equals("Purpose")) {
                        myOutWriter.append(Purpose + "\r\n");
                    }
                    if (Display.equals("House No")) {
                        myOutWriter.append(House_number + "\r\n");
                    }
                    if (Display.equals("Flat No")) {
                        myOutWriter.append(Flat_number + "\r\n");
                    }
                    if (Display.equals("Block")) {
                        myOutWriter.append(Block + "\r\n");
                    }
                    if (Display.equals("Visitors")) {
                        myOutWriter.append(No_Visitor + "\r\n");
                    }
                    if (Display.equals("Class")) {
                        myOutWriter.append(aClass + "\r\n");
                    }
                    if (Display.equals("Section")) {
                        myOutWriter.append(Section + "\r\n");
                    }
                    if (Display.equals("Student")) {
                        myOutWriter.append(Student_Name + "\r\n");
                    }
                    if (Display.equals("ID Card No")) {
                        myOutWriter.append(ID_Card + "\r\n");
                    }
                    if (Display.equals("Entry")) {
                        myOutWriter.append(CheckinUser + "\r\n");
                    }
                    if (Display.equals("Email")) {
                        myOutWriter.append(Email + "\r\n");
                    }
                    if (Display.equals("Vehicle Number")) {
                        myOutWriter.append(VehicleNo + "\r\n");
                    }
                    if (Display.equals("Time Bound")) {
                        myOutWriter.append(TimeBound + "\r\n");
                    }
                    /*if (Display.equals("Blood Group")) {
                        myOutWriter.append(Blood_Group + "\r\n");
                    }*/
                }
            }
            myOutWriter.append(" " + "\r\n");
            myOutWriter.append(" "+"\r\n");
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void TimeBoundThread() {
        Log.d("debug", "MobileNo Suggest Timer Started");
        timeboundthread = null;
        Runnable runnable = new SuggestTimer2();
        timeboundthread = new Thread(runnable);
    }

    class SuggestTimer2 implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    suggesting2();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    class TestCheckOut implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    dochecking();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    private void TimeBoundFields(){
        ConnectingTask.FetchTimeBoundFields timeBoundFields=task.new FetchTimeBoundFields(Organization_ID,detailsValue);
        timeBoundFields.execute();
        timeboundthread.start();
    }

    public void dochecking() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detailsValue.isVisitorsCheckOutSuccess()) {
                        detailsValue.setVisitorsCheckOutSuccess(false);
                        dialog.dismiss();
                        checkingoutthread.interrupt();
                        Toast.makeText(Visitor_Details_EL201.this, "Successfully Checked Out", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (detailsValue.isVisitorsCheckOutFailure()) {
                        detailsValue.setVisitorsCheckOutFailure(false);
                        dialog.dismiss();
                        checkingoutthread.interrupt();
                        Toast.makeText(Visitor_Details_EL201.this, "CheckOut Failed Please try once again", Toast.LENGTH_SHORT).show();
                    }
                    String Message = "";
                    if (detailsValue.isSmartIn()) {
                        checkingoutthread.interrupt();
                        detailsValue.setSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(Visitor_Details_EL201.this, Message);
                    }
                    if (detailsValue.isSmartOut()) {
                        checkingoutthread.interrupt();
                        detailsValue.setSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(Visitor_Details_EL201.this, Message);
                    }
                    if (detailsValue.isSmartError()) {
                        checkingoutthread.interrupt();
                        detailsValue.setSmartError(false);
                        dialog.dismiss();
                        Message = "Please check again...";
                        functionCalls.smartCardStatus(Visitor_Details_EL201.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void suggesting2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detailsValue.isTimeBoundSuccess()) {
                        detailsValue.setTimeBoundSuccess(false);
                        timebound_layout.setVisibility(View.VISIBLE);
                        timeboundthread.interrupt();
                    }
                    if (detailsValue.isTimeBoundFailure()) {
                        detailsValue.setTimeBoundFailure(false);
                        timebound_layout.setVisibility(View.GONE);
                        timeboundthread.interrupt();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void showdialog(int id) {
        switch (id) {
            case END_DLG:
                AlertDialog.Builder endbuilder = new AlertDialog.Builder(this);
                endbuilder.setTitle("Printing Details");
                endbuilder.setCancelable(false);
                endbuilder.setMessage("Did a Data got a printed correctly...??");
                endbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                endbuilder.setNegativeButton("REPRINT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (BuildManu.equals("LS888")) {
                            PrintingData();
                        } else {
                            showdialog(END_DLG);
                        }
                    }
                });
                AlertDialog endalert = endbuilder.create();
                endalert.show();
                break;

            case ERROR_DLG:
                AlertDialog.Builder errorbuilder = new AlertDialog.Builder(this);
                errorbuilder.setTitle("Printing Details");
                errorbuilder.setCancelable(false);
                errorbuilder.setMessage("EL201 device will not support for your device, So you will not get a print in this device..");
                errorbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog erroralert = errorbuilder.create();
                erroralert.show();
                break;
        }
    }
    private void TimeboundFieldsLayout(){
            tv_timebound.setText(TimeBound);
    }
    private void DisplayFields() {
        functionCalls.LogStatus("Display field Started");
        HashSet<String> hashSet = new HashSet<>();
        hashSet = fieldsService.fieldset;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(hashSet);
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                String value = arrayList.get(i).toString();
                if (value.equals("Vehicle Number")) {
                    vehiclelayout.setVisibility(View.VISIBLE);
                    tv_vehicleno.setText(Visitor_VehicleNo);
                }
                if (value.equals("Visitor Email")) {
                    emailayout.setVisibility(View.VISIBLE);
                    tv_email.setText(Email);
                }
                if (value.equals("Visitor Designation")) {
                    designationlayout.setVisibility(View.VISIBLE);
                    tv_designation.setText(Visitor_Designation);
                }
                if (value.equals("Department")) {
                    departmentlayout.setVisibility(View.VISIBLE);
                    tv_department.setText(Department);
                }
                if (value.equals("Purpose")) {
                    purposelayout.setVisibility(View.VISIBLE);
                    tv_purpose.setText(Purpose);
                }
                if (value.equals("House No")) {
                    housenolayout.setVisibility(View.VISIBLE);
                    tv_houseno.setText(House_number);
                }
                if (value.equals("Flat No")) {
                    flatnolayout.setVisibility(View.VISIBLE);
                    tv_flatno.setText(Flat_number);
                }
                if (value.equals("Block")) {
                    blocklayout.setVisibility(View.VISIBLE);
                    tv_block.setText(Block);
                }
                if (value.equals("Number of Visitor")) {
                    noofvisitorlayout.setVisibility(View.VISIBLE);
                    tv_noofvisitor.setText(No_Visitor);
                }
                if (value.equals("Class")) {
                    classlayout.setVisibility(View.VISIBLE);
                    tv_class.setText(aClass);
                }
                if (value.equals("Section")) {
                    sectionlayout.setVisibility(View.VISIBLE);
                    tv_section.setText(Section);
                }
                if (value.equals("Student Name")) {
                    studentnamelayout.setVisibility(View.VISIBLE);
                    tv_studentname.setText(Student_Name);
                }
                if (value.equals("ID Card No")) {
                    idcardlayout.setVisibility(View.VISIBLE);
                    tv_idcardno.setText(ID_Card);
                }
                if (value.equals("Blood Group")) {
                    bloodgrouplayout.setVisibility(View.VISIBLE);
                    tv_bloodgroup.setText(Blood_Group);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (ContextView.equals("Visitors")) {
            if (BuildManu.equals("LS888")) {
                printerController.PrinterController_Close();
            }
            functionCalls.deleteTextfile("Organization.txt");
            functionCalls.deleteTextfile("Header.txt");
            functionCalls.deleteTextfile("Empty.txt");
            functionCalls.deleteTextfile("Data.txt");
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, Visitor_Details_EL201.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(Visitor_Details_EL201.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null && parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(Visitor_Details_EL201.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Visitor_Details_EL201.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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
        SmartCheckinout checkOut = task.new SmartCheckinout(detailsValue, result,
                Organization_ID, SecurityID);
        checkOut.execute();
        dialog = ProgressDialog.show(Visitor_Details_EL201.this, "", "Checking...", true);
        checkingoutthread = null;
        Runnable runnable = new TestCheckOut();
        checkingoutthread = new Thread(runnable);
        checkingoutthread.start();
    }
}


