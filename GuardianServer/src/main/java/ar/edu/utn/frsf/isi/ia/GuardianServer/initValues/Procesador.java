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

	public Procesador() {
		super();
		this.archivador = new Archivador();
	}

	public void leer(List<Incidente> listaIncidente, List<AxiomaTieneRiesgo> listaTieneRiesgo,
			List<AxiomaCritica> listaCritica, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {

		// leo el archivo y proceso fila a fila
		List<String> archivo = archivador.leerArchivoInitPl();
		archivo.forEach(fila -> procesarFila(fila, listaIncidente, listaTieneRiesgo, listaCritica, listaLimiteRiesgo));
	}

	private void procesarFila(String fila, List<Incidente> listaIncidente, List<AxiomaTieneRiesgo> listaTieneRiesgo,
			List<AxiomaCritica> listaCritica, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {

		// se procesa la linea y, si corresponde a alguno de los axiomas
		// deseados, se crea el objeto correspondiente
		if(fila.startsWith("tieneRiesgo")){
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

		if(fila.startsWith("critica")){
			int primerComa = fila.indexOf(",");
			int parentesisAbre = fila.indexOf("(");
			int parentesisCierra = fila.indexOf(")");

			String nombreIncidente = fila.substring(parentesisAbre + 1, primerComa).trim();
			Incidente incidente = obtenerIncidente(nombreIncidente, listaIncidente);
			String palabra = fila.substring(primerComa + 1, parentesisCierra).trim();

			AxiomaCritica critica = new AxiomaCritica(incidente, palabra);
			listaCritica.add(critica);
		}

		if(fila.startsWith("limiteRiesgo")){
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

		// se busca el incidente deseado en la lista de incidentes
		for(Incidente i: listaIncidente){
			if(i.getNombre().equals(nombreIncidente)){
				incidente = i;
			}
		}
		// si no existe en la lista se crea un Incidente nuevo y se lo agrega a
		// la lista
		if(incidente == null){
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
		List<AxiomaFrase> listaFrase = crearFrases(listaTieneRiesgo);
		añadirFrase(archivo, listaFrase);

		archivador.escribirArchivoCustomPl(archivo);
	}

	private List<AxiomaFrase> crearFrases(List<AxiomaTieneRiesgo> listaTieneRiesgo) {
		// se crea la lista de axiomas frase, según las palabras de los axiomas
		// tieneRiesgo
		List<AxiomaFrase> listaFrase = new ArrayList<>();
		listaTieneRiesgo.forEach(axioma -> {
			// si el atributo palabra contiene un "_" significa que es una frase
			if(axioma.getPalabra().contains("_")){
				obtenerAxiomasFrase(axioma.getPalabra(), listaFrase);
			}
		});
		return listaFrase;
	}

	private void obtenerAxiomasFrase(String frase, List<AxiomaFrase> listaFrase) {
		// EJEMPLO
		// proceso una frase del estilo "hola_nuevo_mundo" (cantidad de palabras
		// variable)
		// y se añaden a la lista de AxiomaFrase los axiomas equivalentes a:
		// frase(hola,nuevo)
		// frase(hola_nuevo,mundo)
		List<String> listaPalabras = new ArrayList<>();
		String palabra = "";
		for(int i = 0; i < frase.length(); i++){
			if(frase.charAt(i) == '_'){
				listaPalabras.add(palabra);
				palabra = "";
			}
			else{
				palabra += frase.charAt(i);
			}
		}
		String acumulador = "";
		for(int i = 0; i < listaPalabras.size() - 1; i++){
			if(i != 0){
				acumulador += "_";
			}
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
	}

	private void añadirTieneRiesgo(List<String> archivo, List<AxiomaTieneRiesgo> listaTieneRiesgo) {
		archivo.add("% Inicialización tieneRiesgo");
		listaTieneRiesgo.forEach(axioma -> archivo.add(axioma.toString() + "."));
		archivo.add("");
	}

	private void añadirCritica(List<String> archivo, List<AxiomaCritica> listaCritica) {
		archivo.add("% Inicialización critica");
		listaCritica.forEach(axioma -> archivo.add(axioma.toString() + "."));
		archivo.add("");
	}

	private void añadirLimiteRiesgo(List<String> archivo, List<AxiomaLimiteRiesgo> listaLimiteRiesgo) {
		archivo.add("% Inicialización limiteRiesgo");
		listaLimiteRiesgo.forEach(axioma -> archivo.add(axioma.toString() + "."));
		archivo.add("");
	}

	private void añadirInicializacionIncidentes(List<String> archivo, List<Incidente> listaIncidente) {
		archivo.add("% Inicialización contadores de riesgo");
		listaIncidente.forEach(inc -> archivo.add("riesgo(" + inc.getNombre() + ", 0)."));
		archivo.add("");

		archivo.add("% Inicialización del estado de los incidentes");
		listaIncidente.forEach(inc -> archivo.add("noSospecho(" + inc.getNombre() + ")."));
		archivo.add("");
	}

	private void añadirFrase(List<String> archivo, List<AxiomaFrase> listaFrase) {
		archivo.add("% Inicialización diccionario de frases");
		listaFrase.forEach(axioma -> archivo.add(axioma.toString() + "."));
		archivo.add("");
	}
}
