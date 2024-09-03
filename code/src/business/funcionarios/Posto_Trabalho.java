package business.funcionarios;

import business.clientes.Veiculo;
import business.servicos.Servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Posto_Trabalho {

    private int id;
    private Map<Integer, Servico> servicos;

    private Map <Veiculo, List<Servico>> servicosPorFazer;

    public Posto_Trabalho(int id, Map<Integer, Servico> servicos, Map<Veiculo, List<Servico>> servicosPorFazer) {
        this.id = id;
        this.servicos = servicos;
        this.servicosPorFazer = servicosPorFazer;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Servico> getServicos() {
        return servicos;
    }

    public void setServicos(Map<Integer, Servico> servicos) {
        this.servicos = servicos;
    }

    public Map<Veiculo, List<Servico>> getServicosPorFazer() {
        return servicosPorFazer;
    }

    public void addServicos (Veiculo veiculo, Servico servico){
        if(this.servicosPorFazer.containsKey(veiculo)){
            this.servicosPorFazer.get(veiculo).add(servico);
        }
        else {
            List <Servico> servicos = new ArrayList<>();
            servicos.add(servico);
            this.servicosPorFazer.put(veiculo, servicos);
        }
    }

    public void removeServicos (Veiculo veiculo, Servico servico){
        if(this.servicosPorFazer.containsKey(veiculo)) {
            List<Servico> servicos = this.servicosPorFazer.get(veiculo);
            if (servicos.size() == 1) {
                this.servicosPorFazer.remove(veiculo);
            } else {
                servicos.remove(servico);
                this.servicosPorFazer.put(veiculo, servicos);
            }
        }
    }
    public void setServicosPorFazer(Map<Veiculo, List<Servico>> servicosPorFazer) {
        this.servicosPorFazer = servicosPorFazer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posto_Trabalho that)) return false;
        return id == that.id && Objects.equals(servicos, that.servicos) && Objects.equals(servicosPorFazer, that.servicosPorFazer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, servicos, servicosPorFazer);
    }

}
