package support;

public class AuthHelper {
    private static String lastCreatedEmail;
    private static String lastCreatedPassword;

    // 🔹 Método para armazenar o último usuário criado
    public static void setLastCreatedUser(String email, String password) {
        lastCreatedEmail = email;
        lastCreatedPassword = password;
    }

    // 🔹 Método para obter o último e-mail criado
    public static String getLastCreatedEmail() {
        return lastCreatedEmail;
    }

    // 🔹 Método para obter a última senha criada
    public static String getLastCreatedPassword() {
        return lastCreatedPassword;
    }
}
