package business.funcionarios;

import java.time.LocalDateTime;
import java.util.Map;

import business.clientes.Veiculo;
import business.servicos.Servico;
import data.FuncionariosDAO;

public class SSFuncionariosFacade implements ISSFuncionarios{
    private Map <String, Funcionario> funcionarios;

    public Map<String, Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Map<String, Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public SSFuncionariosFacade() {
        this.funcionarios = FuncionariosDAO.getInstance();
    }

    public boolean existefuncionario (String username) {
        return this.funcionarios.containsKey(username);
    }

    public void registafuncionario (String nome, int idade, String username, String password, boolean isAdmin) {
        this.funcionarios.put(username, new Funcionario(nome, idade, username, password, isAdmin,new EmTurno(false, 0, null, new FazerServico(false, null, null)), null, null));
    }

    public void atualizafuncionario (Funcionario funcionario){
        this.funcionarios.put(funcionario.getUsername(), funcionario);
    }

    public boolean validafuncionariopassword (String username, String password) {
        Funcionario f = this.funcionarios.get(username);
        return f != null && f.getPassword().compareTo(password) == 0;
    }

    public Funcionario iniciarTurno (String username, Posto_Trabalho posto) {
        Funcionario f = this.funcionarios.get(username);
        boolean b = false;
        for (Map.Entry <Integer, Servico> entry : posto.getServicos().entrySet()){
            if (f.getServicos().containsKey(entry.getKey())){
                b = true;
                break;
            }
        }
        if(b) {
            f.getEmTurno().setEmTurno(b);
            f.getEmTurno().setId_posto(posto.getId());
            f.getEmTurno().setInicioH(LocalDateTime.now());
        }
        return f;
    }
    public Funcionario finalizarTurno (String username) {
        Funcionario f = this.funcionarios.get(username);
        f.getEmTurno().setEmTurno(false);
        f.getEmTurno().setFimH(LocalDateTime.now());
        f.addHorario();
        return f;
    }

    public Funcionario iniciarServico (Veiculo veiculo, Servico servico, String username){
        Funcionario f = this.funcionarios.get(username);
        f.getEmTurno().getFazerServico().setFazerServico(true);
        f.getEmTurno().getFazerServico().setVeiculo(veiculo);
        f.getEmTurno().getFazerServico().setServico(servico);
        veiculo.getFicha().setData_ultima_atualizacao(LocalDateTime.now());
        veiculo.getFicha().getServicosporfazer().remove(servico);
        return f;
    }
    public Funcionario finalizarServico (String username){
        Funcionario f = this.funcionarios.get(username);
        f.getEmTurno().getFazerServico().getVeiculo().getFicha().addServicos(LocalDateTime.now(), f.getEmTurno().getFazerServico().getServico(), f);
        f.getEmTurno().getFazerServico().setFazerServico(false);
        return f;
    }
}
