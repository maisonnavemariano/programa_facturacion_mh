package controller.db;

import java.sql.*;

public class DBEngine {
	
	public DBEngine(){
		try{
			String myDriver = "com.mysql.jdbc.Driver";
		    String myUrl    = "jdbc:mysql://localhost/programa_facturacion_mh";
		    Class.forName(myDriver);
		    Connection conn = DriverManager.getConnection(myUrl, "root", "maisonnave1");
		    
		    String query = "SELECT * FROM Cliente";
		    
		    Statement st = conn.createStatement();
		    
		    ResultSet rs = st.executeQuery(query);
		    
		    
		    while (rs.next()){
		    	int Codigo_Cliente = rs.getInt("Codigo_Cliente");
		    	String CUIT = rs.getString("CUIT");
		    	String denominacion = rs.getString("Denominacion");
		    	System.out.println(Codigo_Cliente + ", "+CUIT+ ", "+denominacion+".");
		    	
		    }
		    st.close();
		}catch(Exception e){e.printStackTrace();}
	}

}
