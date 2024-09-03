package data;

import business.servicos.Servico;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServicosDAO implements Map<Integer, Servico>{
    private static ServicosDAO singleton = null;

    private ServicosDAO(){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS servicos(" +
                    "id INT NOT NULL,"+
                    "nome VARCHAR(50) NOT NULL,"+
                    "duracao_media INT NOT NULL," +
                    "preco DECIMAL(8,2) NOT NULL," +
                    "PRIMARY KEY (id));";
            String sql2 = "CREATE TABLE IF NOT EXISTS servicos_type ( /**/\n" +
                    "    id INT NOT NULL,\n" +
                    "    type INT NOT NULL,\n" +
                    "    PRIMARY KEY (id, type),\n" +
                    "    FOREIGN KEY (id) REFERENCES servicos (id)\n" +
                    ");";

            stm.executeUpdate(sql);
            stm.executeUpdate(sql2);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar a tabela Servicos: " + e.getMessage());
        }
    }

    public static ServicosDAO getInstance() {
        if (ServicosDAO.singleton == null)
            ServicosDAO.singleton = new ServicosDAO();
        return ServicosDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM servicos")) {
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
                     stm.executeQuery("SELECT id FROM servicos WHERE id='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Servico s = (Servico) value;
        return this.containsKey(s.getId());
    }
    public Set<Integer> keySet() {
        Set<Integer> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM servicos")) {
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

    public Set<Map.Entry<Integer, Servico>> entrySet() {
        return this.keySet().stream().map(k -> Map.entry(k, this.get(k))).collect(Collectors.toSet());
    }


    public Servico get(Object key) {
        Servico s = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM servicos WHERE id='"+key+"'")) {
            if (rs.next())
            {
                s = new Servico(
                        gettypes(rs.getInt("id")),
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("duracao_media"),
                        rs.getFloat("preco"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }

    public List<Integer> gettypes (int key) {
        List<Integer> types = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT type FROM servicos_type WHERE id='"+key+"'")) {
            while(rs.next()){
                types.add(rs.getInt("type"));
            }
        } catch (SQLException e) {
        e.printStackTrace();
        throw new NullPointerException(e.getMessage());
        }
        return types;
    }

    @Override
    public Servico put(Integer integer, Servico servico) {
        return null;
    }

    public Servico put(int key, Servico s) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO servicos (id, nome, duracao_media, preco)\n" +
                    "VALUES (?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "id = VALUES(id), nome = VALUES(nome), duracao_media = VALUES(duracao_media), preco = VALUES(preco);\n")){
                pstm.setInt(1,key);
                pstm.setString(2, s.getNome());
                pstm.setInt(3, s.getDuracao_media());
                pstm.setFloat(4, s.getPreco());
                pstm.executeUpdate();
            }
            for (int type : s.getTipos_veiculos()){
                try (PreparedStatement pstm2 = conn.prepareStatement("INSERT INTO servicos_type (id, type)\n" +
                        "VALUES (?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "id = VALUES(id), type = VALUES(type);\n")){
                    pstm2.setInt(1, key);
                    pstm2.setInt(2, type);
                    pstm2.executeUpdate();
                }
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }

    public void putAll(Map<? extends Integer, ? extends Servico> s) {
        s.keySet().forEach(i -> this.put(i, s.get(i)));
    }

    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE servicos");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public Servico remove(Object key) {
        Servico s = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM servicos WHERE id = ?")){
                s = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate();
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return s;
    }

    public Collection<Servico> values() {
        Collection<Servico> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM servicos")) {
            while (rs.next()) {
                String idt = rs.getString("id");
                Servico s = this.get(idt);
                res.add(s);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
}
