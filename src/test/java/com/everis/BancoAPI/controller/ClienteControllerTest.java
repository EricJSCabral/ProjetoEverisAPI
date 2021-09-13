package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.service.ClienteService;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

@RunWith(SpringRunner.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteController clienteController;

    @MockBean
    private ClienteService clienteService;

    @BeforeEach
    public void setup(){
        standaloneSetup(this.clienteController);
    }

    @Test
    public void consultarDeveConsultarCliente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/?codigo=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listarDeveListarTodosOsClientes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cliente/listar"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void salvarDeveSalvarCliente() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nome", "Eric Cabral");
        jsonObject.put("cpf", "49026915829");
        jsonObject.put("endereco", "Rua Palestra Itália, 222");
        jsonObject.put("telefone", "11941384278");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente/salvar")
                .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deletarDeveDeletarCliente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cliente/delete?codigo=1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void atualizarDeveSalvarCliente() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nome", "Eric Cabral");
        jsonObject.put("cpf", "49026915829");
        jsonObject.put("endereco", "Rua Palestra Itália, 222");
        jsonObject.put("telefone", "11941384278");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cliente/atualizar?codigo=1")
                        .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //    @Test
//    public void consultar_deveRetornarSucesso(){
////        when(this.clienteService.consultar(1))
////                .thenReturn(new ClienteModel(1, "Eric Cabral", "49026915829", "Rua Paulista, 123",
////                        "11941384288"));
//
//        given()
//                    .accept(ContentType.JSON)
//                .when()
//                    .get("/api/cliente/")
//                .then()
//                    .statusCode(HttpStatus.OK.value());
//    }
}
