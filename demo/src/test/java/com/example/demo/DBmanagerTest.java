package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;


@SpringBootTest
public class DBmanagerTest {
    @Autowired
    private DBmanage dbmanage;

    @Test
    @Transactional
    public void createUserTest(){
        boolean result = dbmanage.createUser("Taro", "aaa");
        assertThat(result).isFalse();
    }
}
