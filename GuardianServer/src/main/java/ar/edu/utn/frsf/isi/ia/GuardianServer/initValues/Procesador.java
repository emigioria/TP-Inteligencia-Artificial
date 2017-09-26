/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaCritica;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaFrase;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaLimiteRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaTieneRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.Incidente;

public class Procesador {

	private Archivador archivador;

	public Procesador(Archivador archivador) {
		super();
		this.archivador = archivador;
	}

	public void leer(List<Incidente> listaIncidente, List<AxiomaTieneRiesgo> listaTieneRiesgo,
			List<AxiomaCritica> listaCritica, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {

		List<String> archivo = archivador.leerArchivo();
		archivo.forEach(fila -> procesarFila(fila, listaIncidente, listaTieneRiesgo, listaCritica, listaLimiteRiesgo));
	}

	private void procesarFila(String fila, List<Incidente> listaIncidente, List<AxiomaTieneRiesgo> listaTieneRiesgo,
			List<AxiomaCritica> listaCritica, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {
		if(fila.startsWith("tieneRiesgo")) {
			int primerComa = fila.indexOf(",");
			int ultimaComa = fila.lastIndexOf(",");
			int parentesisAbre = fila.indexOf("(");
			int parentesisCierra = fila.indexOf(")");

			String nombreIncidente = fila.substring(parentesisAbre + 1, primerComa).trim();
			Incidente incidente = obtenerIncidente(nombreIncidente, listaIncidente);
			String palabra = fila.substring(primerComa + 1, ultimaComa).trim();
			String valor = fila.substring(ultimaComa + 1, parentesisCierra).trim();

			AxiomaTieneRiesgo tieneRiesgo = new AxiomaTieneRiesgo(incidente, palabra, valor);
			listaTieneRiesgo.add(tieneRiesgo);
		}

		if(fila.startsWith("critica")) {
			int primerComa = fila.indexOf(",");
			int parentesisAbre = fila.indexOf("(");
			int parentesisCierra = fila.indexOf(")");

			String nombreIncidente = fila.substring(parentesisAbre + 1, primerComa).trim();
			Incidente incidente = obtenerIncidente(nombreIncidente, listaIncidente);
			String palabra = fila.substring(primerComa + 1, parentesisCierra).trim();

			AxiomaCritica critica = new AxiomaCritica(incidente, palabra);
			listaCritica.add(critica);
		}

		if(fila.startsWith("limiteRiesgo")) {
			int primerComa = fila.indexOf(",");
			int parentesisAbre = fila.indexOf("(");
			int parentesisCierra = fila.indexOf(")");

			String nombreIncidente = fila.substring(parentesisAbre + 1, primerComa).trim();
			Incidente incidente = obtenerIncidente(nombreIncidente, listaIncidente);
			String valor = fila.substring(primerComa + 1, parentesisCierra).trim();

			AxiomaLimiteRiesgo limiteRiesgo = new AxiomaLimiteRiesgo(incidente, valor);
			listaLimiteRiesgo.add(limiteRiesgo);
		}
	}

	private Incidente obtenerIncidente(String nombreIncidente, List<Incidente> listaIncidente) {
		Incidente incidente = null;

		for(Incidente i : listaIncidente) {
			if(i.getNombre().equals(nombreIncidente)) {
				incidente = i;
			}
		}
		if(incidente == null) {
			incidente = new Incidente(nombreIncidente);
			listaIncidente.add(incidente);
		}

		return incidente;
	}

	public void guardar(List<Incidente> listaIncidente, List<AxiomaTieneRiesgo> listaTieneRiesgo,
			List<AxiomaCritica> listaCritica, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {

		List<String> archivo = new ArrayList<>();

		añadirEncabezado(archivo);
		añadirTieneRiesgo(archivo, listaTieneRiesgo);
		añadirCritica(archivo, listaCritica);
		añadirLimiteRiesgo(archivo, listaLimiteRiesgo);
		añadirInicializacionIncidentes(archivo, listaIncidente);
		List<AxiomaFrase> listaFrase = crearFrases(listaTieneRiesgo, listaCritica);
		añadirFrase(archivo, listaFrase);

		archivador.escribirArchivo(archivo);
	}

	private List<AxiomaFrase> crearFrases(List<AxiomaTieneRiesgo> listaTieneRiesgo, List<AxiomaCritica> listaCritica) {
		List<AxiomaFrase> listaFrase = new ArrayList<>();
		for(AxiomaTieneRiesgo a : listaTieneRiesgo) {
			if(a.getPalabra().contains("_")) {
				obtenerAxiomasFrase(a.getPalabra(), listaFrase);
			}
		}

		for(AxiomaCritica a : listaCritica) {
			if(a.getPalabra().contains("_")) {
				obtenerAxiomasFrase(a.getPalabra(), listaFrase);
			}
		}

		return listaFrase;
	}

	private void obtenerAxiomasFrase(String frase, List<AxiomaFrase> listaFrase) {
		List<String> listaPalabras = new ArrayList<>();
		String palabra = "";
		for(int i = 0; i < frase.length(); i++) {
			if(frase.charAt(i) == '_') {
				listaPalabras.add(palabra);
				palabra = "";
			} else {
				palabra += frase.charAt(i);
			}
		}
		String acumulador = "";
		for(int i = 0; i < listaPalabras.size() - 1; i++) {
			if(i != 0)
				acumulador += "_";
			acumulador += listaPalabras.get(i);
			listaFrase.add(new AxiomaFrase(acumulador, listaPalabras.get(i + 1)));
		}
	}

	private void añadirEncabezado(List<String> archivo) {
		// licencia
		archivo.add("% Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi");
		archivo.add("% This Source Code Form is subject to the terms of the Mozilla Public");
		archivo.add("% License, v. 2.0. If a copy of the MPL was not distributed with this");
		archivo.add("% file, You can obtain one at http://mozilla.org/MPL/2.0/.");
		archivo.add("");
		// autores
		archivo.add("% Autor: Rico Andrés, Rebechi Esteban, Gioria Emiliano.");
		archivo.add("% Fecha: 24/06/2017");
		archivo.add("");
		// predicados
		archivo.add("% Predicados");
		archivo.add(":- dynamic(tieneRiesgo/3).");
		archivo.add(":- dynamic(escuchada/2).");
		archivo.add(":- dynamic(riesgo/2).");
		archivo.add(":- dynamic(limiteRiesgo/2).");
		archivo.add(":- dynamic(accion/1).");
		archivo.add(":- dynamic(sospecho/1).");
		archivo.add(":- dynamic(noSospecho/1).");
		archivo.add(":- dynamic(critica/2).");
		archivo.add(":- dynamic(clasificada/2).");
		archivo.add(":- dynamic(frase/2).");
		archivo.add("");

	}

	private void añadirTieneRiesgo(List<String> archivo, List<AxiomaTieneRiesgo> listaTieneRiesgo) {
		archivo.add("% Inicialización tieneRiesgo");
		for(AxiomaTieneRiesgo a : listaTieneRiesgo) {
			archivo.add(a.toString() + ".");
		}
		archivo.add("");
	}

	private void añadirCritica(List<String> archivo, List<AxiomaCritica> listaCritica) {
		archivo.add("% Inicialización critica");
		for(AxiomaCritica a : listaCritica) {
			archivo.add(a.toString() + ".");
		}
		archivo.add("");
	}

	private void añadirLimiteRiesgo(List<String> archivo, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {
		archivo.add("% Inicialización limiteRiesgo");
		for(AxiomaLimiteRiesgo a : listaLimiteRiesgo) {
			archivo.add(a.toString() + ".");
		}
		archivo.add("");
	}

	private void añadirInicializacionIncidentes(List<String> archivo, List<Incidente> listaIncidente) {
		archivo.add("% Inicialización contadores de riesgo");
		for(Incidente i : listaIncidente) {
			archivo.add("riesgo(" + i.getNombre() + ", 0).");
		}
		archivo.add("");

		archivo.add("% Inicialización del estado de los incidentes");
		for(Incidente i : listaIncidente) {
			archivo.add("noSospecho(" + i.getNombre() + ").");
		}
		archivo.add("");
	}

	private void añadirFrase(List<String> archivo, List<AxiomaFrase> listaFrase) {
		archivo.add("% Inicialización diccionario de frases");
		for(AxiomaFrase a : listaFrase) {
			archivo.add(a.toString() + ".");
		}
		archivo.add("");
	}
}
