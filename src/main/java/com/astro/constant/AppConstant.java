package com.astro.constant;

public class AppConstant {
    public static final int API_SUCCESS = 0;

    //Address code and type
    public static final String PERMANENT_ADDRESS_CODE = "A101";
    public static final String PERMANENT_ADDRESS_TYPE = "Permanent";
    public static final String PRESENT_ADDRESS_CODE = "A102";
    public static final String PRESENT_ADDRESS_TYPE = "Present";

    public static final String INDIVIDUAL_PAN_IDENTITY = "individualPan";
    public static final String BUSINESS_PAN_IDENTITY = "businessPan";
    public static final String AADHAAR_IDENTITY = "aadhaar";
    public static final String PASSPORT_IDENTITY = "passport";
    public static final String DL_IDENTITY = "drivingLicence";

    public static final String INCOMPLETE = "Incomplete";
    public static final String AWAITING_APPROVAL = "Awaiting Approval";
    public static final String APPROVED = "Approved";
    public static final String REJECTED = "Rejected";
    public static final String PENDING = "Pending";
    public static final String ACTIVE_LOAN = "Active Loan";
    public static final String CLOSED_LOAN = "Closed Loan";


    // Error Type Codes
    public static final int ERROR_TYPE_CODE_DB = 1;
    public static final int ERROR_TYPE_CODE_VALIDATION = 2;
    public static final int ERROR_TYPE_CODE_INTERNAL = 3;

    // Error Type
    public static final String ERROR_TYPE_ERROR = "error";
    public static final String ERROR_TYPE_VALIDATION = "validation";


    public static final int INTER_SERVER_ERROR = 1000;

    public static final int USER_NOT_FOUND = 1001;
    public static final int USER_ALREADY_EXISTS = 1002;
    public static final int USER_INVALID_INPUT = 1003;

    public static final int OFFER_NOT_FOUND = 2001;
    public static final int OFFER_ALREADY_EXISTS = 2002;
    public static final int OFFER_INVALID_INPUT = 2003;

    public static final int MODULE_NOT_FOUND = 3001;


    public static final int SEQUENCE_NOT_FOUND = 4001;

    public static final int FILE_NOT_FOUND = 6001;
    public static final int FILE_UPLOAD_ERROR = 6002;
    public static final int FILE_DOWNLOAD_ERROR = 6003;

    public static final int INVALID_BASE64 = 7001;
    public static final int MOBILE_NOT_VALID = 7002;
    public static final String AADHAAR = "AADHAAR";
    public static final String AADHAAR_FRONT = "AADHAAR_FRONT";
    public static final String AADHAAR_BACK = "AADHAAR_BACK";
    public static final String PAN = "PAN";
    public static final String PAN_FRONT = "PAN_FRONT";

    public static final int PSY_TEST_INVALID_INPUT = 7001;

    public static final int LOAN_NOT_FOUND = 8001;
    public static final int LOAN_INVALID_INPUT = 8002;
    public static final String DL = "DL";
    public static final String DL_FRONT = "DL_FRONT";

    public static final int EV_NOT_CALCULATED = 8003;

    public static final int PLAN_NOT_FOUND = 9001;
    public static final int PLAN_ALREADY_EXISTS = 9002;
    public static final int PLAN_INVALID_INPUT = 9003;

    public static final int SUB_NOT_FOUND = 10001;
    public static final int SUB_ALREADY_EXISTS = 10002;
    public static final int SUB_INVALID_INPUT = 10003;


    public static final String PRE_DISBURSEMENT_VRF_CODE = "A4";
    public static final String LOAN_FULLFILMENT_VRF_CODE = "A5";
    public static final String POST_DISBURSEMENT_VRF_CODE = "A6";
    public static final String DEALER_DOCUMENT_VRF_CODE = "A7";


    public static final int PAN_NOT_FOUND = 20001;
    public static final int PAN_VERIFICATION_ERROR = 20002;

    public static final int DL_NOT_FOUND = 30001;
    public static final int DL_VERIFICATION_ERROR = 30002;

    public static final int SIGNZY_LOGIN_ERROR = 40001;

    public static final int AADHAAR_NOT_FOUND = 50001;
    public static final int AADHAAR_VERIFICATION_ERROR = 50002;


    //Folder Path
}
