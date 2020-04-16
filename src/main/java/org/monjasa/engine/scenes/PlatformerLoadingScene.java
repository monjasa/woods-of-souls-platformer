package org.monjasa.engine.scenes;

import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.dsl.FXGL;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class PlatformerLoadingScene extends LoadingScene {

    private ProgressBar progressBar;

    public PlatformerLoadingScene() {

        ImageView imageView = FXGL.texture("app/icon.png");
        imageView.setTranslateX(getAppWidth() / 2.0 - imageView.getImage().getWidth() / 2.0);
        imageView.setTranslateY(getAppHeight() / 2.0 - imageView.getImage().getHeight() / 2.0);

        progressBar = new ProgressBar();
        progressBar.setPrefSize(getAppWidth() - 200.0, 10.0);
        progressBar.setTranslateX(100.0);
        progressBar.setTranslateY(getAppHeight() - 100.0);

        setBackgroundColor(Color.BLACK);
        getContentRoot().getChildren().addAll(imageView, progressBar);
    }

    @Override
    public void onCreate() {
        setCursorInvisible();
    }

    @Override
    protected void bind(@NotNull Task<?> task) {
        progressBar.progressProperty().bind(task.progressProperty());
    }
}
