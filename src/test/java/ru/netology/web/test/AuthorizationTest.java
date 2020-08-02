package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.getAuthInfo;

public class AuthorizationTest {

    @AfterEach
    void deleteAllData()  {
       DataHelper.deleteAllDataInTables();
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
