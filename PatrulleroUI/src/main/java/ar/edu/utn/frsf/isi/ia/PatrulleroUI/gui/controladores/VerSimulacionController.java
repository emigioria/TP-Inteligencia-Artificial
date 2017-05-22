/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.ManejadorArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.FiltroArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.CasoDePruebaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import frsf.cidisi.exercise.patrullero.search.PatrulleroMain;
import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class VerSimulacionController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/VerSimulacion.fxml";

	@FXML
	private ScrollPane scrollEstadoAmbiente;

	@FXML
	private ScrollPane scrollEstadoPatrullero;

	@FXML
	private Label lbUltimaAccion;

	@FXML
	private Label lbHora;

	@FXML
	private Label lbSimulandoOListo;

	private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

	private MapaGUI mapaAmbiente;

	private MapaGUI mapaPatrullero;

	private CasoDePrueba casoDePruebaPatrullero;

	@Override
	protected void inicializar() {
		// TODO Auto-generated method stub

	}

	@FXML
	private void cargarDatos() {
		cargarMapa();
		cargarCasoDePrueba();
	}

	private void cargarMapa() {
		//Cargar mapa
		File archivoMapa = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			this.mapaAmbiente = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
			this.mapaPatrullero = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}
		scrollEstadoAmbiente.setContent(mapaAmbiente.getNode());
		scrollEstadoPatrullero.setContent(mapaPatrullero.getNode());
	}

	private void cargarCasoDePrueba() {
		if(mapaAmbiente == null || mapaPatrullero == null){
			return;
		}

		//Cargar caso de prueba
		File archivoCasoDePrueba = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_CASO_PRUEBA.getFileChooser(), stage);
		if(archivoCasoDePrueba == null){
			return;
		}
		try{
			new CasoDePruebaGUI(manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba), mapaAmbiente.getMapa());
			casoDePruebaPatrullero = manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		mapaAmbiente.actualizarObstaculos();
	}

	@FXML
	private void comenzar() throws IOException {
		File hola = new File("Hola");
		if(hola.exists()){
			hola.delete();
		}
		hola.createNewFile();
		System.setOut(new PrintStream(new FileOutputStream(new File("Hola"))));

		new PatrulleroMain(
				mapaPatrullero.getMapa(),
				(Interseccion) mapaPatrullero.getMapa().getLugar(casoDePruebaPatrullero.getPosicionInicialPatrullero()),
				(Interseccion) mapaPatrullero.getMapa().getLugar(casoDePruebaPatrullero.getPosicionIncidente()),
				mapaAmbiente.getMapa(),
				(Interseccion) mapaAmbiente.getMapa().getLugar(casoDePruebaPatrullero.getPosicionInicialPatrullero())).start();
	}

	@FXML
	private void pausa() {
		// TODO Auto-generated method stub

	}

	@FXML
	private void avanzar() {
		// TODO Auto-generated method stub

	}

	@FXML
	private void irAlFinal() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean sePuedeSalir() {
		return presentadorVentanas.presentarConfirmacion("Salir", "Se perderan los datos simulados ¿Seguro que desea salir?", stage).acepta();
	}
}
