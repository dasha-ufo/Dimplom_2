package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.RegistrationBodyForm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class LoginAccount extends RestClient {
    public static final String ACCOUNT_LOGIN = "/api/auth/login";

    @Step("Отправка запроса для авторизации /api/auth/login с заполненным телом")
    public Response create (RegistrationBodyForm registrationBodyForm) {
        return getDefaultRequestSpecification()
                .body(registrationBodyForm)
                .when()
                .post(ACCOUNT_LOGIN);
    }

    @Step("Проверка, что статус ответа на авторизацию 200 и тело ответа содержит заполненные обязательные поля")
    public void checkSuccessResponseLogin(Response apiResponse){
        apiResponse.then().body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue())
                .body("accessToken", startsWith("Bearer"))
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(200);
    }
    @Step("Проверяем ошибку 401 при авторизации с неверным логином или паролем")
    public void checkWrongEmailOrPassword(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo( "email or password are incorrect"))
                .statusCode(401);
    }

}
