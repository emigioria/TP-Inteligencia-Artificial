package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class ScrollPaneZoomer {

	public void createZoomPane(final Pane zoomPane, final ScrollPane scroller) {
		final double SCALE_DELTA = 1.1;

		final Group scrollContent = new Group(zoomPane);
		scroller.setContent(scrollContent);

		scrollContent.setOnScroll(event -> {
			event.consume();

			if(event.getDeltaY() == 0){
				return;
			}

			double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
					: 1 / SCALE_DELTA;

			// amount of scrolling in each direction in scrollContent coordinate
			// units
			Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);

			zoomPane.setScaleX(zoomPane.getScaleX() * scaleFactor);
			zoomPane.setScaleY(zoomPane.getScaleY() * scaleFactor);

			// move viewport so that old center remains in the center after the
			// scaling
			repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);
		});

		// Panning via drag....
		final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<>();
		scrollContent.setOnMousePressed(event -> lastMouseCoordinates.set(new Point2D(event.getX(), event.getY())));

		scrollContent.setOnMouseDragged(event -> {
			double deltaX = event.getX() - lastMouseCoordinates.get().getX();
			double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
			double deltaH = deltaX * (scroller.getHmax() - scroller.getHmin()) / extraWidth;
			double desiredH = scroller.getHvalue() - deltaH;
			scroller.setHvalue(Math.max(0, Math.min(scroller.getHmax(), desiredH)));

			double deltaY = event.getY() - lastMouseCoordinates.get().getY();
			double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
			double deltaV = deltaY * (scroller.getHmax() - scroller.getHmin()) / extraHeight;
			double desiredV = scroller.getVvalue() - deltaV;
			scroller.setVvalue(Math.max(0, Math.min(scroller.getVmax(), desiredV)));
		});
	}

	private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
		double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
		double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
		return new Point2D(scrollXOffset, scrollYOffset);
	}

	private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
		double scrollXOffset = scrollOffset.getX();
		double scrollYOffset = scrollOffset.getY();
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		if(extraWidth > 0){
			double halfWidth = scroller.getViewportBounds().getWidth() / 2;
			double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
			scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
		}
		else{
			scroller.setHvalue(scroller.getHmin());
		}
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		if(extraHeight > 0){
			double halfHeight = scroller.getViewportBounds().getHeight() / 2;
			double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
			scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
		}
		else{
			scroller.setVvalue(scroller.getVmin());
		}
	}

	public void bindScrollPanes(ScrollPane scrollPane1, ScrollPane scrollPane2) {
		scrollPane1.vvalueProperty().bindBidirectional(scrollPane2.vvalueProperty());
		scrollPane1.hvalueProperty().bindBidirectional(scrollPane2.hvalueProperty());
	}

	public void sincronizeZoom(Pane pane1, Pane pane2) {
		pane1.scaleXProperty().bindBidirectional(pane2.scaleXProperty());
		pane1.scaleYProperty().bindBidirectional(pane2.scaleYProperty());
	}

}
