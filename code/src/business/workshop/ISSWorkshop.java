package business.workshop;

import business.clientes.Veiculo;
import business.servicos.Servico;

import java.util.Map;

public interface ISSWorkshop {
    Map<Integer, Workshop> getWorkshops();
    Workshop PedirServico (Veiculo veiculo, Servico servico, int id);
    Workshop finalizarServico (int id, int posto,Veiculo veiculo, Servico servico);
    void atualizaworkshop (Workshop workshop);
    void registacliente (int id, String nif, String nome, String morada, String contacto, int valorvouchers, Map<String, Veiculo> veiculos);
}
