package main;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowController {

    private static final int RESIZE_MARGIN = 25;

    private static double xOffset;
    private static double yOffset;

    public static void setResizable(Scene scene, Stage stage)
    {
        scene.setOnMouseMoved(e -> {

            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();

            double width = scene.getWidth();
            double height = scene.getHeight();

            boolean left = mouseX < RESIZE_MARGIN;
            boolean right = mouseX > width - RESIZE_MARGIN;
            boolean top = mouseY < RESIZE_MARGIN;
            boolean bottom = mouseY > height - RESIZE_MARGIN;

            if (left && top)
                scene.setCursor(Cursor.NW_RESIZE);
            else if (left && bottom)
                scene.setCursor(Cursor.SW_RESIZE);
            else if (right && top)
                scene.setCursor(Cursor.NE_RESIZE);
            else if (right && bottom)
                scene.setCursor(Cursor.SE_RESIZE);
            else if (left)
                scene.setCursor(Cursor.W_RESIZE);
            else if (right)
                scene.setCursor(Cursor.E_RESIZE);
            else if (top)
                scene.setCursor(Cursor.N_RESIZE);
            else if (bottom)
                scene.setCursor(Cursor.S_RESIZE);
            else
                scene.setCursor(Cursor.DEFAULT);
        });

        scene.setOnMouseDragged(e -> {

            Cursor cursor = scene.getCursor();

            double mouseX = e.getScreenX();
            double mouseY = e.getScreenY();

            if (cursor == Cursor.E_RESIZE || cursor == Cursor.SE_RESIZE || cursor == Cursor.NE_RESIZE)
            {
                stage.setWidth(mouseX - stage.getX());
            }
            if (cursor == Cursor.S_RESIZE || cursor == Cursor.SE_RESIZE || cursor == Cursor.SW_RESIZE)
            {
                stage.setHeight(mouseY - stage.getY());
            }
            if (cursor == Cursor.W_RESIZE || cursor == Cursor.SW_RESIZE || cursor == Cursor.NW_RESIZE)
            {
                double newWidth = stage.getWidth() - (mouseX - stage.getX());

                if (newWidth > stage.getMinWidth())
                {
                    stage.setX(mouseX);
                    stage.setWidth(newWidth);
                }
            }

            if (cursor == Cursor.N_RESIZE || cursor == Cursor.NE_RESIZE || cursor == Cursor.NW_RESIZE)
            {
                double newHeight = stage.getHeight() - (mouseY - stage.getY());

                if (newHeight > stage.getMinHeight())
                {
                    stage.setY(mouseY);
                    stage.setHeight(newHeight);
                }
            }
        });
    }

    public static void setDraggable(Scene scene, Stage stage)
    {
        scene.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        scene.setOnMouseDragged(e -> {

            if (scene.getCursor() == Cursor.DEFAULT)
            {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        });
    }
}