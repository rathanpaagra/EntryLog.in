package in.entrylog.entrylog.main.adhocVisitors.adhoc_el201;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.POSD.controllers.PrinterController;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

import in.entrylog.entrylog.R;
import in.entrylog.entrylog.database.AdhocDatabase;
import in.entrylog.entrylog.dataposting.ConnectingTask;
import in.entrylog.entrylog.dataposting.DataAPI;
import in.entrylog.entrylog.main.adhocVisitors.AdhocVisitorActivity;
import in.entrylog.entrylog.main.adhocVisitors.adhoccamera.AdhocCameraActivity;
import in.entrylog.entrylog.main.camera.CameraActivity;
import in.entrylog.entrylog.main.el201.AddVisitors_EL201;
import in.entrylog.entrylog.main.services.AdhocFieldService;
import in.entrylog.entrylog.main.services.ContractorService;
import in.entrylog.entrylog.main.services.PrintingService;
import in.entrylog.entrylog.main.services.StaffServiceAddVisitor;
import in.entrylog.entrylog.main.services.TimeService;
import in.entrylog.entrylog.main.services.UpdateAdhocData;
import in.entrylog.entrylog.util.ImageProcessing;
import in.entrylog.entrylog.values.DataPrinting;
import in.entrylog.entrylog.values.DetailsValue;
import in.entrylog.entrylog.values.EL201;
import in.entrylog.entrylog.values.FunctionCalls;

import static android.Manifest.permission.CAMERA;

public class AddAdhocVisitors_EL201 extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final int RequestPermissionCode = 2;
    private static final int NFC_DLG = 4;
    private static final int START_DLG = 5;
    private static final int END_DLG = 6;
    private static final int MOBILE_DLG = 7;
    private static final int OTP_DLG = 8;
    private PrinterController printerController = null;
    int flag;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_FOR_ACTIVITY_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private static Uri fileUri; // file url to store image/video
    static File mediaFile;

    EditText name_et, email_et, mobile_et, address_et, vehicle_et;
    AutoCompleteTextView tomeet_et,contractor_et;
    ImageView photo_img;
    LinearLayout addvisitorslayout;
    Button submit_btn;
    String Name, Email="", FromAddress, ToMeet, Vehicleno = "", Contractor="",Contractor_id="", Organizationid, OrganizationName, UpdateVisitorImage="",
            Visitors_ImagefileName = "", GuardID, User, HeaderPath, DataPath, OrganizationPath, DateTime="", BarCodeValue="",
            format, Visitor_Designation="", Department="", Purpose="", House_number="", Flat_number="", Block="", No_Visitor="",
            aClass="", Section="", Student_Name="", ID_Card="", Visitor_Entry="", BuildManu="", ID_Card_type="", Visitor_type="",
            Adhoc_Blood_group="",imagepath="", Issued_Date="", Expiry_Date="",Visitors_id, adhoc ="a";
    int codevalue, digits;
    static String Mobile = "";
    ConnectingTask task;
    DetailsValue details;
    Thread mobilesuggestthread;
    static ProgressDialog dialog = null;
    boolean Visitorsimage = false, textfileready = false, imageprinting = false, barcodeprinting = false, reprint = false,
            writeNFC = false, otpcheck = false, manualcheck = false, otpresent = false, nfcavailable = false,
            mobilesuggestsuccess = false, vipvisitor = false, normalvisitor = false,  result = false;

    View mProgressBar;
    AdhocDatabase dataBase;
    NfcAdapter nfcAdapter;
    NfcManager nfcManager;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    AlertDialog nfcdialog;
    FunctionCalls functionCalls;
    DataPrinting dataPrinting;
    AdhocFieldService fieldsService;

    StaffServiceAddVisitor staffService;
    ContractorService contractorService;
    HashMap<String, String> listid;

    PrintingService printingService;
    EL201 el201device;
    TextInputLayout Til_field1, Til_field2, Til_field3, Til_field4, Til_field5, Til_field6, Til_field7, Til_field8,
            Til_field9, Til_field10, Til_field11, Til_field12,Til_field13,Til_field14,Til_field15, emailLayout;
    EditText Et_field1, Et_field2, Et_field3, Et_field4, Et_field5, Et_field6, Et_field7, Et_field8, Et_field9,
            Et_field10, Et_field11, Et_field12, Et_field13,Et_field14,Et_field15, etmobile;
    ArrayAdapter<String> Staffadapter, ContractorAdapter;
    static ArrayList<String> stafflist, printingdisplay,contractorlist;
    int otpcount = 0;

    TextView textview_issued_date;
    TextView textview_expiry_date;
    Button button_issued, button_expiry;
    private int mYear, mDay, mMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setLogo(R.mipmap.ic_entrylog_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_add_adhoc_visitors__el201);

        BuildManu = Build.MANUFACTURER;

        if (BuildManu.equals("LS888")) {
            printerController = PrinterController.getInstance(this);
            flag = printerController.PrinterController_Open();
            if (flag == 0) {
                Toast.makeText(AddAdhocVisitors_EL201.this, "connect_Success", Toast.LENGTH_SHORT).show();
            } else if (flag == -1){
                Toast.makeText(AddAdhocVisitors_EL201.this, "Will not Connect to this Device", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddAdhocVisitors_EL201.this, "connect_Failure", Toast.LENGTH_SHORT).show();
            }
        }

        details = new DetailsValue();
        task = new ConnectingTask();
        functionCalls = new FunctionCalls();
        dataPrinting = new DataPrinting();

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();

        fieldsService = new AdhocFieldService();
        staffService = new StaffServiceAddVisitor();
        contractorService=new ContractorService();
        printingService = new PrintingService();
        if (BuildManu.equals("LS888")) {
            el201device = new EL201(printerController, settings);
        }

        dataBase = new AdhocDatabase(this);
        dataBase.open();

        digits = 4;
        format = String.format("%%0%dd", digits);

        name_et = (EditText) findViewById(R.id.adhoc_name_EtTxt);
        email_et = (EditText) findViewById(R.id.adhoc_email_EtTxt);
        mobile_et = (EditText) findViewById(R.id.adhoc_mobile_EtTxt);
        address_et = (EditText) findViewById(R.id.adhoc_address_EtTxt);
        tomeet_et = (AutoCompleteTextView) findViewById(R.id.adhoc_tomeet_EtTxt);
        contractor_et= (AutoCompleteTextView) findViewById(R.id.adhoc_contractor_EtTxt);
        vehicle_et = (EditText) findViewById(R.id.adhoc_vehicle_EtTxt);

        textview_issued_date= (TextView) findViewById(R.id.adhoc_issued_date_TxtVew);
        textview_expiry_date= (TextView) findViewById(R.id.adhoc_expiry_date_TxtVew);
        button_issued= (Button) findViewById(R.id.adhoc_button_issued_date);
        button_expiry= (Button) findViewById(R.id.adhoc_button_expiry_date);


        photo_img = (ImageView) findViewById(R.id.adhoc_cameraimage);
        if (settings.getString("ImageAccess","").equals("No")) {
            photo_img.setVisibility(View.GONE);
        }
        submit_btn = (Button) findViewById(R.id.adhoc_submit_btn);

        /*Intent intent=new Intent(AddAdhocVisitors_EL201.this, AdhocFieldService.class);
        startService(intent);*/


        functionCalls.OrientationView(AddAdhocVisitors_EL201.this);

        if (settings.getString("RFID", "").equals("true")) {
            nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
            nfcAdapter = nfcManager.getDefaultAdapter();
            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                nfcavailable = true;
            }
        }

        emailLayout = (TextInputLayout) findViewById(R.id.adhoc_email_Til);
        Til_field1 = (TextInputLayout) findViewById(R.id.adhoc_field1_Til);
        Til_field2 = (TextInputLayout) findViewById(R.id.adhoc_field2_Til);
        Til_field3 = (TextInputLayout) findViewById(R.id.adhoc_field3_Til);
        Til_field4 = (TextInputLayout) findViewById(R.id.adhoc_field4_Til);
        Til_field5 = (TextInputLayout) findViewById(R.id.adhoc_field5_Til);
        Til_field6 = (TextInputLayout) findViewById(R.id.adhoc_field6_Til);
        Til_field7 = (TextInputLayout) findViewById(R.id.adhoc_field7_Til);
        Til_field8 = (TextInputLayout) findViewById(R.id.adhoc_field8_Til);
        Til_field9 = (TextInputLayout) findViewById(R.id.adhoc_field9_Til);
        Til_field10 = (TextInputLayout) findViewById(R.id.adhoc_field10_Til);
        Til_field11 = (TextInputLayout) findViewById(R.id.adhoc_field11_Til);
        Til_field12 = (TextInputLayout) findViewById(R.id.adhoc_field12_Til);
        Til_field13 = (TextInputLayout) findViewById(R.id.adhoc_field13_Til);
        Til_field14 = (TextInputLayout) findViewById(R.id.adhoc_field14_Til);
        Til_field15 = (TextInputLayout) findViewById(R.id.adhoc_field15_Til);


        Et_field1 = (EditText) findViewById(R.id.adhoc_field1_EtTxt);
        Et_field2 = (EditText) findViewById(R.id.adhoc_field2_EtTxt);
        Et_field3 = (EditText) findViewById(R.id.adhoc_field3_EtTxt);
        Et_field4 = (EditText) findViewById(R.id.adhoc_field4_EtTxt);
        Et_field5 = (EditText) findViewById(R.id.adhoc_field5_EtTxt);
        Et_field6 = (EditText) findViewById(R.id.adhoc_field6_EtTxt);
        Et_field7 = (EditText) findViewById(R.id.adhoc_field7_EtTxt);
        Et_field8 = (EditText) findViewById(R.id.adhoc_field8_EtTxt);
        Et_field9 = (EditText) findViewById(R.id.adhoc_field9_EtTxt);
        Et_field10 = (EditText) findViewById(R.id.adhoc_field10_EtTxt);
        Et_field11 = (EditText) findViewById(R.id.adhoc_field11_EtTxt);
        Et_field12 = (EditText) findViewById(R.id.adhoc_field12_EtTxt);
        Et_field13 = (EditText) findViewById(R.id.adhoc_field13_EtTxt);
        Et_field14 = (EditText) findViewById(R.id.adhoc_field14_EtTxt);
        Et_field15 = (EditText) findViewById(R.id.adhoc_field15_EtTxt);


        addvisitorslayout = (LinearLayout) findViewById(R.id.adhoc_addvisitors_layout);

        mProgressBar = findViewById(R.id.adhoc_addvisitors_progress);

        Intent timeservice = new Intent(AddAdhocVisitors_EL201.this, TimeService.class);
        startService(timeservice);

        Intent updateAdhoc = new Intent(AddAdhocVisitors_EL201.this, UpdateAdhocData.class);
        startService(updateAdhoc);

        OrganizationPath = functionCalls.filepath("Textfile") + File.separator + "Organization.txt";
        HeaderPath = functionCalls.filepath("Textfile") + File.separator + "Header.txt";
        DataPath = functionCalls.filepath("Textfile") + File.separator + "Data.txt";

        Organizationid = settings.getString("OrganizationID", "");
        GuardID = settings.getString("GuardID", "");
        OrganizationName = settings.getString("OrganizationName", "");
        User = settings.getString("User", "");

        if (settings.getString("BarCode", "").equals("")) {
            codevalue = 1;
            String value = String.format(format, codevalue);
            BarCodeValue = Organizationid + value + adhoc;
        } else {
            String code = settings.getString("BarCode", "");
            String barvalue = code.substring(0, 4);
            codevalue = Integer.parseInt(barvalue);
            if (codevalue == 9999) {
                codevalue = 1;
                String value = String.format(format, codevalue);
                BarCodeValue = Organizationid + value + adhoc;
            } else {
                codevalue = codevalue + 1;
                String value = String.format(format, codevalue).toString();
                BarCodeValue = Organizationid + value + adhoc;
            }
        }


        functionCalls.LogStatus("Add Visitors Bar Code: "+BarCodeValue);

        MobileNoSuggestThread();

        showdialog(START_DLG);

        // parseImage();
        photo_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Checking camera availability
                Intent intent=new Intent(AddAdhocVisitors_EL201.this, CameraActivity.class);
                // in.putExtra("consid", cons_Num);

                startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        });

       /* photo_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Checking camera availability
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    // will close the app if the device does't have camera
                    finish();
                } else {
                    // capture picture
                    checkforCameraPermissionMandAbove();
                }
            }
        });*/

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settings.getString("ImageAccess","").equals("Yes")) {
                    if (Visitorsimage) {
                        CheckInDetails();
                    } else {
                        Toast.makeText(AddAdhocVisitors_EL201.this, "Please take Visitors Photo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CheckInDetails();
                }
            }
        });

        contractor_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    tomeet_et.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        tomeet_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    vehicle_et.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });


        button_issued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAdhocVisitors_EL201.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                        month=month+1;
                        String strMonth = String.valueOf(month), strDay = String.valueOf(day), strYear = String.valueOf(year);

                        if (month < 10) {
                            strMonth = "0" + strMonth;

                        }

                        if (day < 10) {
                            strDay = "0" + strDay;
                        }

                        // Handle the data here
                        textview_issued_date.setText(strYear + " - " + strMonth + " - " + strDay);


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        button_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAdhocVisitors_EL201.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String strMonth = String.valueOf(month), strDay = String.valueOf(day), strYear = String.valueOf(year);

                        if (month < 10) {
                            strMonth = "0" + strMonth;
                        }

                        if (day < 10) {
                            strDay = "0" + strDay;
                        }

                        // Handle the data here
                        textview_expiry_date.setText(strYear + " - " + strMonth + " - " + strDay);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        if (BuildManu.equals("LS888")) {
            printerController.PrinterController_Close();
        }
        functionCalls.deleteTextfile("Organization.txt");
        functionCalls.deleteTextfile("Header.txt");
        functionCalls.deleteTextfile("Data.txt");
        if (TimeService.Timeservice) {
            Intent timeservice = new Intent(AddAdhocVisitors_EL201.this, TimeService.class);
            stopService(timeservice);
        }
        editor.putString("ServerTime", "");
        editor.commit();
        super.onDestroy();
    }

    private void CheckInDetails() {
        Name = name_et.getText().toString();
        if (!name_et.getText().toString().equals("")) {
            Email = email_et.getText().toString();
            Mobile = mobile_et.getText().toString();
            if (!mobile_et.getText().toString().equals("")) {
                if (Mobile.length() == 10) {
                    FromAddress = address_et.getText().toString();
                    if (!address_et.getText().toString().equals("")) {

                        Contractor = contractor_et.getText().toString();
                        if (!contractor_et.getText().toString().equals("")) {
                            if (!Contractor_id.equals("")) {
                                Contractor = Contractor_id;
                            } else {
                                Contractor = listid.get(contractor_et.getText().toString().toLowerCase());
                            }
                            if (!Contractor.equals("")) {
                                result = true;
                            } else {
                                result = false;
                                Toast.makeText(AddAdhocVisitors_EL201.this, "Please Enter Correct Search ToMeet", Toast.LENGTH_SHORT).show();
                            }

                        Issued_Date = textview_issued_date.getText().toString();
                        if (!textview_issued_date.getText().toString().equals("")) {
                            Expiry_Date = textview_expiry_date.getText().toString();

                            ToMeet = tomeet_et.getText().toString();
                            if (!tomeet_et.getText().toString().equals("")) {
                                Vehicleno = vehicle_et.getText().toString();
                                Random rand = new Random();
                                int num = rand.nextInt(9000) + 1000;
                                Visitors_ImagefileName = Mobile + "" + num + ".jpg";
                                if (!Visitors_ImagefileName.equals("")) {
                                    getExtraFields();
                                    if (!settings.getString("ServerTime", "").equals("")) {
                                        DateTime = settings.getString("ServerTime", "");
                                    } else {
                                        DateTime = functionCalls.CurrentDate() + " " + functionCalls.CurrentTime();
                                    }
                                    if (UpdateVisitorImage.equals("Yes")) {
                                        dataBase.insertentrylogdata(Name, Email, Mobile, FromAddress, ToMeet, Vehicleno,
                                                Visitors_ImagefileName, /*fileUri.getPath()*/imagepath, BarCodeValue, Organizationid, GuardID,
                                                UpdateVisitorImage, Visitor_Designation, Department, Purpose, House_number,
                                                Flat_number, Block, No_Visitor, aClass, Section, Student_Name, ID_Card,
                                                settings.getString("Device", ""), Expiry_Date, Issued_Date, ID_Card_type, Adhoc_Blood_group,Contractor);
                                    } else {
                                        dataBase.insertentrylogdata(Name, Email, Mobile, FromAddress, ToMeet, Vehicleno,
                                                "", "", BarCodeValue, Organizationid, GuardID,
                                                UpdateVisitorImage, Visitor_Designation, Department, Purpose, House_number,
                                                Flat_number, Block, No_Visitor, aClass, Section, Student_Name, ID_Card,
                                                settings.getString("Device", ""), Expiry_Date, Issued_Date, ID_Card_type, Adhoc_Blood_group,Contractor);
                                    }
                                    Log.d("debug", "Service Started");
                                    Intent intent = new Intent(AddAdhocVisitors_EL201.this, UpdateAdhocData.class);
                                    startService(intent);
                                    if (BuildManu.equals("LS888")) {
                                        PrintingData();
                                    } else {
                                        showdialog(END_DLG);
                                        functionCalls.showToast(AddAdhocVisitors_EL201.this, "This Device is not suitable to get print details..");
                                    }
                                } else {
                                    Toast.makeText(AddAdhocVisitors_EL201.this, "Please take a Photo of Visitor", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                tomeet_et.setError("Please Enter To Meet Person");
                            }
                        } else {
                            textview_issued_date.setError("Please Choose Issuing Date");
                        }
                    }else {
                            contractor_et.setError("Please Enter Contractor Name");
                    }
                    }else {
                        address_et.setError("Please Enter From Address");
                    }
                } else {
                    mobile_et.setError("Please Enter Correct Mobile Number");
                }
            } else {
                mobile_et.setError("Please Enter Mobile Number");
            }
        } else {
            name_et.setError("Please Enter Name");
        }
    }

    private void PrintingData() {
        try {
            dialog = ProgressDialog.show(AddAdhocVisitors_EL201.this, "", "Updating file...", true);
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
                    Bitmap actualbitmap = ((BitmapDrawable) photo_img.getDrawable()).getBitmap();
                   /* int width=256;
                    int height=192;*/
                    int width=256;
                    int height=220;
                    Bitmap bitMap = Bitmap.createScaledBitmap(actualbitmap, width, height, true);
                    Bitmap bb = ImageProcessing.bitMaptoGrayscale(bitMap);
                    Bitmap bitmap = ImageProcessing.convertGreyImgByFloyd(bb);
                    printerController.PrinterController_Bitmap(bitmap);
                }
            }, 500);
            //Disable Barcode printing
            /*new Handler().postDelayed(new Runnable() {
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
            }, 2000);*/
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
            Toast.makeText(AddAdhocVisitors_EL201.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void MobileNoSuggestThread() {
        Log.d("debug", "MobileNo Suggest Timer Started");
        mobilesuggestthread = null;
        Runnable runnable = new AddAdhocVisitors_EL201.SuggestTimer();

        mobilesuggestthread = new Thread(runnable);
    }

    class SuggestTimer implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    suggesting();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void suggesting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (details.isAdhocMobileAutoSuggestSuccess()) {
                        details.setAdhocMobileAutoSuggestSuccess(false);
                        Successview();
                        Extrafields();
                        mobilesuggestsuccess = true;
                        if (otpcheck) {
                            otpcheck = false;
                            Random rand = new Random();
                            int num = rand.nextInt(9000) + 1000;
                            editor.putString("OTP", ""+num);
                            editor.commit();
                            ConnectingTask.ADHOCSMSOTP smsotp = task.new ADHOCSMSOTP("91"+Mobile, settings.getString("OTP", ""),settings.getString("OrganizationID", ""));
                            smsotp.execute();
                            showdialog(OTP_DLG);
                            functionCalls.showToast(AddAdhocVisitors_EL201.this, "OTP Sent");
                        } else if (manualcheck) {
                            manualcheck = false;
                            AddAdhocVisitors_EL201.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                                    SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            addvisitorslayout.setVisibility(View.VISIBLE);
                            mobile_et.setText(Mobile);
                            name_et.requestFocus();
                        }
                        mobilesuggestthread.interrupt();
                    }
                    if (details.isAdhocMobileAutoSuggestFailure()) {
                        details.setAdhocMobileAutoSuggestFailure(false);
                        Extrafields();
                        if (otpcheck) {
                            otpcheck = false;
                            String abc = "";
                            Random rand = new Random();
                            int num = rand.nextInt(9000) + 1000;
                            editor.putString("OTP", ""+num);
                            editor.commit();
                            ConnectingTask.ADHOCSMSOTP smsotp = task.new ADHOCSMSOTP("91"+Mobile, settings.getString("OTP", ""),settings.getString("OrganizationID", ""));
                            smsotp.execute();
                            showdialog(OTP_DLG);
                            functionCalls.showToast(AddAdhocVisitors_EL201.this, "OTP Sent");
                        } else if (manualcheck) {
                            manualcheck = false;
                            AddAdhocVisitors_EL201.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                                    SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            addvisitorslayout.setVisibility(View.VISIBLE);
                            mobile_et.setText(Mobile);
                            name_et.requestFocus();
                        }
                        mobilesuggestthread.interrupt();
                    }
                    if (details.isAdhocMobileNoExist()) {
                        details.setAdhocMobileNoExist(false);
                        showdialog(MOBILE_DLG);
                        mobilesuggestthread.interrupt();
                    }
                    String Message = "";
                    if (details.isAdhocSmartIn()) {
                        mobilesuggestthread.interrupt();
                        details.setAdhocSmartIn(false);
                        dialog.dismiss();
                        Message = "Successfully Checked In";
                        functionCalls.smartCardStatus(AddAdhocVisitors_EL201.this, Message);
                    }
                    if (details.isAdhocSmartOut()) {
                        mobilesuggestthread.interrupt();
                        details.setAdhocSmartOut(false);
                        dialog.dismiss();
                        Message = "Successfully Checked Out";
                        functionCalls.smartCardStatus(AddAdhocVisitors_EL201.this, Message);
                    }
                    if (details.isAdhocSmartError()) {
                        mobilesuggestthread.interrupt();
                        details.setAdhocSmartError(false);
                        dialog.dismiss();
                        Message = "Adhoc Visitor is Expired......";
                        functionCalls.smartCardStatus(AddAdhocVisitors_EL201.this, Message);
                    }
                    if (details.isAdhocSmartInvalidCard()) {
                        mobilesuggestthread.interrupt();
                        details.setAdhocSmartInvalidCard(false);
                        dialog.dismiss();
                        //Message = "Checking Error.. Please swipe again..";
                        Message = "Please Check in Again......";
                        functionCalls.smartCardStatus(AddAdhocVisitors_EL201.this, Message);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @TargetApi(23)
    public void checkforCameraPermissionMandAbove() {
        Log.d("debug", "checkForPermissions() called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                functionCalls.LogStatus("Camera Permissions Granted Successfully");
                captureImage();
            } else {
                requestPermission();
            }
        } else {
            functionCalls.LogStatus("Below M, permissions not via code");
            captureImage();
        }
    }

    @TargetApi(23)
    private void requestPermission() {
        ActivityCompat.requestPermissions(AddAdhocVisitors_EL201.this, new String[]
                {
                        CAMERA
                }, RequestPermissionCode);
    }

    @TargetApi(23)
    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission) {
                        functionCalls.LogStatus("Camera Permission Granted");
                        captureImage();
                    } else {
                        functionCalls.LogStatus("Permission Denied");
                    }
                }
                break;
        }
    }

    /**
     * Capturing Camera Image will launch camera app request image capture
     */
    private void captureImage() {
        Intent intent=new Intent(this, AdhocCameraActivity.class);

        startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/
    }


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
        previewCapturedImage();
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view

                /*if(getIntent().hasExtra("picture")) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            getIntent().getByteArrayExtra("picture"), 0, getIntent()
                                    .getByteArrayExtra("picture").length);

                    photo_img.setImageBitmap(rotateImage(bmp, fileUri.getPath()));
                    functionCalls.LogStatus("Image Size: "+sizeOf(bmp));
                    UpdateVisitorImage = "Yes";
                    Visitorsimage = true;
                }

               parseImage();
                functionCalls.checkimage_and_delete("Hello Camera", Mobile, fileUri.getPath());


                //previewCapturedImage();*/
                Bundle bnd = data.getExtras();
                imagepath = bnd.getString("picture");
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                /*options.inSampleSize = 8;*/

                final Bitmap bitmap = BitmapFactory.decodeFile(imagepath,
                        options);
                /*photo_img.setImageBitmap(rotateImage(bitmap, imagepath));*/
                photo_img.setImageBitmap(bitmap);

                functionCalls.LogStatus("Image Size: "+sizeOf(bitmap));
                UpdateVisitorImage = "Yes";
                Visitorsimage = true;
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                //  parseImage();
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_FOR_ACTIVITY_CODE &&  picCount < 5) {
            //curPic = (Bitmap) data.getExtras().get("data");
           Bitmap curPic = (Bitmap) data.getExtras().get("files");
            ImageView images = new ImageView(getApplicationContext());

            //SET PICTURE TO NEW VIEW AND ANIMATE INTO POSITION
            images.setImageBitmap(curPic);


            //ADD TO PREVPICS LAYOUT
            //images.setPadding(3, 0, 0, 0);
           // showCase.addView(images);
           // badge.setImageBitmap(curPic);

            //ADDS CLICK LISTENER TO EACH ELEMENT AND SETS ID
            try {
                Log.i("AFTER TAKING PIC", "PICCOUNT IS NOW:" + picCount);
                //showCase.getChildAt(picCount).setId(picCount);
                images.setId(picCount);
              //  showCase.getChildAt(picCount).setOnClickListener(btnListener);
                images.setTag("pics");

                //SAVE PITCTURE
                //savePic(curPic);

              //  previewImages(picCount);
            } catch (Exception e) {
                Log.e("ERROR TAKING PIC", e.toString());
            }

        } else
            Toast.makeText(getApplicationContext(), "Unable to add more pictures", Toast.LENGTH_SHORT).show();

    }*/
    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            photo_img.setImageBitmap(rotateImage(bitmap, fileUri.getPath()));


            functionCalls.LogStatus("Image Size: "+sizeOf(bitmap));
            UpdateVisitorImage = "Yes";
            Visitorsimage = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            photo_img.setImageBitmap(rotateImage(bitmap, fileUri.getPath()));
            functionCalls.LogStatus("Image Size: "+sizeOf(bitmap));
            UpdateVisitorImage = "Yes";
            Visitorsimage = true;
        }
    }

    private void parseImage(){
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        photo_img.setImageBitmap(bmp);

        //photo_img.setImageBitmap(rotateImage(bmp, fileUri.getPath()));

        functionCalls.LogStatus("Image Size: "+sizeOf(bmp));
        UpdateVisitorImage = "Yes";
        Visitorsimage = true;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    public static Bitmap rotateImage(Bitmap src, String Imagepath) {
        Bitmap bmp = null;
        // create new matrix
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(Imagepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Log.d("debug", ""+orientation);
        if (orientation ==1) {
            bmp = src;
            matrix.postRotate(270);
            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        } else if (orientation == 3) {
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        } else if (orientation == 8) {
            matrix.postRotate(180);
            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        } else {
            matrix.postRotate(270);
            bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        }
        return bmp;
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(),
                "Entrylog" + File.separator + IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        Random rand = new Random();
        int num = rand.nextInt(9000) + 1000;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + Mobile+""+num + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
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
                        myOutWriter.append(Name + "\r\n");
                    }
                    if (Display.equals("Mobile")) {
                        myOutWriter.append(Mobile + "\r\n");
                    }
                    if (Display.equals("From")) {
                        myOutWriter.append(FromAddress + "\r\n");
                    }
                    if (Display.equals("Contractor")) {
                        myOutWriter.append(Contractor + "\r\n");
                    }

                    if (Display.equals("To Meet")) {
                        myOutWriter.append(ToMeet + "\r\n");
                    }

                    if (Display.equals("Issued Date")) {
                        myOutWriter.append(Issued_Date + "\r\n");
                    }

                    if (Display.equals("Expiry Date")) {
                        myOutWriter.append(Expiry_Date + "\r\n");
                    }

                     if (Display.equals("Date")) {
                        /*if (!reprint) {
                            DateTime = CurrentDate() + " " + CurrentTime() + "\r\n";
                            myOutWriter.append(DateTime);
                        } else {
                            myOutWriter.append(DateTime);
                        }*/
                        myOutWriter.append(functionCalls.Convertdate(DateTime) + "\r\n");
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
                    if (Display.equals("Id Card")) {
                        myOutWriter.append(ID_Card + "\r\n");
                    }
                    if (Display.equals("Id Card Type")) {
                        myOutWriter.append(ID_Card_type + "\r\n");
                    }
                    if (Display.equals("Entry")) {
                        myOutWriter.append(User + "\r\n");
                    }
                    if (Display.equals("Email")) {
                        myOutWriter.append(Email + "\r\n");
                    }
                    if (Display.equals("Vehicle Number")) {
                        myOutWriter.append(Vehicleno + "\r\n");
                    }
                    if (Display.equals("Blood Group")) {
                        myOutWriter.append(Adhoc_Blood_group + "\r\n");
                    }
                }
            }
            myOutWriter.append(" " + "\r\n");
            myOutWriter.append(" "+"\r\n");
            myOutWriter.close();
            fOut.close();
            textfileready = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showdialog(int id) {
        switch (id) {
            case START_DLG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Mobile Number");
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.adhocdialogview, null);
                builder.setView(ll);
                builder.setCancelable(false);
                etmobile = (EditText) ll.findViewById(R.id.adhoc_dialogmobile_etTxt);

                RadioGroup visitorselection = (RadioGroup) ll.findViewById(R.id.rg_visitor_type);
                final RadioButton rb_normalvisitor = (RadioButton) ll.findViewById(R.id.rb_normal_visitor);
                final RadioButton rb_vipvisitor = (RadioButton) ll.findViewById(R.id.rb_vip_visitor);
                if (normalvisitor) {
                    rb_normalvisitor.setChecked(true);
                } else if (vipvisitor) {
                    rb_vipvisitor.setChecked(true);
                }
                radiobuttons(rb_vipvisitor, rb_normalvisitor, etmobile);
                visitorselection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radiobuttons(rb_vipvisitor, rb_normalvisitor, etmobile);
                    }
                });

                etmobile.addTextChangedListener(new TextWatcher() {
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
                                etmobile.setText("");
                            }
                        }
                    }
                });
                if (settings.getString("OTPAccess", "").equals("Yes")) {
                    builder.setPositiveButton("OTP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Visitor_Entry = "1";
                            otpcheck = true;
                            checkmobilesuggest(etmobile);
                        }
                    });
                } else {
                    builder.setPositiveButton("MANUAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Visitor_Entry = "2";
                            manualcheck = true;
                            checkmobilesuggest(etmobile);
                        }
                    });
                }

                builder.setNegativeButton("MANUAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Visitor_Entry = "2";
                        manualcheck = true;
                       checkmobilesuggest(etmobile);
                    }
                });

                builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alert1 = builder.create();
                alert1.show();
                break;

            case END_DLG:
                AlertDialog.Builder endbuilder = new AlertDialog.Builder(this);
                endbuilder.setTitle("Printing Details");
                endbuilder.setCancelable(false);
                endbuilder.setMessage("Did a Data got a printed correctly...??");
                endbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("BarCode", BarCodeValue);
                        editor.commit();
                        if (nfcavailable) {
                            writeNFC = true;
                            showdialog(NFC_DLG);
                        } else {
                            finish();
                        }
                    }
                });
                endbuilder.setNegativeButton("REPRINT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (BuildManu.equals("LS888")) {
                            reprint = true;
                            PrintingData();
                        } else {
                            showdialog(END_DLG);
                            functionCalls.showToast(AddAdhocVisitors_EL201.this, "This Device is not suitable to get print details..");
                        }
                    }
                });
                AlertDialog endalert = endbuilder.create();
                endalert.show();
                break;

            case NFC_DLG:
                AlertDialog.Builder nfcbuilder = new AlertDialog.Builder(this);
                nfcbuilder.setTitle("Write Smart Card");
                nfcbuilder.setCancelable(false);
                nfcbuilder.setMessage("Please take a Smart Card Tag to write a data on it...");
                nfcbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                nfcdialog = nfcbuilder.create();
                nfcdialog.show();
                break;

            case MOBILE_DLG:
                AlertDialog.Builder existbuilder = new AlertDialog.Builder(this);
                existbuilder.setTitle("Visitor Details");
                existbuilder.setCancelable(false);
                existbuilder.setMessage("Entered Mobile is already Logged In.. To Check In again please checkout it manually..");
                existbuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog existalert = existbuilder.create();
                existalert.show();
                break;

            case OTP_DLG:
                final AlertDialog.Builder otpbuilder = new AlertDialog.Builder(this);
                otpbuilder.setTitle("Mobile Number");
                if (!otpresent) {
                    if (vipvisitor)
                        otpbuilder.setMessage("**********");
                    else otpbuilder.setMessage(Mobile);
                } else {
                    if (otpcount == 2) {
                        otpbuilder.setMessage("OTP has been resent 2 times"+"\n"+Mobile);
                    } else {
                        otpbuilder.setMessage("OTP has been resent"+"\n"+Mobile);
                    }
                }
                LinearLayout otpll = (LinearLayout) getLayoutInflater().inflate(R.layout.dialogview, null);
                otpbuilder.setView(otpll);
                otpbuilder.setCancelable(false);
                TextInputLayout tilmobile = (TextInputLayout) otpll.findViewById(R.id.timer_Til);
                tilmobile.setVisibility(View.GONE);
                TextInputLayout tilotp = (TextInputLayout) otpll.findViewById(R.id.otp_Til);
                tilotp.setVisibility(View.VISIBLE);
                final EditText otpetTxt = (EditText) otpll.findViewById(R.id.dialogotp_etTxt);
                RadioGroup otpselection = (RadioGroup) otpll.findViewById(R.id.rg_visitor_type);
                otpselection.setVisibility(View.GONE);

                otpbuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!otpetTxt.getText().toString().equals("")) {
                            String OTP = otpetTxt.getText().toString();
                            String SavedOTP = settings.getString("OTP", "");
                            if (OTP.equals(SavedOTP)) {
                                if (mobilesuggestsuccess) {
                                    AddAdhocVisitors_EL201.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                                            SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    addvisitorslayout.setVisibility(View.VISIBLE);
                                } else {
                                    addvisitorslayout.setVisibility(View.VISIBLE);
                                    mobile_et.setText(Mobile);
                                    name_et.requestFocus();
                                }
                            } else {
                                otpetTxt.setError("Entered OTP is not matching please enter correct one..");
                                otpetTxt.setText("");
                                functionCalls.showToast(AddAdhocVisitors_EL201.this,
                                        "Entered OTP is not matching please enter correct one..");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showdialog(OTP_DLG);
                                    }
                                }, 500);
                            }
                        } else {
                            otpetTxt.setError("Please enter OTP");
                            functionCalls.showToast(AddAdhocVisitors_EL201.this, "Please enter OTP");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showdialog(OTP_DLG);
                                }
                            }, 500);
                        }
                    }
                });
                otpbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                if (otpcount == 2) {
                    otpbuilder.setNeutralButton("MANUAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Visitor_Entry = "2";
                            addvisitorslayout.setVisibility(View.VISIBLE);
                            if (mobilesuggestsuccess) {
                                AddAdhocVisitors_EL201.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                                        SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            } else {
                                mobile_et.setText(Mobile);
                                name_et.requestFocus();
                            }
                        }
                    });
                } else {
                    otpbuilder.setNeutralButton("RESEND", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            otpcount = otpcount + 1;
                            otpresent = true;
                            ConnectingTask.ADHOCSMSOTP smsotp = task.new ADHOCSMSOTP("91"+Mobile, settings.getString("OTP", ""),settings.getString("OrganizationID", ""));
                            smsotp.execute();
                            showdialog(OTP_DLG);
                            functionCalls.showToast(AddAdhocVisitors_EL201.this, "OTP Resent");
                        }
                    });
                }
                AlertDialog alert2 = otpbuilder.create();
                alert2.show();
                break;
        }
    }

    private void radiobuttons(RadioButton vip, RadioButton normal, EditText et_mobile) {
        if (normal.isChecked()) {
            normalvisitor = true;
            vipvisitor = false;
            Visitor_type = "normal";
            et_mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_mobile.setSelection(et_mobile.getText().length());
        }
        if (vip.isChecked()) {
            vipvisitor = true;
            normalvisitor = false;
            Visitor_type = "vip";
            et_mobile.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            et_mobile.setSelection(et_mobile.getText().length());
        }
    }

    private void checkmobilesuggest(EditText etmobile) {
        if (!etmobile.getText().toString().equals("")) {
            Mobile = etmobile.getText().toString();
            if (Mobile.length() == 10) {
                etmobile.setText("");
                ConnectingTask.AdhocMobileAutoSuggest mobile = task.new AdhocMobileAutoSuggest(details, Organizationid, Mobile, mProgressBar,
                        AddAdhocVisitors_EL201.this);
                mobile.execute();
                mobilesuggestthread.start();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        functionCalls.showToast(AddAdhocVisitors_EL201.this, "Please Enter Valid Mobile Number");
                        showdialog(START_DLG);
                    }
                }, 1000);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    functionCalls.showToast(AddAdhocVisitors_EL201.this, "Enter Mobile Number");
                    showdialog(START_DLG);
                }
            }, 1000);
        }
    }

    private void Successview() {
        name_et.setText(details.getAdhocVisitors_Name());
        email_et.setText(details.getAdhocVisitors_Email());
        mobile_et.setText(Mobile);
        address_et.setText(details.getAdhocVisitors_Address());
        tomeet_et.setText(details.getAdhocVisitors_tomeet());
        vehicle_et.setText(details.getAdhocVisitors_VehicleNo());
        textview_issued_date.setText(details.getAdhoc_issued_date());
        textview_expiry_date.setText(details.getAdhoc_expiry_date());

         String Image_Url = DataAPI.Image_Url2;
        String Image = details.getAdhocVisitors_Photo();
       // String Image_Path = Image_Url + Image;
        Picasso.with(AddAdhocVisitors_EL201.this).load(Image).into(photo_img);

       /* String Image_Url = DataAPI.Image_Url2;
        String Image = details.getAdhocVisitors_Photo();
        String Image_Path = Image_Url + Image;
        Picasso.with(AddAdhocVisitors_EL201.this).load(Image_Path).into(photo_img);*/

        contractor_et.setText(details.getAdhoc_Contractor());
        Visitors_id = details.getAdhocVisitorsId();
        Et_field1.setText(details.getAdhocVisitor_Designation());
        Et_field2.setText(details.getAdhocDepartment());
        Et_field3.setText(details.getAdhocPurpose());
        Et_field4.setText(details.getAdhocHouse_number());
        Et_field5.setText(details.getAdhocFlat_number());
        Et_field6.setText(details.getAdhocBlock());
        Et_field7.setText(details.getAdhocNo_Visitor());
        Et_field8.setText(details.getAdhocaClass());
        Et_field9.setText(details.getAdhocSection());
        Et_field10.setText(details.getAdhocStudent_Name());
        Et_field11.setText(details.getAdhocID_Card());
        Et_field12.setText(details.getAdhocID_Card_Type());
        Et_field13.setText(details.getAdhoc_Visitor_blood_group());
        UpdateVisitorImage = "No";
        Visitorsimage = true;
    }


    private void Extrafields() {
        functionCalls.LogStatus("Fetch field Started");
        HashSet<String> hashSet = new HashSet<>();
        hashSet = fieldsService.fieldset;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(hashSet);
        if (arrayList.size() > 0) {
            functionCalls.LogStatus("Size is more than 1");
            for (int i = 0; i < arrayList.size(); i++) {
                String value = arrayList.get(i).toString();
                functionCalls.LogStatus("Value["+i+"]: "+value);
                if (value.equals("Visitor Email")) {
                    emailLayout.setVisibility(View.VISIBLE);
                    emailLayout.setHint("Email Address");
                }
                if (value.equals("Visitor Designation")) {
                    Til_field1.setVisibility(View.VISIBLE);
                    Til_field1.setHint(value);
                }
                if (value.equals("Department")) {
                    Til_field2.setVisibility(View.VISIBLE);
                    Til_field2.setHint(value);
                }
                if (value.equals("Purpose")) {
                    Til_field3.setVisibility(View.VISIBLE);
                    Til_field3.setHint(value);
                }
                if (value.equals("House No")) {
                    Til_field4.setVisibility(View.VISIBLE);
                    Til_field4.setHint(value);
                }
                if (value.equals("Flat No")) {
                    Til_field5.setVisibility(View.VISIBLE);
                    Til_field5.setHint(value);
                }
                if (value.equals("Block")) {
                    Til_field6.setVisibility(View.VISIBLE);
                    Til_field6.setHint(value);
                }
                if (value.equals("Number of Visitor")) {
                    Til_field7.setVisibility(View.VISIBLE);
                    Til_field7.setHint(value);
                }
                if (value.equals("Class")) {
                    Til_field8.setVisibility(View.VISIBLE);
                    Til_field8.setHint(value);
                }
                if (value.equals("Section")) {
                    Til_field9.setVisibility(View.VISIBLE);
                    Til_field9.setHint(value);
                }
                if (value.equals("Student Name")) {
                    Til_field10.setVisibility(View.VISIBLE);
                    Til_field10.setHint(value);
                }
                if (value.equals("ID Card No")) {
                    Til_field11.setVisibility(View.VISIBLE);
                    Til_field11.setHint(value);
                }
                if (value.equals("ID Card Type")) {
                    Til_field12.setVisibility(View.VISIBLE);
                    Til_field12.setHint(value);
                }
                if (value.equals("Blood Group")) {
                    Til_field13.setVisibility(View.VISIBLE);
                    Til_field13.setHint(value);
                }

               /* if (value.equals("Expiry Date")) {
                    Til_field14.setVisibility(View.VISIBLE);
                    Til_field14.setHint(value);
                }
                if (value.equals("Issued Date")) {
                    Til_field15.setVisibility(View.VISIBLE);
                    Til_field15.setHint(value);
                }*/
            }
        } else {
            functionCalls.LogStatus("No Fields Available");
            functionCalls.showToast(AddAdhocVisitors_EL201.this, "No Fields Available");
        }

        //Contractor Service Started
       /* HashSet<String> StaffSet1 = new HashSet<>();
        StaffSet1 = contractorService.staffset1;
        contractorlist = new ArrayList<>();
        contractorlist.addAll(StaffSet1);
        if (contractorlist.size() > 0) {
            functionCalls.LogStatus("Contractor list Available");
            ContractorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, contractorlist);
            contractor_et.setAdapter(ContractorAdapter);
            Collections.sort(contractorlist);
            ContractorAdapter.notifyDataSetChanged();
            contractor_et.setThreshold(1);
        } else {
            functionCalls.LogStatus("Staff list not Available");
        }*/


        functionCalls.LogStatus("Contractor field Started");
        HashSet<String> StaffSet2 = new HashSet<>();
        StaffSet2 = contractorService.staffset1;
        contractorlist = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        listid = new HashMap<>();
        list1.addAll(StaffSet2);

        for (int i = 0; i < list1.size(); i++) {
            String liststaff = list1.get(i);
            String staff = liststaff.substring(0, liststaff.lastIndexOf(','));
            String staffid = liststaff.substring(liststaff.lastIndexOf(',')+1, liststaff.length());
            contractorlist.add(staff);
            listid.put(staff.toLowerCase(), staffid);
        }

        if (contractorlist.size() > 0) {
            functionCalls.LogStatus("Staff list Available");
            ContractorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, contractorlist);
            contractor_et.setAdapter(ContractorAdapter);
            Collections.sort(contractorlist);
            ContractorAdapter.notifyDataSetChanged();
            contractor_et.setThreshold(1);
            contractor_et.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contractor = parent.getItemAtPosition(position).toString();
                    Contractor_id = listid.get(parent.getItemAtPosition(position).toString().toLowerCase());
                }
            });

        } else {
            functionCalls.LogStatus("Staff list not Available");
        }



        functionCalls.LogStatus("Staff field Started");
        HashSet<String> StaffSet = new HashSet<>();
        StaffSet = staffService.staffset;
        stafflist = new ArrayList<>();
        stafflist.addAll(StaffSet);
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String liststaff = list.get(i);
            String staff = liststaff.substring(0, liststaff.lastIndexOf(','));
            String staffid = liststaff.substring(liststaff.lastIndexOf(',')+1, liststaff.length());
            stafflist.add(staff);
            // listid.put(staff.toLowerCase(), staffid);
        }

        if (stafflist.size() > 0) {
            functionCalls.LogStatus("Staff list Available");
            Staffadapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stafflist);
            tomeet_et.setAdapter(Staffadapter);
            Collections.sort(stafflist);
            Staffadapter.notifyDataSetChanged();
            tomeet_et.setThreshold(1);
            tomeet_et.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ToMeet = parent.getItemAtPosition(position).toString();
                    // StaffTomeetId = listid.get(parent.getItemAtPosition(position).toString().toLowerCase());
                }
            });

        } else {
            functionCalls.LogStatus("Staff list not Available");
        }
    }


    /*private void tomeetcontent() {
        functionCalls.LogStatus("Staff field Started");
        HashSet<String> StaffSet = new HashSet<>();
        StaffSet = staffService.staffset1;
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
    }*/

    private void getExtraFields() {
        if (emailLayout.getVisibility() == View.VISIBLE) {
            Email = email_et.getText().toString();
        }
        if (Til_field1.getVisibility() == View.VISIBLE) {
            Visitor_Designation = Et_field1.getText().toString();
        }
        if (Til_field2.getVisibility() == View.VISIBLE) {
            Department = Et_field2.getText().toString();
        }
        if (Til_field3.getVisibility() == View.VISIBLE) {
            Purpose = Et_field3.getText().toString();
        }
        if (Til_field4.getVisibility() == View.VISIBLE) {
            House_number = Et_field4.getText().toString();
        }
        if (Til_field5.getVisibility() == View.VISIBLE) {
            Flat_number = Et_field5.getText().toString();
        }
        if (Til_field6.getVisibility() == View.VISIBLE) {
            Block = Et_field6.getText().toString();
        }
        if (Til_field7.getVisibility() == View.VISIBLE) {
            No_Visitor = Et_field7.getText().toString();
        }
        if (Til_field8.getVisibility() == View.VISIBLE) {
            aClass = Et_field8.getText().toString();
        }
        if (Til_field9.getVisibility() == View.VISIBLE) {
            Section = Et_field9.getText().toString();
        }
        if (Til_field10.getVisibility() == View.VISIBLE) {
            Student_Name = Et_field10.getText().toString();
        }
        if (Til_field11.getVisibility() == View.VISIBLE) {
            ID_Card = Et_field11.getText().toString();
        }
        if (Til_field12.getVisibility() == View.VISIBLE) {
            ID_Card_type = Et_field12.getText().toString();
        }
        if (Til_field13.getVisibility() == View.VISIBLE) {
            Adhoc_Blood_group = Et_field13.getText().toString();
        }
        /*if (Til_field14.getVisibility() == View.VISIBLE) {
            Expiry_Date = Et_field14.getText().toString();
        }
        if (Til_field15.getVisibility() == View.VISIBLE) {
            Issued_Date = Et_field15.getText().toString();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        functionCalls.LogStatus("OnResume NFCAvailable: "+nfcavailable);
        if (nfcavailable) {
            enableForegroundDispatchSystem();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(AddAdhocVisitors_EL201.this, "Smart Card Intent", Toast.LENGTH_SHORT).show();
            if (writeNFC) {
                writeSmartTag(intent, BarCodeValue);
            } else {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (parcelables != null && parcelables.length > 0) {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                } else {
                    Toast.makeText(AddAdhocVisitors_EL201.this, "No Ndef Message Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = new Intent(this, AddAdhocVisitors_EL201.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void writeSmartTag(Intent intent, String Data) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage ndefMessage = createNdefMessage(Data);

        writeNdefMessage(tag, ndefMessage);
    }

    private NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ ndefRecord });
        return ndefMessage;
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag == null) {
                functionCalls.showToast(AddAdhocVisitors_EL201.this, "Tag Object cannot be null");
                return;
            }
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();
                if (!ndef.isWritable()) {
                    functionCalls.showToast(AddAdhocVisitors_EL201.this, "Tag is not writable");
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                functionCalls.showToast(AddAdhocVisitors_EL201.this, "Tag written");
                finish();
            }
        } catch (Exception e) {

        }
    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) languageSize & 0x1F);
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable == null) {
                functionCalls.showToast(AddAdhocVisitors_EL201.this, "This is not ndef formatable");
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            functionCalls.showToast(AddAdhocVisitors_EL201.this, "Tag written");
        } catch (Exception e) {

        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagcontent = getTextfromNdefRecord(ndefRecord);
            checkingout(tagcontent);
        } else {
            Toast.makeText(AddAdhocVisitors_EL201.this, "No Ndef Records Found", Toast.LENGTH_SHORT).show();
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

        ConnectingTask.AdhocSmartCheckInOut checkOut = task.new AdhocSmartCheckInOut(details, result, Organizationid, GuardID);
        checkOut.execute();
        dialog = ProgressDialog.show(AddAdhocVisitors_EL201.this, "", "Checking...", true);
        dialog.setCancelable(true);
        mobilesuggestthread = null;
        Runnable runnable = new AddAdhocVisitors_EL201.SuggestTimer();
        mobilesuggestthread = new Thread(runnable);
        mobilesuggestthread.start();
    }
}
