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

	static Runnable countUpHandler;
	static Runnable resetHandler;
	static Runnable closeHandler;
	static Label countLabel;
	static Label resetLabel;

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
		View.countLabel = new Label("0");
		View.countLabel.setFont(Font.font("Meiryo", 80));
		View.countLabel.setStyle("-fx-text-fill: White");
		View.countLabel.setAlignment(Pos.CENTER);
		View.countLabel.setFocusTraversable(false);
		View.countLabel.heightProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(
							final ObservableValue<? extends Number> value,
							final Number oldHeight, final Number newHeight) {
						if (oldHeight.doubleValue() > 0) {
							View.countLabel.setFont(Font.font("Meiryo",
									View.countLabel.getFont().getSize()
											* newHeight.doubleValue()
											/ oldHeight.doubleValue()));
						}
					}
				});
		final AnchorPane anchorPane = new AnchorPane(View.countLabel);
		AnchorPane.setTopAnchor(View.countLabel, 0d);
		AnchorPane.setLeftAnchor(View.countLabel, 0d);
		AnchorPane.setBottomAnchor(View.countLabel, 0d);
		AnchorPane.setRightAnchor(View.countLabel, 0d);
		rootPane.add(anchorPane, 0, 1);
		GridPane.setVgrow(anchorPane, Priority.ALWAYS);
		GridPane.setHgrow(anchorPane, Priority.ALWAYS);
		View.resetLabel = new Label("0");
		View.resetLabel.setFont(Font.font("Meiryo", 40));
		View.resetLabel.setStyle("-fx-text-fill: White");
		View.resetLabel.setFocusTraversable(false);
		final VBox h = new VBox(View.resetLabel);
		h.setAlignment(Pos.BASELINE_RIGHT);
		rootPane.add(h, 0, 2);
		final Scene scene = new Scene(rootPane);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings("incomplete-switch")
			@Override
			public void handle(final KeyEvent event) {
				switch (event.getCode()) {
				case ENTER:
					if (View.countUpHandler != null) {
						View.countUpHandler.run();
					}
					break;
				case ESCAPE:
					if (View.resetHandler != null) {
						View.resetHandler.run();
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
					if (View.countUpHandler != null) {
						View.countUpHandler.run();
					}
					break;
				case SECONDARY:
					if (View.resetHandler != null) {
						View.resetHandler.run();
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
				if (View.closeHandler != null) {
					View.closeHandler.run();
				}
			}
		});
	}
}
