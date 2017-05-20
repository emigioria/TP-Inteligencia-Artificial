/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.io.File;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.ManejadorArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.FiltroArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaPersonalizada;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.CasoDePrueba;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.InterseccionGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;

public class AltaCasoDePruebaController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/AltaCasoDePrueba.fxml";

	private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

	@FXML
	private ScrollPane scrollMapaPanel;

	@FXML
	private GridPane panelDerecho;

	@FXML
	private TreeView<Lugar> tvLugaresObstaculos;

	private MapaGUI mapa;

	private CasoDePrueba casoDePrueba;

	private InterseccionGUI interseccionActual;

	@FXML
	private void nuevoObstaculo() {
		VentanaPersonalizada ventanaNuevoObstaculo = presentadorVentanas.presentarVentanaPersonalizada(AltaObstaculoController.URL_VISTA, stage);
		AltaObstaculoController controlador = (AltaObstaculoController) ventanaNuevoObstaculo.getControlador();
		//TODO hacer set lugar
		controlador.setLugar(null);
		ventanaNuevoObstaculo.showAndWait();

		Obstaculo resultado = controlador.getResultado();
		if(resultado != null){
			//TODO hacer
		}
	}

	@FXML
	private void quitarObstaculo() {
		//TODO hacer

	}

	@Override
	protected void inicializar() {
		//TODO completar
		scrollMapaPanel.setOnMousePressed(t -> {
			sacarPanelDerecho();
		});
	}

	private void actualizarPanelDerecho(InterseccionGUI interseccion) {
		//TODO hacer
		interseccionActual = interseccion;

	}

	private void sacarPanelDerecho() {
		//TODO hacer
		interseccionActual = null;

	}

	@FXML
	private void cargarMapa() {
		//Cargar mapa
		File archivoMapa = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			this.mapa = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
		scrollMapaPanel.setContent(mapa.getNode());

		//Hacer un MouseGesturesAdder para que se puedan arrastrar sus elementos
		mapa.getIntersecciones().stream().forEach(i -> i.getNode().setOnMousePressed(t -> {
			actualizarPanelDerecho(i);
			t.consume();
		}));

		//Sacar panel derecho
		sacarPanelDerecho();
	}

	@FXML
	private void cargarCasoDePrueba() {
		//TODO hacer
	}

	@FXML
	private void guardarCasoDePrueba() {
		//TODO hacer
		File archivoMapa = presentadorVentanas.solicitarArchivoGuardado(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			manejadorArchivos.guardarMapa(mapa.getMapa(), archivoMapa);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
	}
}
