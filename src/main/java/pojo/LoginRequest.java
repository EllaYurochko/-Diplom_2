package pojo;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Step("Ввод персональных данных")
    public static LoginRequest getRandomLoginRequest(UserRequest userRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(userRequest.getPassword());
        return loginRequest;
    }

    @Step("Ввод данных с неверным email")
    public static LoginRequest getLoginRequestWithInvalidEmailField(UserRequest userRequest) {
        LoginRequest loginRequest = new LoginRequest();
        Faker faker = new Faker();
        loginRequest.setEmail(faker.internet().emailAddress());
        loginRequest.setPassword(userRequest.getPassword());
        return loginRequest;
    }

    @Step("Ввод данных с неверным паролем")
    public static LoginRequest getLoginRequestWithInvalidPasswordField(UserRequest userRequest) {
        LoginRequest loginRequest = new LoginRequest();
        Faker faker = new Faker();
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(faker.internet().password());
        return loginRequest;
    }
}