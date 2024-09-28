/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frmFrame;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Admin
 */
public class ConDB {
    public static Connection ketnoiDB(){
        Connection con=null;
        try {
            //B1: Nạp trình điều khiển
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLSIEUTHI;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "1306";
            con=DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
