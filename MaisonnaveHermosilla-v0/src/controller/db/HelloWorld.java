package controller.db;

import java.sql.*;

public class HelloWorld {
	static DBEngine motor;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
		motor = new DBEngine();

		Cliente maiso = new Cliente("20-36698447-0", "MAISONNAVE, Mariano","EXE", -1);

		
		

		

	}
	
	public static void buscarYMostrarClientes(String busqueda){
		System.out.println("Consultando en la base de datos por un cliente que conincida con la busqueda: '"+busqueda+"' ");
		for(Cliente cliente: motor.buscarCliente(busqueda))
			System.out.println(cliente.toString());
	}

}
