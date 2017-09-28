/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;

import ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.ConfiguracionValoresController;
import ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem.GuardianServerController;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.IconoAplicacion;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.PresentadorVentanas;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private List<String> args;

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
	public void start(Stage primaryStage) throws IOException {
		//Analizar parámetros de entrada
		args = getParameters().getRaw();
		verParametros(args);

		//Inicializar parametros
		this.primaryStage = primaryStage;

		//Iniciar el stage en el centro de la pantalla
		primaryStage.centerOnScreen();

		//Setear icono y titulo de aplicacion
		primaryStage.getIcons().add(new IconoAplicacion());
		primaryStage.setTitle("Inteligencia Artificial - Servidor GuardIAn");

		//Setear acción de cierre
		//		primaryStage.setOnCloseRequest((e) -> {
		//		});

		mostrarPantallaConfiguracionInicial();
		new Thread(() -> iniciarServidor()).start();

		Platform.setImplicitExit(false);
	}

	private void iniciarServidor() {
		SpringApplication.run(GuardianServerController.class, args.toArray(new String[0]));
	}

	private void mostrarPantallaConfiguracionInicial() {
		new PresentadorVentanas().presentarVentanaPersonalizada(ConfiguracionValoresController.URL_VISTA, primaryStage).showAndWait();
	}

	private void verParametros(List<String> raw) {
		//Analizar parámetros
	}
}
