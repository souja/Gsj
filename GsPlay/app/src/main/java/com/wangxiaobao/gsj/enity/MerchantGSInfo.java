package com.wangxiaobao.gsj.enity;


import java.util.ArrayList;

public class MerchantGSInfo extends BaseModel {

    public String merchantName;
    public String ptName;
    public String idno;
    public WfxwcjInfo wfxwcjInfo;
    public JlInfo jlInfo;
    public LrhmdInfo lrhmdInfo;

    public class WfxwcjInfo extends BaseModel {
        public ArrayList<XzcfList> xzcfList;
        public ArrayList<QtjstsxxList> qtjstsxxList;
    }

    public class XzcfList extends BaseModel {
        public String register_no;
        public String pt_name;
        public String inst_code;
        public String reg_no;
        public String illegal_type;
        public String writ_no;
        public String illegal_reason;
        public String penalize_content;
        public String organ_name;
        public long penalize_date;
        public String condition_type;
        public String uncondition_type;
        public String administrative_review;
        public String administrative_proceeding;
        public String provide_unit;
        public String provide_time;
    }

    public class QtjstsxxList extends BaseModel {
        public String content;
    }

    public class JlInfo extends BaseModel {
        public ArrayList<Honor> honorList;
    }

    public class Honor extends BaseModel {
        public String register_no;
        public String pt_name;
        public String reg_no;
        public String inst_code;
        public String year;
        public String honor_name;
        public String certificate_no;
        public String mete_untis;
        public long mete_date;
        public String honor_grade;
        public String provide_unit;
        public String provide_time;
    }

    public class LrhmdInfo extends BaseModel {
        public ArrayList<ExceptionList> exceptionList;
        public ArrayList<IlldisList> illdisList;
        public ArrayList<Aqhmd> aqhmdList;
    }

    public class Aqhmd extends BaseModel {
        public  String malfeasanse;
    }

    public class ExceptionList extends BaseModel {
        public String uniscid;
        public String specause_cn;
        public String abntime;
        public String decorg_cn;
        public String entname;
        public String remdate;
        public String redecorg_cn;
        public String remexcpres_cn;
    }

    public class IlldisList extends BaseModel {
        public String uniscid;
        public String regno;
        public String abntime;
        public String decorgcn;
        public String recorgcn;
        public String remexcprescn;
        public String entname;
        public String remdate;
        public String serillreacn;

    }
}
