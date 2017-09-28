/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaLimiteRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.Fila;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.Incidente;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorDialogo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;

public class ConfiguracionValoresController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/ConfiguracionValores.fxml";

	@FXML
	private ComboBox<Incidente> comboBoxIncidente;

	@FXML
	private Label labelIncidente;

	@FXML
	private VBox vBoxContenidoIncidente;

	@FXML
	private Label labelLimite;
	@FXML
	private Spinner<Integer> spinner_limite;

	@FXML
	private TextField textField_palabra;
	@FXML
	private Spinner<Integer> spinner_valor;
	@FXML
	private CheckBox checkBoxCritica;
	@FXML
	private TableView<Fila> tableView;
	@FXML
	private TableColumn<Fila, String> tableColumnPalabra;
	@FXML
	private TableColumn<Fila, String> tableColumnValor;
	@FXML
	private TableColumn<Fila, Boolean> tableColumnCritica;
	@FXML
	private Button buttonQuitar;

	private Adaptador adaptador;

	private List<Fila> listaFilas;
	private List<Incidente> listaIncidentes;
	private List<AxiomaLimiteRiesgo> listaLimiteRiesgo;

	@Override
	@FXML
	protected void inicializar() {
		inicializarDatos();
		setearDatoColumnas();
		habilitarBotones(null);
		setearListeners();
		setearSpinnerValueFactory();
		vBoxContenidoIncidente.setVisible(false);
	}

	private void inicializarDatos() {
		listaFilas = new ArrayList<>();
		listaIncidentes = new ArrayList<>();
		listaLimiteRiesgo = new ArrayList<>();
		adaptador = new Adaptador();
		adaptador.obtenerDatos(listaFilas, listaIncidentes, listaLimiteRiesgo);

		comboBoxIncidente.getItems().addAll(listaIncidentes);
	}

	private void setearDatoColumnas() {
		tableColumnPalabra.setCellValueFactory(cellData -> cellData.getValue().getPalabra());
		tableColumnValor.setCellValueFactory(cellData -> cellData.getValue().getValor());
		tableColumnCritica.setCellValueFactory(cellData -> cellData.getValue().getCritica());
		tableColumnCritica.setCellFactory(cell -> new CheckBoxTableCell<>());

	}

	private void habilitarBotones(Fila fila) {
		if(fila == null) {
			buttonQuitar.setDisable(true);
		} else {
			buttonQuitar.setDisable(false);
		}
	}

	private void setearListeners() {
		// cuando se selecciona una fila de la tabla, se habilitan/deshabilitan
		// botones asociados
		tableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> habilitarBotones(newValue));
		// cuando cambia el incidente elegido se actualiza todo el contenido
		comboBoxIncidente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> actualizarContenido(newValue));
	}

	private void setearSpinnerValueFactory() {
		int minLimite = 1; // valor mínimo para el spinner
		int maxLimite = 30; // valor máximo
		int initLimite = 10; // valor inicial
		SpinnerValueFactory<Integer> valueFactoryLimite = new SpinnerValueFactory.IntegerSpinnerValueFactory(minLimite,
				maxLimite, initLimite);
		spinner_limite.setValueFactory(valueFactoryLimite);

		int minValor = 1; // valor mínimo para el spinner
		int maxValor = 10; // valor máximo
		int initValor = 1; // valor inicial
		SpinnerValueFactory<Integer> valueFactoryValor = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValor,
				maxValor, initValor);
		spinner_valor.setValueFactory(valueFactoryValor);
	}

	private void actualizarContenido(Incidente incidente) {
		tableView.getItems().clear();
		if(incidente != null) {
			// se filtran las filas asociadas al incidente seleccionado y se las
			// añade a la tabla
			List<Fila> nuevaLista = listaFilas.stream().filter(f -> {
				return f.getIncidente().equals(incidente);
			}).collect(Collectors.toList());
			tableView.getItems().addAll(nuevaLista);

			// actualizo limite
			labelLimite.setText(obtenerAxiomaLimite(incidente).getValor());

			// actualizo label
			labelIncidente.setText(incidente.getNombre());

			// habilito la visualización del contenido
			vBoxContenidoIncidente.setVisible(true);
		} else {
			vBoxContenidoIncidente.setVisible(false);
		}
	}

	private AxiomaLimiteRiesgo obtenerAxiomaLimite(Incidente incidente) {
		AxiomaLimiteRiesgo axioma = null;

		for(AxiomaLimiteRiesgo a : listaLimiteRiesgo) {
			if(a.getIncidente().equals(incidente)) {
				axioma = a;
				break;
			}
		}

		return axioma;
	}

	@FXML
	private void cambiarLimite() {
		Incidente incidenteSeleccionado = comboBoxIncidente.getSelectionModel().getSelectedItem();
		String valor = spinner_limite.getValue().toString();
		obtenerAxiomaLimite(incidenteSeleccionado).setValor(valor);
		labelLimite.setText(valor);
	}

	@FXML
	private void agregar() {
		// obtengo datos ingresados por el usuario
		Incidente incidente = comboBoxIncidente.getValue();
		String palabra = textField_palabra.getText().trim().toLowerCase();
		int valorInt = spinner_valor.getValue();
		boolean critica = checkBoxCritica.isSelected();

		String error = error(palabra, incidente);
		if(error != null) {
			// si hay errores se lo muestro al usuario
			mostrarError(error);
		} else {
			// si los datos son válidos creo la nueva fila
			palabra = palabra.replace("\\s", "_"); // reemplazo espacios con "_"
			Fila fila = new Fila(incidente, palabra, valorInt, critica);
			listaFilas.add(fila);
			actualizarContenido(incidente);
		}
	}

	private void mostrarError(String mensajeError) {
		// TODO mostrar al usuario el mensaje de error

	}

	private String error(String palabra, Incidente incidente) {
		if(palabra.isEmpty()) {
			return "No ha ingresado nignuna palabra/frase.";
		}
		if(!esValidoFormato(palabra)) {
			return "La palabra/frase no puede contener caracteres especiales.";
		}
		if(yaExistePalabra(palabra.replace("\\s", "_"), incidente)) {
			return "La palabra/frase ya está asociada al incidente.";
		}
		return null;
	}

	private boolean esValidoFormato(String palabra) {
		Pattern pat = Pattern.compile("[0-9a-zA-Z\\ ÁÉÍÓÚÜÑáéíóúüñ]{1,30}");
		return pat.matcher(palabra).matches();
	}

	private boolean yaExistePalabra(String palabra, Incidente incidente) {
		return listaFilas.stream()
				.anyMatch(f -> f.getIncidente().equals(incidente) && f.getPalabraStr().equals(palabra));
	}

	@FXML
	private void quitar() {
		Fila filaSeleccionada = tableView.getSelectionModel().getSelectedItem();
		tableView.getItems().remove(filaSeleccionada);
	}

	@FXML
	private void guardarConfiguracion() {
		adaptador.guardarDatos(listaFilas, listaIncidentes, listaLimiteRiesgo);
		salir();
	}

}
