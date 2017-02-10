package controller.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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
	

	// ==================================================================================================================================
	//     ** ** ** **                                    CLIENTES                                                 ** ** ** **
	// ==================================================================================================================================
	
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
	/**
	 * 
	 * @param cliente
	 * @return
	 */
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
		    
		    // OBTENIENDO EL Codigo_Cliente del ultimo cliente insertado.
		    
		    String query_aux = "SELECT LAST_INSERT_ID() AS ultimo_codigo_cliente";
			Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query_aux);
		    int codigo_cliente = -1;
		    if(rs.next())
		    	codigo_cliente = rs.getInt("ultimo_codigo_cliente");
		    cliente.actualizarCodigoCliente(codigo_cliente);
		    st.close();
		    
		    
		    return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * 
	 * @param Codigo_Cliente
	 * @return
	 */
	public Cliente eliminarCliente(int Codigo_Cliente){
		Cliente toReturn = this.getCliente(Codigo_Cliente);
		String query = "DELETE FROM Cliente "
				+ "WHERE Codigo_Cliente = ?";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, Codigo_Cliente);
			preparedStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	
	// ==================================================================================================================================
	//     ** ** ** **                                    PRESUPUESTOS                                              ** ** ** **
	// ==================================================================================================================================
	/**
	 * 
	 * @param nro_presupuesto
	 * @return
	 */
	public Presupuesto verPresupuesto(int nro_presupuesto){
		Presupuesto toReturn = null;
		
		String query = "SELECT * FROM Presupuesto WHERE Nro_Presupuesto = "+nro_presupuesto;
		Statement st;
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double monto_total, Date fecha
				toReturn = new Presupuesto(this.getConceptos(nro_presupuesto),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Monto_total"),
						rs.getDate("Fecha"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	private List<Concepto> getConceptos(int Nro_presu){
		List<Concepto> lista = new ArrayList<Concepto>();
		Concepto aux;

		String query = "SELECT * FROM Concepto_presupuesto WHERE Nro_Presupuesto = "+Nro_presu;
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				aux = new Concepto(rs.getString("Concepto"),
						rs.getDouble("Monto"));
				lista.add(aux);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
	}
	/**
	 * 
	 * @param cliente
	 * @return
	 */
	public List<Presupuesto> verPresupuestos(Cliente cliente){
		List<Presupuesto> lista = new ArrayList<Presupuesto>();
		Presupuesto aux;
		Statement st ;
		
		String query = "SELECT * FROM Presupuesto WHERE Codigo_Cliente = "+ cliente.getCodigoCliente()+ " ORDER BY Fecha ASC";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double monto_total, Date fecha
				aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Monto_total"),
						rs.getDate("Fecha"));
				aux.actualizarNroPresupuesto(rs.getInt("Nro_Presupuesto"));
				lista.add(aux);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	/**
	 * 
	 * @param cliente
	 * @return
	 */
	public Presupuesto verUltimoPresupuesto(Cliente cliente){
		String query = "SELECT * "
				+ "FROM Presupuesto "
				+ "WHERE Codigo_Cliente = "+cliente.getCodigoCliente()+" AND "
						+ "Fecha = "
						+ "(SELECT  max(Fecha) "
						+ "FROM  Presupuesto "
						+ "WHERE  Codigo_Cliente = "+cliente.getCodigoCliente()+")";
		Presupuesto toReturn = null;
		Statement st ;
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				toReturn = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Monto_total"),
						rs.getDate("Fecha"));
						toReturn.actualizarNroPresupuesto(rs.getInt("Nro_Presupuesto"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
	}
	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean agregarPresupuesto(Presupuesto p){
		boolean toReturn = false;
		String query = "INSERT INTO Presupuesto (Codigo_Cliente, Fecha, Efectivo, Alicuota, Monto_total) VALUES ('"+p.getCliente().getCodigoCliente()+"', "
				+ "'"+p.getFecha()+"', "
				+ "'"+(p.efectivo()?"S":"N") +"', "
				+ "'"+p.getAlicuota()+"', "
				+ "'"+p.getMontoTotal()+"' ) ; ";
		PreparedStatement pt;
		
		try {
			pt = conn.prepareStatement(query);
			pt.execute();
			toReturn = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (toReturn){
		    // OBTENIENDO EL Nro_Presupuesto del ultimo presupuesto insertado, para actualizarlo del objeto presupuesto.
		    
		    String query_aux = "SELECT LAST_INSERT_ID() AS ultimo_presupuesto";
			Statement st;
			try {
				st = conn.createStatement();
			    ResultSet rs = st.executeQuery(query_aux);
			    int nro_presu = -1;
			    if(rs.next())
			    	nro_presu = rs.getInt("ultimo_presupuesto");
			    p.actualizarNroPresupuesto(nro_presu);
			    st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Para insertar los conceptos necesitamos el paso previo inmediato, INSERTAR EL NRO DE PRESUPUESTO EN EL OBJETO 'p'
			insertarConceptos(p);

		}
		return toReturn;
	}
	
	private void insertarConceptos(Presupuesto p){
		String query = "INSERT INTO Concepto_presupuesto (Nro_Presupuesto, Concepto, Monto) VALUES (?,?,?)";
		
		PreparedStatement pt;
		
		try {
			pt = conn.prepareStatement(query);
			for ( Concepto concepto : p.getConceptos()){
				pt.setInt(1,  p.getNroPresupuesto());
				pt.setString(2, concepto.getConcepto());
				pt.setDouble(3, concepto.getMonto());
				pt.addBatch();
			}
			pt.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean editarPresupuesto(Presupuesto p){
		boolean toReturn = false;
		String query = "UPDATE Presupuesto SET Codigo_Cliente = ?, Fecha = ?, Efectivo = ?, Alicuota = ?, Monto_total = ? WHERE Nro_Presupuesto = ?";
	    PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, p.getCliente().getCodigoCliente());
			preparedStmt.setString(2,  p.getFecha());
			preparedStmt.setString(3, (p.efectivo? "S":"N"));
			preparedStmt.setFloat(4, p.getAlicuota() );
			preparedStmt.setDouble(5, p.getMontoTotal());
			preparedStmt.setInt(6, p.getNroPresupuesto());
			
			// execute the java preparedstatement
			preparedStmt.executeUpdate();
			toReturn = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(toReturn)
			actualizarConceptos(p);
		return toReturn;
	}
	private void actualizarConceptos(Presupuesto p){
		//borrarlos y cargarlos de nuevo..
		boolean eliminados = false;
		String query = "DELETE FROM Concepto_presupuesto WHERE Nro_Presupuesto = ?";
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, p.getNroPresupuesto());
			
			preparedStmt.execute();
			eliminados = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(eliminados){
			//agregamos 
			this.insertarConceptos(p);
		}
	}
	
	public boolean efectivizarPresupuesto(Presupuesto p){
		return false;
	}
	

}
