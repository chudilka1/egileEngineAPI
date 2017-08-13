package com.agileengineapi.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AuthorizationPage extends AbstractPage {

    private final String PHONE_NUMBER = "+380971844811";

    private static SelenideElement loginForm = $(By.id("oauth_form")),
            emailField = $(By.id("username_or_email")),
            passField = $(By.id("password")),
            phoneField = $(By.id("challenge_response")),
            authorizeButton = $(By.id("allow")),
            pinField = $("#oauth_pin>p>kbd>code");

    public AuthorizationPage loginTweeter() {

        loginForm.waitUntil(visible, 15000);

        login(emailField, passField);

        phoneField.sendKeys(PHONE_NUMBER);
        phoneField.submit();

        return this;
    }


    public String getOauthVerifier() {

        loginTweeter();

        authorizeButton.waitUntil(enabled, 10000).click();

        return pinField.waitUntil(visible, 10000).getText();
    }

}
