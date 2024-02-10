package pojo;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderForm{

    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

   public OrderForm (List<String> ingredients) {
    this.ingredients = ingredients;
   }

    public OrderForm(){};

    @Step("Создаем ингридиент с рандомным значением в 10 знаков")
    public static String randomIngredient() {
        return RandomStringUtils.random(10);
    }

    @Step("Создаем список из 1 рандомного ингредиента")
    public static List<String> randomIngredientList() {
        String randomIng = RandomStringUtils.random(10);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(randomIng);
        return ingredients;
    }

}
