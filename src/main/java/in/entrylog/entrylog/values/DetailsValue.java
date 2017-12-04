package in.entrylog.entrylog.values;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;

public class DetailsValue {
    private boolean LoginSuccess;
    private boolean LoginFailure;
    private boolean Fieldsexists;
    private boolean NoFieldsExists;
    private boolean VisitorCheckedIn;
    private boolean VisitorCheckInError;
    private boolean VisitorsCheckOutSuccess;
    private boolean VisitorsCheckOutFailure;
    private boolean VisitorImageUpload;
    private boolean MobileAutoSuggestSuccess;
    private boolean MobileAutoSuggestFailure;
    private boolean MobileNoExist;
    private boolean LoginExist;
    private boolean VisitorsCheckOutDone;
    private boolean NoVisitorsFound;
    private boolean VisitorsFound;
    private boolean StaffExists;
    private boolean NoStaffExist;
    private boolean PrinterOrderSuccess;
    private boolean PurposeStaffExists;
    private boolean NoPurposeStaffExist;
    private boolean SMSOTPSuccces;
    private boolean TimeBoundSuccess;

    public boolean isTimeBoundSuccess() {
        return TimeBoundSuccess;
    }

    public void setTimeBoundSuccess(boolean timeBoundSuccess) {
        TimeBoundSuccess = timeBoundSuccess;
    }

    public boolean isTimeBoundFailure() {
        return TimeBoundFailure;
    }

    public void setTimeBoundFailure(boolean timeBoundFailure) {
        TimeBoundFailure = timeBoundFailure;
    }

    private boolean TimeBoundFailure;



    public boolean isSMSOTPSuccces() {
        return SMSOTPSuccces;
    }

    public void setSMSOTPSuccces(boolean SMSOTPSuccces) {
        this.SMSOTPSuccces = SMSOTPSuccces;
    }

    public boolean isSMSOTPSucccesFailure() {
        return SMSOTPSucccesFailure;
    }

    public void setSMSOTPSucccesFailure(boolean SMSOTPSucccesFailure) {
        this.SMSOTPSucccesFailure = SMSOTPSucccesFailure;
    }

    private boolean SMSOTPSucccesFailure;


    public boolean isPurposeStaffExists() {
        return PurposeStaffExists;
    }

    public void setPurposeStaffExists(boolean purposeStaffExists) {
        PurposeStaffExists = purposeStaffExists;
    }

    public boolean isNoPurposeStaffExist() {
        return NoPurposeStaffExist;
    }

    public void setNoPurposeStaffExist(boolean noPurposeStaffExist) {
        NoPurposeStaffExist = noPurposeStaffExist;
    }

    public boolean isMobileNoSPAM() {
        return MobileNoSPAM;
    }

    public void setMobileNoSPAM(boolean mobileNoSPAM) {
        MobileNoSPAM = mobileNoSPAM;
    }

    private boolean PrinterOrderNoData;
    private boolean Accountblocked;
    private boolean PermissionSuccess;
    private boolean PermissionFailure;
    private boolean Apkfilexist;
    private boolean GotTime;
    private boolean NoTime;
    private boolean SmartIn;
    private boolean SmartOut;
    private boolean SmartError;
    private boolean AppointmentsFound;
    private boolean AppointmentsNotFound;

    private boolean MobileNoSPAM;

    private boolean AdhocSmartIn;
    private boolean AdhocSmartOut;
    private boolean AdhocMobileAutoSuggestSuccess;
    private boolean AdhocMobileAutoSuggestFailure;

    public boolean isContactorsExists() {
        return ContactorsExists;
    }

    public void setContactorsExists(boolean contactorsExists) {
        ContactorsExists = contactorsExists;
    }

    public boolean isNoContractorsExist() {
        return NoContractorsExist;
    }

    public void setNoContractorsExist(boolean noContractorsExist) {
        NoContractorsExist = noContractorsExist;
    }

    private boolean ContactorsExists;
    private boolean NoContractorsExist;

    private boolean AdhocVisitorCheckedIn;

    public boolean isAdhocSmartIn() {
        return AdhocSmartIn;
    }

    public void setAdhocSmartIn(boolean adhocSmartIn) {
        AdhocSmartIn = adhocSmartIn;
    }

    public boolean isAdhocSmartOut() {
        return AdhocSmartOut;
    }

    public void setAdhocSmartOut(boolean adhocSmartOut) {
        AdhocSmartOut = adhocSmartOut;
    }


    public boolean isAdhocVisitorCheckedIn() {
        return AdhocVisitorCheckedIn;
    }

    public void setAdhocVisitorCheckedIn(boolean adhocVisitorCheckedIn) {
        AdhocVisitorCheckedIn = adhocVisitorCheckedIn;
    }

    public boolean isAdhocVisitorCheckInError() {
        return AdhocVisitorCheckInError;
    }

    public void setAdhocVisitorCheckInError(boolean adhocVisitorCheckInError) {
        AdhocVisitorCheckInError = adhocVisitorCheckInError;
    }

    public boolean isAdhocVisitorsCheckOutSuccess() {
        return AdhocVisitorsCheckOutSuccess;
    }

    public void setAdhocVisitorsCheckOutSuccess(boolean adhocVisitorsCheckOutSuccess) {
        AdhocVisitorsCheckOutSuccess = adhocVisitorsCheckOutSuccess;
    }

    public boolean isAdhocVisitorsCheckOutFailure() {
        return AdhocVisitorsCheckOutFailure;
    }

    public void setAdhocVisitorsCheckOutFailure(boolean adhocVisitorsCheckOutFailure) {
        AdhocVisitorsCheckOutFailure = adhocVisitorsCheckOutFailure;
    }

    private boolean AdhocVisitorCheckInError;
    private boolean AdhocVisitorsCheckOutSuccess;
    private boolean AdhocVisitorsCheckOutFailure;
    private boolean NoAdhocVisitorsFound;

    public boolean isNoAdhocVisitorsFound() {
        return NoAdhocVisitorsFound;
    }

    public void setNoAdhocVisitorsFound(boolean noAdhocVisitorsFound) {
        NoAdhocVisitorsFound = noAdhocVisitorsFound;
    }

    public boolean isAdhocVisitorsFound() {
        return AdhocVisitorsFound;
    }

    public void setAdhocVisitorsFound(boolean adhocVisitorsFound) {
        AdhocVisitorsFound = adhocVisitorsFound;
    }

    private boolean AdhocVisitorsFound;

    public boolean isAdhocMobileAutoSuggestSuccess() {
        return AdhocMobileAutoSuggestSuccess;
    }

    public void setAdhocMobileAutoSuggestSuccess(boolean adhocMobileAutoSuggestSuccess) {
        AdhocMobileAutoSuggestSuccess = adhocMobileAutoSuggestSuccess;
    }

    public boolean isAdhocMobileAutoSuggestFailure() {
        return AdhocMobileAutoSuggestFailure;
    }

    public void setAdhocMobileAutoSuggestFailure(boolean adhocMobileAutoSuggestFailure) {
        AdhocMobileAutoSuggestFailure = adhocMobileAutoSuggestFailure;
    }

    public boolean isAdhocMobileNoExist() {
        return AdhocMobileNoExist;
    }

    public void setAdhocMobileNoExist(boolean adhocMobileNoExist) {
        AdhocMobileNoExist = adhocMobileNoExist;
    }

    private boolean AdhocMobileNoExist;

    public boolean isAdhocSmartError() {
        return AdhocSmartError;
    }

    public void setAdhocSmartError(boolean adhocSmartError) {
        AdhocSmartError = adhocSmartError;
    }

    private boolean AdhocSmartError;

    public boolean isAdhocSmartInvalidCard() {
        return AdhocSmartInvalidCard;
    }

    public void setAdhocSmartInvalidCard(boolean adhocSmartInvalidCard) {
        AdhocSmartInvalidCard = adhocSmartInvalidCard;
    }

    private boolean AdhocSmartInvalidCard;



    public boolean isAdhocLoginSuccess() {
        return AdhocLoginSuccess;
    }

    public void setAdhocLoginSuccess(boolean adhocLoginSuccess) {
        AdhocLoginSuccess = adhocLoginSuccess;
    }

    public boolean isAdhocLoginFailure() {
        return AdhocLoginFailure;
    }

    public void setAdhocLoginFailure(boolean adhocLoginFailure) {
        AdhocLoginFailure = adhocLoginFailure;
    }

    private boolean AdhocLoginSuccess;
    private boolean AdhocLoginFailure;
    private boolean AdhocFieldsexists;

    private boolean AdhocDeleteSuccess;

    public boolean isAdhocDeleteSuccess() {
        return AdhocDeleteSuccess;
    }

    public void setAdhocDeleteSuccess(boolean adhocDeleteSuccess) {
        AdhocDeleteSuccess = adhocDeleteSuccess;
    }

    public boolean isAdhocDeleteFailure() {
        return AdhocDeleteFailure;
    }

    public void setAdhocDeleteFailure(boolean adhocDeleteFailure) {
        AdhocDeleteFailure = adhocDeleteFailure;
    }

    private boolean AdhocDeleteFailure;

    public boolean isAdhocFieldsexists() {
        return AdhocFieldsexists;
    }

    public void setAdhocFieldsexists(boolean adhocFieldsexists) {
        AdhocFieldsexists = adhocFieldsexists;
    }

    public boolean isNoAdhocFieldsExists() {
        return NoAdhocFieldsExists;
    }

    public void setNoAdhocFieldsExists(boolean noAdhocFieldsExists) {
        NoAdhocFieldsExists = noAdhocFieldsExists;
    }

    private boolean NoAdhocFieldsExists;
    private String GuardID, OrganizationID, OrganizationName, Fields, ImageFileName, VisitorsId, OverNightStay_Time;

    private int DisplayFields;
    private String Visitors_Name;
    private String Visitors_Email;
    private String Visitors_Mobile;
    private String Visitors_Address;
    private String Visitors_tomeet;
    private String Visitors_VehicleNo;
    private String Visitors_Photo;
    private String Visitors_CheckInTime;
    private String Visitors_CheckInBy;
    private String Visitor_ID;
    private String Visitors_CheckOutTime;
    private String Visitors_BarCode;
    private String Check_in_User;
    private String Check_out_User;
    private String Visitor_Designation;
    private String Department;
    private String Purpose;
    private String House_number;
    private String Flat_number;
    private String Block;
    private String No_Visitor;
    private String aClass;
    private String Section;
    private String Student_Name;
    private String ID_Card;
    private String OTPAccess;
    private String ImageAccess;
    private String Printertype;
    private String Scannertype;
    private String RfidStatus;
    private String DeviceModel;
    private String Cameratype;
    private String Apkfile;
    private String ApkdownloadUrl;
    private String ServerTime;
    private String ID_Card_Type;
    private String AppointmentDate;
    private String AppointmentTime;
    private String AppointmentStaffName;
    private String AppointmentLocation;
    private String AppointmentFrom;
    private String Visitor_blood_group;
    private String timebound_hour;
    private String timebound_minute;

    public String getTimeBound_timings() {
        return TimeBound_timings;
    }

    public void setTimeBound_timings(String timeBound_timings) {
        TimeBound_timings = timeBound_timings;
    }

    private String TimeBound_timings;

    public String getVisitor_blood_group() {
        return Visitor_blood_group;
    }

    public String getTimebound_hour() {
        return timebound_hour;
    }

    public void setTimebound_hour(String timebound_hour) {
        this.timebound_hour = timebound_hour;
    }

    public String getTimebound_minute() {
        return timebound_minute;
    }

    public void setTimebound_minute(String timebound_minute) {
        this.timebound_minute = timebound_minute;
    }

    public void setVisitor_blood_group(String visitor_blood_group) {
        Visitor_blood_group = visitor_blood_group;
    }

    public String getAppointmentFrom() {
        return AppointmentFrom;
    }

    public void setAppointmentFrom(String appointmentFrom) {
        AppointmentFrom = appointmentFrom;
    }

    private String AdhocVisitorsId;
    private String AdhocVisitors_Name;
    private String AdhocVisitors_Email;
    private String AdhocVisitors_Mobile;
    private String AdhocVisitors_Address;
    private String AdhocVisitors_tomeet;
    private String AdhocVisitors_VehicleNo;
    private String AdhocVisitors_Photo;
    private String AdhocVisitors_CheckInTime;
    private String AdhocVisitors_CheckInBy;
    private String AdhocVisitor_ID;
    private String AdhocVisitors_CheckOutTime;
    private String AdhocVisitors_BarCode;
    private String AdhocCheck_in_User;
    private String AdhocCheck_out_User;
    private String AdhocVisitor_Designation;
    private String AdhocDepartment;
    private String AdhocPurpose;
    private String AdhocHouse_number;
    private String AdhocFlat_number;
    private String AdhocBlock;
    private String AdhocNo_Visitor;
    private String AdhocaClass;
    private String AdhocSection;
    private String AdhocStudent_Name;
    private String AdhocID_Card;
    private String AdhocOTPAccess;
    private String AdhocImageAccess;
    private String AdhocPrintertype;
    private String AdhocScannertype;
    private String AdhocRfidStatus;
    private String AdhocDeviceModel;
    private String AdhocCameratype;
    private String AdhocID_Card_Type;
    private String Adhoc_Visitor_blood_group;
    private String Adhoc_issued_date;
    private String Adhoc_expiry_date;

    public String getAdhoc_Contractor() {
        return Adhoc_Contractor;
    }

    public void setAdhoc_Contractor(String adhoc_Contractor) {
        Adhoc_Contractor = adhoc_Contractor;
    }

    private String Adhoc_Contractor;

    public String getAdhoc_Contractor_id() {
        return Adhoc_Contractor_id;
    }

    public void setAdhoc_Contractor_id(String adhoc_Contractor_id) {
        Adhoc_Contractor_id = adhoc_Contractor_id;
    }

    private String Adhoc_Contractor_id;

    public String getAdhocVisitorsId() {
        return AdhocVisitorsId;
    }

    public String getAppointmentLocation() {
        return AppointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        AppointmentLocation = appointmentLocation;
    }

    public void setAdhocVisitorsId(String adhocVisitorsId) {
        AdhocVisitorsId = adhocVisitorsId;
    }

    public String getAdhocVisitors_Name() {
        return AdhocVisitors_Name;
    }

    public void setAdhocVisitors_Name(String adhocVisitors_Name) {
        AdhocVisitors_Name = adhocVisitors_Name;
    }

    public String getAdhocVisitors_Email() {
        return AdhocVisitors_Email;
    }

    public void setAdhocVisitors_Email(String adhocVisitors_Email) {
        AdhocVisitors_Email = adhocVisitors_Email;
    }

    public String getAdhocVisitors_Mobile() {
        return AdhocVisitors_Mobile;
    }

    public void setAdhocVisitors_Mobile(String adhocVisitors_Mobile) {
        AdhocVisitors_Mobile = adhocVisitors_Mobile;
    }

    public String getAdhocVisitors_Address() {
        return AdhocVisitors_Address;
    }

    public void setAdhocVisitors_Address(String adhocVisitors_Address) {
        AdhocVisitors_Address = adhocVisitors_Address;
    }

    public String getAdhocVisitors_tomeet() {
        return AdhocVisitors_tomeet;
    }

    public void setAdhocVisitors_tomeet(String adhocVisitors_tomeet) {
        AdhocVisitors_tomeet = adhocVisitors_tomeet;
    }

    public String getAdhocVisitors_VehicleNo() {
        return AdhocVisitors_VehicleNo;
    }

    public void setAdhocVisitors_VehicleNo(String adhocVisitors_VehicleNo) {
        AdhocVisitors_VehicleNo = adhocVisitors_VehicleNo;
    }

    public String getAdhocVisitors_Photo() {
        return AdhocVisitors_Photo;
    }

    public void setAdhocVisitors_Photo(String adhocVisitors_Photo) {
        AdhocVisitors_Photo = adhocVisitors_Photo;
    }

    public String getAdhocVisitors_CheckInTime() {
        return AdhocVisitors_CheckInTime;
    }

    public void setAdhocVisitors_CheckInTime(String adhocVisitors_CheckInTime) {
        AdhocVisitors_CheckInTime = adhocVisitors_CheckInTime;
    }

    public String getAdhocVisitors_CheckInBy() {
        return AdhocVisitors_CheckInBy;
    }

    public void setAdhocVisitors_CheckInBy(String adhocVisitors_CheckInBy) {
        AdhocVisitors_CheckInBy = adhocVisitors_CheckInBy;
    }

    public String getAdhocVisitor_ID() {
        return AdhocVisitor_ID;
    }

    public void setAdhocVisitor_ID(String adhocVisitor_ID) {
        AdhocVisitor_ID = adhocVisitor_ID;
    }

    public String getAdhocVisitors_CheckOutTime() {
        return AdhocVisitors_CheckOutTime;
    }

    public void setAdhocVisitors_CheckOutTime(String adhocVisitors_CheckOutTime) {
        AdhocVisitors_CheckOutTime = adhocVisitors_CheckOutTime;
    }

    public String getAdhocVisitors_BarCode() {
        return AdhocVisitors_BarCode;
    }

    public void setAdhocVisitors_BarCode(String adhocVisitors_BarCode) {
        AdhocVisitors_BarCode = adhocVisitors_BarCode;
    }

    public String getAdhocCheck_in_User() {
        return AdhocCheck_in_User;
    }

    public void setAdhocCheck_in_User(String adhocCheck_in_User) {
        AdhocCheck_in_User = adhocCheck_in_User;
    }

    public String getAdhocCheck_out_User() {
        return AdhocCheck_out_User;
    }

    public void setAdhocCheck_out_User(String adhocCheck_out_User) {
        AdhocCheck_out_User = adhocCheck_out_User;
    }

    public String getAdhocVisitor_Designation() {
        return AdhocVisitor_Designation;
    }

    public void setAdhocVisitor_Designation(String adhocVisitor_Designation) {
        AdhocVisitor_Designation = adhocVisitor_Designation;
    }

    public String getAdhocDepartment() {
        return AdhocDepartment;
    }

    public void setAdhocDepartment(String adhocDepartment) {
        AdhocDepartment = adhocDepartment;
    }

    public String getAdhocPurpose() {
        return AdhocPurpose;
    }

    public void setAdhocPurpose(String adhocPurpose) {
        AdhocPurpose = adhocPurpose;
    }

    public String getAdhocHouse_number() {
        return AdhocHouse_number;
    }

    public void setAdhocHouse_number(String adhocHouse_number) {
        AdhocHouse_number = adhocHouse_number;
    }

    public String getAdhocFlat_number() {
        return AdhocFlat_number;
    }

    public void setAdhocFlat_number(String adhocFlat_number) {
        AdhocFlat_number = adhocFlat_number;
    }

    public String getAdhocBlock() {
        return AdhocBlock;
    }

    public void setAdhocBlock(String adhocBlock) {
        AdhocBlock = adhocBlock;
    }

    public String getAdhocNo_Visitor() {
        return AdhocNo_Visitor;
    }

    public void setAdhocNo_Visitor(String adhocNo_Visitor) {
        AdhocNo_Visitor = adhocNo_Visitor;
    }

    public String getAdhocaClass() {
        return AdhocaClass;
    }

    public void setAdhocaClass(String adhocaClass) {
        AdhocaClass = adhocaClass;
    }

    public String getAdhocSection() {
        return AdhocSection;
    }

    public void setAdhocSection(String adhocSection) {
        AdhocSection = adhocSection;
    }

    public String getAdhocStudent_Name() {
        return AdhocStudent_Name;
    }

    public void setAdhocStudent_Name(String adhocStudent_Name) {
        AdhocStudent_Name = adhocStudent_Name;
    }

    public String getAdhocID_Card() {
        return AdhocID_Card;
    }

    public void setAdhocID_Card(String adhocID_Card) {
        AdhocID_Card = adhocID_Card;
    }

    public String getAdhocOTPAccess() {
        return AdhocOTPAccess;
    }

    public void setAdhocOTPAccess(String adhocOTPAccess) {
        AdhocOTPAccess = adhocOTPAccess;
    }

    public String getAdhocImageAccess() {
        return AdhocImageAccess;
    }

    public void setAdhocImageAccess(String adhocImageAccess) {
        AdhocImageAccess = adhocImageAccess;
    }

    public String getAdhocPrintertype() {
        return AdhocPrintertype;
    }

    public void setAdhocPrintertype(String adhocPrintertype) {
        AdhocPrintertype = adhocPrintertype;
    }

    public String getAdhocScannertype() {
        return AdhocScannertype;
    }

    public void setAdhocScannertype(String adhocScannertype) {
        AdhocScannertype = adhocScannertype;
    }

    public String getAdhocRfidStatus() {
        return AdhocRfidStatus;
    }

    public void setAdhocRfidStatus(String adhocRfidStatus) {
        AdhocRfidStatus = adhocRfidStatus;
    }

    public String getAdhocDeviceModel() {
        return AdhocDeviceModel;
    }

    public void setAdhocDeviceModel(String adhocDeviceModel) {
        AdhocDeviceModel = adhocDeviceModel;
    }

    public String getAdhocCameratype() {
        return AdhocCameratype;
    }

    public void setAdhocCameratype(String adhocCameratype) {
        AdhocCameratype = adhocCameratype;
    }

    public String getAdhocID_Card_Type() {
        return AdhocID_Card_Type;
    }

    public void setAdhocID_Card_Type(String adhocID_Card_Type) {
        AdhocID_Card_Type = adhocID_Card_Type;
    }

    public String getAdhoc_Visitor_blood_group() {
        return Adhoc_Visitor_blood_group;
    }

    public void setAdhoc_Visitor_blood_group(String adhoc_Visitor_blood_group) {
        Adhoc_Visitor_blood_group = adhoc_Visitor_blood_group;
    }

    public String getAdhoc_issued_date() {
        return Adhoc_issued_date;
    }

    public void setAdhoc_issued_date(String adhoc_issued_date) {
        Adhoc_issued_date = adhoc_issued_date;
    }

    public String getAdhoc_expiry_date() {
        return Adhoc_expiry_date;
    }

    public void setAdhoc_expiry_date(String adhoc_expiry_date) {
        Adhoc_expiry_date = adhoc_expiry_date;
    }

    public DetailsValue(String adhocVisitors_Name, String adhocVisitors_Email, String adhocVisitors_Mobile,
                        String adhocVisitors_Address, String adhocVisitors_tomeet, String adhocVisitors_VehicleNo, String adhocVisitors_Photo, String adhoc_issued_date) {
        AdhocVisitors_Name = adhocVisitors_Name;
        AdhocVisitors_Email = adhocVisitors_Email;
        AdhocVisitors_Mobile = adhocVisitors_Mobile;
        AdhocVisitors_Address = adhocVisitors_Address;
        AdhocVisitors_tomeet = adhocVisitors_tomeet;
        AdhocVisitors_VehicleNo = adhocVisitors_VehicleNo;
        AdhocVisitors_Photo = adhocVisitors_Photo;
        Adhoc_issued_date = adhoc_issued_date;
    }

    private BluetoothSocket Socket;
    private BluetoothDevice device;
    private static ArrayList<String> arrayFields;

    public DetailsValue() {
    }

    public DetailsValue(String visitors_Name, String visitors_Email, String visitors_Mobile, String visitors_Address,
                        String visitors_tomeet, String visitors_VehicleNo, String visitors_Photo, String visitors_CheckInTime,
                        String visitors_CheckInBy) {
        Visitors_Name = visitors_Name;
        Visitors_Email = visitors_Email;
        Visitors_Mobile = visitors_Mobile;
        Visitors_Address = visitors_Address;
        Visitors_tomeet = visitors_tomeet;
        Visitors_VehicleNo = visitors_VehicleNo;
        Visitors_Photo = visitors_Photo;
        Visitors_CheckInTime = visitors_CheckInTime;
        Visitors_CheckInBy = visitors_CheckInBy;
    }

    public DetailsValue(String visitors_Name, String visitors_Email, String visitors_Mobile, String visitors_Address,
                        String visitors_tomeet, String visitors_VehicleNo, String visitors_Photo) {
        Visitors_Name = visitors_Name;
        Visitors_Email = visitors_Email;
        Visitors_Mobile = visitors_Mobile;
        Visitors_Address = visitors_Address;
        Visitors_tomeet = visitors_tomeet;
        Visitors_VehicleNo = visitors_VehicleNo;
        Visitors_Photo = visitors_Photo;
    }

    public boolean isLoginSuccess() {
        return LoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        LoginSuccess = loginSuccess;
    }

    public boolean isLoginFailure() {
        return LoginFailure;
    }

    public void setLoginFailure(boolean loginFailure) {
        LoginFailure = loginFailure;
    }

    public boolean isLoginExist() {
        return LoginExist;
    }

    public void setLoginExist(boolean loginExist) {
        LoginExist = loginExist;
    }

    public boolean isAccountblocked() {
        return Accountblocked;
    }

    public void setAccountblocked(boolean accountblocked) {
        Accountblocked = accountblocked;
    }

    public String getFields() {
        return this.Fields;
    }

    public void setFields(String fields) {
        this.Fields = fields;
    }

    public String getOrganizationID() {
        return this.OrganizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.OrganizationID = organizationID;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public int getDisplayFields() {
        return this.DisplayFields;
    }

    public void setDisplayFields(int displayFields) {
        this.DisplayFields = displayFields;
    }

    public boolean isFieldsexists() {
        return Fieldsexists;
    }

    public void setFieldsexists(boolean fieldsexists) {
        Fieldsexists = fieldsexists;
    }

    public boolean isStaffExists() {
        return StaffExists;
    }

    public void setStaffExists(boolean staffExists) {
        StaffExists = staffExists;
    }

    public boolean isNoFieldsExists() {
        return NoFieldsExists;
    }

    public void setNoFieldsExists(boolean noFieldsExists) {
        NoFieldsExists = noFieldsExists;
    }

    public boolean isNoStaffExist() {
        return NoStaffExist;
    }

    public void setNoStaffExist(boolean noStaffExist) {
        NoStaffExist = noStaffExist;
    }

    public String getImageFileName() {
        return ImageFileName;
    }

    public void setImageFileName(String imageFileName) {
        ImageFileName = imageFileName;
    }

    public boolean isVisitorCheckedIn() {
        return VisitorCheckedIn;
    }

    public void setVisitorCheckedIn(boolean visitorCheckedIn) {
        VisitorCheckedIn = visitorCheckedIn;
    }

    public boolean isVisitorCheckInError() {
        return VisitorCheckInError;
    }

    public void setVisitorCheckInError(boolean visitorCheckInError) {
        VisitorCheckInError = visitorCheckInError;
    }

    public String getVisitorsId() {
        return VisitorsId;
    }

    public void setVisitorsId(String visitorsId) {
        VisitorsId = visitorsId;
    }

    public boolean isVisitorsCheckOutSuccess() {
        return VisitorsCheckOutSuccess;
    }

    public void setVisitorsCheckOutSuccess(boolean visitorsCheckOutSuccess) {
        VisitorsCheckOutSuccess = visitorsCheckOutSuccess;
    }

    public boolean isVisitorsCheckOutFailure() {
        return VisitorsCheckOutFailure;
    }

    public void setVisitorsCheckOutFailure(boolean visitorsCheckOutFailure) {
        VisitorsCheckOutFailure = visitorsCheckOutFailure;
    }

    public boolean isVisitorsCheckOutDone() {
        return VisitorsCheckOutDone;
    }

    public void setVisitorsCheckOutDone(boolean visitorsCheckOutDone) {
        VisitorsCheckOutDone = visitorsCheckOutDone;
    }

    public String getGuardID() {
        return GuardID;
    }

    public void setGuardID(String guardID) {
        GuardID = guardID;
    }

    public String getVisitor_ID() {
        return Visitor_ID;
    }

    public void setVisitor_ID(String visitor_ID) {
        Visitor_ID = visitor_ID;
    }

    public String getVisitors_Name() {
        return Visitors_Name;
    }

    public void setVisitors_Name(String visitors_Name) {
        Visitors_Name = visitors_Name;
    }

    public String getVisitors_Email() {
        return Visitors_Email;
    }

    public void setVisitors_Email(String visitors_Email) {
        Visitors_Email = visitors_Email;
    }

    public String getVisitors_Mobile() {
        return Visitors_Mobile;
    }

    public void setVisitors_Mobile(String visitors_Mobile) {
        Visitors_Mobile = visitors_Mobile;
    }

    public String getVisitors_Address() {
        return Visitors_Address;
    }

    public void setVisitors_Address(String visitors_Address) {
        Visitors_Address = visitors_Address;
    }

    public String getVisitors_tomeet() {
        return Visitors_tomeet;
    }

    public void setVisitors_tomeet(String visitors_tomeet) {
        Visitors_tomeet = visitors_tomeet;
    }

    public String getVisitors_VehicleNo() {
        return Visitors_VehicleNo;
    }

    public void setVisitors_VehicleNo(String visitors_VehicleNo) {
        Visitors_VehicleNo = visitors_VehicleNo;
    }

    public String getVisitors_BarCode() {
        return Visitors_BarCode;
    }

    public void setVisitors_BarCode(String visitors_BarCode) {
        Visitors_BarCode = visitors_BarCode;
    }

    public String getVisitors_Photo() {
        return Visitors_Photo;
    }

    public void setVisitors_Photo(String visitors_Photo) {
        Visitors_Photo = visitors_Photo;
    }

    public String getVisitors_CheckInTime() {
        return Visitors_CheckInTime;
    }

    public void setVisitors_CheckInTime(String visitors_CheckInTime) {
        Visitors_CheckInTime = visitors_CheckInTime;
    }

    public String getVisitors_CheckOutTime() {
        return Visitors_CheckOutTime;
    }

    public void setVisitors_CheckOutTime(String visitors_CheckOutTime) {
        Visitors_CheckOutTime = visitors_CheckOutTime;
    }

    public String getVisitors_CheckInBy() {
        return Visitors_CheckInBy;
    }

    public void setVisitors_CheckInBy(String visitors_CheckInBy) {
        Visitors_CheckInBy = visitors_CheckInBy;
    }

    public BluetoothSocket getSocket() {
        return Socket;
    }

    public void setSocket(BluetoothSocket socket) {
        Socket = socket;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public boolean isVisitorImageUpload() {
        return VisitorImageUpload;
    }

    public void setVisitorImageUpload(boolean visitorImageUpload) {
        VisitorImageUpload = visitorImageUpload;
    }

    public boolean isMobileAutoSuggestSuccess() {
        return MobileAutoSuggestSuccess;
    }

    public void setMobileAutoSuggestSuccess(boolean mobileAutoSuggestSuccess) {
        MobileAutoSuggestSuccess = mobileAutoSuggestSuccess;
    }

    public boolean isMobileAutoSuggestFailure() {
        return MobileAutoSuggestFailure;
    }

    public void setMobileAutoSuggestFailure(boolean mobileAutoSuggestFailure) {
        MobileAutoSuggestFailure = mobileAutoSuggestFailure;
    }

    public String getOverNightStay_Time() {
        return OverNightStay_Time;
    }

    public void setOverNightStay_Time(String overNightStay_Time) {
        OverNightStay_Time = overNightStay_Time;
    }

    public boolean isMobileNoExist() {
        return MobileNoExist;
    }

    public void setMobileNoExist(boolean mobileNoExist) {
        MobileNoExist = mobileNoExist;
    }

    public boolean isNoVisitorsFound() {
        return NoVisitorsFound;
    }

    public void setNoVisitorsFound(boolean noVisitorsFound) {
        NoVisitorsFound = noVisitorsFound;
    }

    public boolean isVisitorsFound() {
        return VisitorsFound;
    }

    public void setVisitorsFound(boolean visitorsFound) {
        VisitorsFound = visitorsFound;
    }

    public String getCheck_in_User() {
        return Check_in_User;
    }

    public void setCheck_in_User(String check_in_User) {
        Check_in_User = check_in_User;
    }

    public String getCheck_out_User() {
        return Check_out_User;
    }

    public void setCheck_out_User(String check_out_User) {
        Check_out_User = check_out_User;
    }

    public static ArrayList<String> getArrayFields() {
        return arrayFields;
    }

    public static void setArrayFields(ArrayList<String> arrayFields) {
        DetailsValue.arrayFields = arrayFields;
    }

    public boolean isPrinterOrderSuccess() {
        return PrinterOrderSuccess;
    }

    public void setPrinterOrderSuccess(boolean printerOrderSuccess) {
        PrinterOrderSuccess = printerOrderSuccess;
    }

    public boolean isPrinterOrderNoData() {
        return PrinterOrderNoData;
    }

    public void setPrinterOrderNoData(boolean printerOrderNoData) {
        PrinterOrderNoData = printerOrderNoData;
    }

    public String getVisitor_Designation() {
        return Visitor_Designation;
    }

    public void setVisitor_Designation(String visitor_Designation) {
        Visitor_Designation = visitor_Designation;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getHouse_number() {
        return House_number;
    }

    public void setHouse_number(String house_number) {
        House_number = house_number;
    }

    public String getFlat_number() {
        return Flat_number;
    }

    public void setFlat_number(String flat_number) {
        Flat_number = flat_number;
    }

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getNo_Visitor() {
        return No_Visitor;
    }

    public void setNo_Visitor(String no_Visitor) {
        No_Visitor = no_Visitor;
    }

    public String getaClass() {
        return aClass;
    }

    public void setaClass(String aClass) {
        this.aClass = aClass;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getID_Card() {
        return ID_Card;
    }

    public void setID_Card(String ID_Card) {
        this.ID_Card = ID_Card;
    }

    public boolean isPermissionSuccess() {
        return PermissionSuccess;
    }

    public void setPermissionSuccess(boolean permissionSuccess) {
        PermissionSuccess = permissionSuccess;
    }

    public boolean isPermissionFailure() {
        return PermissionFailure;
    }

    public void setPermissionFailure(boolean permissionFailure) {
        PermissionFailure = permissionFailure;
    }

    public String getOTPAccess() {
        return OTPAccess;
    }

    public void setOTPAccess(String OTPAccess) {
        this.OTPAccess = OTPAccess;
    }

    public String getImageAccess() {
        return ImageAccess;
    }

    public void setImageAccess(String imageAccess) {
        ImageAccess = imageAccess;
    }

    public String getPrintertype() {
        return Printertype;
    }

    public void setPrintertype(String printertype) {
        Printertype = printertype;
    }

    public String getScannertype() {
        return Scannertype;
    }

    public void setScannertype(String scannertype) {
        Scannertype = scannertype;
    }

    public String getRfidStatus() {
        return RfidStatus;
    }

    public void setRfidStatus(String rfidStatus) {
        RfidStatus = rfidStatus;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
    }

    public String getCameratype() {
        return Cameratype;
    }

    public void setCameratype(String cameratype) {
        Cameratype = cameratype;
    }

    public String getApkfile() {
        return Apkfile;
    }

    public void setApkfile(String apkfile) {
        Apkfile = apkfile;
    }

    public String getApkdownloadUrl() {
        return ApkdownloadUrl;
    }

    public void setApkdownloadUrl(String apkdownloadUrl) {
        ApkdownloadUrl = apkdownloadUrl;
    }

    public boolean isApkfilexist() {
        return Apkfilexist;
    }

    public void setApkfilexist(boolean apkfilexist) {
        Apkfilexist = apkfilexist;
    }

    public boolean isGotTime() {
        return GotTime;
    }

    public void setGotTime(boolean gotTime) {
        GotTime = gotTime;
    }

    public boolean isNoTime() {
        return NoTime;
    }

    public void setNoTime(boolean noTime) {
        NoTime = noTime;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public String getID_Card_Type() {
        return ID_Card_Type;
    }

    public void setID_Card_Type(String ID_Card_Type) {
        this.ID_Card_Type = ID_Card_Type;
    }

    public boolean isSmartIn() {
        return SmartIn;
    }

    public void setSmartIn(boolean smartIn) {
        SmartIn = smartIn;
    }

    public boolean isSmartOut() {
        return SmartOut;
    }

    public void setSmartOut(boolean smartOut) {
        SmartOut = smartOut;
    }

    public boolean isSmartError() {
        return SmartError;
    }

    public void setSmartError(boolean smartError) {
        SmartError = smartError;
    }

    public boolean isAppointmentsFound() {
        return AppointmentsFound;
    }

    public void setAppointmentsFound(boolean appointmentsFound) {
        AppointmentsFound = appointmentsFound;
    }

    public boolean isAppointmentsNotFound() {
        return AppointmentsNotFound;
    }

    public void setAppointmentsNotFound(boolean appointmentsNotFound) {
        AppointmentsNotFound = appointmentsNotFound;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getAppointmentStaffName() {
        return AppointmentStaffName;
    }

    public void setAppointmentStaffName(String appointmentStaffName) {
        AppointmentStaffName = appointmentStaffName;
    }


}
