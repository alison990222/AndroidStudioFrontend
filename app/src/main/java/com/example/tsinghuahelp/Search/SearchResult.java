package com.example.tsinghuahelp.Search;

public class SearchResult {

    private String title;
    private String teacherOrStudent;
    private String department;
    private String descriptionOrPersonalInfo;
    private String type;
    private int id;

    public SearchResult(String title, String teacherOrStudent, String department, String descriptionOrPersonalInfo,String type,int id) {
        this.title = title;
        this.teacherOrStudent=teacherOrStudent;
        this.department = department;
        this.descriptionOrPersonalInfo = descriptionOrPersonalInfo;
        this.type = type;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getTeacherOrStudent() {
        return teacherOrStudent;
    }

    public String getDepartment() {
        if(department==null||department.length()<1){return "未验证";}
        return department;
    }

    public String getDescriptionOrPersonalInfo() {
        if(descriptionOrPersonalInfo==null||descriptionOrPersonalInfo.length()<1){return "未填写";}
        return descriptionOrPersonalInfo;
    }


}