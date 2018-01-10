package IDE.GUI.Components;

import IDE.Utils.Permissions;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.io.File;

class ImageTab extends CustomTab {
   ImageTab(File file) {
        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);
        setText(file.getName());
        getStyleClass().add("tabDesign");

        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        imageView.setOnZoom((ZoomEvent zoom) -> {
            double zoomAmt = zoom.getTotalZoomFactor();
            imageView.setScaleX(zoomAmt);
            imageView.setScaleY(zoomAmt);
        });

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(imageView);
        HBox.setHgrow(imageView, Priority.ALWAYS);
        setContent(hBox);
        setUserData(file);
   }

   @Override
   public boolean Enabled(Permissions p) {
       return (p == Permissions.COPY);
   }
}
