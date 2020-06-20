package com.example.tsinghuahelp.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posts {

    private String teacher;
    private String title;
    private String researchDirection;
    private String department;
    private String description;
    private String requirement;
    private String tchID;
    private int ID;

    public Posts(String teacher, String title, String researchDirection, String department, String description, String requirement, int id, String tchID) {
        this.teacher = teacher;
        this.title = title;
        this.researchDirection = researchDirection;
        this.department = department;
        this.description = description;
        this.requirement = requirement;
        this.ID = id;
        this.tchID = tchID;

    }


    public String getTeacher() {
        return teacher;
    }

    public String getTitle() {
        return title;
    }

    public String getResearchDirection() {
        return researchDirection;
    }

    public String getDepartment() {
        return department;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirement() {
        return requirement;
    }

    public int getID() { return ID;}

    public String getTchID() {return tchID; }

}

