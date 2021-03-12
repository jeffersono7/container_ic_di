package br.com.container.aplicacao.usuario;

import java.util.Objects;
import java.util.Optional;

public class Usuario {

    private final Integer id;
    private final String nome;
    private final String email;
    private final String senhaRaw;
    private final String senhaHash;

    public Usuario(Integer id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaRaw = senha;
        this.senhaHash = generateHash(senha);
    }

    public Usuario withId(Integer id) {
        return new Usuario(id, this.nome, this.email, this.senhaRaw);
    }

    public Usuario withNome(String nome) {
        return new Usuario(this.id, nome, this.email, this.senhaRaw);
    }

    public Usuario withEmail(String email) {
        return new Usuario(this.id, this.nome, email, this.senhaRaw);
    }

    public Usuario withSenha(String senha) {
        return new Usuario(this.id, this.nome, this.email, senha);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senhaHash;
    }

    @Override
    public Usuario clone() {
        return new Usuario(id, nome, email, senhaRaw);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senhaHash='" + senhaHash + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private String generateHash(String senha) {
        return Optional.of(senha)
                .map(Objects::hash)
                .map(i -> i + 100)
                .map(Objects::toString)
                .orElse(null);
        // somente para exemplo mesmo
    }
}
