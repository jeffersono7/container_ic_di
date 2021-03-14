package br.com.container.aplicacao.usuario.services.impl;

import br.com.container.aplicacao.usuario.Usuario;
import br.com.container.aplicacao.usuario.repositories.UsuarioRepository;
import br.com.container.aplicacao.usuario.services.UsuarioService;
import org.junit.jupiter.api.Test;

import static br.com.container.supports.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioServiceImplTest {

    private UsuarioService service;

    @Test
    void criar_deveCriarUsuario() {
        var repository = createMock(UsuarioRepository.class);
        service = new UsuarioServiceImpl(repository);

        var usuario = new Usuario(null, "nome", "email@email.com", "senha123");
        var usuarioSalvo = usuario.withId(1);

        when(UsuarioRepository.class, repository.salvar(any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        var result = service.criar(usuario);

        assertNotNull(result);
        assertEquals(usuarioSalvo, result);
    }
}