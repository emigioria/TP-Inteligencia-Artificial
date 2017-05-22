/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.PilaPane;
import javafx.collections.ListChangeListener.Change;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MenuAdministracionController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/MenuAdministracion.fxml";

	@FXML
	private ToggleButton toggleButtonSimular;

	@FXML
	private ToggleButton toggleButtonCargarMapa;

	@FXML
	private ToggleButton toggleButtonCargarCasoDePrueba;

	private ToggleGroup toggleGroup = new ToggleGroup();

	@FXML
	private Pane background;

	private PilaPane backgroundApilador;

	private Toggle botonAnteriormenteSeleccionado;

	@FXML
	private void simular() {
		//TODO hacer
		//		if(this.nuevaScene("") == null){
		//			botonAnteriormenteSeleccionado.setSelected(true);
		//		}
	}

	@FXML
	private void cargarMapa() {
		if(this.nuevaScene(AltaMapaController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void cargarCasoDePrueba() {
		if(this.nuevaScene(AltaCasoDePruebaController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@Override
	public void actualizar() {
		stage.setTitle("Panel de administración");
	}

	@Override
	protected void inicializar() {
		//Agrupados botones
		toggleButtonSimular.setToggleGroup(toggleGroup);
		toggleButtonCargarMapa.setToggleGroup(toggleGroup);
		toggleButtonCargarCasoDePrueba.setToggleGroup(toggleGroup);
		addAlwaysOneSelectedSupport(toggleGroup);

		for(Toggle t: toggleGroup.getToggles()){
			agregarListenerSeleccionado(t);
		}

		actualizar();

		//Primera pantalla a mostrar
		toggleButtonCargarMapa.setSelected(true);
		cargarMapa();
	}

	private void agregarListenerSeleccionado(Toggle t) {
		t.selectedProperty().addListener((obs, oldV, newV) -> {
			if(!newV){
				botonAnteriormenteSeleccionado = t;
			}
		});
	}

	private void addAlwaysOneSelectedSupport(final ToggleGroup toggleGroup) {
		toggleGroup.getToggles().addListener((Change<? extends Toggle> c) -> {
			while(c.next()){
				for(final Toggle addedToggle: c.getAddedSubList()){
					addConsumeMouseEventfilter(addedToggle);
				}
			}
		});
		toggleGroup.getToggles().forEach(t -> {
			addConsumeMouseEventfilter(t);
		});
	}

	private void addConsumeMouseEventfilter(Toggle toggle) {
		EventHandler<MouseEvent> consumeMouseEventfilter = mouseEvent -> {
			if(((Toggle) mouseEvent.getSource()).isSelected()){
				mouseEvent.consume();
			}
		};

		((ToggleButton) toggle).addEventFilter(MouseEvent.MOUSE_PRESSED, consumeMouseEventfilter);
		((ToggleButton) toggle).addEventFilter(MouseEvent.MOUSE_RELEASED, consumeMouseEventfilter);
		((ToggleButton) toggle).addEventFilter(MouseEvent.MOUSE_CLICKED, consumeMouseEventfilter);
	}

	@Override
	protected ControladorPatrullero nuevaScene(String URLVista) {
		if(backgroundApilador == null || backgroundApilador.sePuedeSalir()){
			backgroundApilador = new PilaPane(background, this);
			return nuevaCambiarScene(URLVista, backgroundApilador, false);
		}
		return null;
	}

	@Override
	protected ControladorPatrullero cambiarScene(String URLVista) {
		throw new RuntimeException();
	}

	@Override
	public Boolean sePuedeSalir() {
		if(backgroundApilador != null){
			return backgroundApilador.sePuedeSalir();
		}
		return super.sePuedeSalir();
	}
}
