package com.test;

public class student {
    private String name;
    private String age;
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public student(String name, String age, String num) {
        this.name = name;
        this.age = age;
        this.num = num;

    }

    public student() {
    }
}

