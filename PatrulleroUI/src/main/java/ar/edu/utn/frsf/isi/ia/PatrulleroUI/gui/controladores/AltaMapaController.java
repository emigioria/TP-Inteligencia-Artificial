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
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.MouseGesturesAdder;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.StackPaneWithTag;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaPersonalizada;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.AristaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.InterseccionGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

public class AltaMapaController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/AltaMapa.fxml";

	private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

	@FXML
	private ScrollPane scrollMapaPanel;

	@FXML
	private GridPane panelDerecho;

	@FXML
	private Label InterseccionLabel;

	@FXML
	private Spinner<Integer> spPeso;

	@FXML
	private TableView<AristaGUI> tablaAristas;

	@FXML
	private TableColumn<AristaGUI, Number> columnaDestino;

	@FXML
	private TableColumn<AristaGUI, String> columnaCalle;

	@FXML
	private TableColumn<AristaGUI, Number> columnaPeso;

	private MouseGesturesAdder mga;

	private MapaGUI mapa;

	private InterseccionGUI interseccionActual;

	@FXML
	private void nuevaInterseccion() {
		InterseccionGUI nuevaInterseccion = new InterseccionGUI();
		mapa.agregarInterseccionGUI(nuevaInterseccion);
		mga.makeDraggable(nuevaInterseccion.getNode());
	}

	@FXML
	private void eliminarInterseccion() {
		if(interseccionActual == null){
			return;
		}
		mapa.quitarInterseccionGUI(interseccionActual);
		sacarPanelDerecho();
	}

	@FXML
	private void nuevaArista() {
		nuevaArista(null);
	}

	@FXML
	private void agregarArista() {
		nuevaArista(interseccionActual);
	}

	private void nuevaArista(InterseccionGUI origen) {
		VentanaPersonalizada ventanaNuevaArista = presentadorVentanas.presentarVentanaPersonalizada(AltaAristaController.URL_VISTA, stage);
		AltaAristaController controlador = (AltaAristaController) ventanaNuevaArista.getControlador();
		controlador.inicializarCon(mapa);
		if(origen != null){
			controlador.setOrigen(origen);
		}
		ventanaNuevaArista.showAndWait();

		AristaGUI resultado = controlador.getResultado();
		if(resultado != null){
			mapa.agregarAristaGUI(resultado);
		}
	}

	@FXML
	private void quitarArista() {
		AristaGUI aristaAQuitar = tablaAristas.getSelectionModel().getSelectedItem();
		if(aristaAQuitar == null){
			return;
		}
		mapa.quitarAristaGUI(aristaAQuitar);
	}

	@Override
	protected void inicializar() {
		scrollMapaPanel.setOnMousePressed(t -> {
			sacarPanelDerecho();
		});

		spPeso.getEditor().setTextFormatter(new TextFormatter<>(
				new IntegerStringConverter(), 0,
				c -> {
					if(c.isContentChange()){
						Integer numeroIngresado = null;
						try{
							numeroIngresado = new Integer(c.getControlNewText());
						} catch(Exception e){
							//No ingreso un entero;
						}
						if(numeroIngresado == null){
							return null;
						}
					}
					return c;
				}));
		spPeso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1));
		spPeso.focusedProperty().addListener((obs, oldV, newV) -> {
			spPeso.increment(0);
			if(interseccionActual != null){
				interseccionActual.getInterseccion().setPeso(spPeso.getValue());
			}
		});

		columnaCalle.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(formateadorString.nombrePropio(param.getValue().getArista().getCalle().toString()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		columnaDestino.setCellValueFactory(param -> {
			try{
				return new SimpleLongProperty(param.getValue().getArista().getDestino().getId());
			} catch(NullPointerException e){
				return new SimpleLongProperty(-1L);
			}
		});

		columnaPeso.setCellValueFactory(param -> {
			try{
				return new SimpleIntegerProperty(param.getValue().getArista().getPeso());
			} catch(NullPointerException e){
				return new SimpleIntegerProperty(-1);
			}
		});

		nuevoMapa();
	}

	private void actualizarPanelDerecho(InterseccionGUI interseccion) {
		interseccionActual = interseccion;
		panelDerecho.setVisible(true);
		InterseccionLabel.setText("Intersección " + interseccion.getInterseccion().getId());
		spPeso.getValueFactory().setValue(interseccion.getInterseccion().getPeso());
		tablaAristas.getItems().clear();
		tablaAristas.getItems().addAll(interseccion.getSalientes());
	}

	private void sacarPanelDerecho() {
		interseccionActual = null;
		panelDerecho.setVisible(false);
	}

	@FXML
	private void nuevoMapa() {
		//Crear nuevo mapa
		this.mapa = new MapaGUI();
		scrollMapaPanel.setContent(mapa.getNode());

		//Hacer un MouseGesturesAdder para que se puedan arrastrar sus elementos
		crearMouseGesturesAdder();

		//Sacar panel derecho
		sacarPanelDerecho();
	}

	private void crearMouseGesturesAdder() {
		mga = new MouseGesturesAdder(mapa.getNode());
		mga.setOnMousePressed(t -> {
			if(t.getSource() instanceof StackPaneWithTag){
				StackPaneWithTag<?> a = (StackPaneWithTag<?>) t.getSource();
				Object interseccionTalVez = a.getTag();
				if(interseccionTalVez instanceof InterseccionGUI){
					InterseccionGUI interseccion = (InterseccionGUI) interseccionTalVez;
					actualizarPanelDerecho(interseccion);
				}
				t.consume();
			}
		});
	}

	@FXML
	private void cargarMapa() {
		//Cargar mapa
		File archivoMapa = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		try{
			this.mapa = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
		scrollMapaPanel.setContent(mapa.getNode());

		//Hacer un MouseGesturesAdder para que se puedan arrastrar sus elementos
		crearMouseGesturesAdder();

		//Sacar panel derecho
		sacarPanelDerecho();
	}

	@FXML
	private void guardarMapa() {
		File archivoMapa = presentadorVentanas.solicitarArchivoGuardado(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		try{
			manejadorArchivos.guardarMapa(mapa.getMapa(), archivoMapa);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
	}
}