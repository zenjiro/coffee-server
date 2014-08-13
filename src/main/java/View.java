import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class View extends Application {
	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("Coffee Server");
		final GridPane rootPane = new GridPane();
		rootPane.setStyle("-fx-background-color: Black");
		rootPane.setPadding(new Insets(20));
		rootPane.setEffect(new Glow(1));
		final Label usageLabel = new Label(
				"Enter・左クリック：カウントアップ\nEsc・右クリック：リセット");
		usageLabel.setFont(Font.font("Meiryo", 20));
		usageLabel.setStyle("-fx-text-fill: Gray");
		usageLabel.setTextAlignment(TextAlignment.CENTER);
		VBox b = new VBox(usageLabel);
		b.setAlignment(Pos.CENTER);
		rootPane.add(b, 0, 0);
		final Label countLabel = new Label("0");
		countLabel.setFont(Font.font("Meiryo", 80));
		countLabel.setStyle("-fx-text-fill: White");
		countLabel.setAlignment(Pos.CENTER);
		countLabel.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(final ObservableValue<? extends Number> value,
					final Number oldHeight, final Number newHeight) {
				if (oldHeight.doubleValue() > 0) {
					countLabel.setFont(Font.font(
							"Meiryo",
							countLabel.getFont().getSize()
									* newHeight.doubleValue()
									/ oldHeight.doubleValue()));
				}
			}
		});
		final AnchorPane anchorPane = new AnchorPane(countLabel);
		AnchorPane.setTopAnchor(countLabel, 0d);
		AnchorPane.setLeftAnchor(countLabel, 0d);
		AnchorPane.setBottomAnchor(countLabel, 0d);
		AnchorPane.setRightAnchor(countLabel, 0d);
		rootPane.add(anchorPane, 0, 1);
		GridPane.setVgrow(anchorPane, Priority.ALWAYS);
		GridPane.setHgrow(anchorPane, Priority.ALWAYS);
		final Label resetLabel = new Label("0");
		resetLabel.setFont(Font.font("Meiryo", 40));
		resetLabel.setStyle("-fx-text-fill: White");
		VBox h = new VBox(resetLabel);
		h.setAlignment(Pos.BASELINE_RIGHT);
		rootPane.add(h, 0, 2);
		final Scene scene = new Scene(rootPane);
		stage.setScene(scene);
		stage.show();
	}
}
