package io.student.rangiffler.tests;

import com.github.javafaker.Faker;
import io.student.rangiffler.config.Config;

public class BaseWebTest {

    protected static final Faker FAKER = new Faker();
    protected static final Config CFG = Config.getInstance();

}
