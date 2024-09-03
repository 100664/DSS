package business.servicos;

import java.util.List;
import java.util.Objects;

public class Servico {
    private int id;
    private List<Integer> tipos_veiculos;
    /* aplicavel a que tipos deveiculos:
     *  1- gasolina
     *  2- diesel
     *  3- eletrico
     *  4- hibrido gasolina
     *  5- hibrido diesel
     */
    private String nome;
    private int duracao_media;
    private float preco;

    public Servico(List<Integer> tipos_veiculos, int id, String nome, int duracao_media, float preco) {
        this.id = id;
        this.nome = nome;
        this.duracao_media = duracao_media;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getTipos_veiculos() {
        return tipos_veiculos;
    }

    public void setTipos_veiculos(List<Integer> tipos_veiculos) {
        this.tipos_veiculos = tipos_veiculos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuracao_media() {
        return duracao_media;
    }

    public void setDuracao_media(int duracao_media) {
        this.duracao_media = duracao_media;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servico servico)) return false;
        return getId() == servico.getId() && getDuracao_media() == servico.getDuracao_media() && Float.compare(getPreco(), servico.getPreco()) == 0 && Objects.equals(getTipos_veiculos(), servico.getTipos_veiculos()) && Objects.equals(getNome(), servico.getNome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTipos_veiculos(), getNome(), getDuracao_media(), getPreco());
    }
}



