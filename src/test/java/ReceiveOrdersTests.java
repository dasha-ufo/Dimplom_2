import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderForm;
import pojo.RegistrationBodyForm;

import java.util.List;

public class ReceiveOrdersTests {
    private IngredientsForOrder ingredientsForOrder;
    private RegisterAccount registerAccount;
    private OrderCreate orderCreate;
    private ReceiveOrders receiveOrders;
    @Before
    public void setUp() {
        ingredientsForOrder = new IngredientsForOrder();
        registerAccount = new RegisterAccount();
        orderCreate = new OrderCreate();
        receiveOrders = new ReceiveOrders();

    }

    @Test
    @DisplayName("Получаем заказы пользователя с авторизацией")
    @Description("Проверяем успешный ответ на запрос заказов и наличие обязательных полей")
    public void orderReceiveSuccess() {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        Response registerResponse = registerAccount.create(requestBody);
        String accessToken = RegisterAccount.receiveAccessToken(registerResponse);

        Response ingredientsResponse = ingredientsForOrder.receiveIngredients();
        List<String> fullIngredientsList = IngredientsForOrder.receiveIngredientsList(ingredientsResponse);
        List<String> orderIngredientList = OrderCreate.createOrderIngredientList(fullIngredientsList);

        OrderForm orderForm = new OrderForm(orderIngredientList);
        orderCreate.createOrderAuth(orderForm, accessToken);

        Response receiveOrderResponse = receiveOrders.ordersAuthRequest(accessToken);
        receiveOrders.checkSuccessReceiveOrder(receiveOrderResponse);

        UpdateAccount.deleteUserRequestWithToken(registerResponse);
    }

    @Test
    @DisplayName("Пытаемся получить заказы без авторизации")
    @Description("Проверяем возврат ошибки на запрос заказов без авторизации")
    public void orderReceiveError() {
        Response receiveOrderResponse = receiveOrders.ordersNotAuthRequest();
        receiveOrders.checkRequestOrderError401(receiveOrderResponse);
    }

}
