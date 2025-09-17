package ch.swisssign.base;

import ch.swisssign.utils.ConfigLoader;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
    }
}
