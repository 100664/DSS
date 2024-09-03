package business.clientes;

import business.funcionarios.Funcionario;
import business.servicos.ServicoPrestado;
import business.workshop.Horario;
import business.servicos.Servico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FichaVeiculo {
    private List<Servico> servicosporfazer;
    private List <ServicoPrestado> servicos;
    private LocalDateTime data_ultima_atualizacao;

    public FichaVeiculo() {
        this.servicosporfazer = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.data_ultima_atualizacao = null;
    }

    public FichaVeiculo(List<Servico> servicosporfazer, List<ServicoPrestado> servicos, LocalDateTime data_ultima_atualizacao) {
        this.servicosporfazer = servicosporfazer;
        this.servicos = servicos;
        this.data_ultima_atualizacao = data_ultima_atualizacao;
    }

    public List <ServicoPrestado> getServicos() {
        return servicos;
    }

    public void setServicos(List <ServicoPrestado> servicos) {
        this.servicos = servicos;
    }

    public void addServicos (LocalDateTime fim, Servico servico, Funcionario funcionario){
        Horario h = new Horario(this.data_ultima_atualizacao, fim);
        ServicoPrestado s = new ServicoPrestado(funcionario, servico, h);
    }
    public void addServicosPorFazer (Servico servico){
        this.servicosporfazer.add(servico);
    }

    public LocalDateTime getData_ultima_atualizacao() {
        return data_ultima_atualizacao;
    }

    public void setData_ultima_atualizacao(LocalDateTime data_ultima_atualizacao) {
        this.data_ultima_atualizacao = data_ultima_atualizacao;
    }

    public List<Servico> getServicosporfazer() {
        return servicosporfazer;
    }

    public void setServicosporfazer(List<Servico> servicosporfazer) {
        this.servicosporfazer = servicosporfazer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FichaVeiculo that)) return false;
        return Objects.equals(servicosporfazer, that.servicosporfazer) && Objects.equals(servicos, that.servicos) && Objects.equals(data_ultima_atualizacao, that.data_ultima_atualizacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(servicosporfazer, servicos, data_ultima_atualizacao);
    }
}
