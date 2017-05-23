/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui;

import java.util.List;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.IconoAplicacion;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores.MenuAdministracionController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private PilaScene apilador;

	public static void main(String[] args) {
		//Ocultar logs
		java.util.Enumeration<String> loggers = java.util.logging.LogManager.getLogManager().getLoggerNames();
		while(loggers.hasMoreElements()){
			String log = loggers.nextElement();
			java.util.logging.Logger.getLogger(log).setLevel(java.util.logging.Level.WARNING);
		}
		//Iniciar aplicacion
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		//Analizar parámetros de entrada
		verParametros(getParameters().getRaw());

		//Inicializar parametros
		this.primaryStage = primaryStage;

		//Iniciar el stage en el centro de la pantalla
		primaryStage.centerOnScreen();

		//Setear icono y titulo de aplicacion
		primaryStage.getIcons().add(new IconoAplicacion());
		primaryStage.setTitle("Inteligencia Artificial - Simulación Patrullero");

		//Setear acción de cierre
		primaryStage.setOnCloseRequest((e) -> {
			if(!apilador.sePuedeSalir()){
				e.consume();
			}
			else{
				System.exit(0);
			}
		});

		iniciar();
	}

	private void iniciar() {
		apilador = ControladorPatrullero.crearYMostrarPrimeraVentana(primaryStage, MenuAdministracionController.URL_VISTA);
	}

	private void verParametros(List<String> raw) {
		//Analizar parámetros
	}
}
