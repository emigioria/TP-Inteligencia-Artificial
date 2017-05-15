package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class VerMapaController {

	public static List<Circle> circles = new ArrayList<>();

	public void hola(Stage primaryStage) {
		Group root = new Group();

		Circle circle1 = new Circle(50);
		circle1.setStroke(Color.GREEN);
		circle1.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.7));
		circle1.relocate(100, 100);

		Circle circle2 = new Circle(50);
		circle2.setStroke(Color.BLUE);
		circle2.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.7));
		circle2.relocate(200, 200);

		Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
		line.setStrokeWidth(20);

		Pane overlay = new Pane();
		overlay.getChildren().addAll(circle1, circle2, line);

		MouseGestures mg = new MouseGestures();
		mg.makeDraggable(circle1);
		mg.makeDraggable(circle2);
		mg.makeDraggable(line);

		root.getChildren().add(overlay);

		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
	}

	public static class MouseGestures {
		private double orgSceneX, orgSceneY;
		private double orgTranslateX, orgTranslateY;

		public void makeDraggable(Node node) {
			node.setOnMousePressed(t -> {
				orgSceneX = t.getSceneX();
				orgSceneY = t.getSceneY();

				Node p = ((Node) (t.getSource()));
				orgTranslateX = p.getTranslateX();
				orgTranslateY = p.getTranslateY();
			});
			node.setOnMouseDragged(t -> {
				double offsetX = t.getSceneX() - orgSceneX;
				double offsetY = t.getSceneY() - orgSceneY;
				double newTranslateX = orgTranslateX + offsetX;
				double newTranslateY = orgTranslateY + offsetY;

				Node p = ((Node) (t.getSource()));
				p.setTranslateX(newTranslateX);
				p.setTranslateY(newTranslateY);
			});
		}
	}
}
