package business.funcionarios;

import business.clientes.Veiculo;
import business.servicos.Servico;

import java.util.Objects;

public class FazerServico {
    private boolean fazerServico;
    private Veiculo veiculo;
    private Servico servico;

    public FazerServico(boolean fazerServico, Veiculo veiculo, Servico servico) {
        this.fazerServico = fazerServico;
        this.veiculo = veiculo;
        this.servico = servico;
    }

    public boolean isFazerServico() {
        return fazerServico;
    }

    public void setFazerServico(boolean fazerServico) {
        this.fazerServico = fazerServico;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FazerServico that)) return false;
        return fazerServico == that.fazerServico && Objects.equals(veiculo, that.veiculo) && Objects.equals(servico, that.servico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fazerServico, veiculo, servico);
    }
}
