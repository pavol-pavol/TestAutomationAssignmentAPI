package ch.swisssign.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SwissSignApiClient {

    public Response loginUser(String userName, String userSecret) {
        return given()
                .filter(new AllureRestAssured())
                .contentType("application/x-www-form-urlencoded")
                .formParam("userSecret", userSecret)
                .when()
                .post("/jwt/" + userName);
    }

    public Response getClientInfo(String token) {
        return given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .when()
                .post("/clients");
    }

    public Response getClientInfoWithoutToken() {
        return given()
                .filter(new AllureRestAssured())
                .accept("application/json")
                .when()
                .post("/clients");
    }
}
