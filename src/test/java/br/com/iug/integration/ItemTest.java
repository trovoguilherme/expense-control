package br.com.iug.integration;

import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.request.ParcelaRequest;
import br.com.iug.entity.response.ItemResponse;
import br.com.iug.exception.ItemNotFoundException;
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
    @DisplayName("Deve criar um item com parcela")
    void shouldCreteItemWithParcela() {
        var itemRequest = new ItemRequest("com-parcela", "NUBANK", 800, new ParcelaRequest(2, 10));

        Response response = client.create(itemRequest);

        var itemFound = itemDBHelper.findByNameOrCreate(itemRequest.getNome());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(itemFound.getNome()).isEqualTo(itemRequest.getNome());
        assertThat(itemFound.getParcela()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar um item sem parcela")
    void shouldCreteItemWithoutParcela() {
        var itemRequest = new ItemRequest("sem-parcela", "NUBANK", 800, null);

        Response response = client.create(itemRequest);

        var itemFound = itemDBHelper.findByNameOrCreate(itemRequest.getNome());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(itemFound.getNome()).isEqualTo(itemRequest.getNome());
        assertThat(itemFound.getParcela()).isNull();
    }

    @Test
    @DisplayName("Deve atualizar um item com parcela")
    void shouldUpdateItemWithParcela() throws ItemNotFoundException {
        var itemCreated = itemDBHelper.findByNameOrCreate("caderno");
        var itemRequest = new ItemRequest("atualizar-com-parcela", "NUBANK", 800, new ParcelaRequest(2, 10));

        Response response = client.update(itemCreated.getId(), itemRequest);

        var itemFound = itemDBHelper.findById(itemCreated.getId());

        assertThat(itemFound.getId()).isEqualTo(itemCreated.getId());
        assertThat(itemFound.getNome()).isEqualTo(itemRequest.getNome());
        assertThat(itemFound.getValorRestante()).isEqualTo(itemRequest.getValor() * itemRequest.getParcela().getQtdRestante());
    }

    @Test
    @DisplayName("Deve atualizar um item sem parcela")
    void shouldUpdateItemWithoutParcela() throws ItemNotFoundException {
        var itemCreated = itemDBHelper.findByNameOrCreateWithoutParcela("sem");
        var itemRequest = new ItemRequest("atualizado-sem-parcela", "NUBANK", 800, null);

        Response response = client.update(itemCreated.getId(), itemRequest);

        var itemFound = itemDBHelper.findById(itemCreated.getId());

        assertThat(itemFound.getId()).isEqualTo(itemCreated.getId());
        assertThat(itemFound.getNome()).isEqualTo(itemRequest.getNome());
        assertThat(itemFound.getValorRestante()).isEqualTo(itemRequest.getValor());
        assertThat(itemFound.getParcela()).isNull();
    }

    @Test
    @DisplayName("Deve dar erro ao tentar atualizar um item que tem parcela com um sem parcela")
    void shouldReturnInternalServerError()  {
        var itemCreated = itemDBHelper.findByNameOrCreate("com-parcela");
        var itemRequest = new ItemRequest("pc", "NUBANK", 800, null);

        Response response = client.update(itemCreated.getId(), itemRequest);

        response.then().log().all();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
     }

    @Test
    @DisplayName("Deve pagar um item pelo nome")
    void shouldPayItemById() {
        var itemCreated = itemDBHelper.findByNameOrCreate("pay");

        Response response = client.payItemById(itemCreated.getId());

        var itemFound = itemDBHelper.findByNameOrCreate(itemCreated.getNome());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(itemFound.getParcela().getQtdPaga()).isEqualTo(itemCreated.getParcela().getQtdPaga()+1);
    }

}
