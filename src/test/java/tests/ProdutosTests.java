package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ExtentReportListener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(ExtentReportListener.class)
public class ProdutosTests extends BaseTest {

    private static String token;
    private static String produtoId;

    // Método para obter ou gerar um token válido para testes de produto
    private String getToken() {
        if (token == null) {
            String email = UsuarioUtils.generateUniqueEmail();
            String nome = "Produto User";
            String senha = "Senha123";
            UsuarioUtils.cadastrarUsuario(nome, email, senha)
                    .then()
                    .statusCode(201);
            token = UsuarioUtils.realizarLogin(email, senha);
        }
        return token;
    }

    @Test
    public void testListarProdutos() {
        given()
                .spec(spec)
                .when()
                .get("/produtos")
                .then()
                .statusCode(200)
                .body("produtos", not(empty()));
    }

    @Test
    public void testCadastrarProduto() {
        String token = getToken();
        String nome = "Produto Teste " + System.currentTimeMillis();
        String payload = String.format("{\"nome\": \"%s\", \"preco\": 100.0, \"descricao\": \"Produto para teste\", \"quantidade\": 10}", nome);

        Response response = given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .post("/produtos");

        response.then()
                .statusCode(201)
                .body("message", containsString("Cadastro realizado com sucesso"));

        produtoId = response.jsonPath().getString("_id");
        System.out.println("Produto cadastrado com ID: " + produtoId);
    }

    @Test
    public void testAtualizarProduto() {
        if (produtoId == null) {
            testCadastrarProduto();
        }
        String token = getToken();
        String novoNome = "Produto Atualizado " + System.currentTimeMillis();
        // Incluindo todos os campos obrigatórios: nome, preco, descricao e quantidade
        String payload = String.format(
                "{\"nome\": \"%s\", \"preco\": 150.0, \"descricao\": \"Produto para teste atualizado\", \"quantidade\": 10}",
                novoNome);

        given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .put("/produtos/" + produtoId)
                .then()
                .statusCode(200)
                .body("message", containsString("Registro alterado com sucesso"));
    }

    @Test
    public void testDeletarProduto() {
        if (produtoId == null) {
            testCadastrarProduto();
        }
        String token = getToken();
        given()
                .spec(spec)
                .header("Authorization", token)
                .when()
                .delete("/produtos/" + produtoId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }
}
