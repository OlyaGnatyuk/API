package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestRequests {

    public static String checkAccess(String baseUri, String entrance, Integer keyId, Integer roomId,
                                     Integer expectedStatusCode) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .param("entrance", entrance)
                .param("keyId", keyId)
                .param("roomId", roomId)
                .when()
                .get("/check")
                .then()
                .log().body(true)
                .statusCode(expectedStatusCode)
                .extract()
                .response().asString();
    }

    public static Response  getRoomsInfo(String baseUri, Integer expectedStatusCode) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .when()
                .get("/info/rooms")
                .then()
                .log().body(true)
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }

    public static Response getUsersInfo(String baseUri, Integer start, Integer end, Integer expectedStatusCode) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .param("start", start)
                .param("end", end)
                .when()
                .get("/info/users")
                .then()
                .log().body(true)
                .statusCode(expectedStatusCode)
                .extract()
                .response();
    }
}
