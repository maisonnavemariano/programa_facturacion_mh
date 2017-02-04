package controller.db;

import java.sql.*;

public class HelloWorld {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
		DBEngine motor = new DBEngine();
		
		for(int i = 1 ; i<= 99; i++){
			Cliente c= motor.getCliente(i);
			if (c!=null) 
				System.out.println(c.toString());
		}
	}

}
