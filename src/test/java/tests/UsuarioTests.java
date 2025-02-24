package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ExtentReportListener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(ExtentReportListener.class)
public class UsuarioTests extends BaseTest {

    private static String email;
    private static final String senha = "Senha123"; // senha padrão para testes
    private static String token;
    private static String usuarioId;

    @Test
    public void testCadastroUsuario() {
        email = UsuarioUtils.generateUniqueEmail(); // Gera um email único para evitar duplicidade
        String nome = "Fulano da Silva";
        String payload = String.format(
                "{\"nome\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"administrador\": \"true\"}",
                nome, email, "teste");

        usuarioId = given()
                .spec(spec)
                .body(payload)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .body("message", containsString("Cadastro realizado com sucesso"))
                .extract().path("_id");
    }


    @Test
    public void testLoginUsuario() {
        if (email == null) {
            // Caso o usuário ainda não tenha sido criado, efetua o cadastro
            email = UsuarioUtils.generateUniqueEmail();
            String nome = "Teste User";
            UsuarioUtils.cadastrarUsuario(nome, email, senha)
                    .then()
                    .statusCode(201);
        }

        // Realiza o login e extrai o token
        token = UsuarioUtils.realizarLogin(email, senha);
        given()
                .spec(spec)
                .header("Authorization", token)
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200);

        System.out.println("Token obtido: " + token);
    }

    @Test
    public void testAtualizarUsuario() {
        if (usuarioId == null) {
            testCadastroUsuario();
        }
        // Para garantir que houve alteração, você pode mudar o nome
        String novoNome = "Fulano da Silva Atualizado";
        String payload = String.format(
                "{\"nome\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"administrador\": \"true\"}",
                novoNome, email, senha);

        given()
                .spec(spec)
                .body(payload)
                .when()
                .put("/usuarios/" + usuarioId)
                .then()
                .statusCode(200)
                .body("message", containsString("alterado"));
    }

    @Test
    public void testDeletarUsuario() {
        if (usuarioId == null) {
            testCadastroUsuario();
        }
        given()
                .spec(spec)
                .when()
                .delete("/usuarios/" + usuarioId)
                .then()
                .statusCode(200)
                .body("message", containsString("Registro excluído com sucesso"));
    }
}
