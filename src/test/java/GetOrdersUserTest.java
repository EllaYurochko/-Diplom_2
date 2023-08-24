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
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersUserTest {
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
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersAuthorizationUserTest() {
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(ingredientsList));
        orderClient.createOrderWithAuthorization(orderRequest, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        orderClient.getOrdersUserWithAuthorization(accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersWithoutAuthorizationUserTest() {
        orderRequest = new OrderRequest(OrderRequest.getRandomIngredients(ingredientsList));
        orderClient.createOrderWithAuthorization(orderRequest, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        orderClient.getOrdersUserWithoutAuthorization()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
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
