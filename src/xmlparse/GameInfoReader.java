package xmlparse;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.util.Map;

/**
 * Parses XML files containing initial Cell Society game information. Stores the
 * information from the file, to be easily accessible by other classes that need
 * it.
 *
 * @author Will Long
 */
public class GameInfoReader {
    private String filename;
    GameInfoHandler gameInfoHandler;

    public GameInfoReader(String fname) {
        filename = fname;
        gameInfoHandler = new GameInfoHandler();
    }

    public void readGameInfoFile() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        try {
            saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File(filename), gameInfoHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("GAME TITLE : " + gameInfoHandler.getTitle());
        System.out.println("GAME RULE : " + gameInfoHandler.getRule());
        System.out.println("GAME AUTHOR : " + gameInfoHandler.getAuthor());
        Map<String, Integer> params = gameInfoHandler.getParameterMap();
        Map<Integer, String> cellTypes = gameInfoHandler.getCellTypeMap();
        for (String param : params.keySet()) {
            System.out.println("PARAM " + param + " = " + params.get(param));
        }
        for (Integer cellType : cellTypes.keySet()) {
            System.out.println("CELLTYPE " + cellTypes.get(cellType) + " = " + cellType);
        }
    }

    public static void main(String argv[]) {
        GameInfoReader gr = new GameInfoReader("data/game_of_life.xml");
        gr.readGameInfoFile();
    }
}
