package com.example.ecommerce;
import java.sql.*;

public class DbConnection {

    public  final String dbUrl="jdbc:mysql://localhost:3306/ecommerce";

    private  final String userName= "root";

    private  final String passWord="Aniket@123";

    private Statement getStatement(){
        try{
            Connection connection=  DriverManager.getConnection(dbUrl,userName,passWord);
            return connection.createStatement();
        }
        catch (Exception e){
                e.printStackTrace();
        }

        return null;

    }
    public ResultSet getQueryTable(String query){

        try{
            Statement statement= getStatement();
            return statement.executeQuery(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }
    
    public int updateDatabase(String query)
    {
        try{
            Statement statement= getStatement();
            return statement.executeUpdate(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    public static void main(String[] args) {
        DbConnection con= new DbConnection();
        ResultSet rs= con.getQueryTable("SELECT * FROM customer");

        if(rs!=null){
            System.out.println("Connection Successful");
        }
        else{
            System.out.println("Connection Failed  ");
        }
    }

}
