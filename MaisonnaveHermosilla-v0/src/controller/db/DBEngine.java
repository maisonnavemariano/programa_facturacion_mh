package controller.db;

import java.sql.*;
import java.util.ArrayList;
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
	
	/**
	 * 
	 * @param Codigo_Cliente
	 * @return
	 */
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
	/**
	 * 
	 * @param denominacion
	 * @return
	 */
	public List<Cliente> buscarCliente(String denominacion){
		Cliente aux;
		List<Cliente> lista = new ArrayList<Cliente>();
		String query = "SELECT * "
				+ "FROM Cliente "
				+ "WHERE Denominacion LIKE '%"+denominacion+"%' "
						+ "ORDER BY Denominacion ASC ";
		
		Statement st;
		try {
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    while(rs.next()){
		    	//int Codigo_Cliente, String CUIT, String denominacion, String direccion, String localidad,
	    		//String telefono, String correoElectronico, String condicionIva, String habilitado
		    	aux = new Cliente(rs.getInt("Codigo_Cliente"),
		    			rs.getString("CUIT"),
		    			rs.getString("Denominacion"),
		    			rs.getString("Direccion"),
		    			rs.getString("Localidad"),
		    			rs.getString("Telefono"),
		    			rs.getString("Email"),
		    			rs.getString("Condicion_iva"),
		    			rs.getString("Habilitado"));
		    	lista.add(aux);
		    }
		    st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	/**
	 * 
	 * @param CUIT
	 * @return
	 */
	public List<Cliente> buscarCUIT(String CUIT){ 
		Cliente aux;
		List<Cliente> lista = new ArrayList<Cliente>();
		String query = "SELECT * "
				+ "FROM Cliente "
				+ "WHERE CUIT LIKE '%"+CUIT+"%' "
						+ "ORDER BY Denominacion ASC ";
		
		Statement st;
		try {
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    while(rs.next()){
		    	//int Codigo_Cliente, String CUIT, String denominacion, String direccion, String localidad,
	    		//String telefono, String correoElectronico, String condicionIva, String habilitado
		    	aux = new Cliente(rs.getInt("Codigo_Cliente"),
		    			rs.getString("CUIT"),
		    			rs.getString("Denominacion"),
		    			rs.getString("Direccion"),
		    			rs.getString("Localidad"),
		    			rs.getString("Telefono"),
		    			rs.getString("Email"),
		    			rs.getString("Condicion_iva"),
		    			rs.getString("Habilitado"));
		    	lista.add(aux);
		    }
		    st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	
	public boolean agregarCliente(Cliente cliente){
		String query = "INSERT INTO Cliente "
				+ "( CUIT, Denominacion, Direccion, Localidad, Telefono, Email, Habilitado, Condicion_iva ) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
//				+ "( '"+cliente.getCuit()+"',"
//				+ "'"+cliente.getDireccion()+"',"
//				+ "'"+cliente.getLocalidad()+"',"
//				+ "'"+cliente.getTelefono()+"',"
//				+ " '"+cliente.getCorreoElectronico()+"', "
//				+ "'"+cliente.getHabilitado()+"', "
//				+ "'"+cliente.getCondicionIva()+"', "
//				+ " )";
		

		try {
		    PreparedStatement preparedStmt = conn.prepareStatement(query);

		    preparedStmt.setString (1, cliente.getCuit());
		    preparedStmt.setString (2, cliente.getDenominacion());
		    preparedStmt.setString (3, cliente.getDireccion());
		    preparedStmt.setString (4, cliente.getLocalidad());
		    preparedStmt.setString (5, cliente.getTelefono());
		    preparedStmt.setString (6, cliente.getCorreoElectronico());
		    preparedStmt.setString (7, cliente.getHabilitado());
		    preparedStmt.setString (8, cliente.getCondicionIva());
		      preparedStmt.execute();
		    return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	public Cliente eliminarCliente(int Codigo_Cliente){
		return null;
	}

}
