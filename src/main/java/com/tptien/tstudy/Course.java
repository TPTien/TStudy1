package com.tptien.tstudy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("idcourse")
    @Expose
    private String idCourse;
    @SerializedName("coursename")
    @Expose
    private String courseName;
    @SerializedName("topic")
    @Expose
    private String topicCourse;
    @SerializedName("nummember")
    @Expose
    private String numberMember;
    @SerializedName("username")
    @Expose
    private String nameHostCourse;
    @SerializedName("numVoca")
    @Expose
    private String numberVoca;
    @SerializedName("datecreate")
    @Expose
    private String dateCreate;
    @SerializedName("idhost")
    @Expose
    private String idHost;
    public Course(String courseName,  String topicCourse, String numberMember,String nameHostCourse, String numberVoca,String dateCreate,String idHost) {
        this.courseName = courseName;
        this.topicCourse = topicCourse;
        this.numberMember = numberMember;
        this.nameHostCourse=nameHostCourse;
        this.numberVoca =numberVoca;
        this.dateCreate =dateCreate;
        this.idHost=idHost;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getIdHost() {
        return idHost;
    }

    public void setIdHost(String idHost) {
        this.idHost = idHost;
    }

    public String getTopicCourse() {
        return topicCourse;
    }

    public void setTopicCourse(String topicCourse) {
        this.topicCourse = topicCourse;
    }

    public String getNumberMember() {
        return numberMember;
    }

    public void setNumberMember(String numberMember) {
        this.numberMember = numberMember;
    }

    public String getNameHostCourse() {
        return nameHostCourse;
    }

    public void setNameHostCourse(String nameHostCourse) {
        this.nameHostCourse = nameHostCourse;
    }

    public String getNumberVoca() {
        return numberVoca;
    }

    public void setNumberVoca(String numberVoca) {
        this.numberVoca = numberVoca;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

}
