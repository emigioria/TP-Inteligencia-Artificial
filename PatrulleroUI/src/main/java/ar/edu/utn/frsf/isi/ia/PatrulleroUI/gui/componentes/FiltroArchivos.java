/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public enum FiltroArchivos {
	ARCHIVO,
	ARCHIVO_JSON {
		@Override
		public FileChooser getFileChooser() {
			String tipo = "(*.json)";

			ArrayList<String> tiposFiltro = new ArrayList<>();
			tiposFiltro.add("*.json");

			ExtensionFilter filtro = new ExtensionFilter("Archivo JSON " + tipo, tiposFiltro);

			FileChooser archivoSeleccionado = new FileChooser();
			archivoSeleccionado.getExtensionFilters().add(filtro);

			return archivoSeleccionado;
		}
	},
	ARCHIVO_MAPA {
		@Override
		public FileChooser getFileChooser() {
			String tipo = "(*.json)";

			ArrayList<String> tiposFiltro = new ArrayList<>();
			tiposFiltro.add("*.json");

			ExtensionFilter filtro = new ExtensionFilter("Archivo de mapa " + tipo, tiposFiltro);

			FileChooser archivoSeleccionado = new FileChooser();
			archivoSeleccionado.getExtensionFilters().add(filtro);

			return archivoSeleccionado;
		}
	},
	ARCHIVO_CASO_PRUEBA {
		@Override
		public FileChooser getFileChooser() {
			String tipo = "(*.json)";

			ArrayList<String> tiposFiltro = new ArrayList<>();
			tiposFiltro.add("*.json");

			ExtensionFilter filtro = new ExtensionFilter("Archivo de caso de prueba " + tipo, tiposFiltro);

			FileChooser archivoSeleccionado = new FileChooser();
			archivoSeleccionado.getExtensionFilters().add(filtro);

			return archivoSeleccionado;
		}
	},
	ARCHIVO_IMAGEN {
		@Override
		public FileChooser getFileChooser() {
			String tipos = "(";
			ArrayList<String> tiposFiltro = new ArrayList<>();
			for(String formato: ImageIO.getReaderFormatNames()){
				tipos += "*." + formato + ";";
				tiposFiltro.add("*." + formato);
			}
			tipos = tipos.substring(0, tipos.length() - 1);
			tipos += ")";

			ExtensionFilter filtro = new ExtensionFilter("Archivo de imágen " + tipos, tiposFiltro);

			FileChooser archivoSeleccionado = new FileChooser();
			archivoSeleccionado.getExtensionFilters().add(filtro);
			return archivoSeleccionado;
		}
	};

	public FileChooser getFileChooser() {
		ExtensionFilter filtro = new ExtensionFilter("Archivo ", new ArrayList<>());
		FileChooser archivoSeleccionado = new FileChooser();
		archivoSeleccionado.getExtensionFilters().add(filtro);
		return archivoSeleccionado;
	};
}
