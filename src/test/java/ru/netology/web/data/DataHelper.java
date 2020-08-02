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

    @Value
    public static class DataBaseConn {
        String url;
        String user;
        String password;
    }

    public static DataBaseConn getDataBaseConn() {
        return new DataBaseConn("jdbc:mysql://192.168.99.100:3306/app", "app", "123");
    }

    public static String getUserIdFor(AuthInfo authInfo) throws SQLException {
        String id = null;
        try {
            val userIdSQL = "SELECT id FROM users WHERE login='" + authInfo.getLogin() + "'";
            val runner = new QueryRunner();
            val conn = DriverManager.getConnection(
                    getDataBaseConn().url, getDataBaseConn().user, getDataBaseConn().password
            );
            val userId = runner.query(conn, userIdSQL, new ScalarHandler<>());
            id = (String) userId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        VerificationCode verificationCode = null;
        try {
            val codeSQL = "SELECT code FROM auth_codes WHERE user_id='" + getUserIdFor(authInfo) + "'";
            val runner = new QueryRunner();
            val conn = DriverManager
                    .getConnection(getDataBaseConn().url, getDataBaseConn().user, getDataBaseConn().password);
            val code = runner.query(conn, codeSQL, new ScalarHandler<>());
            verificationCode = new VerificationCode((String) code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return verificationCode;
    }

    public static void deleteAllDataInTables() {
        try {
            val runner = new QueryRunner();
            val deleteAuthCodes = "DELETE FROM auth_codes";
            val deleteCardTransactions = "DELETE FROM card_transactions";
            val deleteCards = "DELETE FROM cards";
            val deleteUsers = "DELETE FROM users";
            val conn = DriverManager.getConnection(
                    DataHelper.getDataBaseConn().getUrl(), DataHelper.getDataBaseConn().getUser(), DataHelper.getDataBaseConn().getPassword()
            );
            runner.update(conn, deleteAuthCodes);
            runner.update(conn, deleteCardTransactions);
            runner.update(conn, deleteCards);
            runner.update(conn, deleteUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
