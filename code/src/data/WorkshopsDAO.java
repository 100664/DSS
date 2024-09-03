package data;

import business.clientes.Cliente;
import business.clientes.FichaVeiculo;
import business.clientes.Veiculo;
import business.funcionarios.Funcionario;
import business.funcionarios.Posto_Trabalho;
import business.servicos.Servico;
import business.workshop.Horario;
import business.workshop.Workshop;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class WorkshopsDAO implements Map<Integer, Workshop> {
    private static WorkshopsDAO singleton = null;

    private WorkshopsDAO(){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS workshops(" +
                    "id INT NOT NULL," +
                    "nome VARCHAR(50) NOT NULL,"+
                    "localidade VARCHAR(50) NOT NULL,"+
                    "inicio DATETIME NOT NULL," +
                    "fim DATETIME NOT NULL," +
                    "PRIMARY KEY (id));";
            String sql2 = "CREATE TABLE IF NOT EXISTS workshops_postos (\n" +
                    "     workshop_id INT NOT NULL,\n" +
                    "     posto_id INT NOT NULL,\n" +
                    "     PRIMARY KEY (workshop_id, posto_id),\n" +
                    "     FOREIGN KEY (workshop_id) REFERENCES workshops (id),\n" +
                    "     FOREIGN KEY (posto_id) REFERENCES postoTrabalho (id)\n" +
                    ");";
            String sql3 = "CREATE TABLE IF NOT EXISTS workshops_funcionarios (\n" +
                    "     workshop_id INT NOT NULL,\n" +
                    "     funcionario_id VARCHAR(50) NOT NULL,\n" +
                    "     PRIMARY KEY (workshop_id, funcionario_id),\n" +
                    "     FOREIGN KEY (workshop_id) REFERENCES workshops (id),\n" +
                    "     FOREIGN KEY (funcionario_id) REFERENCES funcionarios (username)\n" +
                    ");";
            String sql4 = "CREATE TABLE IF NOT EXISTS workshops_clientes (\n" +
                    "    workshop_id INT NOT NULL,\n" +
                    "    cliente_nif VARCHAR(9) NOT NULL,\n" +
                    "    PRIMARY KEY (workshop_id, cliente_nif),\n" +
                    "    FOREIGN KEY (workshop_id) REFERENCES workshops (id),\n" +
                    "    FOREIGN KEY (cliente_nif) REFERENCES clientes (nif)\n" +
                    ");";
            stm.executeUpdate(sql);
            stm.executeUpdate(sql2);
            stm.executeUpdate(sql3);
            stm.executeUpdate(sql4);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar a tabela Workshops: " + e.getMessage());
        }
    }

    public static WorkshopsDAO getInstance() {
        if (WorkshopsDAO.singleton == null)
            WorkshopsDAO.singleton = new WorkshopsDAO();
        return WorkshopsDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM workshops")) {
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
                     stm.executeQuery("SELECT id FROM workshops WHERE id='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Workshop w = (Workshop) value;
        return this.containsKey(w.getId());
    }
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM workshops")) {
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

    public Set<Entry<Integer, Workshop>> entrySet() {
        return this.keySet().stream().map(k -> Map.entry(k, this.get(k))).collect(Collectors.toSet());
    }

    public Workshop get(Object key) {
        Workshop w = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM workshops WHERE id='"+key+"'")) {
            if (rs.next())
            {
                w = new Workshop(rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("localidade"),
                        new Horario(rs.getTimestamp("inicio").toLocalDateTime(),
                                rs.getTimestamp("fim").toLocalDateTime()),
                        getclientes(rs.getInt("id")),
                        getfuncionarios(rs.getInt("id")),
                        getpostos(rs.getInt("id")));
                //TODO: não está bem por causa da compatibilidade de tipos, corrigir tambem na proxima funcao
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return w;
    }

    public Map <Integer, Posto_Trabalho> getpostos (int key){
        Map <Integer, Posto_Trabalho> postos = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT posto_id\n" +
                     "FROM workshops_postos\n" +
                     "WHERE workshop_id = '"+key+"'")){
            while (rs.next()){
                Posto_Trabalho p = PostosTrabalhoDAO.getInstance().get(rs.getInt("posto_id"));
                postos.put(p.getId(), p);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return postos;
    }


    public Map <String, Cliente> getclientes (int key){
        Map <String, Cliente> clientes = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT cliente_nif\n" +
                     "FROM workshops_clientes\n" +
                     "WHERE workshop_id = '"+key+"'")){
            while (rs.next()){
                Cliente c = ClientesDAO.getInstance().get(rs.getString("cliente_nif"));
                clientes.put(c.getNif(), c);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return clientes;
    }

    public Map <String, Funcionario> getfuncionarios (int key){
        Map <String, Funcionario> funcionarios = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT funcionario_id\n" +
                     "FROM workshops_funcionarios\n" +
                     "WHERE workshop_id = '"+key+"'")){
            while (rs.next()){
                Funcionario f = FuncionariosDAO.getInstance().get(rs.getString("funcionario_id"));
                funcionarios.put(f.getUsername(), f);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return funcionarios;
    }

    public Workshop put(Integer key, Workshop w) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO workshops (id, nome, localidade, inicio, fim)\n" +
                    "VALUES (?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "nome = VALUES(nome),\n" +
                    "localidade = VALUES(localidade),\n" +
                    "inicio = VALUES(inicio),\n" +
                    "fim = VALUES(fim);\n")){
                pstm.setInt(1,key);
                pstm.setString(2, w.getNome());
                pstm.setString(3, w.getLocalidade());
                pstm.setTimestamp(4, Timestamp.valueOf(w.getHorario().getInicio()));
                pstm.setTimestamp(5, Timestamp.valueOf(w.getHorario().getFim()));
                pstm.executeUpdate();
            }
            for (Map.Entry<Integer, Posto_Trabalho> entry : w.getPosto().entrySet()) {
                try (PreparedStatement pstm2 = conn.prepareStatement("INSERT INTO workshops_postos (workshop_id, posto_id)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "workshop_id = VALUES(workshop_id),\n" +
                        "posto_id = VALUES(posto_id);\n")) {
                    pstm2.setInt(1, key);
                    pstm2.setInt(2, entry.getKey());
                    pstm2.executeUpdate();
                }
            }
            for (Map.Entry<String, Funcionario> entry : w.getFuncionarios().entrySet()) {
                try (PreparedStatement pstm3 = conn.prepareStatement("INSERT INTO workshops_funcionarios (workshop_id, funcionario_id)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "workshop_id = VALUES(workshop_id),\n" +
                        "funcionario_id = VALUES(funcionario_id);\n")) {
                    pstm3.setInt(1, key);
                    pstm3.setString(2, entry.getKey());
                    pstm3.executeUpdate();
                }
            }
            for (Map.Entry<String, Cliente> entry : w.getClientes().entrySet()) {
                try (PreparedStatement pstm4 = conn.prepareStatement("INSERT INTO workshops_clientes (workshop_id, cliente_nif)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "workshop_id = VALUES(workshop_id),\n" +
                        "cliente_nif = VALUES(cliente_nif);\n")) {
                    pstm4.setInt(1, key);
                    pstm4.setString(2, entry.getKey());
                    pstm4.executeUpdate();
                }
            }

        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return w;
    }

    public void putAll(Map<? extends Integer, ? extends Workshop> w) {
        w.keySet().forEach(i -> this.put(i, w.get(i)));
    }

    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE workshops");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public Workshop remove(Object key) {
        Workshop w = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM workshops WHERE id = ?")){
                w = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate();
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return w;
    }

    public Collection<Workshop> values() {
        Collection<Workshop> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM workshops")) {
            while (rs.next()) {
                String idt = rs.getString("matricula");
                Workshop w = this.get(idt);
                res.add(w);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
}
