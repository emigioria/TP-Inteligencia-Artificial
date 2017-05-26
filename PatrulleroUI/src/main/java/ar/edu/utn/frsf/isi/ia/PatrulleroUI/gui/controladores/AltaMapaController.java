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
import javafx.scene.input.MouseButton;
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

	private MapaGUI mapaGUI;

	private InterseccionGUI interseccionActual;

	private InterseccionGUI ultimaInterseccionAgregada;

	@FXML
	private void nuevaInterseccion() {
		ultimaInterseccionAgregada = new InterseccionGUI();
		mapaGUI.agregarInterseccionGUI(ultimaInterseccionAgregada);
		mga.makeDraggable(ultimaInterseccionAgregada);
	}

	@FXML
	private void eliminarInterseccion() {
		if(interseccionActual == null){
			return;
		}
		mapaGUI.quitarInterseccionGUI(interseccionActual);
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
		controlador.inicializarCon(mapaGUI);
		if(origen != null){
			controlador.setOrigen(origen);
		}
		ventanaNuevaArista.showAndWait();

		AristaGUI resultado = controlador.getResultado();
		if(resultado != null){
			mapaGUI.agregarAristaGUI(resultado);
			if(origen != null){
				actualizarPanelDerecho(origen);
			}
		}
	}

	@FXML
	private void quitarArista() {
		AristaGUI aristaAQuitar = tablaAristas.getSelectionModel().getSelectedItem();
		if(aristaAQuitar == null){
			return;
		}
		InterseccionGUI interseccion = aristaAQuitar.getOrigen();
		mapaGUI.quitarAristaGUI(aristaAQuitar);
		actualizarPanelDerecho(interseccion);
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
		this.mapaGUI = new MapaGUI();
		scrollMapaPanel.setContent(mapaGUI);

		//Hacer un MouseGesturesAdder para que se puedan arrastrar sus elementos
		crearMouseGesturesAdder();

		//Sacar panel derecho
		sacarPanelDerecho();
	}

	private void crearMouseGesturesAdder() {
		mga = new MouseGesturesAdder(mapaGUI);
		mga.setOnMousePressed(t -> {
			if(t.getSource() instanceof InterseccionGUI){
				InterseccionGUI interseccion = (InterseccionGUI) t.getSource();
				actualizarPanelDerecho(interseccion);
				t.consume();
			}
		});
		mapaGUI.setOnMousePressed(t -> {
			if(t.getButton().compareTo(MouseButton.SECONDARY) == 0){
				nuevaInterseccion();
				ultimaInterseccionAgregada.getInterseccion().setCoordenadaX(t.getX());
				ultimaInterseccionAgregada.getInterseccion().setCoordenadaY(t.getY());
			}
		});
	}

	@FXML
	private void cargarMapa() {
		//Cargar mapa
		File archivoMapa = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			this.mapaGUI = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}
		scrollMapaPanel.setContent(mapaGUI);

		//Hacer un MouseGesturesAdder para que se puedan arrastrar sus elementos
		crearMouseGesturesAdder();
		mapaGUI.getIntersecciones().stream().forEach(i -> mga.makeDraggable(i));

		//Sacar panel derecho
		sacarPanelDerecho();
	}

	@FXML
	private void guardarMapa() {
		File archivoMapa = presentadorVentanas.solicitarArchivoGuardado(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			manejadorArchivos.guardarMapa(mapaGUI.getMapa(), archivoMapa);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
	}

	@Override
	public Boolean sePuedeSalir() {
		return presentadorVentanas.presentarConfirmacion("Salir", "Se perderan los cambios no guardados ¿Seguro que desea salir?", stage).acepta();
	}
}
