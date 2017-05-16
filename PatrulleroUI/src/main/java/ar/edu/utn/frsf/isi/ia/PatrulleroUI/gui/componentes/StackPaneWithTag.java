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
