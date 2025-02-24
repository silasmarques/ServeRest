package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class PedidosTests extends BaseTest {

    private static String token;
    private static String pedidoId;

    // Obtém um token válido para os testes de pedido
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

    @Test
    public void testListarPedidos() {
        String token = getToken();
        given()
                .spec(spec)
                .header("Authorization", token)
                .when()
                .get("/pedidos")
                .then()
                .statusCode(200);
    }

    @Test
    public void testCadastrarPedido() {
        String token = getToken();

        // Cria um produto para incluir no pedido
        String produtoId = createProdutoParaPedido(token);

        // Exemplo de payload: array de produtos com id e quantidade
        String payload = String.format("{\"produtos\": [{\"idProduto\": \"%s\", \"quantidade\": 2}]}", produtoId);

        Response response = given()
                .spec(spec)
                .header("Authorization", token)
                .body(payload)
                .when()
                .post("/pedidos");

        response.then()
                .statusCode(201)
                .body("message", containsString("cadastrado"));

        pedidoId = response.jsonPath().getString("_id");
        System.out.println("Pedido cadastrado com ID: " + pedidoId);
    }

    // Método auxiliar para criar um produto específico para os pedidos
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
