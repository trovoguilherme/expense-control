package br.com.iug.integration;

import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.request.ParcelaRequest;
import br.com.iug.entity.response.ItemResponse;
import br.com.iug.integration.helper.db.ItemDBHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ItemTest {

    @Autowired
    private ItemDBHelper itemDBHelper;

    @LocalServerPort
    private Integer port;

    private ItemClient client;

    @BeforeEach
    public void setup() {
        client = new ItemClient(port);
    }

    @Test
    @DisplayName("Deve encontrar um item pelo id")
    void findItemById() {
        var item = itemDBHelper.findByNameOrCreate("Nike");

        Response response = client.findById(item.getId());

        response.then().log().body();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(ItemResponse.class).getNome()).isEqualTo(item.getNome());
    }

    @Test
    @DisplayName("Deve criar um item")
    void shouldCreteItem() {
        var itemRequest = new ItemRequest("pc", "NUBANK", 800, new ParcelaRequest(2, 10));

        Response response = client.create(itemRequest);

        var itemFound = itemDBHelper.findByNameOrCreate(itemRequest.getNome());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(itemFound.getNome()).isEqualTo(itemRequest.getNome());
    }

    @Test
    @DisplayName("Deve pagar um item pelo nome")
    void shouldPayItemById() {
        var itemCreated = itemDBHelper.findByNameOrCreate("pay");

        Response response = client.payItemById(itemCreated.getId());

        var itemFound = itemDBHelper.findByNameOrCreate(itemCreated.getNome());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(itemFound.getParcela().getQtdPaga()).isEqualTo(itemCreated.getParcela().getQtdRestante()+1);
    }

}
