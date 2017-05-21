/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorDialogo;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.NombreObstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.Visibilidad;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.util.converter.IntegerStringConverter;

public class AltaObstaculoController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/AltaObstaculo.fxml";

	@FXML
	private Label lbLugar;

	@FXML
	private ComboBox<NombreObstaculo> cbNombre;

	@FXML
	private ComboBox<Visibilidad> cbVisibilidad;

	private ToggleGroup radioButtonsTipoObstaculo = new ToggleGroup();

	@FXML
	private RadioButton rbParcial;

	@FXML
	private RadioButton rbTotal;

	@FXML
	private Spinner<Integer> spRetardo;

	@FXML
	private Spinner<Integer> spHoraInicio;

	@FXML
	private Spinner<Integer> spHoraFin;

	private Lugar lugar;

	private Obstaculo obstaculo;

	@FXML
	private void guardar() {
		NombreObstaculo nombreObstaculo = cbNombre.getValue();
		Visibilidad visibilidad = cbVisibilidad.getValue();
		Integer retardo = spRetardo.getValue();
		Integer horaInicio = spHoraInicio.getValue();
		Integer horaFin = spHoraFin.getValue();

		if(rbParcial.isSelected()){
			obstaculo = CasoDePrueba.crearObstaculoParcial(nombreObstaculo, horaInicio, horaFin, visibilidad, lugar, retardo);
		}
		else if(rbTotal.isSelected()){
			obstaculo = CasoDePrueba.crearObstaculoTotal(nombreObstaculo, horaInicio, horaFin, visibilidad, lugar);
		}
		else{
			return;
		}

		salir();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nuevo obstaculo");
		stage.setResizable(false);

		lbLugar.setText("Obstáculo en " + lugar.toString());

		cbNombre.getItems().addAll(NombreObstaculo.values());
		cbNombre.getSelectionModel().select(0);
		cbVisibilidad.getItems().addAll(Visibilidad.values());
		cbVisibilidad.getSelectionModel().select(0);

		radioButtonsTipoObstaculo.getToggles().add(rbParcial);
		radioButtonsTipoObstaculo.getToggles().add(rbTotal);
		rbParcial.setSelected(true);

		spRetardo.getEditor().setTextFormatter(new TextFormatter<>(
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
		spRetardo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 10, 5));
		spRetardo.focusedProperty().addListener((obs, oldV, newV) -> {
			spRetardo.increment(0);
		});

		spHoraInicio.getEditor().setTextFormatter(new TextFormatter<>(
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
		spHoraInicio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000, 1));
		spHoraInicio.focusedProperty().addListener((obs, oldV, newV) -> {
			spHoraInicio.increment(0);
		});

		spHoraFin.getEditor().setTextFormatter(new TextFormatter<>(
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
		spHoraFin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000, 1));
		spHoraFin.focusedProperty().addListener((obs, oldV, newV) -> {
			spHoraFin.increment(0);
		});
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}

	public Obstaculo getResultado() {
		return obstaculo;
	}
}
