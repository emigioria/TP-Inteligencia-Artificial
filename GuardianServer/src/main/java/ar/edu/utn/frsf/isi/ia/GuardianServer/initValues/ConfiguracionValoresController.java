package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaCritica;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaLimiteRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.AxiomaTieneRiesgo;
import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo.Incidente;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorDialogo;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ConfiguracionValoresController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/ConfiguracionValores.fxml";

	@FXML
	private TextField textFieldNombreIncidente;
	@FXML
	private Button buttonQuitarIncidente;
	@FXML
	private TableView<Incidente> tableViewIncidente;
	@FXML
	private TableColumn<Incidente, String> tableColumnNombreIncidente;

	@FXML
	private ComboBox<Incidente> tieneRiesgo_comboBox_tipoIncidente;
	@FXML
	private Spinner<Integer> tieneRiesgo_spinner_valor;
	@FXML
	private TextField tieneRiesgo_textField_palabra;
	@FXML
	private TableView<AxiomaTieneRiesgo> tableViewTieneRiesgo;
	@FXML
	private TableColumn<AxiomaTieneRiesgo, String> tableColumnTieneRiesgo;
	@FXML
	private Button buttonQuitarTieneRiesgo;

	@FXML
	private ComboBox<Incidente> critica_comboBox_tipoIncidente;
	@FXML
	private TextField critica_textField_palabra;
	@FXML
	private TableView<AxiomaCritica> tableViewCritica;
	@FXML
	private TableColumn<AxiomaCritica, String> tableColumnCritica;
	@FXML
	private Button buttonQuitarCritica;

	@FXML
	private ComboBox<Incidente> limiteRiesgo_comboBox_tipoIncidente;
	@FXML
	private Spinner<Integer> limiteRiesgo_spinner_valor;
	@FXML
	private TableView<AxiomaLimiteRiesgo> tableViewLimiteRiesgo;
	@FXML
	private TableColumn<AxiomaLimiteRiesgo, String> tableColumnLimiteRiesgo;
	@FXML
	private Button buttonQuitarLimiteRiesgo;

	private Procesador procesador;

	@Override
	@FXML
	protected void inicializar() {
		procesador = new Procesador(new Archivador());

		inicializarDatos();
		setearDatoColumnas();

		habilitarBotonesIncidente(null);
		habilitarBotonesTieneRiesgo(null);
		habilitarBotonesCritica(null);
		habilitarBotonesLimiteRiesgo(null);

		setearListeners();
	}

	private void inicializarDatos() {
		List<Incidente> listaIncidente = new ArrayList<>();
		List<AxiomaTieneRiesgo> listaTieneRiesgo = new ArrayList<>();
		List<AxiomaCritica> listaCritica = new ArrayList<>();
		List<AxiomaLimiteRiesgo> listaLimiteRiesgo = new ArrayList<>();

		procesador.leer(listaIncidente, listaTieneRiesgo, listaCritica, listaLimiteRiesgo);

		tableViewIncidente.getItems().addAll(listaIncidente);
		tableViewTieneRiesgo.getItems().addAll(listaTieneRiesgo);
		tableViewCritica.getItems().addAll(listaCritica);
		tableViewLimiteRiesgo.getItems().addAll(listaLimiteRiesgo);

		tieneRiesgo_comboBox_tipoIncidente.getItems().addAll(listaIncidente);
		critica_comboBox_tipoIncidente.getItems().addAll(listaIncidente);
		limiteRiesgo_comboBox_tipoIncidente.getItems().addAll(listaIncidente);
	}

	private void setearDatoColumnas() {
		tableColumnNombreIncidente
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
		tableColumnTieneRiesgo
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
		tableColumnCritica.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
		tableColumnLimiteRiesgo
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));

	}

	private void habilitarBotonesIncidente(Incidente incidente) {
		if(incidente == null){
			buttonQuitarIncidente.setDisable(true);
		}
		else{
			buttonQuitarIncidente.setDisable(false);
		}
	}

	private void habilitarBotonesTieneRiesgo(AxiomaTieneRiesgo axioma) {
		if(axioma == null){
			buttonQuitarTieneRiesgo.setDisable(true);
		}
		else{
			buttonQuitarTieneRiesgo.setDisable(false);
		}
	}

	private void habilitarBotonesCritica(AxiomaCritica axioma) {
		if(axioma == null){
			buttonQuitarCritica.setDisable(true);
		}
		else{
			buttonQuitarCritica.setDisable(false);
		}
	}

	private void habilitarBotonesLimiteRiesgo(AxiomaLimiteRiesgo axioma) {
		if(axioma == null){
			buttonQuitarLimiteRiesgo.setDisable(true);
		}
		else{
			buttonQuitarLimiteRiesgo.setDisable(false);
		}
	}

	private void setearListeners() {
		tableViewIncidente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> habilitarBotonesIncidente(newValue));
		tableViewTieneRiesgo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> habilitarBotonesTieneRiesgo(newValue));
		tableViewCritica.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> habilitarBotonesCritica(newValue));
		tableViewLimiteRiesgo.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> habilitarBotonesLimiteRiesgo(newValue));

	}

	@FXML
	private void agregarIncidente() {
		String nombreIncidente = textFieldNombreIncidente.getText().trim();

		if(nombreIncidente.isEmpty()){
			// TODO mostrar alerta. No se ingresó ningun nombre
		}
		else{
			nombreIncidente = nombreIncidente.replaceAll("\\s", "");
			String primerLetra = nombreIncidente.substring(0, 1);
			nombreIncidente = primerLetra.toLowerCase() + nombreIncidente.substring(1);
			if(existeIncidenteConNombre(nombreIncidente)){
				// TODO mostrar alerta. Ya existe un incidente con ese nombre
			}
			else{
				Incidente incidente = new Incidente(nombreIncidente);
				tableViewIncidente.getItems().add(incidente);
				tieneRiesgo_comboBox_tipoIncidente.getItems().add(incidente);
				critica_comboBox_tipoIncidente.getItems().add(incidente);
				limiteRiesgo_comboBox_tipoIncidente.getItems().add(incidente);
			}
		}
	}

	private boolean existeIncidenteConNombre(String nombreIncidente) {
		boolean existe = false;
		for(Incidente i: tableViewIncidente.getItems()){
			if(i.getNombre().equals(nombreIncidente)){
				existe = true;
			}
		}
		return existe;
	}

	@FXML
	private void quitarIncidente() {
		Incidente incidenteSeleccionado = tableViewIncidente.getSelectionModel().getSelectedItem();

		// lo quito de los combo box
		tieneRiesgo_comboBox_tipoIncidente.getItems().remove(incidenteSeleccionado);
		critica_comboBox_tipoIncidente.getItems().remove(incidenteSeleccionado);
		limiteRiesgo_comboBox_tipoIncidente.getItems().remove(incidenteSeleccionado);

		// quito los axiomas relacionados a ese incidente
		List<AxiomaTieneRiesgo> tieneRiesgoRelacionados = new ArrayList<>();
		for(AxiomaTieneRiesgo a: tableViewTieneRiesgo.getItems()){
			if(a.equals(incidenteSeleccionado)){
				tieneRiesgoRelacionados.add(a);
			}
		}
		tableViewTieneRiesgo.getItems().removeAll(tieneRiesgoRelacionados);

		List<AxiomaCritica> criticaRelacionados = new ArrayList<>();
		for(AxiomaCritica a: tableViewCritica.getItems()){
			if(a.equals(incidenteSeleccionado)){
				criticaRelacionados.add(a);
			}
		}
		tableViewCritica.getItems().removeAll(criticaRelacionados);

		List<AxiomaLimiteRiesgo> limiteRiesgoRelacionados = new ArrayList<>();
		for(AxiomaLimiteRiesgo a: tableViewLimiteRiesgo.getItems()){
			if(a.equals(incidenteSeleccionado)){
				limiteRiesgoRelacionados.add(a);
			}
		}
		tableViewLimiteRiesgo.getItems().removeAll(limiteRiesgoRelacionados);

		// lo quito de la tabla de incidentes donde se lo seleccionó
		tableViewIncidente.getItems().remove(incidenteSeleccionado);
	}

	@FXML
	private void agregarTieneRiesgo() {
		Incidente incidente = tieneRiesgo_comboBox_tipoIncidente.getValue();
		String palabra = tieneRiesgo_textField_palabra.getText().trim().toLowerCase();
		// TODO ver cuando es una frase....... palabra.replaceAll("\\s", "") y
		// todo eso
		int valorInt = tieneRiesgo_spinner_valor.getValue();

		if(incidente == null || palabra.isEmpty()){
			// TODO mostrar alerta
		}
		else{
			AxiomaTieneRiesgo axioma = new AxiomaTieneRiesgo(incidente, palabra, valorInt);
			tableViewTieneRiesgo.getItems().add(axioma);
		}
	}

	@FXML
	private void quitarTieneRiesgo() {
		AxiomaTieneRiesgo axiomaSeleccionado = tableViewTieneRiesgo.getSelectionModel().getSelectedItem();
		tableViewTieneRiesgo.getItems().remove(axiomaSeleccionado);
	}

	@FXML
	private void agregarCritica() {
		Incidente incidente = tieneRiesgo_comboBox_tipoIncidente.getValue();
		String palabra = tieneRiesgo_textField_palabra.getText().trim().toLowerCase();
		// TODO ver cuando es una frase....... palabra.replaceAll("\\s", "") y
		// todo eso

		if(incidente == null || palabra.isEmpty()){
			// TODO mostrar alerta
		}
		else{
			AxiomaCritica axioma = new AxiomaCritica(incidente, palabra);
			tableViewCritica.getItems().add(axioma);
		}
	}

	@FXML
	private void quitarCritica() {
		AxiomaCritica axiomaSeleccionado = tableViewCritica.getSelectionModel().getSelectedItem();
		tableViewCritica.getItems().remove(axiomaSeleccionado);
	}

	@FXML
	private void agregarLimiteRiesgo() {
		Incidente incidente = tieneRiesgo_comboBox_tipoIncidente.getValue();
		int valorInt = tieneRiesgo_spinner_valor.getValue();

		if(incidente == null){
			// TODO mostrar alerta
		}
		else{
			AxiomaLimiteRiesgo axioma = new AxiomaLimiteRiesgo(incidente, valorInt);
			tableViewLimiteRiesgo.getItems().add(axioma);
		}
	}

	@FXML
	private void quitarLimiteRiesgo() {
		AxiomaLimiteRiesgo axiomaSeleccionado = tableViewLimiteRiesgo.getSelectionModel().getSelectedItem();
		tableViewLimiteRiesgo.getItems().remove(axiomaSeleccionado);
	}

	@FXML
	private void guardarConfiguracion() {
		List<Incidente> listaIncidente = tableViewIncidente.getItems();
		List<AxiomaTieneRiesgo> listaTieneRiesgo = tableViewTieneRiesgo.getItems();
		List<AxiomaCritica> listaCritica = tableViewCritica.getItems();
		List<AxiomaLimiteRiesgo> listaLimiteRiesgo = tableViewLimiteRiesgo.getItems();
		procesador.guardar(listaIncidente, listaTieneRiesgo, listaCritica, listaLimiteRiesgo);
		salir();
	}

}
