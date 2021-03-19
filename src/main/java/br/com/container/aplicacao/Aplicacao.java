package br.com.container.aplicacao;

import br.com.container.aplicacao.usuario.controllers.UsuarioController;
import br.com.container.framework.Container;

public class Aplicacao {

    public static void main(String... args) {
        Container container = Container.getInstance(Aplicacao.class);

        var controller1 = container.get(UsuarioController.class);
        controller1.criar();

        var controller2 = container.get(UsuarioController.class);
        controller2.criar();
    }
}
