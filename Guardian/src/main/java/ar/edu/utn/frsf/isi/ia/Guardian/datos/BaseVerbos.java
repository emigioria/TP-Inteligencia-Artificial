/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.datos;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;

/**
 * Write a description of class BaseVerbos here.
 *
 * @author Rey Salcedo
 * @version (a version number or a date)
 */
public class BaseVerbos {
	private Connection conexion;
	private String ruta;

	public BaseVerbos() throws URISyntaxException {
		ruta = new URI(BaseVerbos.class.getResource("/db/verbos.db").toString()).getPath();
	}

	public void conectar() {
		try{
			Class.forName("org.sqlite.JDBC");
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try{
			conexion = DriverManager.getConnection("jdbc:sqlite:" + ruta);
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void desconectar() {
		try{
			conexion.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public String infinitivo(String verbo) {
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
					return resultado.getString(1);
				} catch(Exception e){

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
				"'\u00F3','o') ,'\u00F5','o') ,'\u00F4' ,'o'),'\u00FA','u'), '\u00E7' ,'c') LIKE '" + verbo + "%'";
	}
}
