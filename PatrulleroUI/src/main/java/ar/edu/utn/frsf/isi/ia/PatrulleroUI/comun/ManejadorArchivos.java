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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;

import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;

public class ManejadorArchivos {

	public Mapa cargarMapa(File archivoMapa) throws FileNotFoundException {
		Gson gson = new Gson();
		InputStream is = new FileInputStream(archivoMapa);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		Object objeto = gson.fromJson(bufferedReader, Object.class);
		return null;
	}

	public void guardarMapa(Mapa mapa, File archivoMapa) throws FileNotFoundException {
		Gson gson = new Gson();

		OutputStream os = new FileOutputStream(archivoMapa);
		BufferedWriter bufferedReader = new BufferedWriter(new OutputStreamWriter(os));
	}
}
