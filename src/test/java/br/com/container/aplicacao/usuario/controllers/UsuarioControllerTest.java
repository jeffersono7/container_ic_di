package br.com.container.aplicacao.usuario.controllers;

import br.com.container.aplicacao.usuario.Usuario;
import br.com.container.aplicacao.usuario.services.UsuarioService;
import org.junit.jupiter.api.Test;

import static br.com.container.supports.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioControllerTest {

    @Test
    void criar_deveCriarUmUsuario() {
        var usuario = new Usuario(null, "nome", "nome@testador.com", "12345");
        var usuarioSalvo = usuario.withId(1);
        var mockService = createMock(UsuarioService.class);

        when(UsuarioService.class, mockService.criar(any(Usuario.class))).thenReturn(usuarioSalvo);

        var controller = new UsuarioController(mockService);

        var result = controller.criar();

        assertNotNull(result);
        assertNotNull(result.getId());
    }
}