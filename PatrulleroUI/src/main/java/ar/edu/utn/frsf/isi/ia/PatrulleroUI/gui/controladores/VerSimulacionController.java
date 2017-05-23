/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.output.TeeOutputStream;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.ManejadorArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.FiltroArchivos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.CasoDePruebaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.MapaGUI;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo.PatrulleroGUI;
import frsf.cidisi.exercise.patrullero.search.AmbienteCiudad;
import frsf.cidisi.exercise.patrullero.search.ChangeListenerPatrullero;
import frsf.cidisi.exercise.patrullero.search.Patrullero;
import frsf.cidisi.exercise.patrullero.search.PatrulleroMain;
import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.EstrategiasDeBusqueda;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

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

	private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

	private MapaGUI mapaAmbiente;

	private MapaGUI mapaPatrullero;

	private CasoDePrueba casoDePruebaPatrullero;

	private Patrullero agentePatrullero;

	private AmbienteCiudad ambienteCiudad;

	private Semaphore esperar = new Semaphore(0);

	private PatrulleroGUI imagenPatrulleroMapaAmbiente = new PatrulleroGUI();

	private PatrulleroGUI imagenPatrulleroMapaPatrullero = new PatrulleroGUI();

	@Override
	protected void inicializar() {
		cbEstrategia.getItems().addAll(EstrategiasDeBusqueda.values());
		cbEstrategia.getSelectionModel().select(0);

		scrollEstadoAmbiente.vvalueProperty().bindBidirectional(scrollEstadoPatrullero.vvalueProperty());
		scrollEstadoAmbiente.hvalueProperty().bindBidirectional(scrollEstadoPatrullero.hvalueProperty());
	}

	@FXML
	private void cargarDatos() {
		mapaAmbiente = null;
		mapaPatrullero = null;
		casoDePruebaPatrullero = null;
		scrollEstadoAmbiente.setContent(null);
		scrollEstadoPatrullero.setContent(null);
		cargarMapa();
		cargarCasoDePrueba();
	}

	private void cargarMapa() {
		//Cargar mapa
		File archivoMapa = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_MAPA.getFileChooser(), stage);
		if(archivoMapa == null){
			return;
		}
		try{
			this.mapaAmbiente = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
			this.mapaPatrullero = new MapaGUI(manejadorArchivos.cargarMapa(archivoMapa));
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}
		scrollEstadoAmbiente.setContent(mapaAmbiente.getNode());
		scrollEstadoPatrullero.setContent(mapaPatrullero.getNode());
	}

	private void cargarCasoDePrueba() {
		if(mapaAmbiente == null || mapaPatrullero == null){
			return;
		}

		//Cargar caso de prueba
		File archivoCasoDePrueba = presentadorVentanas.solicitarArchivoCarga(FiltroArchivos.ARCHIVO_CASO_PRUEBA.getFileChooser(), stage);
		if(archivoCasoDePrueba == null){
			return;
		}
		try{
			new CasoDePruebaGUI(manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba), mapaAmbiente.getMapa());
			casoDePruebaPatrullero = manejadorArchivos.cargarCasoDePrueba(archivoCasoDePrueba);

			cargarDatosSimulacion();
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		mapaAmbiente.actualizarObstaculos();
	}

	private void cargarDatosSimulacion() {
		mapaAmbiente.getNode().getChildren().add(imagenPatrulleroMapaAmbiente);
		mapaPatrullero.getNode().getChildren().add(imagenPatrulleroMapaPatrullero);

		mapaAmbiente.getMapa().getEsquinas().stream().forEach(e -> e.getSalientes().sort((x, y) -> x.getCalle().getNombre().compareTo(y.getCalle().getNombre())));
		mapaPatrullero.getMapa().getEsquinas().stream().forEach(e -> e.getSalientes().sort((x, y) -> x.getCalle().getNombre().compareTo(y.getCalle().getNombre())));

		agentePatrullero = new Patrullero(
				mapaPatrullero.getMapa(),
				(Interseccion) mapaPatrullero.getMapa().getLugar(casoDePruebaPatrullero.getPosicionInicialPatrullero()),
				(Interseccion) mapaPatrullero.getMapa().getLugar(casoDePruebaPatrullero.getPosicionIncidente()),
				casoDePruebaPatrullero.getTipoIncidente());
		agentePatrullero.setEstrategia(cbEstrategia.getValue());
		ChangeListenerPatrullero clp = new ChangeListenerPatrullero() {

			@Override
			public void cambio() {
				actualizarSimulacion();
			}

			@Override
			public void finSimulacionExitosa() {
				actualizarSimulacion();
				Platform.runLater(() -> {
					presentadorVentanas.presentarInformacion(
							"El agente ha llegado a su destino!",
							"El agente pudo llegar al incidente y resolverlo! :D",
							stage);
				});
			}

			@Override
			public void finSimulacionNoExitosa() {
				actualizarSimulacion();
				Platform.runLater(() -> {
					presentadorVentanas.presentarError(
							"El agente ha muerto",
							"El agente no pudo llegar a su destino! :(",
							stage);
				});
			}
		};
		agentePatrullero.setAvisarCambios(clp);

		ambienteCiudad = new AmbienteCiudad(
				mapaAmbiente.getMapa(),
				(Interseccion) mapaAmbiente.getMapa().getLugar(casoDePruebaPatrullero.getPosicionInicialPatrullero()));

		moverPatrullero();
	}

	@FXML
	private void comenzar() throws IOException {
		if(ambienteCiudad == null || agentePatrullero == null || casoDePruebaPatrullero == null){
			return;
		}
		File archivoSalida = new File("SalidaSimulacion.txt");
		if(archivoSalida.exists()){
			archivoSalida.delete();
		}
		archivoSalida.createNewFile();
		TeeOutputStream tee = new TeeOutputStream(new PrintStream(new FileOutputStream(archivoSalida)), System.out);
		PrintStream ps = new PrintStream(tee, true); //true - auto-flush after println
		System.setOut(ps);

		presentadorVentanas.presentarInformacion("Iniciando patrullero", agentePatrullero.getGoalString(), stage);
		new Thread(() -> {
			new PatrulleroMain(ambienteCiudad, agentePatrullero).start();

		}).start();
	}

	private void actualizarSimulacion() {
		Platform.runLater(() -> {
			lbSimulandoOListo.setText("Listo");
			lbHora.setText("Hora: " + ambienteCiudad.getEnvironmentState().getHora());
			mapaPatrullero.actualizarObstaculos();
			moverPatrullero();

			new Thread(() -> {
				try{
					Thread.sleep(2000);
				} catch(InterruptedException e){
					//Termina la espera
				}
				esperar.release();
			}).start();
		});

		try{
			esperar.acquire();
		} catch(InterruptedException e){
			//Termina la espera
		}

		Platform.runLater(() -> {
			lbSimulandoOListo.setText("Simulando");
		});
	}

	private void moverPatrullero() {
		imagenPatrulleroMapaAmbiente.setPosicion(mapaAmbiente.getInterseccion(ambienteCiudad.getEnvironmentState().getPosicionAgente()));
		imagenPatrulleroMapaPatrullero.setPosicion(mapaPatrullero.getInterseccion(agentePatrullero.getAgentState().getPosicion()));
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
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean sePuedeSalir() {
		return presentadorVentanas.presentarConfirmacion("Salir", "Se perderan los datos simulados ¿Seguro que desea salir?", stage).acepta();
	}
}
