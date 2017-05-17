/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PresentadorVentanas {

	public VentanaConfirmacion presentarConfirmacion(String titulo, String mensaje, Window padre) {
		return new VentanaConfirmacion(titulo, mensaje, padre);
	}

	public VentanaError presentarError(String titulo, String mensaje, Window padre) {
		return new VentanaError(titulo, mensaje, padre);
	}

	public VentanaInformacion presentarInformacion(String titulo, String mensaje, Window padre) {
		return new VentanaInformacion(titulo, mensaje, padre);
	}

	public VentanaErrorExcepcion presentarExcepcion(Exception e, Window padre) {
		e.printStackTrace();
		return new VentanaErrorExcepcion(e.getMessage(), padre);
	}

	public VentanaErrorExcepcionInesperada presentarExcepcionInesperada(Exception e, Window padre) {
		System.err.println("Excepción inesperada!!");
		e.printStackTrace();
		return new VentanaErrorExcepcionInesperada(padre);
	}

	public VentanaPersonalizada presentarVentanaPersonalizada(String URLVista, Stage padre) {
		return new VentanaPersonalizada(URLVista, padre);
	}

	public void presentarToast(String mensaje, Window padre, int ajusteHeight) {
		int toastMsgTime = 2500; //2.5 seconds
		int fadeInTime = 700; //0.7 seconds
		int fadeOutTime = 500; //0.5 seconds
		new Toast(padre, mensaje, toastMsgTime, fadeInTime, fadeOutTime, ajusteHeight);
	}

	public void presentarToast(String mensaje, Window padre) {
		presentarToast(mensaje, padre, 20);
	}

	public File solicitarArchivoCarga(FileChooser archivoSeleccionado, Stage stage) {
		File retorno = archivoSeleccionado.showOpenDialog(stage);
		if(retorno != null){
			retorno = new File(retorno.toString());
		}
		return retorno;
	}

	public File solicitarArchivoGuardado(FileChooser archivoSeleccionado, Stage stage) {
		File retorno = archivoSeleccionado.showSaveDialog(stage);
		if(retorno != null){
			retorno = new File(retorno.toString());
		}
		return retorno;
	}
}
