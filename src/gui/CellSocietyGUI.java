package gui;

import java.io.File;
import java.util.List;

import cellsociety_team13.AppResources;
import cellsociety_team13.CellGrid;
import cellsociety_team13.CellGridSquare;
import cellsociety_team13.GameParameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rule.*;
import xmlparser.GameInfoReader;

public class CellSocietyGUI {
    private static final double TITLE_BOX_HEIGHT = 100;
    private static final double CELL_GRID_SIZE = 500;
    private static final double INPUT_PANEL_HEIGHT = 100;
    private static final double PADDING = 25;

    private static final double SCENE_WIDTH = CELL_GRID_SIZE + (2 * PADDING);
    private static final double SCENE_HEIGHT = TITLE_BOX_HEIGHT + CELL_GRID_SIZE +
            INPUT_PANEL_HEIGHT + (4 * PADDING);

    private static final Color BACKGROUND_COLOR = Color.DARKBLUE;
    private static final String DEFAULT_XML_FILE = "data/fire.xml";

    private Group sceneRoot;
    private Scene scene;

    private double appWidth, appHeight;

    private GameInfoReader gameInfoReader;
    private Rule rule;
    private TitleScreen titleScreen;
    private TitleBox titleBox;
    private CellGrid cellGrid;
    private InputPanel inputPanel;

    public CellSocietyGUI() {
        sceneRoot = new Group();
        appWidth = AppResources.APP_WIDTH.getDoubleResource();
        appHeight = AppResources.APP_HEIGHT.getDoubleResource();
        scene = new Scene(sceneRoot, appWidth, appHeight);
        scene.getStylesheets().add(getClass().getResource(AppResources.APP_CSS.getResource()).toExternalForm());
        sceneRoot.setId("root");
        gameInfoReader = new GameInfoReader(DEFAULT_XML_FILE);
        loadTitleScreen();
    }

    private void loadGame() {
        sceneRoot.getChildren().clear();
        Rectangle background = new Rectangle(appWidth, appHeight);
        background.setId("main-bg");
        sceneRoot.getChildren().add(background);
        loadRule();
        createTitleBox();
        createCellGrid();
        createInputPanel();
    }

    private void loadTitleScreen() {
        sceneRoot.getChildren().clear();
        EventHandler<ActionEvent> startButtonHandler = event -> {
            loadGame();
        };
        titleScreen = new TitleScreen(1280, 960, startButtonHandler);
        sceneRoot.getChildren().add(titleScreen);
    }


    private void loadRule() {
        String ruleName = gameInfoReader.getRuleClassName();
        if (ruleName.equals("GameOfLife")) {
            rule = new GameOfLife();
        } else if (ruleName.equals("SchellingModel")) {
            rule = new SchellingModel();
        } else if (ruleName.equals("WatorWorld")) {
            rule = new WatorWorld();
        } else if (ruleName.equals("SpreadingOfFire")) {
            rule = new SpreadingOfFire();
        }
    }

    private void createTitleBox() {
        double height = AppResources.TITLE_BOX_HEIGHT.getDoubleResource();
        String title = gameInfoReader.getTitle();
        titleBox = new TitleBox(appWidth, height, title);
        sceneRoot.getChildren().add(titleBox);
    }


    private void createCellGrid() {
        double xPos = PADDING;
        double yPos = PADDING * 2 + TITLE_BOX_HEIGHT;
        double drawWidth = SCENE_WIDTH - PADDING * 2;
        double drawHeight = drawWidth;
        int gridWidth = gameInfoReader.getGridWidth();
        int gridHeight = gameInfoReader.getGridHeight();
        List<String> initialCellTypes = gameInfoReader.getInitialCellTypeLocations();
        List<GameParameter> initialParameters = gameInfoReader.getGameParameters();
        cellGrid = new CellGridSquare(xPos, yPos, drawWidth, drawHeight, gridWidth,
                gridHeight, initialCellTypes, rule, initialParameters);
        sceneRoot.getChildren().add(cellGrid);
    }

    private void createInputPanel() {
        double x = PADDING;
        double y = cellGrid.getBoundsInParent().getMaxY() + PADDING;
        double width = SCENE_WIDTH - (PADDING * 2);
        double height = INPUT_PANEL_HEIGHT;

        EventHandler<ActionEvent> submitFileHandler = event -> {
            String filename = inputPanel.getXMLFilename();
            gameInfoReader = new GameInfoReader(filename);
            loadGame();
        };

        List<GameParameter> params = gameInfoReader.getGameParameters();

        inputPanel = new InputPanel(x, y, width, height, submitFileHandler, cellGrid, params);
        sceneRoot.getChildren().add(inputPanel);
    }

    public Scene getScene() {
        return scene;
    }
}
