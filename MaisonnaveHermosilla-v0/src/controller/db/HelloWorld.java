package controller.db;

import java.sql.*;

public class HelloWorld {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
		DBEngine motor = new DBEngine();
		
		for ( Cliente cliente : motor.buscarCliente("Arr"))
			System.out.println(cliente.toString());
	}

}
