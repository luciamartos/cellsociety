package xmlparser;

import cellsociety_team13.GameParameter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;


/**
 * Extends the DefaultHandler class, to properly interpret XML files formatted
 * to store CellSociety game information.
 */
class GameInfoHandler extends DefaultHandler {
    private static final int REMOVE = -1, ADD = 1;
    private static final String MAIN_SECTION = "MAIN";
    private static final String PARAMETER_SECTION = "PARAMETER";
    private static final String CELL_TYPE_SECTION = "CELLTYPE";
    private static final String GRID_SECTION = "GRID";
    private static final String LOCATION_SECTION = "LOCATIONS";

    private Stack<String> elementStack;

    private Map<String, Parser> parserMap;
    private MainInfoParser mainInfoParser;
    private ParameterParser parameterParser;
    private CellTypeParser cellTypeParser;
    private GridParser gridParser;




    String gridFillMethod;
    private String nextCellType;
    private int nextCellRow, nextCellCol;
    private int nextCellPercentFill;
    private Map<String, Integer> cellFillInfo;

    private Map<String, String> metadataMap;
    private List<GameParameter> gameParameterList;
    private int gridWidth, gridHeight;
    private List<String> initialCellTypeLocations;

    GameInfoHandler() {
        elementStack = new Stack<>();

        parserMap = new HashMap<>();
        mainInfoParser = new MainInfoParser();
        parserMap.put(MAIN_SECTION, mainInfoParser);
        parameterParser = new ParameterParser();
        parserMap.put(PARAMETER_SECTION, parameterParser);
        cellTypeParser = new CellTypeParser();
        parserMap.put(CELL_TYPE_SECTION, cellTypeParser);
        gridParser = new GridParser();
        parserMap.put(GRID_SECTION, gridParser);

        gridFillMethod = null;
        nextCellType = null;
        nextCellRow = -1;
        nextCellCol = -1;

        metadataMap = new HashMap<>();
        gameParameterList = new ArrayList<>();
        cellTypeMap = new HashMap<>();
        initialCellTypeLocations = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        String section = qName.toUpperCase();
        updateCurrentSection(section, ADD);
        if (qName.equalsIgnoreCase("PARAMETER")) {
            nextParameterName = null;
            nextParameterVals = new int[]{-1, -1, -1};
        } else if (qName.equalsIgnoreCase("CELLTYPE")) {
            nextCellTypeID = -1;
            nextCellTypeName = null;
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        updateCurrentSection(qName, REMOVE);
        if (qName.equalsIgnoreCase("PARAMETER")) {
            addParameter();
        } else if (qName.equalsIgnoreCase("CELLTYPE")) {
            addCellType();
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String information = new String(ch, start, length);
        String section = elementStack.get(0);

        if (elementStack.contains("MAIN")) {
            parseMetadata(information);
        } else if (elementStack.contains("PARAMETER")) {
            parseParameter(information);
        } else if (elementStack.contains("CELLTYPE")) {
            parseCellType(information);
        } else if (elementStack.contains("GRID")) {
            parseGrid(information);
        }
    }

    private void updateCurrentSection(String sectionName, int operation) {
        if (operation == ADD) {
            elementStack.push(sectionName.toUpperCase());
        } else if (operation == REMOVE) {
            elementStack.pop();
        }
    }

    private void parseMetadata(String information) {
        String metadataName = elementStack.peek();
        metadataMap.put(metadataName, information);
    }

    private void parseParameter(String information) {
        if (elementStack.peek().equals("NAME")) {
            nextParameterName = information;
        } else if (elementStack.peek().equals("MIN")) {
            int val = Integer.parseInt(information);
            nextParameterVals[0] = val;
        } else if (elementStack.peek().equals("MAX")) {
            int val = Integer.parseInt(information);
            nextParameterVals[1] = val;
        } else if (elementStack.peek().equals("CURRENT")) {
            int val = Integer.parseInt(information);
            nextParameterVals[2] = val;
        }
    }

    private void addParameter() {

    }

    private void addCellType() {

    }

    private void parseGrid(String information) {
        if (elementStack.peek().equals("WIDTH")) {
            int width = Integer.parseInt(information);
            gridWidth = width;
        } else if (elementStack.peek().equals("HEIGHT")) {
            int height = Integer.parseInt(information);
            gridHeight = height;
        } else if (elementStack.peek().equals("DEFAULTID")) {
            int defaultID = Integer.parseInt(information);
            initializeGrid(defaultID);
        } else if (elementStack.peek().equals("FILLMETHOD")) {
            gridFillMethod = information;
        } else if (elementStack.contains("CELL")) {
            parseCell(information);
        }
    }

    private void initializeGrid(int defaultID) {
        String defaultCellType = cellTypeMap.get(defaultID);
        for (int i = 0; i < gridHeight * gridWidth; i++) {
            initialCellTypeLocations.add(defaultCellType);
        }
    }

    private void parseCell(String information) {
        if (elementStack.peek().equals("ID")) {
            int id = Integer.parseInt(information);
            String cellType = cellTypeMap.get(id);
            nextCellType = cellType;
        } else if (elementStack.peek().equals("ROW")) {
            int row = Integer.parseInt(information);
            nextCellRow = row;
        } else if (elementStack.peek().equals("COL")) {
            int col = Integer.parseInt(information);
            nextCellCol = col;
            int pos = nextCellRow * gridWidth + nextCellCol;
            initialCellTypeLocations.add(pos, nextCellType);
        }
    }

    String getMetadata(String metadataName) {
        return metadataMap.get(metadataName);
    }

    List<GameParameter> getGameParameters() {
        return gameParameterList;
    }

    int getGridWidth() {
        return gridWidth;
    }

    int getGridHeight() {
        return gridHeight;
    }

    List<String> getInitialCellTypeLocations() {
        return initialCellTypeLocations;
    }
}
