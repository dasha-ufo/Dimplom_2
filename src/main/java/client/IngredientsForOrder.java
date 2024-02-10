package client;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;

public class IngredientsForOrder extends RestClient {
    public static final String INGREDIENTS_LIST = "/api/ingredients";
    private List<IngredientsForOrder> ingredientsForOrders;

    @Step("Отправка запроса на список доступных ингридиентов для заказа")
    public Response receiveIngredients () {
        return getDefaultRequestSpecification()
                .when()
                .get(INGREDIENTS_LIST);
    }

    @Step("Вытащить из ответа id ингредиентов в виде списка")
    public static List<String> receiveIngredientsList(Response apiResponse) {
        JsonPath jsonPath = apiResponse
                            .then()
                            .extract()
                            .jsonPath();
        return jsonPath.getList("data._id");
    }

}
