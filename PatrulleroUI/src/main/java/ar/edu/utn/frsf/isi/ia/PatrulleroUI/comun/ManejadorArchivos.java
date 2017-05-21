/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.AristaGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.CalleGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.CasoDePruebaGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.InterseccionGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.MapaGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.ObstaculoGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.ObstaculoParcialGsonAdapter;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson.ObstaculoTotalGsonAdapter;
import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoParcial;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoTotal;

public class ManejadorArchivos {

	private Gson crearGson() {
		return new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(Mapa.class, new MapaGsonAdapter())
				.registerTypeAdapter(Calle.class, new CalleGsonAdapter())
				.registerTypeAdapter(Interseccion.class, new InterseccionGsonAdapter())
				.registerTypeAdapter(Arista.class, new AristaGsonAdapter())
				.registerTypeAdapter(CasoDePrueba.class, new CasoDePruebaGsonAdapter())
				.registerTypeAdapter(Obstaculo.class, new ObstaculoGsonAdapter())
				.registerTypeAdapter(ObstaculoParcial.class, new ObstaculoParcialGsonAdapter())
				.registerTypeAdapter(ObstaculoTotal.class, new ObstaculoTotalGsonAdapter())
				.create();
	}

	public Mapa cargarMapa(File archivoMapa) throws IOException {
		Gson gson = crearGson();
		InputStream is = new FileInputStream(archivoMapa);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		Mapa retorno = gson.fromJson(bufferedReader, Mapa.class);
		is.close();
		bufferedReader.close();
		return retorno;
	}

	public void guardarMapa(Mapa mapa, File archivoMapa) throws IOException {
		if(archivoMapa.exists()){
			archivoMapa.delete();
		}
		archivoMapa.createNewFile();
		Gson gson = crearGson();
		OutputStream os = new FileOutputStream(archivoMapa);
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
		bufferedWriter.write(gson.toJson(mapa));
		bufferedWriter.close();
		os.close();
	}

	public CasoDePrueba cargarCasoDePrueba(File archivoCasoDePrueba) throws IOException {
		Gson gson = crearGson();
		InputStream is = new FileInputStream(archivoCasoDePrueba);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		CasoDePrueba retorno = gson.fromJson(bufferedReader, CasoDePrueba.class);
		is.close();
		bufferedReader.close();
		return retorno;
	}

	public void guardarCasoDePrueba(CasoDePrueba casoDePrueba, File archivoCasoDePrueba) throws IOException {
		if(archivoCasoDePrueba.exists()){
			archivoCasoDePrueba.delete();
		}
		archivoCasoDePrueba.createNewFile();
		Gson gson = crearGson();
		OutputStream os = new FileOutputStream(archivoCasoDePrueba);
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
		bufferedWriter.write(gson.toJson(casoDePrueba));
		bufferedWriter.close();
		os.close();
	}
}
