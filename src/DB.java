import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String url = "jdbc:postgresql://vinson.liara.cloud:33839/nice_wing";
    private static final String user = "root";
    private static final String password = "4qQixj5bMn4DGH7nGEjUlSli";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
