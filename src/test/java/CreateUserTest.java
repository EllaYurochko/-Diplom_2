import client.UserClient;
import pojo.UserRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private UserClient userClient;
    private String accessToken;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void createUserTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        response = userClient.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Регистрация нового пользователя без поля email")
    public void createUserWithoutEmailFieldTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequestWithoutEmailField();
        response = userClient.create(userRequest);
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Регистрация нового пользователя без поля имя")
    public void createUserWithoutNameFieldTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequestWithoutNameField();
        response = userClient.create(userRequest);
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Регистрация нового пользователя без поля пароль")
    public void createUserWithoutPasswordFieldTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequestWithoutPasswordField();
        response = userClient.create(userRequest);
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Регистрация пользователя который уже существует")
    public void createUserNotUniqueTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        response = userClient.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        userClient.create(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken)
                    .assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }
}
