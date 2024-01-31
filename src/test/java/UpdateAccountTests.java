import client.LoginAccount;
import client.RegisterAccount;
import client.UpdateAccount;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.RegistrationBodyForm;
import pojo.UpdateInfoForm;

public class UpdateAccountTests {
    private UpdateAccount updateAccount;
    private RegisterAccount registerAccount;
    private Response responseForDelete;

    @Before
    public void setUp() {
        updateAccount = new UpdateAccount();
        registerAccount = new RegisterAccount();
    }

    @After
    public void cleanUp() {
        UpdateAccount.deleteUserRequestWithToken(responseForDelete);
    }


    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Успешно изменяем значение в поле email")
    public void changeEmailSuccess () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        Response response = registerAccount.create(requestBody);
        responseForDelete = response;
        String accessToken = RegisterAccount.receiveAccessToken(response);

        String newEmail = RegistrationBodyForm.randomEmail();
        String name = requestBody.getName();

        UpdateInfoForm requestUpdateBody = new UpdateInfoForm(newEmail, name);

        Response updateUserResponse = updateAccount.updateAuthRequest(requestUpdateBody, accessToken);
        UpdateAccount.checkSuccessUpdate(updateUserResponse, newEmail, name);
    }

    @Test
    @DisplayName("Изменение name пользователя с авторизацией")
    @Description("Успешно изменяем значение в поле name")
    public void changeNameSuccess () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        Response response = registerAccount.create(requestBody);
        responseForDelete = response;
        String accessToken = RegisterAccount.receiveAccessToken(response);

        String newName = RegistrationBodyForm.randomName();
        String email = requestBody.getEmail();

        UpdateInfoForm requestUpdateBody = new UpdateInfoForm(email, newName);

        Response updateUserResponse = updateAccount.updateAuthRequest(requestUpdateBody, accessToken);
        UpdateAccount.checkSuccessUpdate(updateUserResponse, email, newName);
    }

    @Test
    @DisplayName("Изменение name пользователя без авторизации")
    @Description("Проверяем возврат ошибки при изменении поля name без параметра authorization")
    public void changeNameWithoutAuth() {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String newName = RegistrationBodyForm.randomName();
        String email = requestBody.getEmail();

        UpdateInfoForm requestUpdateBody = new UpdateInfoForm(email, newName);

        Response updateUserResponse = updateAccount.updateNotAuthRequest(requestUpdateBody);
        UpdateAccount.checkUpdateError401(updateUserResponse);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Проверяем возврат ошибки при изменении поля email без параметра authorization")
    public void changeEmailWithoutAuth () {
        RegistrationBodyForm requestBody = RegistrationBodyForm.randomAccount();
        responseForDelete = registerAccount.create(requestBody);

        String newEmail = RegistrationBodyForm.randomEmail();
        String name = requestBody.getName();

        UpdateInfoForm requestUpdateBody = new UpdateInfoForm(newEmail, name);

        Response updateUserResponse = updateAccount.updateNotAuthRequest(requestUpdateBody);
        UpdateAccount.checkUpdateError401(updateUserResponse);
    }

}
