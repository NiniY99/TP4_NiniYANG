package application;

import java.util.ArrayList;

import javafx.scene.shape.Shape;

public class Dessin {

	private ArrayList<Shape> list;
	public Dessin() {
		list = new ArrayList<Shape>();
	}
	
	public void addDessin(Shape shape) {
		list.add(shape);
	}
	
	public void deleteDessin(Shape shape) {
		list.remove(shape);
	}
	
	public ArrayList<Shape> getList(){
		return list;
	}
}

