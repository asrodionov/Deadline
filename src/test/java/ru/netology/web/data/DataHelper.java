package ru.netology.web.data;

import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static String getUserIdFor(AuthInfo authInfo) throws SQLException {
        val userIdSQL = "SELECT id FROM users WHERE login='" + authInfo.getLogin() + "'";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://192.168.99.100:3306/app", "app", "123"
                );
        ) {

            val id = runner.query(conn, userIdSQL, new ScalarHandler<>());
            return (String) id;
        }
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) throws SQLException {
        val codeSQL = "SELECT code FROM auth_codes WHERE user_id='" + getUserIdFor(authInfo) + "'";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://192.168.99.100:3306/app", "app", "123"
                );
        ) {
            val code = runner.query(conn, codeSQL, new ScalarHandler<>());
            return new VerificationCode((String) code);
        }
    }
}
