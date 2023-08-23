package client;

import pojo.LoginRequest;
import pojo.UserRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String CREATE_USER = "api/auth/register";
    private static final String LOGIN_USER = "api/auth/login";
    private static final String DELETE_USER = "api/auth/user";
    private static final String INFO_USER = "api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse create(UserRequest userRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(userRequest)
                .post(CREATE_USER)
                .then();
    }

    @Step("Вход пользователя")
    public ValidatableResponse login(LoginRequest loginRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequest)
                .post(LOGIN_USER)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .delete(DELETE_USER)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateDataUser(UserRequest userRequest, String  accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .and()
                .body(userRequest)
                .when()
                .patch(INFO_USER)
                .then();
    }
}