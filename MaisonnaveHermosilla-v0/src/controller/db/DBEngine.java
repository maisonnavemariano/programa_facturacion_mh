package controller.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import exception.InvalidBudgetException;
import exception.InvalidClientException;

/**
 * Clase que permite un manejo transparente de la base de datos 'programa_facturacion_mh'. Brindando operaciones para manipular Clientes, Presupuestos, Transacciones y Pagos.
 * 
 * DESCRIPCIÓN DE LA BASE DE DATOS:
 * +-----------------------------------+
 * | Tables_in_programa_facturacion_mh |
 * +-----------------------------------+
 * | Cliente                           |
 * | Concepto_presupuesto              |
 * | Cuenta_corriente                  |
 * | Presupuesto                       |
 * | Transaccion                       |
 * +-----------------------------------+
 * 
 * CONFIGURACIÓN POR DEFECTO A LA BASE DE DATOS:
 * 		Por defecto la conección se establece con el jdbc Driver para mysql.
 * 		La conección es con el usuario root, conectado a 192.168.3.107 directo a la base de datos 'programa_facturacion_mh'. 
 * 
 * @author maiso
 *
 */
public class DBEngine {
	protected final String myDriver = "com.mysql.jdbc.Driver";
	protected final String myUrl  = "jdbc:mysql://localhost/programa_facturacion_mh"; 
	
	
	
	protected int ProximoNumeroPresupuesto; // para poder ofrecer el proximo numero presupuesto.
	protected Connection conn;
	
	public DBEngine(){
		try{

		    Class.forName(myDriver);
		    conn = DriverManager.getConnection(myUrl, "virginia", "lospiojos");
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	//private int getProximoNumeroPresupuesto(){
	//	return this.ProximoNumeroPresupuesto;
	//}
	
	
	// ==================================================================================================================================
	//     ** ** ** **                                    CLIENTES                                                 ** ** ** **
	// ==================================================================================================================================
	
	/**
	 * Método que consulta la db para saber cuantos clientes habilitados hay.
	 * @return El entero que representa la cantidad de clientes habilitados, -1 uno de haber un problema (el caso donde no haya conexion con la base de datos por ej).
	 */
	public int cantidadClientesHabilitados() {
		int cantidad = -1;
		String query = "SELECT (COUNT(*)) AS Cant_Clientes_hab from Cliente WHERE Habilitado='S'";
	    Statement st;
		try {
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    if(rs.next()){
		    	cantidad = rs.getInt("Cant_Clientes_hab");
		    }
		    st.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return cantidad;
				
				
	}
	
	/**
	 * El método consulta a la base de datos por el único cliente que tiene ese dado Codigo_Cliente, si el mismo no aparece retorna un objeto nulo. 
	 * Si el cliente es encontrado, se recuperan sus atributos de la base de datos y se los retornan a la clase que llamo el método. 
	 * Obs: Todos los atributos pueden ser nulos, menos el codigo_cliente y el CUIT.
	 * 
	 * @param Codigo_Cliente Número de identificación ÚNICO del cliente.
	 * @return Objeto de tipo Cliente, con todos sus parámetros cargados: Codigo_Cliente (NOT NULL), CUIT (NOT NULL),Denominacion,Direccion,Localidad,Telefono,Email, Condicion_iva, Habilitado. 
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
			
			e.printStackTrace();
		}
		return toReturn;
	}
	/**
	 * Busca en la base de Clientes por un cliente que contenga en algun lugar del campo "denominacion", el String pasado por parámetro ('denominacion'). No es sensible a minusculas y mayusculas.
	 * 
	 * Retorna todo aquel cliente que contenga el String buscado. Si no encuentra ningun cliente retorna una lista vacía.
	 * @param denominacion Un String que representa la busqueda del cliente, se buscara en el campo "Denominacion", de la base de datos "Cliente" por un String que coincida con el pasado por parámetro.
	 * @return Una lista conteniendo los Clientes que coinciden con la busqueda, o una lista vacía si no existe ninguna coincidencia.
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
			e.printStackTrace();
		}
		return lista;
	}
	/**
	 * Utiliza el String pasado por parámetro para buscar clientes. Todo aquel Cliente que tenga un CUIT coincidente, o que contenga al CUIT pasado por parámetro sera retornado en la lista.
	 * Para la busqueda no es necesario pasar un CUIT completo, busca por coincidencias en todo el String, y retorna todas las coincidencias en una lista. Si no hay coincidencias retorna una lsita vacía.
	 * @param CUIT el String que se utilizará para la búsqueda, puede ser un CUIT parcial, no necesita ser completo.
	 * @return Retorna una lista de Clientes con todos aquellos que en su CUIT contengan al CUIT buscado, retornará una lista vacía en caso de que no haya coincidencias.
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
	 * Recibe un cliente por parámetro y lo inserta en la base de datos. UTiliza todos sus atributos a excepción del 'Codigo_Cliente', el cual hasta ese momento debería ser inválido ya que es un numero asociado a la base de datos.
	 * Luego de insertar el cliente en la base de datos, recupera el su número de cliente (Codigo_Cliente), el cual lo identifica únivocamente en la base de datos, y lo inserta en el objeto pasado por parámetro.
	 * @param cliente Recibe un Objeto tipo cliente, y lo inserta en la base de datos. Luego de insertar actualiza su Codigo_Cliente, por aquel con el cuál fue cargado en la base de datos.
	 * @return True si fue insertado correctamente. False si hubo algún error (Base de datos desconectada, problemas de conexión).
	 */
	public boolean agregarCliente(Cliente cliente){
		String query = "INSERT INTO Cliente "
				+ "( CUIT, Denominacion, Direccion, Localidad, Telefono, Email, Habilitado, Condicion_iva ) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
		
		//System.out.println("en la DB" + cliente.getCondicionIva());

		try {
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
//TODO:  Que pasa si el cliente tiene atributos nulos?
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
	 * Elimina un cliente de la base de datos. Retorna un objeto conteniendo el Cliente eliminado, con su Codigo_Cliente anulado (ya que ese Codigo_Cliente no existe más en la base de datos). 
	 * @param Codigo_Cliente Código del cliente buscado para ser eliminado, cada código representa a un único cliente. 
	 * @return Retorna el cliente eliminado, si no encontró ningún cliente con el código pasado por parámetro retorna null.
	 */
	public Cliente eliminarCliente(int Codigo_Cliente){
		Cliente toReturn = this.getCliente(Codigo_Cliente);
		if(toReturn != null){
			String query = "DELETE FROM Cliente "
					+ "WHERE Codigo_Cliente = ?";
			PreparedStatement preparedStmt;
			try {
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setInt(1, Codigo_Cliente);
				preparedStmt.execute();
				toReturn.invalidarCliente();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return toReturn;
	}
	
	private boolean inhabilitarCliente(Cliente c){
	    PreparedStatement preparedStmt;
		if(c.esValidoCodigoCliente()){
			String update = "UPDATE Cliente SET Habilitado = 'N' WHERE Codigo_Cliente = ?";
		    try {
				preparedStmt = conn.prepareStatement(update);
				preparedStmt.setInt(1, c.getCodigoCliente());
				preparedStmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		return false;
	}
	private boolean habilitarCliente(Cliente c){
	    PreparedStatement preparedStmt;
		if(c.esValidoCodigoCliente()){
			String update = "UPDATE Cliente SET Habilitado = 'S' WHERE Codigo_Cliente = ?";
		    try {
				preparedStmt = conn.prepareStatement(update);
				preparedStmt.setInt(1, c.getCodigoCliente());
				preparedStmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		return false;
	}

	public void actualizarCliente(Cliente c){
	    PreparedStatement preparedStmt;
		if(c.esValidoCodigoCliente()){
			String update = "UPDATE Cliente SET CUIT = ?, Denominacion = ?, Direccion = ?, "
					+ "Localidad = ?, Telefono = ?, Email = ?, Habilitado = ?, Condicion_iva = ?  WHERE Codigo_Cliente = ?";
		    try {
				preparedStmt = conn.prepareStatement(update);
				preparedStmt.setString(1, c.getCuit());
				preparedStmt.setString(2, c.getDenominacion());
				preparedStmt.setString(3, c.getDireccion());
				preparedStmt.setString(4, c.getLocalidad());
				preparedStmt.setString(5, c.getTelefono());
				preparedStmt.setString(6, c.getCorreoElectronico());
				preparedStmt.setString(7, c.getHabilitado());
				preparedStmt.setString(8, c.getCondicionIva());
				preparedStmt.setInt(9, c.getCodigoCliente());
				preparedStmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
	}
	
	// ==================================================================================================================================
	//     ** ** ** **                                    PRESUPUESTOS                                              ** ** ** **
	// ==================================================================================================================================

	public List<Presupuesto> BuscarDesdeHasta(String denom, String cuit, String desde, String hasta){
		List<Presupuesto> toReturn = new ArrayList<Presupuesto>();
		if(denom==null || cuit == null || desde ==null || hasta == null) {
			System.out.println("[WARNING] Uno de los Strings pasados para la consulta BuscarDesdeHasta son nulos.");
		}
		else {
		
			Presupuesto aux;
			Statement st ;
			
			String query = "SELECT * FROM Presupuesto INNER JOIN Cliente ON Presupuesto.Codigo_cliente = Cliente.Codigo_cliente "
					+ "WHERE Cliente.Denominacion LIKE '%"+ denom+ "%' AND "
							+ "Cliente.CUIT LIKE '%"+cuit+"%' AND "
									+ "Fecha BETWEEN '"+desde+"' AND '"+hasta+"';";
			
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(query);
				while(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double Subtotal, Date fecha
					aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
							this.getCliente(rs.getInt("Codigo_Cliente")),
							(rs.getString("Efectivo").equals("S") ? true:false),
							rs.getFloat("Alicuota"),
							rs.getDouble("Subtotal"),
							rs.getDate("Fecha"));
					aux.actualizarNroPresupuesto(rs.getInt("Nro_Presupuesto"));
					toReturn.add(aux);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return toReturn;
	}
	
	
	public List<Presupuesto> BuscarFechaExacta(String denom, String cuit, String fecha_actual){
		List<Presupuesto> toReturn = new ArrayList<Presupuesto>();
		if(denom==null || cuit == null || fecha_actual ==null ) {
			System.out.println("[WARNING] Uno de los Strings pasados para la consulta BuscarDesdeHasta son nulos.");
		}
		else {	
			
			Presupuesto aux;
			Statement st ;
			
			String query = "SELECT * FROM Presupuesto INNER JOIN Cliente ON Presupuesto.Codigo_cliente = Cliente.Codigo_cliente "
					+ "WHERE Cliente.Denominacion LIKE '%"+ denom+ "%' AND "
							+ "Cliente.CUIT LIKE '%"+cuit+"%' AND "
									+ "Fecha = '"+fecha_actual+"';";
			
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(query);
				while(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double Subtotal, Date fecha
					aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
							this.getCliente(rs.getInt("Codigo_Cliente")),
							(rs.getString("Efectivo").equals("S") ? true:false),
							rs.getFloat("Alicuota"),
							rs.getDouble("Subtotal"),
							rs.getDate("Fecha"));
					aux.actualizarNroPresupuesto(rs.getInt("Nro_Presupuesto"));
					toReturn.add(aux);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return toReturn;
	}
	
	public void eliminarPresupuestosNoEfectivos() {
		try {
			String query = "DELETE FROM Presupuesto WHERE Efectivo = 'N'";
			PreparedStatement preparedStmt;
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void eliminarNoEfectivo(Presupuesto p) throws InvalidBudgetException {
		if (!p.hasValidNumber())
			throw new InvalidBudgetException("El presupuesto no existe en la base de datos");
		if (p.getEfectivo())
			throw new InvalidBudgetException("Para eliminar un presupuesto tiene que ser no efectivo.");
		int nro_presu = p.getNroPresupuesto();
		// * * * ELIMINAMOS EL PRESUPUESTO DE LA TABLA DE PRESUPUESTOS * * * 	
		try {
			String query = "DELETE FROM Presupuesto WHERE Nro_Presupuesto = ?";
			PreparedStatement preparedStmt;
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, nro_presu);
			preparedStmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void desefectivizar(Presupuesto p) throws InvalidBudgetException {
		if ((!p.hasValidNumber()) || !(p.getEfectivo()) )
			throw new InvalidBudgetException("Presupuesto no existente en base de datos, o no ha sido efectivizado aún.");
		
		int nro_transaccion = p.getNroTransaccion();
		int nro_presupuesto = p.getNroPresupuesto();
		
		double monto_a_restar = p.calcularMontoTotal();
		
		double estado_cta_corriente;
		try {
			// * * * ACTUALIZAMOS CUENTA CORRIENTE * * * 
			estado_cta_corriente = this.obtenerEstadoCuentaCorriente(p.getCliente());
			this.actualizarEstadoCuentaCorriente(p.getCliente(), estado_cta_corriente+monto_a_restar);
			
			// * * * ELIMINAMOS TRANSACCION DE TABLA DE TRANSACCIONES * * * 		
			String query = "DELETE FROM Transaccion WHERE Nro_Transaccion = ?";
			PreparedStatement preparedStmt;
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, nro_transaccion);
			preparedStmt.execute();
				
				
			// * * * MARCAMOS PRESUPUESTO COMO NO EFECTIVO. * * * 
			query = "UPDATE Presupuesto SET Efectivo = 'N' WHERE Nro_Presupuesto = ? "; // lo hacemos no efectivo
			PreparedStatement pt;
			pt = conn.prepareStatement(query);
			pt.setInt(1, nro_presupuesto);
			pt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		} catch (InvalidClientException e) {
			// No debería entrar acá
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Método que recibe un nro_presupuesto y consulta a la base de datos por el presupuesto que se corresponde con dicho numero. Como el número es único en la base de datos, solo retorna uno, o ninguno (null) si 
	 * no existe tal presupuesto. 
	 * @param nro_presupuesto Número de presupuesto buscado.
	 * @return Objeto tipo Presupuesto conteniendo todos los datos recuperados de la base de datos sobre el presupuesto 'Nro_Presupuestpo', o NULL si no existe dicho prespuesto en la base de datos.
	 */
	public Presupuesto verPresupuesto(int nro_presupuesto){
		Presupuesto toReturn = null;
		
		String query = "SELECT * FROM Presupuesto WHERE Nro_Presupuesto = "+nro_presupuesto;
		Statement st;
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double Subtotal, Date fecha
				toReturn = new Presupuesto(this.getConceptos(nro_presupuesto),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Subtotal"),
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
	 * Retorna una lista de todos los prespuestos existentes en la base de datos que fueron realizados al cliente 'cliente'. 
	 * @param cliente Cliente al cual se le quieren pedir todos los presupuestos. La busqueda se realiza mediante su código de cliente.
	 * @return Una lista con todos los presupuestos que le corresponden al cliente 'cliente', o una lista vacía en caso de que no existe el cliente, o no tenga presupuestos asociados.
	 * @throws InvalidClientException 
	 */
	public List<Presupuesto> verPresupuestos(Cliente cliente) throws InvalidClientException{
		if (!cliente.esValidoCodigoCliente())
			throw new InvalidClientException("Cliente no insertado en la base de datos.");
		
		
		List<Presupuesto> lista = new ArrayList<Presupuesto>();
		Presupuesto aux;
		Statement st ;
		
		String query = "SELECT * FROM Presupuesto WHERE Codigo_Cliente = "+ cliente.getCodigoCliente()+ " ORDER BY Fecha ASC";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double Subtotal, Date fecha
				aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Subtotal"),
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
	//temp

	/**
	 */
	public List<Presupuesto> verPresupuestosNoEfectivosPorDenominacion(String denom){

		List<Presupuesto> lista = new ArrayList<Presupuesto>();
		Presupuesto aux;
		Statement st ;
		
		String query = "SELECT * FROM Presupuesto INNER JOIN Cliente ON Presupuesto.Codigo_cliente = Cliente.Codigo_cliente WHERE Cliente.Denominacion LIKE '%"+ denom+ "%' AND Presupuesto.Efectivo = 'N'";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double Subtotal, Date fecha
				aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Subtotal"),
						rs.getDate("Fecha"));
				aux.actualizarNroPresupuesto(rs.getInt("Nro_Presupuesto"));
				lista.add(aux);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}	//temp

	/**
	 */
	public List<Presupuesto> verPresupuestosNoEfectivosPorCuit(String cuit){

	
		List<Presupuesto> lista = new ArrayList<Presupuesto>();
		Presupuesto aux;
		Statement st ;
		
		String query = "SELECT * FROM Presupuesto INNER JOIN Cliente ON Presupuesto.Codigo_cliente = Cliente.Codigo_cliente WHERE Cliente.CUIT LIKE '%"+ cuit+ "%' AND Presupuesto.Efectivo = 'N'";
		
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){ // List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double Subtotal, Date fecha
				aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Subtotal"),
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
	//temp
	
	
	
	/**
	 * Retorna el último presupuesto que se le realizó al cliente 'cliente'.
	 * @param cliente Cliente al cual se le requiere el último presupuesto. Se utiliza su código de cliente para la busqueda.
	 * @return El ultimo presupuesto del cliente pasado por parámetro, o nulo en caso de que no existe el cliente, o no tenga presupuestos asociados.
	 * @throws InvalidClientException 
	 */
	public Presupuesto verUltimoPresupuesto(Cliente cliente) throws InvalidClientException{
		if (!cliente.esValidoCodigoCliente())
			throw new InvalidClientException("Cliente no insertado en la base de datos.");
		
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
						rs.getDouble("Subtotal"),
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
	 * Agrega un nuevo presupuesto a la base de datos. Lo vincula con el cliente que contiene el presupuesto mediante su Codigo_Cliente. 
	 * Al insertar en la base de datos al presupuesto se lo hace con un nuevo Nro_Presupuesto, a este mismo numero se lo agrega al objeto 'p'. Para que mantenga la referencia de su propio Nro_Presupuesto.
	 * 
	 * Por último se guarda al Nro_Presupuesto+1 como el proximo numero de presupuesto que va a salir, para poder ofrecer el servicio de devolver este numero para la interfaz gráfica de construccion de presupuestos.
	 * @param p Presupuesto a agregar en la base de datos
	 * @return True si pudo insertar correctamente en la base de datos. False caso contrario.
	 */
	public boolean agregarPresupuesto(Presupuesto p){
		boolean toReturn = false;
		String query = "INSERT INTO Presupuesto (Codigo_Cliente, Fecha, Efectivo, Alicuota, Subtotal) VALUES ('"+p.getCliente().getCodigoCliente()+"', "
				+ "'"+p.getFecha()+"', "
				+ "'"+(p.getEfectivo()?"S":"N") +"', "
				+ "'"+p.getAlicuota()+"', "
				+ "'"+p.getSubtotal()+"' ) ; ";
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
			    if(rs.next()){
			    	nro_presu = rs.getInt("ultimo_presupuesto");
			    	this.ProximoNumeroPresupuesto = nro_presu+1;
			    }
			    p.actualizarNroPresupuesto(nro_presu);
			    st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Para insertar los conceptos necesitamos el paso previo inmediato, INSERTAR EL NRO DE PRESUPUESTO EN EL OBJETO 'p'
			try {
				insertarConceptos(p);
			} catch (InvalidBudgetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return toReturn;
	}
	
	private void insertarConceptos(Presupuesto p) throws InvalidBudgetException{
		if(!p.hasValidNumber())
			throw new InvalidBudgetException("Presupuesto no creado en base de datos.");
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
	 * Realiza una operacion de UPDATE sobre la base de datos, cambiando los atributos del presupuesto. Para buscar el presupuesto utiliza el nro de presupuesto el cual es único.
	 *  
	 * @param p Presupuesto buscado en la base de datos para ser actualizado. Actualiza todos su atributos: Codigo_Cliente, Fecha, Efectivo, Alicuota y Subtotal. 
	 * @return True si se pudo modificar correctamente. False en caso contrario.
	 * 
	 * 
	 * OBS IMPORTANTE: el parametro efectivo no debe ser cambiado a mano, en lugar de eso se debe usar el método 'efectivizarPresupuesto'.
	 * @throws InvalidBudgetException 
	 */
	//TODO: limitar la edicion a presupuestos no efectivos
	public boolean editarPresupuesto(Presupuesto p) throws InvalidBudgetException{
		if (!p.hasValidNumber())
			throw new InvalidBudgetException("El presupuesto no existe en la base de datos");
		if (p.getEfectivo())
			throw new InvalidBudgetException("El presupuesto ya es efectivo, no puede ser modificado.");
		// TODO chequear que el presupuesto no sea efectivo!!!
		// ===========================================================================================================================================
		boolean toReturn = false;
		String query = "UPDATE Presupuesto SET Codigo_Cliente = ?, Fecha = ?, Efectivo = ?, Alicuota = ?, Subtotal = ? WHERE Nro_Presupuesto = ?";
	    PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, p.getCliente().getCodigoCliente());
			preparedStmt.setString(2,  p.getFecha());
			preparedStmt.setString(3, (p.getEfectivo()? "S":"N"));
			preparedStmt.setFloat(4, p.getAlicuota() );
			preparedStmt.setDouble(5, p.getSubtotal());
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
	private void actualizarConceptos(Presupuesto p) throws InvalidBudgetException{
		if (! p.hasValidNumber())
			throw new InvalidBudgetException("Presupuesto no creado en base de datos");
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
	
	/**
	 * Método que genera PARA TODOS los clientes HABILITADOS, una nueva factura NO efectivizada, con los mismos conceptos que el presupuesto anterior. 
	 */
	public void facturarTodos(){
		// +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * 
		// 																			lo que hace
		// +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * 
		/*
		 * PASOS:
		 * 		1 - Para cada Cliente habilitado hacer:
		 * 			I   - Recuperar ultimo presupuesto (método verUltimoPresupuesto(Cliente cliente) ).
		 * 			II  - Crear un nuevo presupuesto idéntco, pero con un nuevo número de presupuesto, fecha nueva, efectivo en NO y número de transacción -1 (porq aún no es efectivo) (ver 'facturarBorrador').
		 * 			III - Insertar nuevo presupuesto en la base de datos (método: agregarPresupuesto(Presupuesto p).
		 */
		String query = "SELECT * FROM Cliente WHERE Habilitado = 'S'";
		List<Cliente> clientes = new ArrayList<Cliente>();
		Cliente auxiliar;
		Statement st;
		try {
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    while(rs.next()){
		    	//int Codigo_Cliente, String CUIT, String denominacion, String direccion, String localidad,
	    		//String telefono, String correoElectronico, String condicionIva, String habilitado
		    	auxiliar = new Cliente(rs.getInt("Codigo_Cliente"),
		    			rs.getString("CUIT"),
		    			rs.getString("Denominacion"),
		    			rs.getString("Direccion"),
		    			rs.getString("Localidad"),
		    			rs.getString("Telefono"),
		    			rs.getString("Email"),
		    			rs.getString("Condicion_iva"),
		    			rs.getString("Habilitado"));
		    	clientes.add(auxiliar);
		    }
		    st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Cliente c : clientes){
			try {
				this.facturarBorrador(c);
			} catch (InvalidClientException e) {// TODO 
				e.printStackTrace();}
			}
		

	}
	
	//HECHO POR VIRI: CAMBIO DE ENCABEZADO
	//private void facturarBorrador(Cliente cliente) throws InvalidClientException{
	public Presupuesto facturarBorrador(Cliente cliente) throws InvalidClientException{
		Presupuesto ultimo = this.verUltimoPresupuesto(cliente);
	//	System.out.println(ultimo==null);
		//Calculo para obtener el monto total nuevo (fix para el caso donde el ultimo presupuesto fue realizado con el
		// sistema viejo y el monto total y la suma de los montos da distinto.
		float montoTotal = 0;
		for (Concepto c: ultimo.getConceptos())
			montoTotal += c.getMonto();
		// El presupuesto se guarda con una fecha por default que es la fecha del borrador, pero al efectivizarse se cambia
		// por la fecha de efectivización, la vista no debería mostrar la fecha de un presupuesto borrador porque no es la
		// fecha que quedará, la que queda es la del día de efectivización.
		Presupuesto nuevo = new Presupuesto(ultimo.getConceptos()	,cliente ,false, ultimo.getAlicuota(),montoTotal, Calendar.getInstance().getTime()) ; 
		this.agregarPresupuesto(nuevo);
		return nuevo;
		
	}
	/**
	 * Permite recuperar aquellos presupuestos no efectivos (los borradores), para poder editarlos uno por uno, modificarlos, guardarlos en la base de datos nuevamente corregidos, o incluso para efectivizarlos.
	 * @return Una lista con todos los presupuestos no efectivos, o una lista vacía en caso de que no haya presupuestos sin efectivizar. 
	 * 
	 * Obs: Puede entregar presupuestos de clientes no habilitados, si es que existen clientes no habilitados con presupuestos borrador creados.
	 */
	public List<Presupuesto> obtenerPresupuestosNoEfectivos(){
		// +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * 
		// 																			TRABAJO POR HACER
		// +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * +++++++++++++++++++++++++++++++++ * 
		String query = "SELECT * "
				+ "FROM Presupuesto "
				+ "WHERE Efectivo = 'N'";
		
		List<Presupuesto> toReturn = new ArrayList<Presupuesto>();
		Presupuesto aux;
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				aux = new Presupuesto(this.getConceptos(rs.getInt("Nro_Presupuesto")),
						this.getCliente(rs.getInt("Codigo_Cliente")),
						(rs.getString("Efectivo").equals("S") ? true:false),
						rs.getFloat("Alicuota"),
						rs.getDouble("Subtotal"),
						rs.getDate("Fecha"));
				aux.actualizarNroPresupuesto(rs.getInt("Nro_Presupuesto"));
				toReturn.add(aux);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	
	// ==================================================================================================================================
	//     ** ** ** **                                    TRANSACCIONES                                                ** ** ** **
	// ==================================================================================================================================

	/**
	 * Método complejo que toma un presupuesto y lo efectiviza realizando los siguientes pasos:
	 * 		1. Crea la transaccion asociada a la efectivización, con fecha actual y con el monto y los valores del presupuesto.
	 * 		2. Actualiza el estado de la cuenta corriente restandole el monto del presupuesto (generando deuda),en la base de datos (métodos auxiliares: obtenerEstadoCuentaCorriente,actualizarEstadoCuentaCorriente).
	 * 		3. Se registra la transacción en la base de datos.
	 * 		4. Se vincula el presupuesto a el numero de transaccion que generó.
	 * 		5. Se cambia el atributo del presupuesto para reflejar que ya es efectivo.
	 * 		6. Este último cambio es enviado a la base de datos y termina el método.
	 * @param p
	 * @return Retorna la Transaccion que se generó al efectiviar el presupuesto
	 * @throws InvalidBudgetException 
	 */
	public Transaccion efectivizarPresupuesto(Presupuesto p) throws InvalidBudgetException{
		if (!p.hasValidNumber())
			throw new InvalidBudgetException("Presupuesto no creado en base de datos.");

		double montoTotal = p.calcularMontoTotal();
		// proceso de actualizar las cuentas corrientes.
		Transaccion t = null;
		boolean efectivo = false;
		
		String query = "INSERT INTO Transaccion (Codigo_Cliente, Fecha, Evento, Monto, Concepto, Estado_cuenta_corriente) VALUES (?,?,?,?,?,?) ";
		PreparedStatement preparedStmt;

		try {
			// MODIFICAR CUENTA CORRIENTE
			double estado_cuenta_corriente = 0;
			try {
				estado_cuenta_corriente = this.obtenerEstadoCuentaCorriente(p.getCliente());
			} catch (InvalidClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double nuevo_estado = estado_cuenta_corriente - montoTotal;
			this.actualizarEstadoCuentaCorriente(p.getCliente(), nuevo_estado);
			// REGISTRAR TRANSACCION
			t = new Transaccion(p.getCliente(), Calendar.getInstance().getTime(), 'D', montoTotal,"",nuevo_estado);
			
			
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, p.getCliente().getCodigoCliente());
			preparedStmt.setString(2, t.getFecha());
			preparedStmt.setString(3, "P");
			preparedStmt.setDouble(4, montoTotal );
			preparedStmt.setString(5, "Efectivización de presupuesto");
			preparedStmt.setDouble(6, nuevo_estado);
			
			
			preparedStmt.execute();
			
			efectivo = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		// 
		if(efectivo){ 
			// REGISTRAR CAMBIO EN EL OBJETO PRESUPUESTO.
			// 1. AGREGAMOS EL NRO DE TRANSACCION GENERADA AL PRESUPUESTO
			// 2. MARCAMOS PRESUPUESTO COMO EFECTIVO
			// 3. REGISTRAMOS CAMBIO EN EFECTIVO EN LA BASE DE DE DATOS.
			String query_aux = "SELECT LAST_INSERT_ID() AS ultima_transaccion";
			Statement st;
			try {
				st = conn.createStatement();
			    ResultSet rs = st.executeQuery(query_aux);
			    int nro_trans = -1;
			    if(rs.next())
			    	nro_trans = rs.getInt("ultima_transaccion");
			    p.actualizarNroTransaccion(nro_trans);
			    st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			p.setFecha(Calendar.getInstance().getTime()); // Ponemos la fecha en que se efectivizo en el objeto y posteriormente
															// la corregimos en la base de datos
			//HACEMOS EFECTIVO EL PRESU EN LA BASE DE DATOS
			query = "UPDATE Presupuesto SET Efectivo = 'S', Fecha = ? WHERE Nro_Presupuesto = ? "; // lo hacemos efectivo
																									// y colocamos la fecha
																									//de efectivizacion
			PreparedStatement pt;
			
			try {
				pt = conn.prepareStatement(query);
				pt.setString(1, p.getFecha());
				pt.setInt(2, p.getNroPresupuesto());
				pt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// HACEMOS EFECTIVO EL PRESU EN EL OBJETO
		
			p.setEfectivo(true);		
		}
		return (efectivo? t : null);//devolvemos la transaccion solo si la logramos guardar en la base de datos
	}
	
	private double obtenerEstadoCuentaCorriente(Cliente C) throws InvalidClientException{
		if (!C.esValidoCodigoCliente())
			throw new InvalidClientException("Cliente no insertado en la base de datos.");
		
		
		String query = "SELECT * FROM Cuenta_corriente WHERE Codigo_Cliente = "+C.getCodigoCliente();
		double estado = -1;
	    Statement st;
		try {
			st = conn.createStatement();
		    ResultSet rs = st.executeQuery(query);
		    if(rs.next()){
		    	//int Codigo_Cliente, String CUIT, String denominacion, String direccion, String localidad,
	    		//String telefono, String correoElectronico, String condicionIva, String habilitado
		    	estado = rs.getDouble("Monto");
		    }
		    st.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return estado;
	}
	private void actualizarEstadoCuentaCorriente(Cliente c,double monto){
		String query = "UPDATE Cuenta_corriente SET Codigo_Cliente=?,  Monto = ?  WHERE Codigo_Cliente=? ";
		PreparedStatement pt;
		
		try {
			pt = conn.prepareStatement(query);
			pt.setInt(1, c.getCodigoCliente());
			pt.setDouble(2, monto);
			pt.setInt(3, c.getCodigoCliente());
			pt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Método que permite reflejar la situación en la que un cliente realizo un pago. Es un pago de un importe de 'monto_pagado' en la cuenta del cliente 'cliente'. Con la posibilidad de dejar una observación
	 * mediante el String "obs".
	 * 
	 * PASOS:
	 * 		1. genera el cambio en la cuenta corriente
	 * 		2. genera la transaccion.
	 * 		3. guarda la transaccion en la base de datos.
	 * 
	 * @param cliente Cliente que efectúa el pago.
	 * @param monto_pagado importe del monto pagado.
	 * @param obs Observación sobre el pago.
	 * @return Retorna la transacción que genero en la base de datos.  Returna null si hay algun problema al insertar en la base de datos. 
	 * @throws InvalidClientException 
	 */
	public Transaccion efectuarPago(Cliente cliente, double monto_pagado, String obs) throws InvalidClientException{
		if (!cliente.esValidoCodigoCliente())
			throw new InvalidClientException("Cliente no insertado en la base de datos.");
		
		Transaccion toReturn = null;
		boolean efectivo = false;
		String query = "INSERT INTO Transaccion (Codigo_Cliente, Fecha, Evento, Monto, Concepto, Estado_cuenta_corriente) VALUES (?,?,?,?,?,?) ";
		PreparedStatement preparedStmt;
		try{

			// MODIFICAR CUENTA CORRIENTE
			double estado_cuenta_corriente = this.obtenerEstadoCuentaCorriente(cliente);
			double nuevo_estado = estado_cuenta_corriente +  monto_pagado;
			this.actualizarEstadoCuentaCorriente(cliente, nuevo_estado);
			// REGISTRAR TRANSACCION
			toReturn = new Transaccion(cliente, Calendar.getInstance().getTime(), 'C', monto_pagado,obs,nuevo_estado);
						
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, cliente.getCodigoCliente());
			preparedStmt.setString(2, toReturn.getFecha());
			preparedStmt.setString(3, "R");
			preparedStmt.setDouble(4, monto_pagado);
			preparedStmt.setString(5, obs);
			preparedStmt.setDouble(6, nuevo_estado);
			
			
			preparedStmt.execute();
			efectivo = true; //llego sin excepcion hasta aquí
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return (efectivo?toReturn:null);//devolvemos la transaccion solo si la logramos guardar en la base de datos
	}
	

	

}
