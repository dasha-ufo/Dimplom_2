import client.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderForm;
import pojo.RegistrationBodyForm;

import java.util.List;

public class OrderCreateTests {
    private IngredientsForOrder ingredientsForOrder;
    private RegisterAccount registerAccount;
    private OrderCreate orderCreate;
    @Before
    public void setUp() {
        ingredientsForOrder = new IngredientsForOrder();
        registerAccount = new RegisterAccount();
        orderCreate = new OrderCreate();

    }

    @Test
    @DisplayName("Успешно содздаем заказ с авторизацией")
    @Description("Создаем заказ с двумя ингредиентами под авторизованным пользователем")
    public void orderSuccessCreateAuth () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        Response registerResponse = registerAccount.create(requestBody);
        String accessToken = RegisterAccount.receiveAccessToken(registerResponse);

        Response ingredientsResponse = ingredientsForOrder.receiveIngredients();
        List<String> fullIngredientsList = IngredientsForOrder.receiveIngredientsList(ingredientsResponse);
        List<String> orderIngredientList = OrderCreate.createOrderIngredientList(fullIngredientsList);

        OrderForm orderForm = new OrderForm(orderIngredientList);
        Response responseOrderCreate = orderCreate.createOrderAuth(orderForm, accessToken);
        orderCreate.checkSuccessCreateOrder(responseOrderCreate);

        UpdateAccount.deleteUserRequestWithToken(registerResponse);
    }

    @Test
    @DisplayName("Cодздаем заказ без авторизации")
    @Description("Проверяем возврат ошибки на создание заказа с двумя ингредиентами без токена пользователя")
    public void orderErrorCreateNotAuth () {
        Response ingredientsResponse = ingredientsForOrder.receiveIngredients();
        List<String> fullIngredientsList = IngredientsForOrder.receiveIngredientsList(ingredientsResponse);
        List<String> orderIngredientList = OrderCreate.createOrderIngredientList(fullIngredientsList);

        OrderForm orderForm = new OrderForm(orderIngredientList);
        Response responseOrderCreate = orderCreate.createOrderNotAuth(orderForm);
        OrderCreate.checkOrderCreateError401(responseOrderCreate);
    }

    @Test
    @DisplayName("Создаем заказ без ингредиентов с авторизацией")
    @Description("Проверяем возврат ошибки на создание заказа с пустым списком ингредиентов")
    public void orderErrorEmptyOrderAuth() {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        Response registerResponse = registerAccount.create(requestBody);
        String accessToken = RegisterAccount.receiveAccessToken(registerResponse);

        OrderForm orderForm = new OrderForm();
        Response responseOrderCreate = orderCreate.createOrderAuth(orderForm, accessToken);
        OrderCreate.checkOrderCreateError400(responseOrderCreate);

        UpdateAccount.deleteUserRequestWithToken(registerResponse);
    }

    @Test
    @DisplayName("Создаем заказ без ингредиентов без авторизации")
    @Description("Проверяем возврат ошибки на создание заказа с пустым списком ингредиентов")
    public void orderErrorEmptyOrderNotAuth() {
        OrderForm orderForm = new OrderForm();
        Response responseOrderCreate = orderCreate.createOrderNotAuth(orderForm);
        OrderCreate.checkOrderCreateError400(responseOrderCreate);
    }

    @Test
    @DisplayName("Создаем заказ с неверными хешем ингредиентов и авторизацией")
    @Description("Проверяем возврат ошибки на создание заказа с неправильным списком ингредиентов")
    public void orderErrorCreateWrongIngredAuth () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        Response registerResponse = registerAccount.create(requestBody);
        String accessToken = RegisterAccount.receiveAccessToken(registerResponse);

        List<String> orderIngredientList = OrderForm.randomIngredientList();

        OrderForm orderForm = new OrderForm(orderIngredientList);
        Response responseOrderCreate = orderCreate.createOrderAuth(orderForm, accessToken);
        orderCreate.checkOrderCreateError500(responseOrderCreate);

        UpdateAccount.deleteUserRequestWithToken(registerResponse);
    }

}
