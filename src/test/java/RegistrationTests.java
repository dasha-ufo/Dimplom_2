import client.RegisterAccount;
import client.UpdateAccount;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojo.RegistrationBodyForm;

public class RegistrationTests {
private RegisterAccount registerAccount;
private UpdateAccount updateAccount;

    @Before
    public void setUp() {
        registerAccount = new RegisterAccount();
        updateAccount = new UpdateAccount();
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Регистрируем нового уникального пользователя с заполненными обязательными полями")
    public void registrationSuccess () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();

        Response response = registerAccount.create(requestBody);
        registerAccount.checkSuccessResponseRegistration(response);
        UpdateAccount.deleteUserRequestWithToken(response);
    }


    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Проверяем возврат ошибки при регистрации с существующим именем, паролем и email")
    public void registrationOfExistingUser() {

        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();

        Response response = registerAccount.create(requestBody);
        Response errorExistedUserResponse = registerAccount.create(requestBody);
        registerAccount.checkErrorExistedUser(errorExistedUserResponse);
    }

    @Test
    @DisplayName("Регистрация пользователя без поля name")
    @Description("Проверяем возврат ошибки при регистрации с незаполненными обязательным полем name")
    public void registrationErrorNoName () {
        RegistrationBodyForm requestBody = new RegistrationBodyForm();
        requestBody.setPassword(RegistrationBodyForm.randomPassword());
        requestBody.setEmail(RegistrationBodyForm.randomEmail());

        Response response = registerAccount.create(requestBody);
        registerAccount.checkErrorEmptyFieldRegistr(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без поля password")
    @Description("Проверяем возврат ошибки при регистрации пользователя с незаполненными обязательным полем password")
    public void registrationErrorNoPassword () {
        RegistrationBodyForm requestBody = new RegistrationBodyForm();
        requestBody.setName(RegistrationBodyForm.randomName());
        requestBody.setEmail(RegistrationBodyForm.randomEmail());

        Response response = registerAccount.create(requestBody);
        registerAccount.checkErrorEmptyFieldRegistr(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без поля email")
    @Description("Проверяем возврат ошибки при регистрации с незаполненными обязательным полем email")
    public void registrationErrorNoEmail () {
        RegistrationBodyForm requestBody = new RegistrationBodyForm();
        requestBody.setPassword(RegistrationBodyForm.randomPassword());
        requestBody.setName(RegistrationBodyForm.randomName());

        Response response = registerAccount.create(requestBody);
        registerAccount.checkErrorEmptyFieldRegistr(response);
    }




}
