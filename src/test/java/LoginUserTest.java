import client.UserClient;
import pojo.LoginRequest;
import pojo.UserRequest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private UserClient userSteps;
    private String accessToken;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userSteps = new UserClient();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginCreatedUserTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        LoginRequest loginRequest = LoginRequest.getRandomLoginRequest(userRequest);
        userSteps.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин c неверным email")
    public void loginCreatedUserWithInvalidEmailFieldTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        LoginRequest loginRequest = LoginRequest.getLoginRequestWithInvalidEmailField(userRequest);
        userSteps.login(loginRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Логин c неверным паролем")
    public void loginCreatedUserWithInvalidPasswordFieldTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        response = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        LoginRequest loginRequest = LoginRequest.getLoginRequestWithInvalidPasswordField(userRequest);
        userSteps.login(loginRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken)
                    .assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }
}
