/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.util.Optional;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorDialogo;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.InterseccionGUIListCell;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.AristaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.InterseccionGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class AltaAristaController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/AltaArista.fxml";

	@FXML
	private ComboBox<InterseccionGUI> cbOrigen;

	@FXML
	private ComboBox<InterseccionGUI> cbDestino;

	@FXML
	private ComboBox<Calle> cbCalle;

	@FXML
	private Spinner<Integer> spPeso;

	private AristaGUI aristaGUI;

	@FXML
	private void guardar() {
		InterseccionGUI origen = cbOrigen.getValue();
		InterseccionGUI destino = cbDestino.getValue();
		String calleStr = cbCalle.getEditor().getText();

		if(calleStr.isEmpty()){
			return;
		}
		Optional<Calle> a = cbCalle.getItems().stream().filter(c -> c.getNombre().equals(calleStr)).findFirst();
		Calle calle;
		if(a.isPresent()){
			calle = a.get();
		}
		else{
			calle = MapaGUI.crearCalle(formateadorString.nombrePropio(calleStr));
		}

		if(origen == null || destino == null || calle == null || origen.getInterseccion().equals(destino.getInterseccion())){
			return;
		}
		Arista arista;
		try{
			arista = AristaGUI.crearArista(spPeso.getValue(), origen.getInterseccion(), destino.getInterseccion(), calle);
		} catch(Exception e){
			return;
		}
		aristaGUI = new AristaGUI(arista, origen, destino);

		salir();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nueva arista");
		stage.setResizable(false);

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
		spPeso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 5));
		spPeso.focusedProperty().addListener((obs, oldV, newV) -> {
			spPeso.increment(0);
		});

		cbOrigen.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if(oldV != null){
				cbDestino.getItems().add(oldV);
			}
			if(newV != null){
				cbDestino.getItems().remove(newV);
			}
		});
		cbDestino.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if(oldV != null){
				cbOrigen.getItems().add(oldV);
			}
			if(newV != null){
				cbOrigen.getItems().remove(newV);
			}
		});

		cbOrigen.setButtonCell(new InterseccionGUIListCell());
		cbOrigen.setCellFactory(l -> new InterseccionGUIListCell());
		cbDestino.setButtonCell(new InterseccionGUIListCell());
		cbDestino.setCellFactory(l -> new InterseccionGUIListCell());
	}

	public void inicializarCon(MapaGUI mapa) {
		Platform.runLater(() -> {
			cbOrigen.getItems().addAll(mapa.getIntersecciones());
			cbDestino.getItems().addAll(mapa.getIntersecciones());
			cbCalle.getItems().addAll(mapa.getMapa().getCalles());
			cbCalle.getItems().sort((x, y) -> x.getNombre().compareTo(y.getNombre()));
		});
	}

	public void setOrigen(InterseccionGUI origen) {
		Platform.runLater(() -> {
			cbOrigen.setDisable(true);
			cbOrigen.getSelectionModel().select(origen);
		});
	}

	public AristaGUI getResultado() {
		return aristaGUI;
	}
}
