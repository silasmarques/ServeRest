package support;

public class AuthHelper {
    private static String lastCreatedEmail;
    private static String lastCreatedPassword;

    public static void setLastCreatedUser(String email, String password) {
        lastCreatedEmail = email;
        lastCreatedPassword = password;
    }

    public static String getLastCreatedEmail() {
        return lastCreatedEmail;
    }

    public static String getLastCreatedPassword() {
        return lastCreatedPassword;
    }
}
