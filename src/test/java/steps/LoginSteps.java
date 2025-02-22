package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import support.ApiHelper;
import support.AuthHelper;
import support.ReportHelper;

import java.util.Map;

public class LoginSteps {

    private final CommonSteps commonSteps;
    private static String email;
    private static String password;

    public LoginSteps() {
        this.commonSteps = new CommonSteps();
    }

    @Given("que o usuário foi criado anteriormente")
    public void obterUsuarioCriado() {
        email = AuthHelper.getLastCreatedEmail();
        password = AuthHelper.getLastCreatedPassword();

        Assertions.assertNotNull(email, "Erro: Nenhum usuário cadastrado previamente!");
        Assertions.assertNotNull(password, "Erro: Senha do usuário não armazenada!");

        ReportHelper.logInfo("Recuperando usuário criado para login. Email: " + email);
    }

    @When("realizo login com esse usuário")
    public void realizarLogin() {
        Response response = ApiHelper.post("/login", Map.of("email", email, "password", password), false);
        Assertions.assertEquals(200, response.getStatusCode(), "Erro ao realizar login!");

        String token = response.jsonPath().getString("authorization");
        ApiHelper.setToken(token);
        CommonSteps.setLastResponse(response);

        ReportHelper.logInfo("Login realizado com sucesso. Token armazenado.");
    }
}
