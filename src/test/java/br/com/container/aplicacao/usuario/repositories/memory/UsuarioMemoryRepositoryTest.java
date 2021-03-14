package br.com.container.aplicacao.usuario.repositories.memory;

import br.com.container.aplicacao.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMemoryRepositoryTest {

    @Test
    void salvar_deveSalvar() {
        var repository = new UsuarioMemoryRepository();
        var usuario = new Usuario(null, "nome", "email@email.com", "asdf123");

        var result = repository.salvar(usuario);

        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void salvar_deveAtualizar() {
        var repository = new UsuarioMemoryRepository();
        var usuario = new Usuario(null, "nome", "email@email.com", "asdf123");

        var usuarioSalvo = repository.salvar(usuario);

        var result = repository.salvar(usuarioSalvo.withNome("outro nome"));

        assertEquals(usuarioSalvo, result);
        assertEquals("outro nome", result.getNome());
    }
}