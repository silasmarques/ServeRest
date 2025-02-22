package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import support.ApiHelper;
import support.AuthHelper;
import support.ReportHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsersSteps {

    private String email;
    private String password;
    private Response lastResponse;

    private final CommonSteps commonSteps;

    public UsersSteps() {
        this.commonSteps = new CommonSteps();
    }

    @Given("que eu tenho os dados de um novo usuário único")
    public void queEuTenhoOsDadosDeUmNovoUsuarioUnico() {
        // Criando um email e senha únicos para evitar duplicação
        email = "user_" + UUID.randomUUID() + "@example.com";
        password = "senha123";

        ReportHelper.logInfo("Gerados dados do usuário: " + email);
    }

    @When("eu envio uma requisição para criar um usuário único")
    public void euEnvioUmaRequisicaoParaCriarUmUsuarioUnico() {
        // Criando payload para a requisição
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("nome", "Usuário Teste");
        userPayload.put("email", email);
        userPayload.put("password", password);
        userPayload.put("administrador", "false");

        // Enviando requisição para criar usuário
        lastResponse = ApiHelper.post("/usuarios", userPayload, false);

        // ⚠️ Aqui garantimos que `lastResponse` foi armazenado no `CommonSteps`
        commonSteps.setLastResponse(lastResponse);

        Assertions.assertNotNull(lastResponse, "Erro: A resposta da API não deve ser nula!");
        Assertions.assertEquals(201, lastResponse.getStatusCode(), "Erro ao cadastrar o usuário!");

        // Salvando usuário no AuthHelper para uso no login
        AuthHelper.setLastCreatedUser(email, password);
        ReportHelper.logInfo("Usuário criado com sucesso: " + email);
    }

    @Then("a mensagem de sucesso deve conter {string}")
    public void aMensagemDeSucessoDeveSer(String expectedMessage) {
        Assertions.assertNotNull(lastResponse, "Erro: Nenhuma resposta foi armazenada!");

        String actualMessage = lastResponse.jsonPath().getString("message");
        Assertions.assertEquals(expectedMessage, actualMessage, "Erro: Mensagem de resposta incorreta!");

        ReportHelper.logInfo("Mensagem de sucesso validada: " + actualMessage);
    }
}
