package sample.Utility;

import java.sql.*;

/** This class queries a database */
public class DBQuery {
    private static String query;
    private static PreparedStatement stmt;
    private static ResultSet result;

    /** This method executes a database query. It reads in a query and if necessary initializes a ResultSet.
     @param sql a SQL query string */
    public static void makeQuery(String sql){
        query = sql;
        try {
            stmt= DBConnection.getConnection().prepareStatement(sql);
            if(query.toLowerCase().startsWith("select"))
                result = stmt.executeQuery();
            if(query.toLowerCase().startsWith("delete")
                    || query.toLowerCase().startsWith("insert")
                    || query.toLowerCase().startsWith("update"))
                stmt.executeUpdate();

        }
        catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    /** This method queries a database to return a ResultSet.
     @return Returns a ResultSet */
    public static ResultSet runQueryToRead(String sql) {
        makeQuery(sql);
        return result;
    }
}
