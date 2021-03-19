package br.com.container.aplicacao.usuario.controllers;

import br.com.container.aplicacao.usuario.Usuario;
import br.com.container.aplicacao.usuario.services.UsuarioService;
import br.com.container.framework.annotations.Component;

@Component(scope = Component.Scope.PROTOTYPE)
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    public Usuario criar() {
        var usuario = new Usuario(null, "jefferson", "jefferson@email.com", "password");

        var usuarioSalvo = service.criar(usuario);

        System.out.println(usuarioSalvo);

        return usuarioSalvo;
    }
}
