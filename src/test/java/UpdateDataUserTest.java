import client.UserClient;
import pojo.UserRequest;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateDataUserTest {
    private UserClient userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserClient();

    }

    @Test
    @DisplayName("Изменение поля email с авторизацией")
    public void updateFieldEmailWithAuthorizationTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        String newEmail = userRequest.getEmail().toLowerCase();
        userSteps.updateDataUser(userRequest, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Изменение поля имя с авторизацией")
    public void updateFieldNameWithAuthorizationTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateDataUser(userRequest, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(userRequest.getName()));
    }

    @Test
    @DisplayName("Изменение поля email без авторизации")
    public void updateFieldEmailWithoutAuthorizationTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.updateDataUser(userRequest, "")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение поля имя без авторизации")
    public void updateFieldNameWithoutAuthorizationTest() {
        UserRequest userRequest = UserRequest.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateDataUser(userRequest, "")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken).assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }
}