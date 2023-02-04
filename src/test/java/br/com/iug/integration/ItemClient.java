package br.com.iug.integration;

import br.com.iug.entity.request.ItemRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ItemClient {

    private final RequestSpecBuilder specBuilder;

    public static final String ENDPOINT = "/item";

    public ItemClient(int serverPort) {
        RestAssured.defaultParser = Parser.JSON;
        specBuilder = new RequestSpecBuilder();
        specBuilder.addHeader("Content-Type", "application/json");
        specBuilder.setBaseUri("http://localhost:" + serverPort);
    }

    public Response findAllWithParams(String nome, String banco) {
        return given()
                .spec(specBuilder.build())
                .queryParam("nome", nome)
                .queryParam("banco", banco)
                .when()
                .get(ENDPOINT);
    }

    public Response findById(long id) {
        return given()
                .spec(specBuilder.build())
                .when()
                .get(String.format(ENDPOINT + "/%s", id));
    }

    public Response create(ItemRequest request) {
        return given()
                .spec(specBuilder.build())
                .body(request)
                .when()
                .post(ENDPOINT);
    }

    public Response payItemById(long id) {
        return given()
                .spec(specBuilder.build())
                .pathParam("id", id)
                .when()
                .patch(ENDPOINT + "/{id}/pay");
    }

    public Response payItemBanco(String banco) {
        return given()
                .spec(specBuilder.build())
                .queryParam("banco", banco)
                .when()
                .patch(ENDPOINT + "/pay");
    }

}
