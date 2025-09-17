package ch.swisssign.tests;

import ch.swisssign.base.BaseApiTest;
import ch.swisssign.client.SwissSignApiClient;
import ch.swisssign.utils.ConfigLoader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("SwissSign API Automation")
@Feature("Authentication and Client Management")
public class SwissSignApiTests extends BaseApiTest {

    private static String token;
    private final SwissSignApiClient apiClient = new SwissSignApiClient();

    @Test
    @Order(1)
    @DisplayName("User Login - Positive")
    @Story("User Login - Positive")
    @Severity(SeverityLevel.BLOCKER)
    void testUserLoginPositive() {
        JsonNode users = ConfigLoader.getUsers();

        String userName = users.get("ValidUser").get("username").asText();
        String userSecret = users.get("ValidUser").get("secret").asText();

        Response response = apiClient.loginUser(userName, userSecret);

        response.then().statusCode(200);

        token = response.asString().replace("\"", "");
        assertThat("Token should not be empty", token, not(emptyOrNullString()));

        Allure.addAttachment("JWT Token", token);
    }

    @Test
    @Order(2)
    @DisplayName("Fetch Client Information - Positive")
    @Story("Fetch Client Information - Positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that client information contains at least one product when token is valid")
    void testReadClientInformationPositive() {
        Assumptions.assumeTrue(token != null, "Token must be available from login");

        Response response = apiClient.getClientInfo(token);

        response.then().statusCode(200);

        int productCount = response.jsonPath().getList("[0].products").size();
        assertThat("Product count must be >= 1", productCount, greaterThan(0));

        Allure.addAttachment("Client Response", response.asPrettyString());
    }

    @Test
    @Order(3)
    @DisplayName("User Login - Negative")
    @Story("User Login - Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that login fails with invalid user credentials")
    void testUserLoginNegative() {
        JsonNode users = ConfigLoader.getUsers();

        String userName = users.get("InvalidUser").get("username").asText();
        String userSecret = users.get("InvalidUser").get("secret").asText();

        Response response = apiClient.loginUser(userName, userSecret);

        response.then().statusCode(400);

        String errorMessage = response.jsonPath().getString("errorMessage");
        assertThat(errorMessage, containsStringIgnoringCase("invalid"));

        Allure.addAttachment("Error Response", response.asPrettyString());
    }

    @Test
    @Order(4)
    @DisplayName("Fetch Client Information - Negative")
    @Story("Fetch Client Information - Negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that fetching client info without token fails")
    void testReadClientInformationWithoutToken() {
        Response response = apiClient.getClientInfoWithoutToken();

        response.then().statusCode(401);

        Allure.addAttachment("Error Response", response.asPrettyString());
    }
}