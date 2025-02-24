package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ExtentReportListener;
//import utils.TestResultLogger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

//@ExtendWith(TestResultLogger.class)
@ExtendWith(ExtentReportListener.class)
public class CarrinhosTests extends BaseTest {

    private static String token;
    private static String carrinhoId;

    // Metodo para obter ou gerar um token válido para os testes de carrinho
    private String getToken() {
        if (token == null) {
            String email = UsuarioUtils.generateUniqueEmail();
            String nome = "Carrinho User";
            String senha = "teste";
            UsuarioUtils.cadastrarUsuario(nome, email, senha)
                    .then()
                    .statusCode(201);
            token = UsuarioUtils.realizarLogin(email, senha);
        }
        return token;
    }

    // Metodo auxiliar para criar um produto que será adicionado ao carrinho
    private String createProductForCarrinho(String token) {
        String nome = "Produto Carrinho " + System.currentTimeMillis();
        String payload = String.format(
                "{\"nome\": \"%s\", \"preco\": 99.0, \"descricao\": \"Produto para carrinho\", \"quantidade\": 5}",
                nome);
        Response response = given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .post("/produtos");
        response.then().statusCode(201);
        return response.jsonPath().getString("_id");
    }

    // Testa a listagem dos carrinhos
    @Test
    public void testListarCarrinhos() {
        String token = getToken();
        given()
                .spec(spec)
                .header("Authorization", token)
                .when()
                .get("/carrinhos")
                .then()
                .statusCode(200);
    }

    // Testa a criação de um novo carrinho
    @Test
    public void testCriarCarrinho() {
        String token = getToken();
        // Cria um produto para ser adicionado ao carrinho
        String productId = createProductForCarrinho(token);

        // Monta o payload para criação do carrinho (ajuste se necessário conforme a especificação da API)
        String payload = String.format("{\"produtos\": [{\"idProduto\": \"%s\", \"quantidade\": 1}]}", productId);

        Response response = given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .post("/carrinhos");

        response.then()
                .statusCode(201)
                .body("message", containsString("Cadastro realizado com sucesso"));

        carrinhoId = response.jsonPath().getString("_id");
        System.out.println("Carrinho criado com ID: " + carrinhoId);
    }
    @Test
    public void testConcluirCompra() {
        String token = getToken();
        // Se não houver carrinho criado, cria um primeiro
        if (carrinhoId == null) {
            testCriarCarrinho();
        }
        given()
                .spec(spec)
                .header("Authorization", token)
                .header("Accept", "application/json")
                .when()
                .delete("/carrinhos/concluir-compra")
                .then()
                .statusCode(200)
                .body("message", containsString("Registro excluído com sucesso"));
    }

    @Test
    public void testRemoverCarrinho() {
        String token = getToken();
        given()
                .spec(spec)
                .header("Authorization", token)
                .header("Accept", "application/json")
                .when()
                .delete("/carrinhos/cancelar-compra")
                .then()
                .statusCode(200)
                .body("message", containsString("Não foi encontrado carrinho para esse usuário"));
    }

}