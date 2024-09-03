package business.clientes;


import java.time.LocalDateTime;
import java.util.Objects;

public class Veiculo {
    private int type;
    /*
     *  1- gasolina
     *  2- diesel
     *  3- eletrico
     *  4- hibrido gasolina
     *  5- hibrido diesel
     */
    private String matricula;
    private String marca;
    private String modelo;
    private int quilometragem;
    private int ano;
    private LocalDateTime data_revisao;

    private FichaVeiculo ficha;

    private String nif;

    public Veiculo(int type, String matricula, String marca, String modelo, int quilometragem, int ano, LocalDateTime data_revisao, FichaVeiculo ficha, String nif) {
        this.type = type;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.quilometragem = quilometragem;
        this.ano = ano;
        this.data_revisao = data_revisao;
        this.ficha = ficha;
        this.nif = nif;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public LocalDateTime getData_revisao() {
        return data_revisao;
    }

    public void setData_revisao(LocalDateTime data_revisao) {
        this.data_revisao = data_revisao;
    }

    public FichaVeiculo getFicha() {
        return ficha;
    }

    public void setFicha(FichaVeiculo ficha) {
        this.ficha = ficha;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo veiculo)) return false;
        return type == veiculo.type && quilometragem == veiculo.quilometragem && ano == veiculo.ano && Objects.equals(matricula, veiculo.matricula) && Objects.equals(marca, veiculo.marca) && Objects.equals(modelo, veiculo.modelo) && Objects.equals(data_revisao, veiculo.data_revisao) && Objects.equals(ficha, veiculo.ficha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, matricula, marca, modelo, quilometragem, ano, data_revisao, ficha);
    }
}
