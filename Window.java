import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class  Window extends Application {

    private Label[][] cells;

    private static Window instance;

    private Label moveLabel;
    private Label controlLabel;

    private boolean running = true;
    public static boolean restart = false;

    private int sleepTime = 100;

    @Override
    public void start(Stage stage) {

        instance = this;

        int size = 70;

        cells = new Label[size][size];

        GridPane root = new GridPane();

        // Строки
        for (int i = 0; i < size; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(1.359);
            root.getRowConstraints().add(row);
        }

        // Столбцы
        for (int i = 0; i < size; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(1.4);
            root.getColumnConstraints().add(column);
        }

        // Клетки
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                Label cell = new Label("_");

                cell.setMinSize(0, 0);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.setAlignment(Pos.CENTER);
                cell.setPadding(javafx.geometry.Insets.EMPTY);

                cells[x][y] = cell;

                root.add(cell, x, y);
            }
        }

        root.setGridLinesVisible(true);

        // Счётчик ходов
        moveLabel = new Label("Ходы: 0");

        // Кнопки
        Button startStopButton = new Button("Стоп");
        Button restartButton = new Button("Рестарт");
        Button fasterButton = new Button("Быстрее");
        Button slowerButton = new Button("Медленнее");

        // Старт / Стоп
        startStopButton.setOnAction(event -> {

            running = !running;

            if (running) {
                startStopButton.setText("Стоп");
            } else {
                startStopButton.setText("Старт");
            }
        });

        // Рестарт
        restartButton.setOnAction(event -> {

            Window.restart = true;

            new Thread(() -> {

                // Ждём, пока старая симуляция завершится
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Window.restart = false;
                Platform.runLater(() -> {
                    controlLabel.setText("");
                });
                new Main().main();

            }).start();

        });

        // Быстрее
        fasterButton.setOnAction(event -> {

            sleepTime -= 20;

            if (sleepTime < 0) {
                sleepTime = 0;
            }
        });

        // Медленнее
        slowerButton.setOnAction(event -> {

            sleepTime += 20;
        });

        HBox controls = new HBox(
                5,
                startStopButton,
                restartButton,
                fasterButton,
                slowerButton,
                moveLabel
        );

        controls.setAlignment(Pos.CENTER);
        controls.setPrefHeight(50);

        controlLabel = new Label("");
        controlLabel.setAlignment(Pos.CENTER);


        VBox bottom = new VBox(
                5,
                controlLabel,
                controls
        );
        bottom.setAlignment(Pos.CENTER);

        // Основной контейнер
        BorderPane mainRoot = new BorderPane();

        mainRoot.setCenter(root);
        mainRoot.setBottom(bottom);

        Scene scene = new Scene(mainRoot, 1900, 1000);

        stage.setTitle("Лаба 1");
        stage.setScene(scene);
        stage.show();

        // Запускаем симуляцию
        new Thread(() -> {
            new Main().main();
        }).start();
    }

    public void updateField(String[][] symbols) {

        Platform.runLater(() -> {

            for (int y = 0; y < symbols.length; y++) {
                for (int x = 0; x < symbols[y].length; x++) {
                    cells[x][y].setText(symbols[y][x]);
                }
            }
        });
    }

    public void updateMove(int move) {

        Platform.runLater(() -> {
            moveLabel.setText("Ходы: " + move);
        });
    }
    public void end(int reason) {

        Platform.runLater(() -> {
            switch (reason) {
                case 1:
                    controlLabel.setText("Трава исчезла");
                    break;
                case 2:
                    controlLabel.setText("Антилопы вымерли");
                    break;
                case 3:
                    controlLabel.setText("Волки вымерли");
                    break;
            }
        });
    }

    public boolean isRunning() {
        return running;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public static Window getInstance() {
        return instance;
    }
}