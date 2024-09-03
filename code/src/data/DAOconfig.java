package data;

public class DAOconfig {
    static final String USERNAME = "root";
    static final String PASSWORD = "arrozdebatata";
    private static final String DATABASE = "esideal";

    //private static final String DRIVER = "jdbc:mariadb";
    private static final String DRIVER = "jdbc:mysql";
    static final String URL = DRIVER+"://localhost:3307/"+DATABASE;
}
