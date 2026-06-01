package main.app;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowController {

    private static final int RESIZE_MARGIN = 13;

    private static double xOffset;
    private static double yOffset;
    private static boolean isResizing = false;

    public static void setResizable(Scene scene, Stage stage, double min_x, double min_y) {

        // -------LIMITS---------
        stage.setMinWidth(min_y);
        stage.setMinHeight(min_x);
        //------------------------
        
        scene.setOnMouseMoved(e -> {

            double x = e.getSceneX();
            double y = e.getSceneY();

            double w = scene.getWidth();
            double h = scene.getHeight();

            boolean left = x < RESIZE_MARGIN;
            boolean right = x > w - RESIZE_MARGIN;
            boolean top = y < RESIZE_MARGIN;
            boolean bottom = y > h - RESIZE_MARGIN;

            if (left && top) scene.setCursor(Cursor.NW_RESIZE);
            else if (left && bottom) scene.setCursor(Cursor.SW_RESIZE);
            else if (right && top) scene.setCursor(Cursor.NE_RESIZE);
            else if (right && bottom) scene.setCursor(Cursor.SE_RESIZE);
            else if (left) scene.setCursor(Cursor.W_RESIZE);
            else if (right) scene.setCursor(Cursor.E_RESIZE);
            else if (top) scene.setCursor(Cursor.N_RESIZE);
            else if (bottom) scene.setCursor(Cursor.S_RESIZE);
            else 
        	{
            	isResizing = false;
            	scene.setCursor(Cursor.DEFAULT);
        	}
        });

        scene.setOnMouseDragged(e -> {
            Cursor c = scene.getCursor();
            
            double mx = e.getScreenX();
            double my = e.getScreenY();

            // ---------------- RIGHT / BOTTOM ----------------
            if (c == Cursor.E_RESIZE || c == Cursor.SE_RESIZE || c == Cursor.NE_RESIZE) {
                double newW = mx - stage.getX();
                stage.setWidth(Math.max(newW, min_y));
                isResizing = true;
            }

            if (c == Cursor.S_RESIZE || c == Cursor.SE_RESIZE || c == Cursor.SW_RESIZE) {
                double newH = my - stage.getY();
                stage.setHeight(Math.max(newH, min_x));
                isResizing = true;
            }

            // ---------------- LEFT ----------------
            if (c == Cursor.W_RESIZE || c == Cursor.SW_RESIZE || c == Cursor.NW_RESIZE) {

                double newW = stage.getWidth() - (mx - stage.getX());
                newW = Math.max(newW, min_y);

                stage.setX(mx);
                stage.setWidth(newW);
                isResizing = true;
            }

            // ---------------- TOP ----------------
            if (c == Cursor.N_RESIZE || c == Cursor.NE_RESIZE || c == Cursor.NW_RESIZE) {

                double newH = stage.getHeight() - (my - stage.getY());
                newH = Math.max(newH, min_x);

                stage.setY(my);
                stage.setHeight(newH);
                isResizing = true;
            }
        });
    }

    public static void setDraggable(Node node, Stage stage)
    {
        node.setOnMousePressed(e -> {
        	if(!isResizing)
        	{
        		xOffset = e.getScreenX() - stage.getX();
        		yOffset = e.getScreenY() - stage.getY();        		
        	}
        });

        node.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
            	if(!isResizing)
            	{
            		stage.setX(e.getScreenX() - xOffset);
            		stage.setY(e.getScreenY() - yOffset);            		
            	}
            }
        });
        
//        node.setOnMousePressed(e -> System.out.println("PRESSED"));
//        node.setOnMouseDragged(e -> System.out.println("DRAGGED"));
//        node.setOnMouseReleased(e -> System.out.println("RELEASED"));
    }
}