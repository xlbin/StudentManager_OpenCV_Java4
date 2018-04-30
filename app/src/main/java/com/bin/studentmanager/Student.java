package com.bin.studentmanager;

import java.io.Serializable;

/**
 * Created by xiaolang on 2018/2/16.
 */

public class Student implements Serializable {
    private long id;
    private String name;
    private int age;
    private String sex;
    private String like;
    private String phoneNumber;
    private String trainDate;
    private String modifyDateTime;
    private byte[] faceInfo;
    public Student() {
        super();
    }
    public Student(long id, String name, int age, String sex, String like, String phoneNumber,
                   String trainDate, String modifyDateTime, byte[] faceInfo) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.like = like;
        this.phoneNumber = phoneNumber;
        this.trainDate = trainDate;
        this.modifyDateTime = modifyDateTime;
        this.faceInfo = faceInfo;
    }
    public Student(String name, int age, String sex, String like, String phoneNumber,
                   String trainDate, String modifyDateTime, byte[] faceInfo) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.like = like;
        this.phoneNumber = phoneNumber;
        this.trainDate = trainDate;
        this.modifyDateTime = modifyDateTime;
        this.faceInfo = faceInfo;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return sex;     }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getLike() {
        return like;
    }
    public void setLike(String like) {
        this.like = like;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;     }
    public String getTrainDate() {
        return trainDate;
    }
    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }
    public String getModifyDateTime() {
        return modifyDateTime;
    }
    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public byte[] getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(byte[] faceInfo) {
        this.faceInfo = faceInfo;
    }
}