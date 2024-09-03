package business.servicos;

import data.ServicosDAO;

import java.util.Map;
import java.util.Objects;

public class SSServicosFacade implements ISSServicos{
    private Map<Integer, Servico> servicos;

    public SSServicosFacade() {
        this.servicos = ServicosDAO.getInstance();
    }

    public Map<Integer, Servico> getServicos() {
        return servicos;
    }

    public void setServicos(Map<Integer, Servico> servicos) {
        this.servicos = servicos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SSServicosFacade that)) return false;
        return Objects.equals(getServicos(), that.getServicos());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServicos());
    }
}
