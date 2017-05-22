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
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.CasoDePruebaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.InterseccionGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AltaCasoDePruebaController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/AltaCasoDePrueba.fxml";

	private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

	@FXML
	private ScrollPane scrollMapaPanel;

	@FXML
	private TreeView<Object> tvLugaresObstaculos;

	@FXML
	private ComboBox<Interseccion> cbPatrullero;

	@FXML
	private ComboBox<Interseccion> cbIncidente;

	private MapaGUI mapa;

	private CasoDePruebaGUI casoDePrueba;

	private InterseccionGUI interseccionActual;

	@FXML
	private void nuevoObstaculo() {
		Lugar lugar;
		TreeItem<Object> ramaSeleccionada = tvLugaresObstaculos.getSelectionModel().getSelectedItem();
		if(ramaSeleccionada != null && ramaSeleccionada.getValue() != null && ramaSeleccionada.getValue() instanceof Lugar){
			lugar = (Lugar) ramaSeleccionada.getValue();
		}
		else{
			return;
		}

		VentanaPersonalizada ventanaNuevoObstaculo = presentadorVentanas.presentarVentanaPersonalizada(AltaObstaculoController.URL_VISTA, stage);
		AltaObstaculoController controlador = (AltaObstaculoController) ventanaNuevoObstaculo.getControlador();
		controlador.setLugar(lugar);
		ventanaNuevoObstaculo.showAndWait();

		Obstaculo resultado = controlador.getResultado();
		if(resultado != null){
			casoDePrueba.getCasoDePrueba().getObstaculos().add(resultado);
			if(interseccionActual == null){
				iniciarPanelDerecho();
			}
			else{
				actualizarPanelDerecho(interseccionActual);
			}
			mapa.actualizarObstaculos(lugar);
		}
	}

	@FXML
	private void quitarObstaculo() {
		Obstaculo obstaculo;
		TreeItem<Object> ramaSeleccionada = tvLugaresObstaculos.getSelectionModel().getSelectedItem();
		if(ramaSeleccionada != null && ramaSeleccionada.getValue() != null && ramaSeleccionada.getValue() instanceof Obstaculo){
			obstaculo = (Obstaculo) ramaSeleccionada.getValue();
		}
		else{
			return;
		}
		obstaculo.getLugar().getObstaculos().remove(obstaculo);
		casoDePrueba.getCasoDePrueba().getObstaculos().remove(obstaculo);
		if(interseccionActual == null){
			iniciarPanelDerecho();
		}
		else{
			actualizarPanelDerecho(interseccionActual);
		}
		mapa.actualizarObstaculos(obstaculo.getLugar());
	}

	@Override
	protected void inicializar() {
		scrollMapaPanel.setOnMousePressed(t -> {
			sacarLugarPanelDerecho();
		});
		cbPatrullero.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			casoDePrueba.getCasoDePrueba().setPosicionInicialPatrullero(newV);
		});
		cbIncidente.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			casoDePrueba.getCasoDePrueba().setPosicionIncidente(newV);
		});

		nuevoCasoDePrueba();
	}

	private void actualizarPanelDerecho(InterseccionGUI interseccion) {
		interseccionActual = interseccion;

		TreeItem<Object> lugarItem = crearItemLugarTree(interseccion.getInterseccion());
		lugarItem.setExpanded(true);

		tvLugaresObstaculos.setRoot(lugarItem);
		tvLugaresObstaculos.getSelectionModel().select(lugarItem);
	}

	private TreeItem<Object> crearItemLugarTree(Interseccion interseccion) {
		TreeItem<Object> lugarItem = new TreeItem<>(interseccion);

		TreeItem<Object> aristasSalientes = new TreeItem<>("Aristas salientes");
		interseccion.getSalientes().stream().forEach(a -> {
			TreeItem<Object> arista = new TreeItem<>(a);
			aristasSalientes.getChildren().add(arista);

			TreeItem<Object> obstaculos;
			if(a.getObstaculos().isEmpty()){
				obstaculos = new TreeItem<>("Sin Obstaculos");
			}
			else{
				obstaculos = new TreeItem<>("Obstaculos");
				obstaculos.setExpanded(true);
				arista.setExpanded(true);
				aristasSalientes.setExpanded(true);
				lugarItem.setExpanded(true);
			}
			a.getObstaculos().stream().forEach(o -> {
				obstaculos.getChildren().add(new TreeItem<>(o));
			});
			arista.getChildren().add(obstaculos);
		});
		lugarItem.getChildren().add(aristasSalientes);

		TreeItem<Object> obstaculos;
		if(interseccion.getObstaculos().isEmpty()){
			obstaculos = new TreeItem<>("Sin Obstaculos");
		}
		else{
			obstaculos = new TreeItem<>("Obstaculos");
			obstaculos.setExpanded(true);
			lugarItem.setExpanded(true);
		}
		interseccion.getObstaculos().stream().forEach(o -> {
			obstaculos.getChildren().add(new TreeItem<>(o));
		});
		lugarItem.getChildren().add(obstaculos);

		return lugarItem;
	}

	private void sacarLugarPanelDerecho() {
		if(interseccionActual == null){
			return;
		}
		iniciarPanelDerecho();
	}

	private void iniciarPanelDerecho() {
		interseccionActual = null;

		TreeItem<Object> lugarRoot = new TreeItem<>("Lugares");
		lugarRoot.setExpanded(true);
		mapa.getIntersecciones().stream().forEach(i -> {
			TreeItem<Object> lugarItem = crearItemLugarTree(i.getInterseccion());
			lugarRoot.getChildren().add(lugarItem);
		});
		tvLugaresObstaculos.setRoot(lugarRoot);
	}

	@FXML
	private void nuevoCasoDePrueba() {
		CasoDePruebaGUI casoViejo = casoDePrueba;
		if(casoViejo != null){
			//Borrar obstaculos viejos
			casoViejo.getCasoDePrueba().getObstaculos().stream().forEach(o -> o.getLugar().getObstaculos().remove(o));
		}

		//Crear nuevo caso de prueba
		this.casoDePrueba = new CasoDePruebaGUI();
		cbPatrullero.getSelectionModel().select(null);
		cbIncidente.getSelectionModel().select(null);

		//Sacar lugar del panel derecho
		if(mapa != null){
			iniciarPanelDerecho();
			mapa.actualizarObstaculos();
		}
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
			return;
		}
		scrollMapaPanel.setContent(mapa.getNode());

		//Hacer algo al seleccionar una interseccion
		mapa.getIntersecciones().stream().forEach(i -> i.getNode().setOnMousePressed(t -> {
			actualizarPanelDerecho(i);
			t.consume();
		}));

		//Sacar lugar del panel derecho
		iniciarPanelDerecho();

		cbPatrullero.getItems().clear();
		cbPatrullero.getItems().addAll(mapa.getMapa().getEsquinas());
		cbIncidente.getItems().clear();
		cbIncidente.getItems().addAll(mapa.getMapa().getEsquinas());

		nuevoCasoDePrueba();
	}

	@FXML
	private void cargarCasoDePrueba() {
		if(mapa == null){
			return;
		}
		CasoDePruebaGUI casoViejo = casoDePrueba;

		//Cargar mapa
		File archivoCasoDePrueba = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_CASO_PRUEBA.getFileChooser(), stage);
		if(archivoCasoDePrueba == null){
			return;
		}
		try{
			this.casoDePrueba = new CasoDePruebaGUI(manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba), mapa.getMapa());
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		if(casoViejo != null){
			//Borrar obstaculos viejos
			casoViejo.getCasoDePrueba().getObstaculos().stream().forEach(o -> o.getLugar().getObstaculos().remove(o));
		}

		//Cargar interfaz
		cbPatrullero.getSelectionModel().select(casoDePrueba.getCasoDePrueba().getPosicionInicialPatrullero());
		cbIncidente.getSelectionModel().select(casoDePrueba.getCasoDePrueba().getPosicionIncidente());
		iniciarPanelDerecho();

		mapa.actualizarObstaculos();
	}

	@FXML
	private void guardarCasoDePrueba() {
		if(casoDePrueba.getCasoDePrueba().getPosicionInicialPatrullero() == null || casoDePrueba.getCasoDePrueba().getPosicionIncidente() == null){
			presentadorVentanas.presentarError("Error al guardar", "Debe seleccionar un origen para el patrullero y para el incidente", stage);
			return;
		}
		File archivoCasoDePrueba = presentadorVentanas.solicitarArchivoGuardado(FiltroArchivos.ARCHIVO_CASO_PRUEBA.getFileChooser(), stage);
		if(archivoCasoDePrueba == null){
			return;
		}
		try{
			manejadorArchivos.guardarCasoDePrueba(casoDePrueba.getCasoDePrueba(), archivoCasoDePrueba);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
	}

	@Override
	public Boolean sePuedeSalir() {
		return presentadorVentanas.presentarConfirmacion("Salir", "Se perderan los cambios no guardados ¿Seguro que desea salir?", stage).acepta();
	}
}
