import client.OrderClient;
import client.UserClient;
import pojo.OrderRequest;
import pojo.UserRequest;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private UserRequest userRequest;
    private OrderRequest orderRequest;

    private List<String> ingredientsList;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        userRequest = UserRequest.getRandomUserRequest();
        accessToken = userClient.create(userRequest).extract().path("accessToken");
        ingredientsList = orderClient.getAllIngredients().extract().path("data._id");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    public void createOrderWithAuthorizationAndWithIngredientsTest() {
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(ingredientsList));
        orderClient.createOrderWithAuthorization(orderRequest, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthorizationAndWithoutIngredientsTest() {
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(null));;
        orderClient.createOrderWithAuthorization(orderRequest, accessToken)
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }

    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    public void createOrderWithoutAuthorizationAndWithIngredientsTest() {
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(ingredientsList));
        orderClient.createOrderWithoutAuthorization(orderRequest)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthorizationAndWithoutIngredientsTest() {
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(null));
        orderClient.createOrderWithoutAuthorization(orderRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хешем ингредиентов")
    public void createOrderWithInvalidHashIngredientsTest() {
        List<String> newIngredients = List.of("hgjfyr6584vchdye647ecx", "hgjfyr6594vchdye647ecx");
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(newIngredients));
        orderClient.createOrderWithAuthorization(orderRequest, accessToken)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken).assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }
}