package br.com.iug.integration.client;

import br.com.iug.dto.request.PagamentoRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PagamentoClient {

    private final RequestSpecBuilder specBuilder;

    public static final String ENDPOINT = "/pagamento";

    public PagamentoClient(int serverPort) {
        RestAssured.defaultParser = Parser.JSON;
        specBuilder = new RequestSpecBuilder();
        specBuilder.addHeader("Content-Type", "application/json");
        specBuilder.setBaseUri("http://localhost:" + serverPort);
    }

    public Response create(PagamentoRequest request) {
        return given()
                .spec(specBuilder.build())
                .body(request)
                .when()
                .post(ENDPOINT);
    }

}
