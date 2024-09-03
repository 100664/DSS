package data;

import business.clientes.Cliente;
import business.clientes.FichaVeiculo;
import business.clientes.Veiculo;
import business.servicos.Servico;
import business.servicos.ServicoPrestado;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VeiculosDAO implements Map<String,Veiculo>{
    private static VeiculosDAO singleton = null;

    private VeiculosDAO(){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS veiculos(" +
                    "type INT NOT NULL," +
                    "matricula VARCHAR(8) NOT NULL,"+
                    "marca VARCHAR(50) NOT NULL,"+
                    "modelo VARCHAR(50) NOT NULL," +
                    "quilometragem INT NOT NULL," +
                    "ano INT NOT NULL,"+
                    "data_revisao DATETIME NOT NULL," +
                    "nifcliente VARCHAR(9) NOT NULL," +
                    "FOREIGN KEY (nifcliente) REFERENCES clientes (nif),"+
                    "PRIMARY KEY (matricula));";
            String sql2 = "CREATE TABLE IF NOT EXISTS veiculos_servicos ( /**/\n" +
                    "    veiculo_matricula VARCHAR(8) NOT NULL,\n" +
                    "    servico_id INT NOT NULL,\n" +
                    "    PRIMARY KEY (veiculo_matricula,servico_id),\n" +
                    "    FOREIGN KEY (veiculo_matricula) REFERENCES veiculos(matricula),\n" +
                    "    FOREIGN KEY (servico_id) REFERENCES servicos(id)\n" +
                    ");";
            String sql3 = "CREATE TABLE IF NOT EXISTS servicosPrestados ( /**/\n" +
                    "    id INT NOT NULL AUTO_INCREMENT,\n" +
                    "    funcionario_username VARCHAR(50) NOT NULL,\n" +
                    "    servico_id INT NOT NULL,\n" +
                    "    inicio DATETIME NOT NULL,\n" +
                    "    fim DATETIME NOT NULL,\n" +
                    "    PRIMARY KEY (id),\n" +
                    "    FOREIGN KEY (funcionario_username) REFERENCES funcionarios (username),\n" +
                    "    FOREIGN KEY (servico_id) REFERENCES servicos (id)\n" +
                    ");";
            String sql4 = "CREATE TABLE IF NOT EXISTS veiculos_servicosPrestados ( /**/\n" +
                    "    veiculo_matricula VARCHAR(8) NOT NULL,\n" +
                    "    servicosPrestados_id INT NOT NULL AUTO_INCREMENT,\n" +
                    "    PRIMARY KEY (veiculo_matricula, servicosPrestados_id),\n" +
                    "    FOREIGN KEY (veiculo_matricula) REFERENCES veiculos(matricula),\n" +
                    "    FOREIGN KEY (servicosPrestados_id) REFERENCES servicosPrestados(id)\n" +
                    "\n" +
                    ");";

            stm.executeUpdate(sql);
            stm.executeUpdate(sql2);
            stm.executeUpdate(sql3);
            stm.executeUpdate(sql4);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar a tabela Veiculos: " + e.getMessage());
        }
    }

    public static VeiculosDAO getInstance() {
        if (VeiculosDAO.singleton == null)
            VeiculosDAO.singleton = new VeiculosDAO();
        return VeiculosDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM veiculos")) {
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
                     stm.executeQuery("SELECT matricula FROM veiculos WHERE matricula='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Veiculo v = (Veiculo) value;
        return this.containsKey(v.getMatricula());
    }
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT maticula FROM veiculos")) {
            while (rs.next()) {
                String idu = rs.getString("nif");
                res.add(idu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public Set<Map.Entry<String, Veiculo>> entrySet() {
        return this.keySet().stream().map(k -> Map.entry(k, this.get(k))).collect(Collectors.toSet());
    }

    public Veiculo get(Object key) {
        Veiculo v = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM veiculos WHERE matricula='"+key+"'")) {
            if (rs.next())
            {
                v = new Veiculo(
                        rs.getInt("type"),
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("quilometragem"),
                        rs.getInt("ano"),
                        rs.getTimestamp("data_revisao").toLocalDateTime(),
                        new FichaVeiculo(),
                        rs.getString("nifcliente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }


    public Veiculo put(String key, Veiculo v) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO veiculos (type, matricula, marca, modelo, quilometragem, ano, data_revisao, nifcliente)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "type = VALUES(type),\n" +
                    "marca = VALUES(marca),\n" +
                    "modelo = VALUES(modelo),\n" +
                    "quilometragem = VALUES(quilometragem),\n" +
                    "ano = VALUES(ano),\n" +
                    "data_revisao = VALUES(data_revisao),\n" +
                    "nifcliente = VALUES(nifcliente);\n")){
                pstm.setInt(1,v.getType());
                pstm.setString(2, key);
                pstm.setString(3, v.getMarca());
                pstm.setString(4, v.getModelo());
                pstm.setInt(5, v.getQuilometragem());
                pstm.setInt(6, v.getAno());
                pstm.setTimestamp(7, Timestamp.valueOf(v.getData_revisao()));
                pstm.setString(8, v.getNif());
                pstm.executeUpdate();
            }
            for (Servico servico : v.getFicha().getServicosporfazer()) {
                try (PreparedStatement pstm2 = conn.prepareStatement("INSERT INTO veiculos_servicos (veiculo_matricula, servico_id)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "veiculo_matricula = VALUES(veiculo_matricula),\n" +
                        "servico_id = VALUES(servico_id);\n")) {
                    pstm2.setString(1, key);
                    pstm2.setInt(2, servico.getId());
                    pstm2.executeUpdate();
                }
            }
            for (ServicoPrestado servico : v.getFicha().getServicos()) {
                try (PreparedStatement pstm3 = conn.prepareStatement("INSERT INTO servicosPrestados (id, funcionario_username, servico_id, inicio, fim)\n" +
                        "VALUES (?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "funcionario_username = VALUES(funcionario_username),\n" +
                        "servico_id = VALUES(servico_id),\n" +
                        "inicio = VALUES(inicio),\n" +
                        "fim = VALUES(fim);\n")) {
                    pstm3.setString(2, servico.getFuncionario().getUsername());
                    pstm3.setInt(3, servico.getServico().getId());
                    pstm3.setTimestamp(4, Timestamp.valueOf(servico.getHorario().getInicio()));
                    pstm3.setTimestamp(5, Timestamp.valueOf(servico.getHorario().getFim()));
                    pstm3.executeUpdate();
                }

                try (PreparedStatement pstm4 = conn.prepareStatement("INSERT INTO veiculos_servicosPrestados (veiculo_matricula, servicoPrestado_id)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "veiculo_matricula = VALUES(veiculo_matricula),\n" +
                        "servicoPrestado_id = VALUES(servicoPrestado_id);\n")) {
                    pstm4.setString(1, key);
                    pstm4.executeUpdate();
                }

            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }

    public void putAll(Map<? extends String, ? extends Veiculo> v) {
        v.keySet().forEach(i -> this.put(i, v.get(i)));
    }

    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE veiculos");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public Veiculo remove(Object key) {
       Veiculo v = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM veiculos WHERE matricula = ?")){
                v = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate();
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return v;
    }

    public Collection<Veiculo> values() {
        Collection<Veiculo> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT matricula FROM veiculos")) {
            while (rs.next()) {
                String idt = rs.getString("matricula");
                Veiculo v = this.get(idt);
                res.add(v);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
}
