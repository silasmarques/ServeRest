package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ExtentReportListener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(ExtentReportListener.class)
public class PedidosTests extends BaseTest {

    private static String token;
    private static String pedidoId;

    private String getToken() {
        if (token == null) {
            String email = UsuarioUtils.generateUniqueEmail();
            String nome = "Pedido User";
            String senha = "Senha123";
            UsuarioUtils.cadastrarUsuario(nome, email, senha)
                    .then()
                    .statusCode(201);
            token = UsuarioUtils.realizarLogin(email, senha);
        }
        return token;
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

    @Test
    public void testCadastrarPedido() {
        String token = getToken();

        String produtoId = createProdutoParaPedido(token);

        String payload = String.format("{\"produtos\": [{\"idProduto\": \"%s\", \"quantidade\": 2}]}", produtoId);

        Response response = given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .post("/carrinhos");  // Endpoint correto para cadastrar pedidos é '/carrinhos'

        response.then()
                .statusCode(201)
                .body("message", containsString("Cadastro realizado com sucesso"));

        pedidoId = response.jsonPath().getString("_id");
        System.out.println("Pedido cadastrado com ID: " + pedidoId);
    }

    private String createProdutoParaPedido(String token) {
        String nome = "Produto Pedido " + System.currentTimeMillis();
        String payload = String.format("{\"nome\": \"%s\", \"preco\": 50.0, \"descricao\": \"Produto para pedido\", \"quantidade\": 20}", nome);

        Response response = given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .post("/produtos");

        response.then()
                .statusCode(201);
        return response.jsonPath().getString("_id");
    }
}
