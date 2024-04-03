package hoan.com.springboot.common.validator;

public interface ValidateConstraint {

    interface LENGTH {
        int CODE_MAX_LENGTH = 50;
        int GL_CODE_MAX_LENGTH = 50;
        int NAME_MAX_LENGTH = 100;
        int TITLE_MAX_LENGTH = 200;
        int DESC_MAX_LENGTH = 1000;
        int NOTE_MAX_LENGTH = 1000;
        int ENUM_MAX_LENGTH = 20;
        int ID_MIN_LENGTH = 1;
        int ID_MAX_LENGTH = 36;
        int PASSWORD_MIN_LENGTH = 3;
        int PASSWORD_MAX_LENGTH = 50;
        int CONTENT_MAX_LENGTH = 2000;
        int VALUE_MAX_LENGTH = 200;
        int PHONE_MAX_LENGTH = 20;
        int EMAIL_MAX_LENGTH = 50;
        int ADDRESS_MAX_LENGTH = 200;
        int ACCOUNT_NUMBER_MAX_LENGTH = 20;
        int COMPANY_NAME = 200;
        int ISSUED_UNIT = 200;
        int POSITION = 100;
        int COMMISSIONER = 100;
        int WEBSITE = 200;
        int IDENTIFY_CARD = 50;
        int BUILDING_NAME_MAX_LENGTH = 200;
        int INVOICE_ISSUING_EMAIL_MAX_LENGTH = 150;
        int KEYWORD_MAX_LENGTH = 200;
        int FILE_NAME = 300;
        int BUSINESS_PLAN_NUMBER_MAX_LENGTH = 100;
    }

    interface VALUE {
        int FLOOR_NUMBER_MAX = 200;
        int BASEMENT_NUMBER_MAX = 20;
        int BUILDING_AREA_MAX = 1000000;
        int INVOICE_ISSUING_EMAIL_MAX = 3;
    }

    interface FORMAT {
        String PHONE_NUMBER_PATTERN = "^(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?([0-9][0-9\\- \\.]+[0-9])$";
        String EMAIL_PATTERN = "^(\\s){0,}[a-zA-Z0-9-_\\.]{1,50}[a-zA-Z0-9]{1,50}@[a-zA-Z0-9_-]{2,}(\\.[a-zA-Z0-9]{2,4}){1,2}(\\s){0,}$";
        String CODE_PATTERN = "^[A-Za-z0-9_.]{4,50}$";
        String BUSINESS_CODE = "^[A-Za-z0-9-]{10,20}$";
        String WEBSITE = "((http|https)?:\\/\\/)?[-a-zA-Z0-9]{1,}\\.((\\-?[a-zA-Z0-9])+\\.)*([a-zA-Z0-9]+\\-?)*([a-zA-Z0-9]+)";
        String ACCOUNT_NUMBER = "^(\\d{3,20})$";
        String GL_CODE_PATTERN = "[A-Za-z0-9]{2,50}";
        String BUSINESS_PLAN_NUMBER_PATTERN = "\\S{1,100}";
    }
}
