package com.everis.BancoAPI.controller;

import com.everis.BancoAPI.service.ContaService;
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
@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaController contaController;

    @MockBean
    private ContaService contaService;

    @BeforeEach
    public void setup(){
        standaloneSetup(this.contaController);
    }

    @Test
    public void consultarDeveConsultarContas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/conta?numero=1234"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void listarDeveListarTodosOsClientes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/conta/listar"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void salvarDeveSalvarConta() throws Exception {
        JSONObject clienteJson = new JSONObject();
        clienteJson.put("codigo", 1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("agencia", "12345");
        jsonObject.put("numero", "1234");
        jsonObject.put("digitoVerificador", "9");
        jsonObject.put("tipo", "FISICA");
        jsonObject.put("cliente", clienteJson);
        jsonObject.put("saldo", 100.0);
        jsonObject.put("saques", 0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/conta/salvar")
                        .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deletarDeveDeletarConta() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/conta/delete?numero=1115"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void atualizarDeveAtualizarConta() throws Exception {
        JSONObject clienteJson = new JSONObject();
        clienteJson.put("codigo", 1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("agencia", "12345");
        jsonObject.put("numero", "1234");
        jsonObject.put("digitoVerificador", "9");
        jsonObject.put("tipo", "FISICA");
        jsonObject.put("cliente", clienteJson);
        jsonObject.put("saldo", 100.0);
        jsonObject.put("saques", 0);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/conta/atualizar?codigo=1")
                        .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void depositarDeveSomarSaldo() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("valor", 1000);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/conta/depositar?numero=1164")
                        .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void sacarDeveSubtrairSaldo() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("valor", 1000);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/conta/sacar?numero=1164")
                        .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TransferirDeveTransferirSaldo() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("valor", 1000);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/conta/transferir?numero1=1164&numero2=1165")
                        .content(String.valueOf(jsonObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void extratoDeveExibirOperacoes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/conta/extrato/?numero=1234"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void extratoDeveExibirTudo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/conta/extrato/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}