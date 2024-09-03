package business.clientes;

import java.util.Map;
import java.util.Objects;

public class Cliente {
    private String nif;
    private String nome;
    private String morada;
    private String contacto;

    private int valorvachouers;
    private Map<String, Veiculo> veiculos;

    public Cliente(String nif, String nome, String morada, String contacto, int valorvachouers, Map<String, Veiculo> veiculos) {
        this.nif = nif;
        this.nome = nome;
        this.morada = morada;
        this.contacto = contacto;
        this.valorvachouers = valorvachouers;
        this.veiculos = veiculos;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public int getValorvachouers() {
        return valorvachouers;
    }

    public void setValorvachouers(int valorvachouers) {
        this.valorvachouers = valorvachouers;
    }

    public Map<String, Veiculo> getVeiculos() {
        return veiculos;
    }

    public void addVeiculo(String matricula, Veiculo veiculo){
        this.veiculos.put(matricula, veiculo);
    }

    public void setVeiculos(Map<String, Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return Objects.equals(nif, cliente.nif) && Objects.equals(nome, cliente.nome) && Objects.equals(morada, cliente.morada) && Objects.equals(contacto, cliente.contacto) && Objects.equals(veiculos, cliente.veiculos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif, nome, morada, contacto, veiculos);
    }
}
