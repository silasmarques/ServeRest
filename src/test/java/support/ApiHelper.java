package support;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import java.util.Map;

public class ApiHelper {
    private static final String BASE_URL = "https://serverest.dev";
    private static String token;

    // 🔹 Método para obter a especificação da requisição
    private static RequestSpecification getRequestSpecification(boolean withAuth) {
        RequestSpecification request = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json");

        if (withAuth && token != null) {
            request.header("Authorization", "Bearer " + token);
        }

        return request;
    }

    // 🔹 Método para realizar login e obter token
    public static void realizarLogin(String email, String password) {
        Map<String, String> loginPayload = Map.of(
                "email", email,
                "password", password
        );

        Response response = post("/login", loginPayload);
        Assertions.assertEquals(200, response.getStatusCode(), "Erro: Falha no login!");

        token = response.jsonPath().getString("authorization");
        Assertions.assertNotNull(token, "Erro: Token de autenticação não foi gerado!");
    }

    // 🔹 Método para garantir que há um token válido antes dos testes
    public static void garantirAutenticacao() {
        if (token == null || token.isEmpty()) {
            String email = AuthHelper.getLastCreatedEmail();
            String password = AuthHelper.getLastCreatedPassword();

            Assertions.assertNotNull(email, "Erro: Nenhum e-mail de usuário salvo!");
            Assertions.assertNotNull(password, "Erro: Nenhuma senha de usuário salva!");

            realizarLogin(email, password);
        }
    }

    // 🔹 Método para definir um token manualmente (caso necessário)
    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getToken() {
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("Token não foi definido. Certifique-se de realizar a autenticação primeiro.");
        }
        return token;
    }

    // 🔹 Métodos para requisições HTTP
    public static Response get(String endpoint, boolean withAuth) {
        return getRequestSpecification(withAuth)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    // 🔹 Overload para post sem precisar passar `withAuth`
    public static Response post(String endpoint, Object payload) {
        return post(endpoint, payload, false);
    }

    public static Response post(String endpoint, Object payload, boolean withAuth) {
        return getRequestSpecification(withAuth)
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response put(String endpoint, Object payload, boolean withAuth) {
        return getRequestSpecification(withAuth)
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response delete(String endpoint, boolean withAuth) {
        return getRequestSpecification(withAuth)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    // 🔹 Métodos auxiliares para requisições autenticadas
    public static Response getWithAuth(String endpoint) {
        return get(endpoint, true);
    }

    public static Response postWithAuth(String endpoint, Object payload) {
        return post(endpoint, payload, true);
    }

    public static Response putWithAuth(String endpoint, Object payload) {
        return put(endpoint, payload, true);
    }

    public static Response deleteWithAuth(String endpoint) {
        return delete(endpoint, true);
    }
}
