package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.RegistrationBodyForm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class RegisterAccount extends RestClient {
    public static final String ACCOUNT_REGISTER = "/api/auth/register";

    @Step("Отправка запроса для регистрации пользователя /api/auth/register с заполненным телом")
    public Response create (RegistrationBodyForm registrationBodyForm) {
        return getDefaultRequestSpecification()
                .body(registrationBodyForm)
                .when()
                .post(ACCOUNT_REGISTER);
    }

    @Step("Проверка, что статус ответа на регистрацию 200 и тело ответа содержит заполненные обязательные поля")
    public void checkSuccessResponseRegistration(Response apiResponse){
        apiResponse.then().body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue())
                .body("accessToken", startsWith("Bearer"))
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Проверяем ошибку 403 на создание существующего пользователя")
    public void checkErrorExistedUser(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo( "User already exists"))
                .statusCode(403);
    }

    @Step("Проверяем ошибку 403 на создание пользователя с незаполненным полем")
    public void checkErrorEmptyFieldRegistr(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo( "Email, password and name are required fields"))
                .statusCode(403);
    }

    @Step("Вытащить из ответа accessToken")
    public static String receiveAccessToken (Response apiResponse) {
        return apiResponse.then()
                .extract()
                .jsonPath()
                .getString("accessToken");
    }

}
