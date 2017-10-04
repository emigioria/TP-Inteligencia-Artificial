/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;

public class BaseVerbos {
	private Connection conexion;
	private String USER = "postgres";
	private String PASSWORD = "postgres";
	private String URL = "jdbc:postgresql://localhost:5432/postgres";

	public BaseVerbos() {
	}

	public void conectar() throws SQLException {
		conexion = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public void desconectar() {
		try{
			conexion.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public String infinitivo(String verbo) {
		if(conexion == null){
			return null;
		}
		ResultSet resultado;

		//Normalizar verbo
		verbo = verbo.toLowerCase();
		verbo = Normalizer.normalize(verbo, Normalizer.Form.NFKD);
		verbo = verbo.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

		Object[] sqlParams = new Object[] {
				new String[] { "infinitive", "gerund", "gerund" },
				new String[] { "infinitive", "infinitive", "infinitive" },
				new String[] { "infinitive", "pastparticiple", "pastparticiple" },
				new String[] { "infinitive", "form_1s", "verbs" },
				new String[] { "infinitive", "form_2s", "verbs" },
				new String[] { "infinitive", "form_3s", "verbs" },
				new String[] { "infinitive", "form_1p", "verbs" },
				new String[] { "infinitive", "form_2p", "verbs" },
				new String[] { "infinitive", "form_3p", "verbs" }
		};
		try{
			for(Object o: sqlParams){
				String[] s = (String[]) o;
				resultado = conexion.createStatement().executeQuery(armarSQL(s[0], s[1], s[2], verbo));
				try{
					if(resultado.getRow() == 0){
						resultado.next();
					}
					if(resultado.getRow() == 1){
						return resultado.getString(1);
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	private String armarSQL(String columnaSelect, String columnaWhere, String tabla, String verbo) {
		return "SELECT " + columnaSelect + " FROM " + tabla + " WHERE replace(replace(replace(replace(replace(replace(replace(replace(" +
				"replace(replace(replace( lower(" + columnaWhere + "), '\u00E1','a'), '\u00E3','a'), '\u00E2','a'), '\u00E9','e'), '\u00EA','e'), '\u00ED','i'), " +
				"'\u00F3','o') ,'\u00F5','o') ,'\u00F4' ,'o'),'\u00FA','u'), '\u00E7' ,'c') LIKE '" + verbo + "'";
	}
}
