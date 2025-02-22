package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import support.ApiHelper;
import support.ReportHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsersSteps {

    private final CommonSteps commonSteps;
    private static String email;
    private static String password;

    public UsersSteps() {
        this.commonSteps = new CommonSteps();
    }

    @Given("que quero criar um novo usuário")
    public void prepararNovoUsuario() {
        email = "user_" + UUID.randomUUID() + "@test.com";
        password = "Senha123";

        ReportHelper.logInfo("Preparando usuário para cadastro. Email: " + email);
    }

    @When("envio uma requisição para cadastrar o usuário")
    public void cadastrarUsuario() {
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("nome", "Usuário Teste");
        userPayload.put("email", email);
        userPayload.put("password", password);
        userPayload.put("administrador", "true");

        Response response = ApiHelper.post("/usuarios", userPayload);
        Assertions.assertNotNull(response, "Erro: A resposta da API não deve ser nula!");
        Assertions.assertEquals(201, response.getStatusCode(), "Erro ao criar usuário!");

        // Guardar credenciais para o login
        AuthHelper.setLastCreatedUser(email, password);

        CommonSteps.setLastResponse(response);
        ReportHelper.logInfo("Usuário cadastrado com sucesso!");
    }
}
