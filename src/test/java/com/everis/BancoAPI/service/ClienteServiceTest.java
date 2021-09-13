package com.everis.BancoAPI.service;

import com.everis.BancoAPI.model.ClienteModel;
import com.everis.BancoAPI.repository.ClienteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteModel cliente(){
        ClienteModel c = new ClienteModel(1, "Eric", "49026915829", "Rua paulista, 155",
                "11944556677");
        return c;
    }
    private ClienteModel cliente2(){
        ClienteModel c = new ClienteModel("Eric", "49026915829", "Rua paulista, 155",
                "11944556677");
        return c;
    }

    @Test
    public void salvarDeveSalvarCliente(){
        ClienteModel c1 = cliente();

        when(repository.save(any(ClienteModel.class))).thenReturn(new ClienteModel());

        ResponseEntity<?> cliente = clienteService.salvar(c1);

        assertThat(c1.getCodigo().equals(cliente));
    }

    @Test
    public void consultarClienteDeveRetornarCliente(){
        ClienteModel cliente = new ClienteModel();
        cliente = cliente();

        when(repository.findById(cliente.getCodigo())).thenReturn(Optional.of(cliente));

        ResponseEntity expected = clienteService.consultar(cliente.getCodigo());

        assertThat(cliente.getCodigo().equals(expected));
        verify(repository).findById(cliente.getCodigo());
    }

    @Test
    public void listarClienteJaExiste(){
        List<ClienteModel> cliente = new ArrayList();

        cliente.add(new ClienteModel());

        given(repository.findAll()).willReturn(cliente);

        ResponseEntity<List<?>> expected = clienteService.listar();

        assertEquals(cliente, expected);
        verify(repository).findAll();
    }

    @Test
    public void deletarDeveDeletarCliente(){
        ClienteModel cliente = new ClienteModel();
        cliente = cliente();

        when(repository.findById(cliente.getCodigo())).thenReturn(Optional.of(cliente));

        clienteService.deletar(cliente.getCodigo());

        verify(repository).deleteById(cliente.getCodigo());
    }

    @Test
    public void atualizarDeveAtualizarCliente(){
        ClienteModel cliente;
        cliente = cliente();

        ClienteModel c2;
        c2 = cliente2();
        c2.setNome("José Roberto");

        given(repository.findById(cliente.getCodigo())).willReturn(Optional.of(cliente));

        clienteService.atualizar(cliente.getCodigo(), c2);
        verify(repository).save(c2);
        verify(repository).findById(cliente.getCodigo());
    }

}