package br.com.container.aplicacao.usuario.repositories.memory;

import br.com.container.aplicacao.usuario.Usuario;
import br.com.container.aplicacao.usuario.repositories.UsuarioRepository;
import br.com.container.framework.annotations.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UsuarioMemoryRepository implements UsuarioRepository {

    private static int id = 0;

    private final Map<Integer, Usuario> usuarios = new HashMap<Integer, Usuario>();

    synchronized public static Integer obterId() {
        return ++id;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        var proximoId = usuario.getId();

        if (proximoId == null) {
            return salvarNovo(usuario);
        }

        var usuarioPersistente = usuario.clone();

        usuarios.replace(proximoId, usuarioPersistente);

        return usuario;
    }

    private Usuario salvarNovo(Usuario usuario) {
        var proximoId = obterId();

        var usuarioPersistente = usuario.withId(proximoId);

        usuarios.put(proximoId, usuarioPersistente);

        return usuarioPersistente.clone();
    }
}
