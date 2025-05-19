package client;

import pojo.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    public static final String ORDERS = "api/orders";
    public static final String INGREDIENTS = "api/ingredients";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuthorization(OrderRequest ingredients, String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post(ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(OrderRequest ingredients) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post(ORDERS)
                .then();
    }

    @Step("Получение заказов пользователя с авторизацией")
    public ValidatableResponse getOrdersUserWithAuthorization(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .get(ORDERS)
                .then();
    }

    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse getOrdersUserWithoutAuthorization() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(ORDERS)
                .then();
    }

    @Step("Получение списка всех ингредиентов")
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(INGREDIENTS)
                .then();
    }
}