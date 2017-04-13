package com.ambientbytes.ocoldemo.models;

/**
 * Model of a robot.
 * @author Pavel Karpenko
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
