package Model;

import javafx.collections.ObservableList;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Db reader.
 */
public class DBReader {
    private Connection connection = null;
    private final String dbName = "dictionary.db";
    private static DBReader dbReader = null;
    public Connection getConnection() {
        return this.connection;
    }

    public URL loadFile(String input){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(dbName);
        return resource;
    }

    private DBReader() {
        URL url = loadFile(dbName);
        try
        {
            // create a database connection
            System.out.println(url.getPath());
            connection = DriverManager.getConnection("jdbc:sqlite::resource:" + dbName) /*url.getPath())*/;
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
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

    ResultSet executeQuery(String query) throws SQLException {
        if (connection == null) throw new NullPointerException("Connection NULL : Can't connect to the database");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        return statement.executeQuery(query);
    }

    ResultSet getRows(int index, int amount) {
        if (index < 0) throw new IllegalArgumentException();
        if (amount == 0) return null;
        try {
            ResultSet rs = this.executeQuery("Select * From definitions where _rowid_ between " + index + " and " + (index + amount));
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


}
