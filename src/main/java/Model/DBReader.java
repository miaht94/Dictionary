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
    private DBReader() {
        URL url = DBReader.class.getClassLoader().getResource(dbName);
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + url.getPath());
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

    List<Word> getAllColumn(String columnLabel) throws SQLException {
        ResultSet rs = this.executeQuery("Select * From definitions");
        List<Word> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Word("", rs.getString(columnLabel), ""));
        }
        rs = null;
        return list;
    }

    public void close() throws SQLException {
        this.connection.close();
    }


}
