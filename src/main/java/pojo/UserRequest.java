package pojo;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserRequest {
    private String email;
    private String password;
    private String name;


    public UserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Step("Ввод данных для создания пользователя")
    public static UserRequest getRandomUserRequest() {
        Faker faker = new Faker();
        return new UserRequest(faker.internet().emailAddress(), faker.internet().password(), faker.name().username());
    }

    @Step("Ввод данных без email")
    public static UserRequest getRandomUserRequestWithoutEmailField() {
        Faker faker = new Faker();
        return new UserRequest(null, faker.internet().password(), faker.name().username());
    }

    @Step("Ввод данных без пароля")
    public static UserRequest getRandomUserRequestWithoutPasswordField() {
        Faker faker = new Faker();
        return new UserRequest(faker.internet().emailAddress(), null, faker.name().username());
    }

    @Step("Ввод данных без имени")
    public static UserRequest getRandomUserRequestWithoutNameField() {
        Faker faker = new Faker();
        return new UserRequest(null, faker.internet().password(), null);
    }
}