package business.funcionarios;

import business.clientes.Veiculo;
import business.servicos.Servico;
import java.util.Map;

public interface ISSFuncionarios {
    boolean existefuncionario (String username);
    Map<String, Funcionario> getFuncionarios();
    void registafuncionario (String nome, int idade, String username, String password, boolean isAdmin);
    public void atualizafuncionario (Funcionario funcionario);

    boolean validafuncionariopassword (String username, String password);
    Funcionario iniciarTurno (String username, Posto_Trabalho posto);
    Funcionario finalizarTurno (String username);

    Funcionario iniciarServico (Veiculo veiculo, Servico servico, String username);
    Funcionario finalizarServico (String username);

}
