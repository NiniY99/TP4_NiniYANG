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
               dessin(button, gc);
            }
        });
		
		onShapeSelected(d.getList());
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
   					if(selectPar instanceof Rectangle) {
   						double x = ((Rectangle)selectPar).getX();
   						double y = ((Rectangle)selectPar).getY();
   						double w = ((Rectangle)selectPar).getWidth();
   						double h = ((Rectangle)selectPar).getHeight();
   						Rectangle r = new Rectangle(x, y, w, h);
   						Color color = (Color) selectPar.getFill();
   						r.getTransforms().add(new Translate(10, 10));
   						gc.fillRect(x, y, w, h);
   						gc.setFill(color);
   					}
   				}
   			});
   			
   		}
	}
	public void dessin(RadioButton button, GraphicsContext gc) {
		if (button == rectangle) {
			dessineRectangle(gc);
		}else if(button == rectangle) {
			dessineLine(gc);
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
				
				rect.setWidth(event.getX() - rect.getX());
				rect.setHeight(event.getY() - rect.getY());
				gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				gc.setFill(couleur.getValue());
				event.consume();
			};
		});
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				rect.setWidth(event.getX() - rect.getX());
				rect.setHeight(event.getY() - rect.getY());
				gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
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
		
	
	public void onShapeSelected(ArrayList<Shape> d) {
		for(Shape s : d) {
			Color initial = (Color)s.getStroke();
			s.setOnMouseClicked(new EventHandler<MouseEvent>() {
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

	
