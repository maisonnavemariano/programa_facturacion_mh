package controller.db;

import java.sql.*;
import java.util.List;

public class DBEngine {
	protected Connection conn;
	protected final String myDriver = "com.mysql.jdbc.Driver";
	protected String myUrl  = "jdbc:mysql://localhost/programa_facturacion_mh";
	
	public DBEngine(){
		try{

		    Class.forName(myDriver);
		    conn = DriverManager.getConnection(myUrl, "root", "maisonnave1");
		
		}catch(Exception e){e.printStackTrace();}
	}
	

	public Cliente getCliente(int Codigo_Cliente){
		String query = "SELECT * FROM Cliente WHERE Codigo_Cliente = "+Codigo_Cliente;
		Cliente toReturn = null;
	      // create the java statement
	      Statement st;
		try {
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    if(rs.next()){
		    	//int Codigo_Cliente, String CUIT, String denominacion, String direccion, String localidad,
	    		//String telefono, String correoElectronico, String condicionIva, String habilitado
		    	toReturn = new Cliente(rs.getInt("Codigo_Cliente"),
		    			rs.getString("CUIT"),
		    			rs.getString("Denominacion"),
		    			rs.getString("Direccion"),
		    			rs.getString("Localidad"),
		    			rs.getString("Telefono"),
		    			rs.getString("Email"),
		    			rs.getString("Condicion_iva"),
		    			rs.getString("Habilitado"));
		    }
		    st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	public List<Cliente> buscarCliente(String denominacion){
		return null;
	}
	public List<Cliente> buscarCUIT(String CUIT){ 
		return null;
	}

}
