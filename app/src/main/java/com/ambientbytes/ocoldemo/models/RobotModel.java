package com.ambientbytes.ocoldemo.models;

/**
 * Created by pakarpen on 3/29/17.
 */

public final class RobotModel implements IModel {

    private int age;
    private String name;

    public RobotModel(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getName() {
        return name;
    }
}
