import client.LoginAccount;
import client.RegisterAccount;
import client.UpdateAccount;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.RegistrationBodyForm;

public class LoginTests {
    private LoginAccount loginAccount;
    private RegisterAccount registerAccount;
    private Response responseForDelete;

    @Before
    public void setUp() {
        loginAccount = new LoginAccount();
        registerAccount = new RegisterAccount();
    }

    @After
    public void cleanUp() {
        UpdateAccount.deleteUserRequestWithToken(responseForDelete);
    }


    @Test
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Успешно авторизуемся под существующим пользователем")
    public void loginSuccess () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String password = requestBody.getPassword();
        String email = requestBody.getEmail();
        RegistrationBodyForm requestLoginBody = new RegistrationBodyForm(password,email);

        Response responseLogin = loginAccount.create(requestLoginBody);
        loginAccount.checkSuccessResponseLogin(responseLogin);
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    @Description("Отправляем запрос на авторизацию с неправильным паролем")
    public void loginWrongPassword () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String email= requestBody.getEmail();
        String password = RegistrationBodyForm.randomPassword();

        RegistrationBodyForm requestLoginBody = new RegistrationBodyForm(password,email);
        Response responseLogin = loginAccount.create(requestLoginBody);
        loginAccount.checkWrongEmailOrPassword(responseLogin);
    }

    @Test
    @DisplayName("Авторизация с неправильным email")
    @Description("Отправляем запрос на авторизацию с неправильным email")
    public void loginWrongEmail () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String email = RegistrationBodyForm.randomEmail();
        String password = requestBody.getPassword();

        RegistrationBodyForm requestLoginBody = new RegistrationBodyForm(password,email);
        Response responseLogin = loginAccount.create(requestLoginBody);
        loginAccount.checkWrongEmailOrPassword(responseLogin);
    }

    @Test
    @DisplayName("Авторизация без указания email")
    @Description("Отправляем запрос на авторизацию без указания email, только с email password")
    public void loginWithoutEmail () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String password = requestBody.getPassword();

        RegistrationBodyForm requestLoginBody = new RegistrationBodyForm(password);
        Response responseLogin = loginAccount.create(requestLoginBody);
        loginAccount.checkWrongEmailOrPassword(responseLogin);
    }

    @Test
    @DisplayName("Авторизация без указания password")
    @Description("Отправляем запрос на авторизацию без указания password, только с email")
    public void loginWithoutPassword () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String email = requestBody.getEmail();

        RegistrationBodyForm requestLoginBody = new RegistrationBodyForm(email);
        Response responseLogin = loginAccount.create(requestLoginBody);
        loginAccount.checkWrongEmailOrPassword(responseLogin);
    }
}


