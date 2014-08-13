import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class View extends Application {
	public static void main(final String[] args) {
		Application.launch(args);
	}

	Runnable countUpHandler;
	Runnable resetHandler;
	Runnable closeHandler;

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
		usageLabel.setFocusTraversable(false);
		final VBox b = new VBox(usageLabel);
		b.setAlignment(Pos.CENTER);
		rootPane.add(b, 0, 0);
		final Label countLabel = new Label("0");
		countLabel.setFont(Font.font("Meiryo", 80));
		countLabel.setStyle("-fx-text-fill: White");
		countLabel.setAlignment(Pos.CENTER);
		countLabel.setFocusTraversable(false);
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
		resetLabel.setFocusTraversable(false);
		final VBox h = new VBox(resetLabel);
		h.setAlignment(Pos.BASELINE_RIGHT);
		rootPane.add(h, 0, 2);
		final Scene scene = new Scene(rootPane);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings("incomplete-switch")
			@Override
			public void handle(final KeyEvent event) {
				switch (event.getCode()) {
				case ENTER:
					if (View.this.countUpHandler != null) {
						View.this.countUpHandler.run();
					}
					break;
				case ESCAPE:
					if (View.this.resetHandler != null) {
						View.this.resetHandler.run();
					}
					break;
				}
			}
		});
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@SuppressWarnings("incomplete-switch")
			@Override
			public void handle(final MouseEvent event) {
				switch (event.getButton()) {
				case PRIMARY:
					if (View.this.countUpHandler != null) {
						View.this.countUpHandler.run();
					}
					break;
				case SECONDARY:
					if (View.this.resetHandler != null) {
						View.this.resetHandler.run();
					}
					break;
				}
			}
		});
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent event) {
				if (View.this.closeHandler != null) {
					View.this.closeHandler.run();
				}
			}
		});
	}
}
