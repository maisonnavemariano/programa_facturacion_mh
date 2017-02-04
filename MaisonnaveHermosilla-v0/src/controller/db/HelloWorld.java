package controller.db;

import java.sql.*;

public class HelloWorld {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
		DBEngine motor = new DBEngine();
		
		motor.agregarCliente(new Cliente("20-36698447-0","MAISONNAVE,Mariano", "EXE",-1));
		
		for(Cliente cliente: motor.buscarCliente("MAIS"))
			System.out.println(cliente.toString());
	}

}
