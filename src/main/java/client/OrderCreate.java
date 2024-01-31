package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.OrderForm;
import java.util.List;

import static config.RestConfig.BASE_URI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreate extends RestClient {

    public static final String ORDER_CREATE = "/api/orders";


    @Step("Отправка запроса для создания заказа под авторизованным пользователем")
    public Response createOrderAuth (OrderForm orderForm, String accessToken) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(orderForm)
                .when()
                .post(ORDER_CREATE);
    }

    @Step("Отправка запроса для создания заказа под неавторизованным пользователем")
    public Response createOrderNotAuth (OrderForm orderForm) {
        return getDefaultRequestSpecification()
                .body(orderForm)
                .when()
                .post(ORDER_CREATE);
    }

    @Step("Создать список из 2х ингредиентов для заказа из общего списка")
    public static List<String> createOrderIngredientList(List<String> receiveIngredientsList) {
        return receiveIngredientsList.subList(0, Math.min(2, receiveIngredientsList.size()));
    }

    @Step("Проверка, что статус ответа на создание заказа 200 и тело содержит заполненные обязательные поля")
    public void checkSuccessCreateOrder(Response apiResponse){
        apiResponse.then().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Проверка, что статус ответа без авторизации 401 и тело ответа содержит поле с ошибкой")
    public static void checkOrderCreateError401(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

    @Step("Проверка, что статус ответа без id ингредиентов 402 и тело ответа содержит поле с ошибкой")
    public static void checkOrderCreateError400(Response apiResponse){
        apiResponse.then().body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"))
                .statusCode(400);
    }

    @Step("Проверяем ошибку 500 при заказе с невалидным хешем ингредиента")
    public void checkOrderCreateError500(Response apiResponse){
        apiResponse.then()
                .statusCode(500);
    }

}
