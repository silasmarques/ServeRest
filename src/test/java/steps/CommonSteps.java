package steps;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import support.ReportHelper;
import support.ApiHelper;

public class CommonSteps {

    private static Response lastResponse;

    public static void setLastResponse(Response response) {
        lastResponse = response;
    }

    @Then("a mensagem de sucesso deve ser {string}")
    public void validarMensagemSucesso(String expectedMessage) {
        Assertions.assertNotNull(lastResponse, "Erro: Nenhuma resposta armazenada para validar a mensagem!");

        String actualMessage = lastResponse.jsonPath().getString("message");
        Assertions.assertNotNull(actualMessage, "Erro: Mensagem de resposta está ausente!");

        Assertions.assertEquals(expectedMessage, actualMessage, "Erro: Mensagem de sucesso diferente do esperado!");

        ReportHelper.logInfo("Mensagem de sucesso validada com sucesso: " + actualMessage);
    }

//    @Then("a resposta deve retornar o status {int}")
//    public void validarStatusResposta(int statusCodeEsperado) {
//        if (lastResponse == null) {
//            throw new AssertionError("Erro: Nenhuma resposta foi armazenada!");
//        }
//        int statusCodeRecebido = lastResponse.getStatusCode();
//        Assertions.assertEquals(statusCodeEsperado, statusCodeRecebido,
//                "Erro: Status esperado " + statusCodeEsperado + " mas recebido " + statusCodeRecebido);
//
//        ReportHelper.logInfo("Status da resposta validado com sucesso: " + statusCodeRecebido);
//    }

    public static void garantirAutenticacao() {
        try {
            String token = ApiHelper.getToken();
            Assertions.assertNotNull(token, "Erro: O token de autenticação não foi gerado!");
        } catch (IllegalStateException e) {
            throw new RuntimeException("Erro: Nenhum usuário autenticado. Faça login primeiro!", e);
        }
    }
}
