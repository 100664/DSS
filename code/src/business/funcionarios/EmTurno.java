package business.funcionarios;

import business.workshop.Horario;

import java.time.LocalDateTime;
import java.util.Objects;

public class EmTurno {
    private boolean emTurno;
    private int id_posto;

    private Horario horario;

    private FazerServico fazerServico;

    public EmTurno(boolean emTurno, int posto, Horario horario, FazerServico fazerServico) {
        this.emTurno = emTurno;
        this.id_posto = posto;
        this.horario = horario;
        this.fazerServico = fazerServico;
    }

    public boolean isEmTurno() {
        return emTurno;
    }

    public void setEmTurno(boolean emTurno) {
        this.emTurno = emTurno;
    }

    public int getId_posto() {
        return id_posto;
    }

    public void setId_posto(int id_posto) {
        this.id_posto = id_posto;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public void setInicioH(LocalDateTime inicio){
        this.horario.setInicio(inicio);
    }

    public void setFimH(LocalDateTime fim){
        this.horario.setFim(fim);
    }

    public FazerServico getFazerServico() {
        return fazerServico;
    }

    public void setFazerServico(FazerServico fazerServico) {
        this.fazerServico = fazerServico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmTurno emTurno1)) return false;
        return emTurno == emTurno1.emTurno && id_posto == emTurno1.id_posto && Objects.equals(horario, emTurno1.horario) && Objects.equals(fazerServico, emTurno1.fazerServico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emTurno, id_posto, horario, fazerServico);
    }
}
