package pojo;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationBodyForm {

    private String email;
    private String password;
    private String name;
    private String authorizationBearer;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorizationBearer() {
        return authorizationBearer;
    }

    public void setAuthorizationBearer(String authorizationBearer) {
        this.authorizationBearer = authorizationBearer;
    }


    public RegistrationBodyForm(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public RegistrationBodyForm(String password, String email) {
        this.email = email;
        this.password = password;
    }

    public RegistrationBodyForm (String email) {
        this.email = email;
    }


    public RegistrationBodyForm() {}

    @Step("Создаем рандомный пароль из 5 цифр")
    public static String randomPassword() {
        return RandomStringUtils.randomNumeric(5);
    }

    @Step("Создаем рандомное имя из 6 букв")
    public static String randomName() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    @Step("Создаем рандомный email из 5 букв с доменом @yandex.ru")
    public static String randomEmail() {
        return RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
    }

    @Step("Создаем пользователя с рандомным именем, паролем и email")
    public static  RegistrationBodyForm randomAccount() {
        RegistrationBodyForm requestBody = new RegistrationBodyForm();
        requestBody.setName(RegistrationBodyForm.randomName());
        requestBody.setPassword(RegistrationBodyForm.randomPassword());
        requestBody.setEmail(RegistrationBodyForm.randomEmail());
        return requestBody;
    }



}
