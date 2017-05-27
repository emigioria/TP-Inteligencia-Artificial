/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.util.Collection;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorDialogo;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.ObstaculoParcial;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class VerObstaculosLugarController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/VerObstaculosLugar.fxml";

	@FXML
	private TableView<Obstaculo> tablaObstaculos;

	@FXML
	private TableColumn<Obstaculo, Number> columnaId;

	@FXML
	private TableColumn<Obstaculo, String> columnaNombre;

	@FXML
	private TableColumn<Obstaculo, Number> columnaTiempoInicio;

	@FXML
	private TableColumn<Obstaculo, Number> columnaTiempoFin;

	@FXML
	private TableColumn<Obstaculo, String> columnaVisibilidad;

	@FXML
	private TableColumn<Obstaculo, Number> columnaRetardo;

	@FXML
	private TableColumn<Obstaculo, String> columnaTipo;

	private Lugar lugar;

	private Collection<Obstaculo> obstaculos;

	@Override
	protected void inicializar() {
		stage.setTitle("Obstáculos de: " + lugar);
		stage.setResizable(true);

		columnaId.setCellValueFactory(param -> {
			try{
				return new SimpleLongProperty(param.getValue().getId());
			} catch(NullPointerException e){
				return new SimpleLongProperty(-1L);
			}
		});

		columnaNombre.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getNombre().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		columnaTiempoInicio.setCellValueFactory(param -> {
			try{
				return new SimpleIntegerProperty(param.getValue().getTiempoInicio());
			} catch(NullPointerException e){
				return new SimpleIntegerProperty(-1);
			}
		});

		columnaTiempoFin.setCellValueFactory(param -> {
			try{
				return new SimpleIntegerProperty(param.getValue().getTiempoFin());
			} catch(NullPointerException e){
				return new SimpleIntegerProperty(-1);
			}
		});

		columnaVisibilidad.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getVisibilidad().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		columnaRetardo.setCellValueFactory(param -> {
			try{
				if(param.getValue().getClass() == ObstaculoParcial.class){
					return new SimpleIntegerProperty(((ObstaculoParcial) param.getValue()).getRetardoMultiplicativo());
				}
				else{
					return new SimpleIntegerProperty(-1);
				}
			} catch(NullPointerException e){
				return new SimpleIntegerProperty(-1);
			}
		});

		columnaTipo.setCellValueFactory(param -> {
			try{
				if(param.getValue().getClass() == ObstaculoParcial.class){
					return new SimpleStringProperty("Parcial");
				}
				else{
					return new SimpleStringProperty("Total");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		tablaObstaculos.getItems().addAll(obstaculos);
	}

	public void inicializarCon(Lugar lugar, Collection<Obstaculo> obstaculos) {
		this.lugar = lugar;
		this.obstaculos = obstaculos;
	}
}
