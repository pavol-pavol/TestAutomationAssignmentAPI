package ch.swisssign.tests;

import ch.swisssign.base.BaseApiTest;
import ch.swisssign.client.SwissSignApiClient;
import ch.swisssign.utils.ConfigLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("SwissSign API Automation")
@Feature("Authentication and Client Management")
public class SwissSignDataDrivenTests extends BaseApiTest {

    private final SwissSignApiClient apiClient = new SwissSignApiClient();


    @ValueSource(strings = {"ValidUser", "InvalidUser", "AnotherInvalidUser"})
    @ParameterizedTest(name = "Login test with user: {0}")
    @Order(1)
    @DisplayName("User Login Data Driven - JSON")
    @Story("User Login Data Driven - JSON")
    @Severity(SeverityLevel.BLOCKER)
    void testUserLoginPositive(String user) {
        JsonNode users = ConfigLoader.getUsers();

        String userName = users.get(user).get("username").asText();
        String userSecret = users.get(user).get("secret").asText();

        Response response = apiClient.loginUser(userName, userSecret);

        String expectedResponseCode = users.get(user).get("code").asText();
        response.then().statusCode(Integer.parseInt(expectedResponseCode));

        String token = response.asString().replace("\"", "");
        assertThat("Token should not be empty", token, not(emptyOrNullString()));

        Allure.addAttachment("JWT Token", token);
    }

    @ParameterizedTest(name = "Login with user: [{argumentsWithNames}]")
    @CsvFileSource(resources = "/testdata/users.csv", numLinesToSkip = 1)
    @Story("User Login Data Driven - CSV")
    @Severity(SeverityLevel.MINOR)
    @Description("Try multiple users with different credentials to validate login behavior")
    void testLoginDataDriven(String userName, String userSecret, int expectedCode) {
        Response response = apiClient.loginUser(userName, userSecret);

        // Verify status code matches code from CSV
        response.then().statusCode(expectedCode);

        if (expectedCode == 200) {
            String token = response.asString().replace("\"", "");
            assertThat(token, not(emptyOrNullString()));
            Allure.addAttachment("Valid Token for " + userName, token);
        } else {
            Allure.addAttachment("Error Response for " + userName, response.asPrettyString());
        }
    }
}