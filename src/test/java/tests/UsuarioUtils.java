package tests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import java.util.UUID;

public class UsuarioUtils {

    public static String generateUniqueEmail() {
        return "user" + UUID.randomUUID().toString() + "@test.com";
    }

    public static Response cadastrarUsuario(String nome, String email, String senha) {
        String payload = String.format(
                "{\"nome\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"administrador\": \"true\"}",
                nome, email, senha);
        return given()
                .spec(BaseTest.spec)
                .body(payload)
                .when()
                .post("/usuarios");
    }


    public static String realizarLogin(String email, String senha) {
        String payload = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, senha);
        Response response = given()
                .spec(BaseTest.spec)
                .body(payload)
                .when()
                .post("/login");
        return response.jsonPath().getString("authorization");
    }
}
