package br.com.container.aplicacao;

import br.com.container.aplicacao.usuario.controllers.UsuarioController;
import br.com.container.framework.Container;

public class Aplicacao {

    public static void main(String... args) {
        Container container = Container.getInstance(Aplicacao.class);

        var controller = container.get(UsuarioController.class);

        controller.criar();
    }
}
