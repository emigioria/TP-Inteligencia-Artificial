/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.gui.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.ManejadorArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ScrollPaneZoomer;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.PresentadorVentanas;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaError;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaInformacion;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaPersonalizada;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores.VerObstaculosLugarController;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Window;
import javafx.util.Duration;

public class VerSimulacionAutomaticaController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/VerSimulacionAutomatica.fxml";

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

	private BooleanProperty animar = new SimpleBooleanProperty(true);

	private ObjectProperty<Thread> ultimaSimulacion = new SimpleObjectProperty<>();

	private BooleanProperty finalizada = new SimpleBooleanProperty(true);

	private BooleanProperty pausada = new SimpleBooleanProperty(false);

	private Semaphore esperarPausa = new Semaphore(0, true);

	private Random random = new Random(System.currentTimeMillis());

	@Override
	protected void inicializar() {
		spz.bindScrollPanes(scrollEstadoAmbiente, scrollEstadoPatrullero);

		iniciar();
	}

	private void iniciar() {
		Platform.runLater(() -> {
			cargarDatos();
		});
	}

	@FXML
	private void cargarDatos() {
		cargarMapa();
	}

	private void cargarMapa() {
		try{
			URI ruta = new URI(this.getClass().getResource("/db/mapa.json").toString());
			File archivoMapa = new File(ruta);

			this.mapaAmbiente = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
			this.mapaAmbiente.mostrarTooltips();

			this.mapaPatrullero = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
			this.mapaPatrullero.mostrarTooltips();
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);

			lbSimulandoOListo.setText("Error de carga");
			return;
		}
		spz.createZoomPane(mapaAmbiente, scrollEstadoAmbiente);
		spz.createZoomPane(mapaPatrullero, scrollEstadoPatrullero);
		spz.sincronizeZoom(mapaAmbiente, mapaPatrullero);

		Platform.runLater(() -> {
			cargarCasoDePrueba();
		});
	}

	private void cargarCasoDePrueba() {
		try{
			URI ruta = new URI(this.getClass().getResource("/db/caso.json").toString());
			File archivoCasoDePrueba = new File(ruta);

			casoDePruebaPatrullero = manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba);
			casoDePruebaPatrullero.setPosicionInicialPatrullero(elementoRandom(mapaAmbiente.getMapa().getEsquinas()));
			casoDePruebaPatrullero.setPosicionIncidente(elementoRandom(mapaAmbiente.getMapa().getEsquinas()));
			new CasoDePruebaGUI(casoDePruebaPatrullero, mapaAmbiente.getMapa());

			cargarDatosSimulacion();
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);

			lbSimulandoOListo.setText("Error de carga");
			return;
		}

		mapaAmbiente.actualizarObstaculos();
		comenzar();
	}

	private Interseccion elementoRandom(List<Interseccion> esquinas) {
		return esquinas.get(random.nextInt(esquinas.size()));
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

						lbSimulandoOListo.setText("Finalizado con éxito");
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
							lbSimulandoOListo.setText("Finalizado sin éxito");
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
							lbSimulandoOListo.setText("Cancelado");
							presentadorVentanas.presentarError(
									"El agente ha cancelado",
									"El agente no pudo llegar a su destino por una orden! :(",
									stage);
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
	}

	private void mostrarObstaculos(Lugar lugar, Collection<Obstaculo> obstaculos) {
		VentanaPersonalizada ventana = presentadorVentanas.presentarVentanaPersonalizada(VerObstaculosLugarController.URL_VISTA, stage);
		((VerObstaculosLugarController) ventana.getControlador()).inicializarCon(lugar, obstaculos);
		ventana.showAndWait();
	}

	@FXML
	private void comenzar() {
		//Inicializar variables
		pausada.set(false);
		finalizada.set(false);
		agentePatrullero.setEstrategia(EstrategiasDeBusqueda.A_ASTERISCO);

		//Redirigir salida estandar a un archivo
		try{
			File archivoSalida = new File("SalidaSimulacionBusqueda.txt");
			if(archivoSalida.exists()){
				archivoSalida.delete();
			}
			archivoSalida.createNewFile();
			System.setOut(new PrintStream(new FileOutputStream(archivoSalida)));
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
			if(!pausada.get()){
				lbSimulandoOListo.setText("Listo");
			}
			else{
				lbSimulandoOListo.setText("Pausado");
			}
			lbUltimaAccion.setText("Última acción: " + agentePatrullero.getSelectedActionStr());
			moverPatrullero();
		});

		Boolean seguir = esperarSemaforo(esperarAnimacion);
		seguir = esperarSemaforo(esperarPausa) && seguir;

		//Debe seguir
		return seguir;
	}

	private Boolean esperarSemaforo(Semaphore semaforo) {
		try{
			semaforo.acquire();
		} catch(InterruptedException e){
			try{
				ultimaSimulacion.set(null);
				semaforo.acquire();
				Thread.currentThread().interrupt();

				//No debe seguir
				return false;
			} catch(InterruptedException e1){

			}
		}
		return true;
	}

	private void terminarSimulacion(Runnable accionFinal) {
		pausada.set(false);
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

		if(animar.get()){
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
			if(!pausada.get()){
				esperarPausa.release();
				lbSimulandoOListo.setText("Simulando");
			}
		});
	}

	private void mostrarResultadoMover() {
		mapaAmbiente.actualizarObstaculos(ambienteCiudad.getEnvironmentState().getHora());
		mapaPatrullero.actualizarObstaculos();
		lbHora.setText("Hora: " + ambienteCiudad.getEnvironmentState().getHora());
	}

	private void reanudar() {
		esperarPausa.drainPermits();
		esperarPausa.release();

		pausada.set(false);
		lbSimulandoOListo.setText("Simulando");
	}

	public void cancelar() {
		if(ultimaSimulacion.get() == null){
			return;
		}
		ultimaSimulacion.get().interrupt();
		if(pausada.get()){
			reanudar();
		}
	}

	@Override
	public Boolean sePuedeSalir() {
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
		esperarPausa.release();
		return true;
	}
}
