package data;

import business.funcionarios.EmTurno;
import business.funcionarios.FazerServico;
import business.funcionarios.Funcionario;
import business.funcionarios.Posto_Trabalho;
import business.servicos.Servico;
import business.workshop.Horario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FuncionariosDAO implements Map<String, Funcionario>{
    private static FuncionariosDAO singleton = null;

    private FuncionariosDAO(){
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS funcionarios (" +
                    "nome VARCHAR(150) NOT NULL," +
                    "idade INT NOT NULL," +
                    "usename VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "isAdmin INT NOT NULL," +
                    "emTurno INT NOT NULL," +
                    "posto INT," +
                    "inicio DATETIME," +
                    "fim DATETIME," +
                    "fazerServico INT NOT NULL," +
                    "carro VARCHAR(8)," +
                    "servico INT,"+
                    "FOREIGN KEY (carro) REFERENCES veiculos (matricula) ,"+
                    "FOREIGN KEY (servico) REFERENCES servicos (id),"+
                    "FOREIGN KEY (posto) REFERENCES postoTrabalho(id),"+
                    "PRIMARY KEY (usename));";

            String sql2 = "CREATE TABLE IF NOT EXISTS funcionario_servico (" +
                    "    funcionario_username VARCHAR(50) NOT NULL," +
                    "    servico_id INT NOT NULL," +
                    "    PRIMARY KEY (funcionario_username, servico_id)," +
                    "    FOREIGN KEY (funcionario_username) REFERENCES funcionarios (username)," +
                    "    FOREIGN KEY (servico_id) REFERENCES servicos(id)" +
                    ");";
            String sql3 = "CREATE TABLE IF NOT EXISTS funcionario_postoTrabalho ( /**/\n" +
                    "    funcionario_username VARCHAR(50) NOT NULL,\n" +
                    "    posto_id INT NOT NULL,\n" +
                    "    inicio DATETIME NOT NULL,\n" +
                    "    fim DATETIME NOT NULL,\n" +
                    "    PRIMARY KEY (funcionario_username, posto_id),\n" +
                    "    FOREIGN KEY (funcionario_username) REFERENCES funcionarios (username),\n" +
                    "    FOREIGN KEY (posto_id) REFERENCES servicos(id)\n" +
                    ");";

            stm.executeUpdate(sql);
            stm.executeUpdate(sql2);
            stm.executeUpdate(sql3);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar a tabela Funcionarios: " + e.getMessage());
        }
    }

    public static FuncionariosDAO getInstance() {
        if (FuncionariosDAO.singleton == null)
            FuncionariosDAO.singleton = new FuncionariosDAO();
        return FuncionariosDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM funcionarios")) {
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
                     stm.executeQuery("SELECT username FROM funcionarios WHERE username='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Funcionario t = (Funcionario) value;
        return this.containsKey(t.getUsername());
    }
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT username FROM funcionarios")) {
            while (rs.next()) {
                String idu = rs.getString("username");
                res.add(idu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public Set<Map.Entry<String, Funcionario>> entrySet() {
        return this.keySet().stream().map(k -> Map.entry(k, this.get(k))).collect(Collectors.toSet());
    }

    public Funcionario get(Object key) {
        Funcionario f = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM funcionarios WHERE username='"+key+"'")) {
            if (rs.next())
            {
                Timestamp inicioTimestamp = rs.getTimestamp("inicio");
                Timestamp fimTimestamp = rs.getTimestamp("fim");

                LocalDateTime inicioLocalDateTime = (inicioTimestamp != null) ? inicioTimestamp.toLocalDateTime() : null;
                LocalDateTime fimLocalDateTime = (fimTimestamp != null) ? fimTimestamp.toLocalDateTime() : null;
                f = new Funcionario(rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("isAdmin") == 1 ? true : false,
                        new EmTurno(rs.getInt("emTurno") == 1 ? true : false, rs.getInt("posto"),
                        new Horario(inicioLocalDateTime, fimLocalDateTime),
                        new FazerServico (rs.getInt("fazerServico") == 1 ? true : false,VeiculosDAO.getInstance().get(rs.getString("carro")), ServicosDAO.getInstance().get(rs.getInt("servico")))),
                        getservicos3(rs.getString("username")),
                        getpostos(rs.getString("username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    public Map <Horario, Integer> getpostos (String key){
        Map <Horario, Integer> postos = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT *\n" +
                     "FROM funcionario_postoTrabalho\n" +
                     "WHERE funcionario_username = '"+key+"'")){
            while (rs.next()){
                Horario h = new Horario(rs.getTimestamp("inicio").toLocalDateTime(),
                        rs.getTimestamp("fim").toLocalDateTime());
                int p =rs.getInt("posto_id");
                postos.put(h, p);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return postos;
    }

    public Map<Integer, Servico> getservicos3 (String key){
        Map<Integer, Servico> servicos = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT servico_id\n" +
                     "FROM funcionario_servico\n" +
                     "WHERE funcionario_username = '"+key+"'")) {
            while (rs.next()) {

                Servico s = ServicosDAO.getInstance().get(rs.getInt("servico_id"));;

                servicos.put(s.getId() ,s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return servicos;
    }

    public Funcionario put(String key, Funcionario f) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("INSERT INTO funcionarios (nome, idade, username, password, isAdmin, emTurno, posto, inicio, fim, fazerServico, carro, servico)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "nome = VALUES(nome),\n" +
                    "idade = VALUES(idade),\n" +
                    "password = VALUES(password),\n" +
                    "isAdmin = VALUES(isAdmin),\n" +
                    "emTurno = VALUES(emTurno),\n" +
                    "posto = VALUES(posto),\n" +
                    "inicio = VALUES(inicio),\n" +
                    "fim = VALUES(fim),\n" +
                    "fazerServico = VALUES(fazerServico),\n" +
                    "carro = VALUES(carro),\n" +
                    "servico = VALUES(servico);")){
                    pstm.setString(1,f.getNome());
                    pstm.setInt(2, f.getIdade());
                    pstm.setString(3, key);
                    pstm.setString(4, f.getPassword());
                    pstm.setInt(5, f.isAdmin() ? 1 : 0);
                    pstm.setInt(6, f.getEmTurno().isEmTurno() ? 1 : 0);
                    if(f.getEmTurno().isEmTurno()) {
                        pstm.setInt(7, f.getEmTurno().getId_posto());
                        pstm.setTimestamp(8, Timestamp.valueOf(f.getEmTurno().getHorario().getInicio()));
                        if (f.getEmTurno().getHorario().getFim() != null) {
                            pstm.setTimestamp(9, Timestamp.valueOf(f.getEmTurno().getHorario().getFim()));
                        }
                        else{
                            pstm.setNull(9, Types.VARCHAR);
                        }
                    }
                    else {
                        pstm.setNull(7, Types.VARCHAR);
                        pstm.setNull(8, Types.VARCHAR);
                        pstm.setNull(9, Types.VARCHAR);
                    }
                    pstm.setInt(10, f.getEmTurno().getFazerServico().isFazerServico() ? 1 : 0);
                    if (f.getEmTurno().getFazerServico().getVeiculo() != null) {
                        pstm.setString(11, f.getEmTurno().getFazerServico().getVeiculo().getMatricula());
                    } else {
                        pstm.setNull(11, Types.VARCHAR); // Ajuste o tipo SQL conforme necessário
                    }

                    if (f.getEmTurno().getFazerServico().getServico() != null) {
                        pstm.setInt(12, f.getEmTurno().getFazerServico().getServico().getId());
                    } else {
                        pstm.setNull(12, Types.INTEGER); // Ajuste o tipo SQL conforme necessário
                    }
                    pstm.executeUpdate();
            }
            if (f.getServicos() != null) {
                for (Map.Entry<Integer, Servico> entry : f.getServicos().entrySet()) {
                    try (PreparedStatement pstm2 = conn.prepareStatement("INSERT INTO funcionario_servico (funcionario_username, servico_id)\n" +
                            "VALUES (?, ?)\n" +
                            "ON DUPLICATE KEY UPDATE\n" +
                            "funcionario_username = VALUES(funcionario_username),\n" +
                            "servico_id = VALUES(servico_id);")) {
                        pstm2.setString(1, key);
                        pstm2.setInt(2, entry.getValue().getId());
                        pstm2.executeUpdate();
                    }
                }
            }
            if (f.getHorario() != null) {
                for (Map.Entry<Horario, Integer> entry : f.getHorario().entrySet()) {
                    try (PreparedStatement pstm2 = conn.prepareStatement("INSERT INTO funcionario_postoTrabalho (funcionario_username, posto_id, inicio, fim)\n" +
                            "VALUES (?, ?, ?, ?)\n" +
                            "ON DUPLICATE KEY UPDATE\n" +
                            "funcionario_username = VALUES(funcionario_username),\n" +
                            "posto_id = VALUES(posto_id),\n" +
                            "inicio = VALUES(inicio),\n" +
                            "fim = VALUES(fim);")) {
                        pstm2.setString(1, key);
                        pstm2.setInt(2, entry.getValue());
                        pstm2.setTimestamp(3, Timestamp.valueOf(entry.getKey().getInicio()));
                        pstm2.setTimestamp(4, Timestamp.valueOf(entry.getKey().getFim()));
                        pstm2.executeUpdate();
                    }
                }
            }

        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    public void putAll(Map<? extends String, ? extends Funcionario> f) {
        f.keySet().forEach(i -> this.put(i, f.get(i)));
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE funcionarios");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public Funcionario remove(Object key) {
        Funcionario f = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM funcionarios WHERE username = ?")){
                f = this.get(key);
                pstm.setString(1,(String)key);
                pstm.executeUpdate();
            }
        }catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return f;
    }

    public Collection<Funcionario> values() {
        Collection<Funcionario> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT username FROM funcionarios")) {
            while (rs.next()) {
                String idt = rs.getString("username");
                Funcionario f = this.get(idt);
                res.add(f);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }
}
