/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.output.TeeOutputStream;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.ManejadorArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.FiltroArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ScrollPaneZoomer;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.PresentadorVentanas;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaError;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaInformacion;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaPersonalizada;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.CasoDePruebaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.IncidenteGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.PatrulleroGUI;
import frsf.cidisi.exercise.patrullero.search.AmbienteCiudad;
import frsf.cidisi.exercise.patrullero.search.ChangeListenerPatrullero;
import frsf.cidisi.exercise.patrullero.search.Patrullero;
import frsf.cidisi.exercise.patrullero.search.PatrulleroMain;
import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.EstrategiasDeBusqueda;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Window;
import javafx.util.Duration;

public class VerSimulacionController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/VerSimulacion.fxml";

	@FXML
	private ScrollPane scrollEstadoAmbiente;

	@FXML
	private ScrollPane scrollEstadoPatrullero;

	@FXML
	private Label lbUltimaAccion;

	@FXML
	private Label lbHora;

	@FXML
	private Label lbSimulandoOListo;

	@FXML
	private ComboBox<EstrategiasDeBusqueda> cbEstrategia;

	@FXML
	private Button botonCargarMCP;

	@FXML
	private Button botonComenzar;

	@FXML
	private Button botonCancelar;

	private ScrollPaneZoomer spz = new ScrollPaneZoomer();

	private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

	private MapaGUI mapaAmbiente;

	private MapaGUI mapaPatrullero;

	private CasoDePrueba casoDePruebaPatrullero;

	private Patrullero agentePatrullero;

	private AmbienteCiudad ambienteCiudad;

	private Semaphore esperarAnimacion = new Semaphore(0, true);

	private PatrulleroGUI imagenPatrulleroMapaAmbiente;

	private PatrulleroGUI imagenPatrulleroMapaPatrullero;

	private IncidenteGUI imagenIncidenteMapaPatrullero;

	private Boolean animar = true;

	private ObjectProperty<Thread> ultimaSimulacion = new SimpleObjectProperty<>();

	private BooleanProperty cargaFinalizada = new SimpleBooleanProperty(false);

	private BooleanProperty finalizada = new SimpleBooleanProperty(true);

	@Override
	protected void inicializar() {
		cbEstrategia.getItems().addAll(EstrategiasDeBusqueda.values());
		cbEstrategia.getSelectionModel().select(0);

		spz.bindScrollPanes(scrollEstadoAmbiente, scrollEstadoPatrullero);

		botonCargarMCP.disableProperty().bind(finalizada.not());

		botonComenzar.disableProperty().bind(ultimaSimulacion.isNotNull().or(cargaFinalizada.not()));
		botonCancelar.disableProperty().bind(botonComenzar.disableProperty().not());

		botonCancelar.visibleProperty().bind(ultimaSimulacion.isNotNull().and(finalizada.not()));
		botonComenzar.visibleProperty().bind(botonCancelar.visibleProperty().not());

		botonCancelar.managedProperty().bind(botonCancelar.visibleProperty());
		botonComenzar.managedProperty().bind(botonCancelar.managedProperty().not());
	}

	@FXML
	private void cargarDatos() {
		if(!finalizada.get()){
			return;
		}
		mapaAmbiente = null;
		mapaPatrullero = null;
		casoDePruebaPatrullero = null;
		scrollEstadoAmbiente.setContent(null);
		scrollEstadoPatrullero.setContent(null);
		ultimaSimulacion.set(null);
		cargaFinalizada.set(false);

		cargarMapa();
	}

	private void cargarMapa() {
		//Cargar mapa
		File archivoMapa = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			this.mapaAmbiente = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
			this.mapaAmbiente.mostrarTooltips();

			this.mapaPatrullero = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
			this.mapaPatrullero.mostrarTooltips();
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}
		spz.createZoomPane(mapaAmbiente, scrollEstadoAmbiente);
		spz.createZoomPane(mapaPatrullero, scrollEstadoPatrullero);
		spz.sincronizeZoom(mapaAmbiente, mapaPatrullero);

		cargarCasoDePrueba();
	}

	private void cargarCasoDePrueba() {
		//Cargar caso de prueba
		File archivoCasoDePrueba = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_CASO_PRUEBA.getFileChooser(), stage);
		if(archivoCasoDePrueba == null){
			return;
		}
		try{
			casoDePruebaPatrullero = manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba);
			new CasoDePruebaGUI(casoDePruebaPatrullero, mapaAmbiente.getMapa());

			cargarDatosSimulacion();
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		mapaAmbiente.actualizarObstaculos();
	}

	private void cargarDatosSimulacion() {
		imagenPatrulleroMapaAmbiente = new PatrulleroGUI();
		mapaAmbiente.getChildren().add(imagenPatrulleroMapaAmbiente);

		imagenPatrulleroMapaPatrullero = new PatrulleroGUI();
		imagenIncidenteMapaPatrullero = new IncidenteGUI(casoDePruebaPatrullero.getTipoIncidente());
		mapaPatrullero.getChildren().add(imagenIncidenteMapaPatrullero);
		mapaPatrullero.getChildren().add(imagenPatrulleroMapaPatrullero);

		mapaAmbiente.getMapa().getEsquinas().stream().forEach(e -> e.getSalientes().sort((x, y) -> x.getCalle().getNombre().compareTo(y.getCalle().getNombre())));
		mapaPatrullero.getMapa().getEsquinas().stream().forEach(e -> e.getSalientes().sort((x, y) -> x.getCalle().getNombre().compareTo(y.getCalle().getNombre())));

		agentePatrullero = new Patrullero(
				mapaPatrullero.getMapa(),
				(Interseccion) mapaPatrullero.getMapa().getLugar(casoDePruebaPatrullero.getPosicionInicialPatrullero()),
				(Interseccion) mapaPatrullero.getMapa().getLugar(casoDePruebaPatrullero.getPosicionIncidente()),
				casoDePruebaPatrullero.getTipoIncidente());
		ChangeListenerPatrullero clp = new ChangeListenerPatrullero() {

			@Override
			public Boolean cambio() {
				return actualizarSimulacion();
			}

			@Override
			public void finSimulacionExitosa() {
				actualizarSimulacion();
				terminarSimulacion(() -> {
					Platform.runLater(() -> {
						mapaPatrullero.getChildren().remove(imagenIncidenteMapaPatrullero);
						presentadorVentanas.presentarInformacion(
								"El agente ha llegado a su destino!",
								"El agente pudo llegar al incidente y resolverlo! :D",
								stage);
					});
				});
			}

			@Override
			public void finSimulacionNoExitosa() {
				if(!Thread.currentThread().isInterrupted()){
					actualizarSimulacion();
					terminarSimulacion(() -> {
						Platform.runLater(() -> {
							presentadorVentanas.presentarError(
									"El agente ha muerto",
									"El agente no pudo llegar a su destino! :(",
									stage);
						});
					});
				}
				else{
					terminarSimulacion(() -> {
						Platform.runLater(() -> {
							presentadorVentanas.presentarError(
									"El agente ha cancelado",
									"El agente no pudo llegar a su destino por una orden! :(",
									stage);
							lbSimulandoOListo.setText("Cancelada");
						});
					});
				}
			}

		};
		agentePatrullero.setAvisarCambios(clp);

		ambienteCiudad = new AmbienteCiudad(
				mapaAmbiente.getMapa(),
				(Interseccion) mapaAmbiente.getMapa().getLugar(casoDePruebaPatrullero.getPosicionInicialPatrullero()));

		imagenPatrulleroMapaAmbiente.inicializar(
				mapaAmbiente.getInterseccion(ambienteCiudad.getEnvironmentState().getPosicionAgente()),
				ambienteCiudad.getEnvironmentState().getPosicionAgente().getSalientes().get(ambienteCiudad.getEnvironmentState().getOrientacionAgente().nextIndex()));
		imagenPatrulleroMapaPatrullero.inicializar(
				mapaPatrullero.getInterseccion(agentePatrullero.getAgentState().getPosicion()),
				agentePatrullero.getAgentState().getPosicion().getSalientes().get(agentePatrullero.getAgentState().getOrientacion().nextIndex()));

		imagenIncidenteMapaPatrullero.inicializar(mapaPatrullero.getInterseccion(agentePatrullero.getAgentState().getIncidente()));

		mapaAmbiente.getIntersecciones().stream().forEach(i -> {
			i.setOnMouseClicked(e -> {
				if(ambienteCiudad.getEnvironmentState().getHora() > 0){
					if(i.getObstaculosActivos() != null && !i.getObstaculosActivos().isEmpty()){
						mostrarObstaculos(i.getInterseccion(), i.getObstaculosActivos());
					}
				}
				else{
					if(!i.getInterseccion().getObstaculos().isEmpty()){
						mostrarObstaculos(i.getInterseccion(), i.getInterseccion().getObstaculos());
					}
				}
			});
			i.getSalientes().stream().forEach(a -> {
				a.setOnMouseClicked(e -> {
					if(ambienteCiudad.getEnvironmentState().getHora() > 0){
						if(a.getObstaculosActivos() != null && !a.getObstaculosActivos().isEmpty()){
							mostrarObstaculos(a.getArista(), a.getObstaculosActivos());
						}
					}
					else{
						if(!a.getArista().getObstaculos().isEmpty()){
							mostrarObstaculos(a.getArista(), a.getArista().getObstaculos());
						}
					}
				});
			});
		});

		cargaFinalizada.set(true);
	}

	private void mostrarObstaculos(Lugar lugar, Collection<Obstaculo> obstaculos) {
		VentanaPersonalizada ventana = presentadorVentanas.presentarVentanaPersonalizada(VerObstaculosLugarController.URL_VISTA, stage);
		((VerObstaculosLugarController) ventana.getControlador()).inicializarCon(lugar, obstaculos);
		ventana.showAndWait();
	}

	@FXML
	private void comenzar() {
		if(ultimaSimulacion.get() != null || ambienteCiudad == null || agentePatrullero == null || casoDePruebaPatrullero == null){
			return;
		}

		//Inicializar variables
		finalizada.set(false);
		animar = true;
		agentePatrullero.setEstrategia(cbEstrategia.getValue());

		//Redirigir salida estandar a un archivo
		try{
			File archivoSalida = new File("SalidaSimulacion.txt");
			if(archivoSalida.exists()){
				archivoSalida.delete();
			}
			archivoSalida.createNewFile();
			TeeOutputStream tee = new TeeOutputStream(new PrintStream(new FileOutputStream(archivoSalida)), System.out);
			PrintStream ps = new PrintStream(tee, true); //true - auto-flush after println
			System.setOut(ps);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		//Avisar inicio e iniciar
		presentadorVentanas.presentarInformacion("Iniciando patrullero", agentePatrullero.getGoalString(), stage);
		ultimaSimulacion.set(new Thread(() -> {
			new PatrulleroMain(ambienteCiudad, agentePatrullero).start();
		}));
		ultimaSimulacion.get().start();
	}

	private Boolean actualizarSimulacion() {
		Platform.runLater(() -> {
			lbSimulandoOListo.setText("Listo");
			lbUltimaAccion.setText("Última acción: " + agentePatrullero.getSelectedActionStr());
			moverPatrullero();
		});

		try{
			esperarAnimacion.acquire();
		} catch(InterruptedException e){
			try{
				ultimaSimulacion.set(null);
				esperarAnimacion.acquire();
				Thread.currentThread().interrupt();

				//No debe seguir
				return false;
			} catch(InterruptedException e1){

			}
		}

		//Debe seguir
		return true;
	}

	private void terminarSimulacion(Runnable accionFinal) {
		finalizada.set(true);
		accionFinal.run();
	}

	private void moverPatrullero() {
		Duration duracionDesplazamiento = new Duration(1800);
		Duration duracionGiro = new Duration(500);
		Duration duracionPausaFinal = new Duration(200);

		//Animar Desplazamiento
		List<Transition> desplazamientoAmbiente = imagenPatrulleroMapaAmbiente.setPosicionTransicionada(
				mapaAmbiente.getInterseccion(ambienteCiudad.getEnvironmentState().getPosicionAgente()),
				duracionDesplazamiento);
		//Animar giro
		Arista aristaApuntadaAmbiente = ambienteCiudad.getEnvironmentState().getPosicionAgente().getSalientes().get(ambienteCiudad.getEnvironmentState().getOrientacionAgente().nextIndex());
		List<Transition> giroAmbiente = imagenPatrulleroMapaAmbiente.girarImagen(
				aristaApuntadaAmbiente,
				duracionGiro);

		//Animar desplazamiento
		List<Transition> desplazamientoPatrullero = imagenPatrulleroMapaPatrullero.setPosicionTransicionada(
				mapaPatrullero.getInterseccion(agentePatrullero.getAgentState().getPosicion()),
				duracionDesplazamiento);
		//Animar giro
		Arista aristaApuntadaPatrullero = agentePatrullero.getAgentState().getPosicion().getSalientes().get(agentePatrullero.getAgentState().getOrientacion().nextIndex());
		List<Transition> giroPatrullero = imagenPatrulleroMapaPatrullero.girarImagen(
				aristaApuntadaPatrullero,
				duracionGiro);

		//Crear animaciones
		SequentialTransition animacionAmbiente = new SequentialTransition();
		animacionAmbiente.getChildren().addAll(desplazamientoAmbiente);
		animacionAmbiente.getChildren().addAll(giroAmbiente);
		animacionAmbiente.getChildren().add(new PauseTransition(duracionPausaFinal));

		SequentialTransition animacionPatrullero = new SequentialTransition();
		animacionPatrullero.getChildren().addAll(desplazamientoPatrullero);
		animacionPatrullero.getChildren().addAll(giroPatrullero);
		animacionPatrullero.getChildren().add(new PauseTransition(duracionPausaFinal));

		if(animar){
			//Iniciar animaciones
			animacionAmbiente.play();
			animacionPatrullero.play();
		}
		else{
			//Saltear animaciones
			animacionAmbiente.playFrom(animacionAmbiente.getTotalDuration());
			animacionPatrullero.playFrom(animacionPatrullero.getTotalDuration());
		}

		//Al finalizar, seguir
		animacionAmbiente.setOnFinished(e -> {
			mostrarResultadoMover();
			esperarAnimacion.release();
		});
	}

	private void mostrarResultadoMover() {
		mapaAmbiente.actualizarObstaculos(ambienteCiudad.getEnvironmentState().getHora());
		mapaPatrullero.actualizarObstaculos();
		lbHora.setText("Hora: " + ambienteCiudad.getEnvironmentState().getHora());
		lbSimulandoOListo.setText("Simulando");
	}

	@FXML
	private void pausa() {
		// TODO Auto-generated method stub

	}

	@FXML
	private void avanzar() {
		// TODO Auto-generated method stub

	}

	@FXML
	private void irAlFinal() {
		animar = false;
	}

	@FXML
	public void cancelar() {
		if(ultimaSimulacion.get() == null){
			return;
		}
		ultimaSimulacion.get().interrupt();
	}

	@Override
	public Boolean sePuedeSalir() {
		if(presentadorVentanas.presentarConfirmacion("Salir", "Se perderan los datos simulados. No cambie de pantalla si la simulación está corriendo ¿Seguro que desea salir?", stage).acepta()){
			presentadorVentanas = new PresentadorVentanas() {
				@Override
				public VentanaError presentarError(String titulo, String mensaje, Window padre) {
					return null;
				}

				@Override
				public VentanaInformacion presentarInformacion(String titulo, String mensaje, Window padre) {
					return null;
				}
			};
			cancelar();
			esperarAnimacion.release();
			return true;
		}
		return false;
	}
}
