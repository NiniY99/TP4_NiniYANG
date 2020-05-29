package application;

import java.util.ArrayList;

import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.*;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

public class Controller {

	@FXML 
	private Button delete;
	@FXML 
	private Button clone;
	@FXML 
	private RadioButton SM;
	@FXML 
	private RadioButton ecllipse;
	@FXML 
	private RadioButton rectangle;
	@FXML 
	private RadioButton line;
	@FXML 
	private ColorPicker couleur;
	@FXML
	private Canvas canvas;
	private RadioButton button;
	
	@FXML
	private ToggleGroup groupe1;
	private Rectangle rect;
	private Line ligne;
	private Dessin d;
	private boolean estSelectionne = false;
	private Shape selectPar;
	
	public Controller() {
		d = new Dessin();
	}
	@FXML
	public void initialize() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		groupe1.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldToggle, Toggle newToggle)
            {
               button = (RadioButton)newToggle.getToggleGroup().getSelectedToggle();
               dessin(button, gc, d);
            }
        });
		dessin(button,gc, d);
   		if (estSelectionne) {
   			couleur.setOnAction(new EventHandler<ActionEvent>() {
   				@Override
   				public void handle(ActionEvent event) {
   						selectPar.setFill(couleur.getValue());
   				}
   			});
   			delete.setOnAction(new EventHandler<ActionEvent>() {
   				@Override
   				public void handle(ActionEvent evet) {
   					
   				}
   			});
   			clone.setOnAction(new EventHandler<ActionEvent>() {
   				@Override
   				public void handle(ActionEvent e) {
   					Color color = (Color) selectPar.getFill();
   					gc.setFill(color);
   					if(selectPar instanceof Rectangle) {
   						double x = ((Rectangle)selectPar).getX();
   						double y = ((Rectangle)selectPar).getY();
   						double w = ((Rectangle)selectPar).getWidth();
   						double h = ((Rectangle)selectPar).getHeight();
   						Rectangle r = new Rectangle(x, y, w, h);
   						r.getTransforms().add(new Translate(10, 10));
   						gc.fillRect(x, y, w, h);
   					}else if(selectPar instanceof Line) {
   						double x1 = ((Line)selectPar).getStartX();
   						double y1 = ((Line)selectPar).getStartY();
   						double x2 = ((Line)selectPar).getEndX();
   						double y2 = ((Line)selectPar).getEndY();
   						Line l = new Line(x1, y1, x2, y2);
   						l.getTransforms().add(new Translate(10, 10));
   						gc.strokeLine(x1, y1, x2, y2);
   					}
   				}
   			});
   			
   		}
	}
	public void dessin(RadioButton button, GraphicsContext gc, Dessin d) {
		if (button == rectangle) {
			dessineRectangle(gc);
		}else if(button == line) {
			dessineLine(gc);
		}else if (button == SM) {
			onShapeSelected(d.getList());
		}else if (button == ecllipse) {
			dessineEllipse(gc);
		}
	}
	
	public void dessineRectangle(GraphicsContext gc) {
		rect = new Rectangle(0, 0, 0, 0);
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				rect.setX(event.getX());
				rect.setY(event.getY());
			};
		});
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				rect.setWidth(Math.abs(event.getX() - rect.getX()));
				rect.setHeight(Math.abs(event.getY() - rect.getY()));
				gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				gc.setFill(couleur.getValue());
				gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				event.consume();
			};
		});
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				rect.setWidth(event.getX() - rect.getX());
				rect.setHeight(event.getY() - rect.getY());
				gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				gc.setFill(couleur.getValue());
				event.consume();
			}
		});
		d.addDessin(rect);
	}

		public void dessineLine(GraphicsContext gc) {
			ligne = new Line(0, 0, 0, 0);
			canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					ligne.setStartX(event.getX());
					ligne.setStartY(event.getY());
				};
			});
			/*canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					
					ligne.setEndX(event.getX());
					ligne.setEndY(event.getY());
					gc.strokeLine(ligne.getStartX(), ligne.getStartY(), ligne.getEndX(), ligne.getEndY());
					gc.setFill(couleur.getValue());
				};
			});*/
			canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					ligne.setEndX(event.getX());
					ligne.setEndY(event.getY());
					gc.strokeLine(ligne.getStartX(), ligne.getStartY(), ligne.getEndX(), ligne.getEndY());
					gc.setFill(couleur.getValue());
				}
			});
			d.addDessin(ligne);
		}
		
		public void dessineEllipse(GraphicsContext gc) {
			Ellipse elip = new Ellipse(0, 0, 0, 0);
			canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					elip.setCenterX(event.getX());
					elip.setCenterY(event.getY());
				};
			});
			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					
					elip.setRadiusX(Math.abs(event.getX() - elip.getCenterX()));
					elip.setRadiusY(Math.abs(event.getY() - elip.getCenterY()));
					gc.fillOval(elip.getCenterX(), elip.getCenterY(), elip.getRadiusX(), elip.getRadiusY());
					gc.setFill(couleur.getValue());
					gc.strokeOval(elip.getCenterX(), elip.getCenterY(), elip.getRadiusX(), elip.getRadiusY());
					event.consume();
				};
			});
			canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent event) {
					elip.setRadiusX(Math.abs(event.getX() - elip.getCenterX()));
					elip.setRadiusY(Math.abs(event.getY() - elip.getCenterY()));
					gc.fillOval(elip.getCenterX(), elip.getCenterY(), elip.getRadiusX(), elip.getRadiusY());
					gc.setFill(couleur.getValue());
					gc.strokeOval(elip.getCenterX(), elip.getCenterY(), elip.getRadiusX(), elip.getRadiusY());
					event.consume();
				}
			});
			d.addDessin(elip);
		}
	
	public void onShapeSelected(ArrayList<Shape> d) {
		for(Shape s : d) {
			Color initial = (Color)s.getStroke();
			s.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					rect.setStroke(Color.AQUA);
					estSelectionne = true;
					selectPar = s;
				}
			});
			s.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent m) {
					s.setStroke(initial);
					estSelectionne = false;
				}
			});
			s.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					double deplaceX = event.getSceneX();
					double deplaceY = event.getSceneY();
					s.getTransforms().add(new Translate(deplaceX, deplaceY));           			
					}
			});
			
		}
	}
}

	
