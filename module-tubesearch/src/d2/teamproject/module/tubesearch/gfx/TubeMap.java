package d2.teamproject.module.tubesearch.gfx;

import d2.teamproject.PARTH;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

public class TubeMap {
    private final SubScene scene;
    private final Group root;
    private final PerspectiveCamera camera;
    private final Double initialCameraXPosition;

    public TubeMap(){
        initialCameraXPosition = -140.0;
        root = new Group();
        scene = new SubScene(root, PARTH.WIDTH, PARTH.HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        camera = new PerspectiveCamera();
        camera.setFieldOfView(40);
        camera.setTranslateX(initialCameraXPosition);
        camera.setTranslateY(scene.getHeight() / -2);
        camera.setRotationAxis(new Point3D(0, 1, 0));

        root.getChildren().add(camera);
        scene.setCamera(camera);
    }

    public SubScene getScene() {
        return scene;
    }

}
