/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import javafx.scene.layout.StackPane;

public class StackPaneWithTag<T> extends StackPane {

	private T tag;

	public T getTag() {
		return tag;
	}

	public void setTag(T tag) {
		this.tag = tag;
	}
}
