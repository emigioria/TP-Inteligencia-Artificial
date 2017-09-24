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

import org.apache.commons.io.output.TeeOutputStream;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.AmbienteCiudad;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.GuardianMain;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorJavaFXApilable;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.PilaScene;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.IconoAplicacion;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.PresentadorVentanas;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaError;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.VentanaInformacion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.Window;

public class VentanaDeComandosController extends ControladorJavaFXApilable {

	public static final String URL_VISTA = "vistas/ventanaDeComandos.fxml";

	@FXML
	private TextArea taEntrada;

	@FXML
	private Button botonDecir;

	private Thread ultimaSimulacion;

	@FXML
	private void tomarEntrada() {
		AmbienteCiudad ambiente = new AmbienteCiudad();
		Guardian agente;
		try{
			agente = new Guardian() {
				private Boolean mandado = false;

				@Override
				protected void mandarPatruIA() {
					super.mandarPatruIA();
					if(!mandado){
						mandado = true;
						Platform.runLater(() -> {
							//Inicializar parametros
							Stage stage = new Stage();

							//Iniciar el stage en el centro de la pantalla
							stage.centerOnScreen();

							//Setear icono y titulo de aplicacion
							stage.getIcons().add(new IconoAplicacion());
							stage.setTitle("Inteligencia Artificial - Sistema PatruIA");

							PilaScene apilador = ControladorJavaFXApilable.crearYMostrarPrimeraVentana(stage, VerSimulacionAutomaticaController.URL_VISTA);

							//Setear acción de cierre
							stage.setOnCloseRequest((e) -> {
								if(!apilador.sePuedeSalir()){
									e.consume();
								}
							});
						});
					}
				}
			};
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}
		GuardianMain main = new GuardianMain(ambiente, agente);

		ambiente.getEnvironmentState().setFrasesDichas(taEntrada.getText());

		//Redirigir salida estandar a un archivo
		try{
			File archivoSalida = new File("SalidaSimulacionGuardian.txt");
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
		ultimaSimulacion = new Thread(() -> {
			main.start();
			Platform.runLater(() -> {
				botonDecir.setDisable(false);
				presentadorVentanas.presentarInformacion("Procesamiento terminado.", "El agente ha terminado de procesar su entrada.", stage);
			});
		});
		ultimaSimulacion.start();
		botonDecir.setDisable(true);
		presentadorVentanas.presentarInformacion("Iniciando procesamiento.", "El agente ha empezado a procesar su entrada.", stage);
	}

	@Override
	protected void inicializar() {

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
		return true;
	}

	private void cancelar() {
		if(ultimaSimulacion == null){
			return;
		}
		ultimaSimulacion.interrupt();
	}
}
