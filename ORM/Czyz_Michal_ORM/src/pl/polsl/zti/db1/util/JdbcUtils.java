package pl.polsl.zti.db1.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import pl.polsl.zti.db1.ConfigConsts;

public final class JdbcUtils {

    private static final DataSource dataSource;

    private JdbcUtils() {
    }

    static {
        final MysqlDataSource mysqlds = new MysqlDataSource();
        mysqlds.setServerName(ConfigConsts.SERVER_NAME);
        mysqlds.setPort(ConfigConsts.SERVER_PORT);
        mysqlds.setDatabaseName(ConfigConsts.DATABASE_NAME);
        mysqlds.setUser(ConfigConsts.USERNAME);
        mysqlds.setPassword(ConfigConsts.PASSWORD);
        dataSource = mysqlds;
    }

    /**
     * Tworzy i zwraca połączenie do bazy danych.
     *
     * @return Połączenie do bazy danych.
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        final Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * Zwraca wartosc pierwszej kolumny z pierwszego wiersza (przydatne dla
     * zapytan zwracajacych jedna wartosc)
     *
     * @param sql Zapytanie do wykonania
     * @return Odczytna wartosc lub null gdy zapytanie nie zwrocilo wynikow
     */
    public static Object executeScalar(String sql) {
        Object result = null;
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = rs.getObject(1);
            }
        } catch (SQLException ex) {
            handleSqlException(ex);
        } finally {
            closeSilently(stmt, con);
        }

        return result;
    }

    /**
     * Przywraca stan bazy (tabele i ich zawartosc) na podstawie podanej
     * definicji schematu
     *
     * @param schemaDef
     */
    public static void restoreDbSchema(DbSchemaDef schemaDef) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            // usun tabele
            for (String tableName : schemaDef.getTablesNames()) {
                stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            }

            // stworz tabele
            for (String tableDef : schemaDef.getTablesDef()) {
                stmt.executeUpdate("CREATE TABLE " + tableDef);
            }

            // zaladuj dane
            for (String d : schemaDef.getData()) {
                stmt.addBatch(d);
            }
            stmt.executeBatch();

        } catch (SQLException ex) {
            handleSqlException(ex);
        } finally {
            closeSilently(stmt, con);
        }
    }

    public static void closeSilently(Statement stmt, Connection con) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            handleSqlException(ex);
        }
    }

    public static void handleSqlException(SQLException ex) {
        ex.printStackTrace();
    }
}
