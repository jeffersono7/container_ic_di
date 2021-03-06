package br.com.container.aplicacao.usuario.services.impl;

import br.com.container.aplicacao.usuario.Usuario;
import br.com.container.aplicacao.usuario.repositories.UsuarioRepository;
import br.com.container.aplicacao.usuario.services.UsuarioService;
import br.com.container.framework.annotations.Component;

@Component(scope = Component.Scope.PROTOTYPE)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario criar(Usuario usuario) {
        return usuarioRepository.salvar(usuario);
    }
}
