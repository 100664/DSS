package business.workshop;

import business.clientes.Veiculo;
import business.funcionarios.EmTurno;
import business.funcionarios.FazerServico;
import business.funcionarios.Funcionario;
import business.clientes.Cliente;
import business.funcionarios.Posto_Trabalho;
import business.servicos.Servico;

import java.util.Map;
import java.util.Objects;

public class Workshop {
    private int id;
    private String nome;
    private String localidade;

    private Horario horario;

    private Map<String, Cliente> clientes;
    private Map<String, Funcionario> funcionarios;

    private Map <Integer, Posto_Trabalho> postos;

    public Workshop(int id, String nome, String localidade, Horario horario, Map<String, Cliente> clientes, Map<String, Funcionario> funcionarios, Map<Integer, Posto_Trabalho> postos) {
        this.id = id;
        this.nome = nome;
        this.localidade = localidade;
        this.horario = horario;
        this.clientes = clientes;
        this.funcionarios = funcionarios;
        this.postos = postos;
    }

    public void registacliente(String nif, Cliente cliente){
        this.clientes.put(nif, cliente);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Map<String, Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Map<String, Cliente> clientes) {
        this.clientes = clientes;
    }

    public Map<String, Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Map<String, Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public void RegistaFuncionarios(String nome, int idade, String username, String password, boolean isAdmin) {
        this.funcionarios.put(username, new Funcionario(nome, idade, username, password, isAdmin,new EmTurno(false, 0, null, new FazerServico(false, null, null)), null, null));
    }

    public Map<Integer, Posto_Trabalho> getPosto() {
        return postos;
    }

    public void setPosto(Map<Integer, Posto_Trabalho> postos) {
        this.postos = postos;
    }

    public void alteraPosto (Posto_Trabalho posto, Veiculo v, Servico s) {
        this.postos.get(posto.getId()).addServicos(v, s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workshop workshop)) return false;
        return id == workshop.id && Objects.equals(nome, workshop.nome) && Objects.equals(localidade, workshop.localidade) && Objects.equals(horario, workshop.horario) && Objects.equals(clientes, workshop.clientes) && Objects.equals(funcionarios, workshop.funcionarios) && Objects.equals(postos, workshop.postos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, localidade, horario, clientes, funcionarios, postos);
    }
}
