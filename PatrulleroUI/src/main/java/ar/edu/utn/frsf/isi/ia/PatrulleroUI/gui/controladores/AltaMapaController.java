package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.MouseGesturesAdder;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.StackPaneWithTag;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaPersonalizada;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.AristaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.InterseccionGUI;
import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
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
import javafx.scene.layout.Pane;
import javafx.util.converter.IntegerStringConverter;

public class AltaMapaController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/AltaMapa.fxml";

	@FXML
	private Pane root;

	@FXML
	private Pane mapaPanel;

	@FXML
	private ScrollPane scrollMapaPanel;

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

	@FXML
	private GridPane panelDerecho;

	private List<InterseccionGUI> intersecciones = new ArrayList<>();

	private MouseGesturesAdder mga;

	private InterseccionGUI interseccionActual;

	private Set<Calle> calles = new HashSet<>();

	@FXML
	private void nuevoMapa() {

	}

	@FXML
	private void cargarMapa() {

	}

	@FXML
	private void guardarMapa() {

	}

	@FXML
	private void nuevaInterseccion() {
		InterseccionGUI nuevaInterseccion = new InterseccionGUI();
		mapaPanel.getChildren().add(nuevaInterseccion.getNode());
		mga.makeDraggable(nuevaInterseccion.getNode());
		intersecciones.add(nuevaInterseccion);
	}

	@FXML
	private void eliminarInterseccion() {
		if(interseccionActual == null){
			return;
		}
		(new ArrayList<>(interseccionActual.getEntrantes())).stream().forEach(a -> a.desactivarArista(mapaPanel));
		(new ArrayList<>(interseccionActual.getSalientes())).stream().forEach(a -> a.desactivarArista(mapaPanel));
		mapaPanel.getChildren().remove(interseccionActual.getNode());
		intersecciones.remove(interseccionActual);
		interseccionActual = null;
		panelDerecho.setVisible(false);
	}

	@FXML
	private void nuevaArista() {
		VentanaPersonalizada ventanaNuevaArista = presentadorVentanas.presentarVentanaPersonalizada(AltaAristaController.URL_VISTA, stage);
		AltaAristaController controlador = (AltaAristaController) ventanaNuevaArista.getControlador();
		controlador.inicializarCon(intersecciones, calles);
		ventanaNuevaArista.showAndWait();
		if(controlador.getResultado() != null){
			mapaPanel.getChildren().add(controlador.getResultado().getNode());
			calles.add(controlador.getResultado().getArista().getCalle());
		}
	}

	@FXML
	private void agregarArista() {
		VentanaPersonalizada ventanaNuevaArista = presentadorVentanas.presentarVentanaPersonalizada(AltaAristaController.URL_VISTA, stage);
		AltaAristaController controlador = (AltaAristaController) ventanaNuevaArista.getControlador();
		controlador.inicializarCon(intersecciones, calles);
		controlador.setOrigen(interseccionActual);
		ventanaNuevaArista.showAndWait();
		if(controlador.getResultado() != null){
			mapaPanel.getChildren().add(controlador.getResultado().getNode());
			calles.add(controlador.getResultado().getArista().getCalle());
		}
	}

	@FXML
	private void quitarArista() {
		AristaGUI aristaAQuitar = tablaAristas.getSelectionModel().getSelectedItem();
		if(aristaAQuitar == null){
			return;
		}
		aristaAQuitar.desactivarArista(mapaPanel);
	}

	@Override
	protected void inicializar() {
		mga = new MouseGesturesAdder(mapaPanel);
		mga.setOnMousePressed(t -> {
			StackPaneWithTag<?> a = (StackPaneWithTag<?>) t.getSource();
			Object interseccionTalVez = a.getTag();
			if(interseccionTalVez instanceof InterseccionGUI){
				InterseccionGUI interseccion = (InterseccionGUI) interseccionTalVez;
				actualizarPanelDerecho(interseccion);
			}
			t.consume();
		});
		scrollMapaPanel.setOnMousePressed(t -> {
			panelDerecho.setVisible(false);
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

		panelDerecho.setVisible(false);
	}

	private void actualizarPanelDerecho(InterseccionGUI interseccion) {
		interseccionActual = interseccion;
		panelDerecho.setVisible(true);
		InterseccionLabel.setText("Intersección " + interseccion.getInterseccion().getId());
		spPeso.getValueFactory().setValue(interseccion.getInterseccion().getPeso());
		tablaAristas.getItems().clear();
		tablaAristas.getItems().addAll(interseccion.getSalientes());
	}

}
