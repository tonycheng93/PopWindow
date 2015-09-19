package com.tony.popwindow.entity;

/**
 * Created by tonycheng on 2015/9/19.
 */
public class SchoolList {
    private String school_id;
    private String school_name;
    private String school_pro_id;
    private String school_schooltype_id;

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_schooltype_id() {
        return school_schooltype_id;
    }

    public void setSchool_schooltype_id(String school_schooltype_id) {
        this.school_schooltype_id = school_schooltype_id;
    }

    public String getSchool_pro_id() {
        return school_pro_id;
    }

    public void setSchool_pro_id(String school_pro_id) {
        this.school_pro_id = school_pro_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }
}
