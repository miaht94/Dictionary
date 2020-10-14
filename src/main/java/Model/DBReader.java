package Model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


/**
 * The type Db reader.
 */
public class DBReader {
    private static Connection connection = null;
    private final String dbName = "./dictionary.db";
    private String table =  Dictionary.currType.toString();
    private static DBReader dbReader = null;
    private static Map<DictionaryType, DBReader> dbReaders = new HashMap<>();

    public Connection getConnection() {
        return this.connection;
    }

    private DBReader() {
        //URL url = loadFile(dbName);
        if (connection == null) {
            try {
                // create a database connection
                //System.out.println(url.getPath());
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbName) /*url.getPath())*/;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private DBReader(DictionaryType type) {
        this.table = type.toString();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static DBReader getInstance() {
        if (dbReader == null) {
            dbReader = new DBReader();
        }
        return dbReader;
    }

    public String getTable() {
        return table;
    }

    public static DBReader getInstance(DictionaryType type) {
        if (dbReader == null) {
            dbReader = new DBReader();
        }
        if (dbReaders.get(type) == null) dbReaders.put(type, new DBReader(type));
        return dbReaders.get(type);
    }

    ResultSet executeQuery(String query) throws SQLException {
        if (connection == null) throw new NullPointerException("Connection NULL : Can't connect to the database");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        return statement.executeQuery(query);
    }

    int executeUpdate(String query) throws SQLException {
        if (connection == null) throw new NullPointerException("Connection NULL : Can't connect to the database");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        return statement.executeUpdate(query);
    }

    ResultSet getRows(int index, int amount) {
        if (index < 0) throw new IllegalArgumentException();
        if (amount == 0) return null;
        try {
            ResultSet rs = this.executeQuery("Select *,_rowid_ From " + this.table + " where _rowid_ between " + index + " and " + (index + amount));
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void delRow(int index) {
        if (index <= 0) throw new IllegalArgumentException();
        try {
            this.executeUpdate("Delete from " + this.table + " where _rowid_ = " + index);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
