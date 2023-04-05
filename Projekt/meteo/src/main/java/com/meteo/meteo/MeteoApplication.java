package com.meteo.meteo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeteoApplication {

	public static void main(String[] args) {
		try {
			Connection connection = DriverManager.getConnection(
					"jdbc:mariadb://172.19.0.2:3306/meteo",
					"root", "Aa123456");
			PreparedStatement statement = connection.prepareStatement("""
						CREATE TABLE CUSTOMERS(
						ID INT NOT NULL,
						NAME VARCHAR (20) NOT NULL,
						AGE INT NOT NULL,
						ADDRESS CHAR (25) ,
						SALARY DECIMAL (18, 2),
						PRIMARY KEY (ID)
						)
					""");
			statement.executeQuery();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		SpringApplication.run(MeteoApplication.class, args);
	}

}
