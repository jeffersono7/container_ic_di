package br.com.container.aplicacao;

import br.com.container.aplicacao.usuario.controllers.UsuarioController;
import br.com.container.aplicacao.usuario.repositories.memory.UsuarioMemoryRepository;
import br.com.container.aplicacao.usuario.services.impl.UsuarioServiceImpl;
import br.com.container.framework.Container;

public class Aplicacao {

    public static void main(String... args) {
        Container container = Container.getInstance()
                .registry(UsuarioMemoryRepository.class)
                .registry(UsuarioServiceImpl.class)
                .registry(UsuarioController.class);

        var controller = container.get(UsuarioController.class);

        controller.criar();
    }
}
