package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.UpdateInfoForm;

import static config.RestConfig.BASE_URI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateAccount extends RestClient {

    private String accessToken;
    public static final String ACCOUNT_INFO_UPDATE = "/api/auth/user";


    @Step("Отправка запроса для получения или изменения информации о пользователе /api/auth/user")
    public Response updateAuthRequest (UpdateInfoForm updateBodyForm, String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(updateBodyForm)
                .when()
                .patch(ACCOUNT_INFO_UPDATE);
    }

    @Step("Отправка запроса на удаление пользователя")
    public Response deleteUserRequest(UpdateInfoForm updateBodyForm, String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete(ACCOUNT_INFO_UPDATE);
    }

    @Step("Удаляем пользователя и проверяем успешный ответ 202")
    public static void deleteUserRequestWithToken(Response apiResponse) {
        String accessToken = apiResponse.then()
                .extract()
                .jsonPath()
                .getString("accessToken");
        Response deleteResponse = given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete(ACCOUNT_INFO_UPDATE);
        UpdateAccount.checkSuccessDelete(deleteResponse);
    }

    @Step("Отправка запроса для получения или изменения информации о пользователе /api/auth/user")
    public Response updateNotAuthRequest (UpdateInfoForm updateBodyForm) {
        return getDefaultRequestSpecification()
                .body(updateBodyForm)
                .when()
                .patch(ACCOUNT_INFO_UPDATE);
    }

    @Step("Проверка, что статус ответа на изменения 200 и тело ответа содержит измененные поля")
    public static void checkSuccessUpdate(Response apiResponse, String email, String name){
        apiResponse.then().body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .statusCode(200);
    }

    @Step("Проверка успешного ответа об удалении со статусом 202")
    public static void checkSuccessDelete(Response apiResponse){
        apiResponse.then().body("success", equalTo(true))
                .statusCode(202);
    }

    @Step("Проверка, что статус ответа без авторизации 401 и тело ответа содержит поле с ошибкой")
    public static void checkUpdateError401(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

}
