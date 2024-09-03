package business.workshop;

import business.clientes.Cliente;
import business.clientes.Veiculo;
import business.funcionarios.Posto_Trabalho;
import business.servicos.Servico;
import data.WorkshopsDAO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SSWorkshopFacade implements ISSWorkshop{
    private Map<Integer, Workshop> workshops;

    public SSWorkshopFacade() {
        this.workshops = WorkshopsDAO.getInstance();
    }

    public Map<Integer, Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(Map<Integer, Workshop> workshops) {
        this.workshops = workshops;
    }

    public void atualizaworkshop (Workshop workshop) {
        this.workshops.put(workshop.getId(), workshop);
    }


    public void registacliente (int id, String nif, String nome, String morada, String contacto, int valorvouchers, Map<String, Veiculo> veiculos){
        this.workshops.get(id).registacliente(nif,  new Cliente(nif, nome, morada, contacto, valorvouchers, veiculos));
    }

    public Workshop finalizarServico (int id, int posto,Veiculo veiculo, Servico servico){
        Workshop w = this.workshops.get(id);
        w.getPosto().get(posto).removeServicos(veiculo, servico);
        return w;
    }

    public Workshop PedirServico (Veiculo veiculo, Servico servico, int id){
        Workshop w = this.workshops.get(id);
        Posto_Trabalho posto = null;
        int i;
        int min = 0;
        int PostoCheckUp = encontrarChaveMenorValor(id);
        if (servico.getId() == 1){
            posto = w.getPosto().get(PostoCheckUp);
        }
        else {
            for (Map.Entry<Integer, Posto_Trabalho> entry :w.getPosto().entrySet()) {
                if (entry.getValue().getServicos().containsKey(servico.getId())) {
                    i = calcula_tempo(entry.getValue());
                    if (i < min || min == 0) {
                        posto = entry.getValue();
                        min = i;
                    }
                }
            }
        }
        w.alteraPosto(posto, veiculo, servico);
        return w;
    }
    private int encontrarChaveMenorValor(int id) {
        if (this.workshops.get(id).getPosto() == null || this.workshops.get(id).getPosto().isEmpty()) {
            throw new IllegalArgumentException("O mapa não pode ser nulo ou vazio.");
        }

        int menorValor = 0;

        for (Map.Entry<Integer, Posto_Trabalho> entry : this.workshops.get(id).getPosto().entrySet()) {
            int valorAtual = entry.getKey(); // Substitua com a propriedade desejada

            if (valorAtual < menorValor || menorValor == 0) {
                menorValor = valorAtual;
            }
        }

        return menorValor;
    }
    private int calcula_tempo (Posto_Trabalho posto){
        int i = 0;
        for (Map.Entry<Veiculo, List<Servico>> entry : posto.getServicosPorFazer().entrySet()){
            for(Servico s : entry.getValue()){
                i += s.getDuracao_media();
            }
        }
        return i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SSWorkshopFacade that)) return false;
        return Objects.equals(getWorkshops(), that.getWorkshops());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorkshops());
    }

}
