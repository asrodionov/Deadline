package ru.netology.web.test;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.getAuthInfo;

public class AuthorizationTest {

    @AfterEach
    void deleteAllData() throws SQLException {
        val runner = new QueryRunner();
        val deleteAuthCodes = "DELETE FROM auth_codes";
        val deleteCardTransactions = "DELETE FROM card_transactions";
        val deleteCards = "DELETE FROM cards";
        val deleteUsers = "DELETE FROM users";
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://192.168.99.100:3306/app", "app", "123"
                );
        ) {
            runner.update(conn, deleteAuthCodes);
            runner.update(conn, deleteCardTransactions);
            runner.update(conn, deleteCards);
            runner.update(conn, deleteUsers);
        }
    }

    @Test
    void shouldAuthorization() throws SQLException {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }
}
