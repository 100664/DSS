package business.servicos;

import business.funcionarios.Funcionario;
import business.workshop.Horario;

public class ServicoPrestado {
    private Funcionario funcionario;
    private Servico servico;
    private Horario horario;

    public ServicoPrestado(Funcionario funcionario, Servico servico, Horario horario) {
        this.funcionario = funcionario;
        this.servico = servico;
        this.horario = horario;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }
}
