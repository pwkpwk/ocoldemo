package com.ambientbytes.ocoldemo.models;

import java.util.Random;

/**
 * Created by pakarpen on 3/29/17.
 */

public final class HumanModel implements IModel {

    private final static String[] firstNames = { "John", "Peter", "Wolfgang", "Beatrice", "Eugene", "Martha", "Carl", "Jessica", "Jennifer" };
    private final static String[] lastNames = { "Jacobs", "MacMuffins", "Allen", "Smith", "Zaff", "Tuckman" };

    private final Random random;
    private int age;
    private int firstName;
    private int lastName;

    public HumanModel(int age, Random random) {
        this.random = random;
        this.age = age;
        this.firstName = random.nextInt(firstNames.length);
        this.lastName = random.nextInt(lastNames.length);
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getName() {
        return firstNames[firstName] + ' ' + lastNames[lastName];
    }
}
