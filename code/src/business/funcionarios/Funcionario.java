package business.funcionarios;

import business.servicos.Servico;
import business.workshop.Horario;

import java.util.Map;
import java.util.Objects;

public class Funcionario {
    private String nome;
    private int idade;
    private String username;
    private String password;
    private boolean isAdmin;
    private EmTurno emTurno;

    private Map<Integer, Servico> servicos;

    private Map <Horario, Integer> horario;



    public Funcionario(String nome, int idade, String username, String password, boolean isAdmin, EmTurno emTurno, Map<Integer, Servico> servicos, Map<Horario, Integer> horario) {
        this.nome = nome;
        this.idade = idade;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.emTurno = emTurno;
        this.servicos = servicos;
        this.horario = horario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public EmTurno getEmTurno() {
        return emTurno;
    }
    public void setEmTurno (EmTurno emTurno){
        this.emTurno = emTurno;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Map<Horario, Integer> getHorario() {
        return horario;
    }

    public void setHorario(Map<Horario, Integer> horario) {
        this.horario = horario;
    }

    public Map<Integer, Servico> getServicos() {
        return servicos;
    }

    public void setServicos(Map<Integer, Servico> servicos) {
        this.servicos = servicos;
    }


    public void addHorario (){
        this.horario.put(this.emTurno.getHorario(), this.emTurno.getId_posto());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Funcionario that)) return false;
        return idade == that.idade && isAdmin == that.isAdmin && emTurno == that.emTurno && Objects.equals(nome, that.nome) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(servicos, that.servicos) && Objects.equals(horario, that.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, idade, username, password, isAdmin, emTurno, servicos, horario);
    }
}
