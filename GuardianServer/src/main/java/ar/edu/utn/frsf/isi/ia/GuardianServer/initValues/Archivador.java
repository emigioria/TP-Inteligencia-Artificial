/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;

public class Archivador {

	public static String INIT_PL;
	public static String CUSTOM_PL;
	public static String MODULE_PL;

	public Archivador() {
		super();
	}

	{
		try{
			INIT_PL = new URI(Guardian.class.getResource("/db/init.pl").toString()).getPath();
			CUSTOM_PL = new URI(Guardian.class.getResource("/db/customInit.pl").toString()).getPath();
			MODULE_PL = new URI(Guardian.class.getResource("/db/moduleMaker.pl").toString()).getPath();
		} catch(URISyntaxException e){
			e.printStackTrace();
		}
	}

	public List<String> leerArchivoInitPl() {
		try{
			return leerArchivo(new File(INIT_PL));
		} catch(IOException e){
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public List<String> leerArchivo(File file) throws IOException {
		List<String> archivo = new ArrayList<>();
		try(Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(reader)){
			String linea;
			archivo.clear();
			while((linea = br.readLine()) != null){
				archivo.add(linea);
			}
		}

		return archivo;
	}

	public void escribirArchivoCustomPl(List<String> archivo) {
		File file = new File(CUSTOM_PL);
		if(file.exists()){
			file.delete();
		}
		try{
			file.createNewFile();
		} catch(IOException e){
			e.printStackTrace();
		}
		try{
			escribirAArchivo(archivo, file);
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public void escribirAArchivo(List<String> archivo, File file) throws IOException {
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
				PrintWriter pw = new PrintWriter(writer)){
			archivo.forEach(linea -> pw.println(linea));
		}
	}
}
