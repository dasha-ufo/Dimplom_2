package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static config.RestConfig.BASE_URI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class ReceiveOrders extends RestClient {

    public static final String RECEIVE_ORDERS = "/api/orders";

    @Step("Отправка запроса для получения заказов авторизованного пользователя")
    public Response ordersAuthRequest (String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(RECEIVE_ORDERS);
    }

    @Step("Отправка запроса для получения заказов неавторизованного пользователя")
    public Response ordersNotAuthRequest () {
        return getDefaultRequestSpecification()
                .when()
                .get(RECEIVE_ORDERS);
    }

    @Step("Проверка, что статус ответа на запрос заказов 200 и тело ответа содержит обязательные поля")
    public void checkSuccessReceiveOrder(Response apiResponse){
        apiResponse.then()
                .body("orders.size()", greaterThan(0))
                .body("orders[0].ingredients.size()", greaterThan(0))
                .body("orders[0]._id", notNullValue())
                .body("orders[0].status", notNullValue())
                .body("orders[0].number", notNullValue())
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue())
                .statusCode(200);
    }

    @Step("Проверка, что статус ответа без авторизации 401 и тело ответа содержит поле с ошибкой")
    public void checkRequestOrderError401(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

}
