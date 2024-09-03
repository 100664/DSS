package data;

import business.clientes.Veiculo;
import business.funcionarios.Posto_Trabalho;
import business.servicos.Servico;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class PostosTrabalhoDAO implements Map<Integer, Posto_Trabalho>{
    private static PostosTrabalhoDAO singleton = null;

    private PostosTrabalhoDAO(){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS postotrabalho (" +
                    "id INT NOT NULL," +
                    "PRIMARY KEY (id));";
            String sql2 = "CREATE TABLE IF NOT EXISTS posto_servico ( /**/\n" +
                    "    posto_id INT NOT NULL,\n" +
                    "    servico_id INT NOT NULL,\n" +
                    "    PRIMARY KEY (posto_id, servico_id),\n" +
                    "    FOREIGN KEY (posto_id) REFERENCES postoTrabalho(id),\n" +
                    "    FOREIGN KEY (servico_id) REFERENCES servicos(id)\n" +
                    ");";
            String sql3 = "CREATE TABLE IF NOT EXISTS posto_veiculo ( /**/\n" +
                    "    posto_id INT NOT NULL,\n" +
                    "    veiculo_id VARCHAR(8) NOT NULL,\n" +
                    "    PRIMARY KEY (posto_id, veiculo_id),\n" +
                    "    FOREIGN KEY (posto_id) REFERENCES postoTrabalho(id),\n" +
                    "    FOREIGN KEY (veiculo_id) REFERENCES veiculos(matricula)\n" +
                    ");";

            stm.executeUpdate(sql);
            stm.executeUpdate(sql2);
            stm.executeUpdate(sql3);


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar a tabela Servicos: " + e.getMessage());
        }
    }

    public static PostosTrabalhoDAO getInstance() {
        if (PostosTrabalhoDAO.singleton == null)
            PostosTrabalhoDAO.singleton = new PostosTrabalhoDAO();
        return PostosTrabalhoDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM postoTrabalho")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT id FROM postoTrabalho WHERE id='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Posto_Trabalho p = (Posto_Trabalho) value;
        return this.containsKey(p.getId());
    }
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM postoTrabalho")) {
            while (rs.next()) {
                int idu = rs.getInt("id");
                res.add(idu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public Set<Map.Entry<Integer, Posto_Trabalho>> entrySet() {
        return this.keySet().stream().map(k -> Map.entry(k, this.get(k))).collect(Collectors.toSet());
    }


    public Posto_Trabalho get(Object key) {
        Posto_Trabalho pt = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM postoTrabalho WHERE id='"+key+"'")) {
            if (rs.next())
            {
                pt = new Posto_Trabalho(rs.getInt("id"),
                        getservicos(rs.getInt("id")),
                        getveiculos(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return pt;
    }

    public Map <Integer, Servico> getservicos (int key){
        Map<Integer, Servico> servicos = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT servico_id\n" +
                     "FROM posto_servico\n" +
                     "WHERE posto_id = '"+key+"'")) {
            while (rs.next()) {

                Servico s = ServicosDAO.getInstance().get(rs.getInt("servico_id"));

                servicos.put(s.getId() ,s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return servicos;
    }

    public List<Servico> getservicos2 (String key){
        List<Servico> servicos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT servico_id\n" +
                     "FROM veiculos_servicos\n" +
                     "WHERE veiculo_matricula = '"+key+"'")) {
            while (rs.next()) {
                servicos.add(ServicosDAO.getInstance().get(rs.getInt("servico_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return servicos;
    }

    public Map <Veiculo, List<Servico>> getveiculos (int key){
        Map <Veiculo, List<Servico>> veiculos = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT veiculo_id\n" +
                     "FROM posto_veiculo\n" +
                     "WHERE posto_id = '"+key+"'")){
            while (rs.next()){
                Veiculo v = getveiculo(rs.getString("veiculo_id"));
                List<Servico> servicos = getservicos2(v.getMatricula());
                veiculos.put(v, servicos);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return veiculos;
    }

    public Veiculo getveiculo (String key){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT matricula\n" +
                     "FROM veiculos\n" +
                     "WHERE matricula = '"+key+"'")){
            if (rs.next()){
                return VeiculosDAO.getInstance().get(rs.getString("matrciula"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    public Posto_Trabalho put(Integer key, Posto_Trabalho pt) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO postoTrabalho (id)\n" +
                    "VALUES (?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "id = VALUES(id);")){
                pstm.setInt(1, key);
                pstm.executeUpdate();
            }
            for (Map.Entry<Integer, Servico> entry : pt.getServicos().entrySet()){
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO posto_servico (posto_id, servico_id)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "posto_id = VALUES(posto_id), servico_id = VALUES(servico_id);")){
                    pstm.setInt(1, key);
                    pstm.setInt(2, entry.getValue().getId());
                    pstm.executeUpdate();
                }
            }
            for (Map.Entry<Veiculo, List<Servico>> entry : pt.getServicosPorFazer().entrySet()){
                try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO posto_veiculo (posto_id, veiculo_id)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "posto_id = VALUES(posto_id), veiculo_id = VALUES(veiculo_id);")){
                    pstm.setInt(1, key);
                    pstm.setString(2, entry.getKey().getMatricula());
                    pstm.executeUpdate();
                }
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return pt;
    }



    public void putAll(Map<? extends Integer, ? extends Posto_Trabalho> pt) {
        pt.keySet().forEach(i -> this.put(i, pt.get(i)));
    }

    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE postos_trabalho");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public Posto_Trabalho remove(Object key) {
        Posto_Trabalho t = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM postos_trabalho WHERE id = ?")){
                t = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate();
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    public Collection<Posto_Trabalho> values() {
        Collection<Posto_Trabalho> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM postos_trabalho")) {
            while (rs.next()) {
                String idt = rs.getString("id");
                Posto_Trabalho t = this.get(idt);
                res.add(t);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
}
