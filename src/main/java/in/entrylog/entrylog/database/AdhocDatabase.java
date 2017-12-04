package in.entrylog.entrylog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by Admin on 16-Mar-17.
 */

public class AdhocDatabase {
    String DATABASE_NAME = "adhocentrylog.db", DATABASE_PATH;
    int DATABASE_VERSION = 2;
    MyHelper mh;
    SQLiteDatabase sdb;
    File databasefile = null;

    public AdhocDatabase(Context context) {
        try {
            databasefile = filestorepath(DATABASE_NAME);
            if (databasefile.exists()) {
                Log.d("Database", "Exists in device");
            }
            else {
                Log.d("Database", "Does not exists in device");
            }
            DATABASE_PATH = filepath() + File.separator + DATABASE_NAME;
            mh = new MyHelper(context, DATABASE_PATH, null, DATABASE_VERSION);
        } catch (Exception e) {
        }
    }

    public void open() {
        sdb = mh.getWritableDatabase();
    }

    public void close() {
        sdb.close();
    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH);
        if(file.exists()) {
            file.delete();
        }
    }

    private class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table ADHOCCHECKINDATA(_id integer primary key, adhoc_visitors_name text, adhoc_visitors_email text, " +
                    "adhoc_visitors_mobile text, adhoc_visitors_address text, adhoc_to_meet text, adhoc_visitors_vehicle_no text, adhoc_visitors_photo text, " +
                    "adhoc_image_path text, rfid_number text, organization_id text, user_id text, adhoc_upload_image text, " +
                    "adhoc_visitor_designation text, adhoc_department text, adhoc_purpose text, adhoc_house_number text, adhoc_flat_number text, adhoc_block text, " +
                    "adhoc_no_visitor text, adhoc_class text, adhoc_section text, adhoc_student_name text, adhoc_id_card_number text, device text, " +
                    "expiry_date text, issued_date text, adhoc_id_card_type text, adhoc_visitors_blood_group text, adhoc_contractor text);");
            db.execSQL("Create table ADHOCENTRYLOGDATA(_id integer primary key, adhoc_visitors_name text, adhoc_visitors_email text, " +
                    "adhoc_visitors_mobile text, adhoc_visitors_address text, adhoc_to_meet text, adhoc_visitors_vehicle_no text, adhoc_visitors_photo text, " +
                    "adhoc_image_path text, rfid_number text, organization_id text, user_id text, adhoc_upload_image text, " +
                    "adhoc_visitor_designation text, adhoc_department text, adhoc_purpose text, adhoc_house_number text, adhoc_flat_number text, adhoc_block text, " +
                    "adhoc_no_visitor text, adhoc_class text, adhoc_section text, adhoc_student_name text, adhoc_id_card_number text, device text, " +
                    "expiry_date text, issued_date text, adhoc_id_card_type text, adhoc_visitors_blood_group text, adhoc_contractor text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public String filepath() {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "Entrylog"
                + File.separator + "Database");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String pathname = "" + dir;
        return pathname;
    }

    public File filestorepath(String file) {
        File dir = new File(android.os.Environment.getExternalStorageDirectory(), "Entrylog"
                + File.separator + "Database");
        Log.d("File Dir", "" + dir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dir1 = new File(dir, File.separator + file);
        Log.d("File Dir1", "" + dir1);
        if (dir1.exists()) {
            Log.d("File Dir1", "Created");
        } else {
            Log.d("File Dir1", "Not Created");
        }
        return dir1;
    }

    public void insertcheckin(String Name, String Email, String Mobile, String Address, String ToMeet, String VehicleNo,
                              String Imagefile, String ImagePath, String Barcode, String OrganizationID, String SecurityID,
                              String Upload_Image, String visitor_Designation, String department, String purpose,
                              String house_number, String flat_number, String block, String no_Visitor, String aclass,
                              String section, String student_Name, String ID_Card, String Device, String Visitor_Entry,
                              String Current_Time, String ID_Card_Type, String adhoc_visitors_blood_group, String contractor) {
        ContentValues cv = new ContentValues();
        cv.put("adhoc_visitors_name", Name);
        cv.put("adhoc_visitors_email", Email);
        cv.put("adhoc_visitors_mobile", Mobile);
        cv.put("adhoc_visitors_address", Address);
        cv.put("adhoc_to_meet", ToMeet);
        cv.put("adhoc_visitors_vehicle_no", VehicleNo);
        cv.put("adhoc_visitors_photo", Imagefile);
        cv.put("adhoc_image_path", ImagePath);
        cv.put("rfid_number", Barcode);
        cv.put("organization_id", OrganizationID);
        cv.put("user_id", SecurityID);
        cv.put("adhoc_upload_image", Upload_Image);
        cv.put("adhoc_visitor_designation", visitor_Designation);
        cv.put("adhoc_department", department);
        cv.put("adhoc_purpose", purpose);
        cv.put("adhoc_house_number", house_number);
        cv.put("adhoc_flat_number", flat_number);
        cv.put("adhoc_block", block);
        cv.put("adhoc_no_visitor", no_Visitor);
        cv.put("adhoc_class", aclass);
        cv.put("adhoc_section", section);
        cv.put("adhoc_student_name", student_Name);
        cv.put("adhoc_id_card_number", ID_Card);
        cv.put("device", Device);
        cv.put("expiry_date", Visitor_Entry);
        cv.put("issued_date", Current_Time);
        cv.put("adhoc_id_card_type", ID_Card_Type);
        cv.put("adhoc_visitors_blood_group", adhoc_visitors_blood_group);
        cv.put("adhoc_contractor",contractor);
        sdb.insert("ADHOCCHECKINDATA", null, cv);
    }


    public void insertentrylogdata(String Name, String Email, String Mobile, String Address, String ToMeet, String VehicleNo,
                                   String Imagefile, String ImagePath, String Barcode, String OrganizationID, String SecurityID,
                                   String Upload_Image, String visitor_Designation, String department, String purpose,
                                   String house_number, String flat_number, String block, String no_Visitor, String aclass,
                                   String section, String student_Name, String ID_Card, String Device, String Expiry_Date,
                                   String Issued_Date, String ID_Card_Type, String adhoc_visitors_blood_group, String contractor) {
        ContentValues cv = new ContentValues();
        cv.put("adhoc_visitors_name", Name);
        cv.put("adhoc_visitors_email", Email);
        cv.put("adhoc_visitors_mobile", Mobile);
        cv.put("adhoc_visitors_address", Address);
        cv.put("adhoc_to_meet", ToMeet);
        cv.put("adhoc_visitors_vehicle_no", VehicleNo);
        cv.put("adhoc_visitors_photo", Imagefile);
        cv.put("adhoc_image_path", ImagePath);
        cv.put("rfid_number", Barcode);
        cv.put("organization_id", OrganizationID);
        cv.put("user_id", SecurityID);
        cv.put("adhoc_upload_image", Upload_Image);
        cv.put("adhoc_visitor_designation", visitor_Designation);
        cv.put("adhoc_department", department);
        cv.put("adhoc_purpose", purpose);
        cv.put("adhoc_flat_number", flat_number);
        cv.put("adhoc_house_number", house_number);
        cv.put("adhoc_block", block);
        cv.put("adhoc_no_visitor", no_Visitor);
        cv.put("adhoc_class", aclass);
        cv.put("adhoc_section", section);
        cv.put("adhoc_student_name", student_Name);
        cv.put("adhoc_id_card_number", ID_Card);
        cv.put("device", Device);
        cv.put("expiry_date", Expiry_Date);
        cv.put("issued_date", Issued_Date);
        cv.put("adhoc_id_card_type", ID_Card_Type);
        cv.put("adhoc_visitors_blood_group",adhoc_visitors_blood_group);
        cv.put("adhoc_contractor",contractor);
        sdb.insert("ADHOCENTRYLOGDATA", null, cv);
    }

    public Cursor readCheckinData() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM ADHOCCHECKINDATA", null);
        return c;
    }

    public Cursor readentrylogdata() {
        Cursor c = null;
        c = sdb.rawQuery("SELECT * FROM ADHOCENTRYLOGDATA", null);
        return c;
    }

    public Cursor deleteCheckinData(String id) {
        Cursor c = null;
        c = sdb.rawQuery("delete from ADHOCCHECKINDATA WHERE _id = "+"'"+id+"'", null);
        return c;
    }

    public Cursor deleteentrylogdata() {
        Cursor c = null;
        c = sdb.rawQuery("delete from ADHOCENTRYLOGDATA", null);
        return c;
    }
}