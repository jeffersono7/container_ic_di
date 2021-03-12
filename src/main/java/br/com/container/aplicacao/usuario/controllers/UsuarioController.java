package br.com.container.aplicacao.usuario.controllers;

import br.com.container.aplicacao.usuario.Usuario;
import br.com.container.aplicacao.usuario.services.UsuarioService;

public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    public void criar() {
        var usuario = new Usuario(null, "jefferson", "jefferson@email.com", "password");

        service.criar(usuario);

        System.out.println(usuario);
    }
}
