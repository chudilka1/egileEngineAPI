package com.agileengineapi.pages;

import com.codeborne.selenide.SelenideElement;

public class AbstractPage {

    private final static String EMAIL = "yepishevsanya@gmail.com",
            PASS = "1234567890";

    public void login(SelenideElement emailField, SelenideElement passField) {
        emailField.sendKeys(EMAIL);
        passField.sendKeys(PASS);
        passField.submit();
    }

}
