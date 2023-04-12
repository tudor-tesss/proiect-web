package com.idis.core.domain.test;

import com.nimblej.core.BaseObject;

import java.util.Date;

public class TestObj extends BaseObject {
    private String name;
    private int age;
    private Date dateOfBirth;

    public TestObj() { }

    public TestObj(String name, int age, Date dateOfBirth) {
        this.name = name;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
    }

    public static TestObj create(String name, int age, Date dateOfBirth) {
        return new TestObj(name, age, dateOfBirth);
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
