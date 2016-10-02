package cellsociety_team13;

public enum AppResources {
    APP_TITLE("Cell Society"),
    APP_WIDTH(1280),
    APP_HEIGHT(960),
    APP_CSS("style.css"),

    TITLE_IMAGE_LOCATION("resources/title_screen_image.png"),
    TITLE_IMAGE_ERROR("Title image not found: "),
    TITLE_BUTTON_WIDTH(250),
    TITLE_BUTTON_OFFSET(200),

    XML_MAIN("MAIN"),
    XML_PARAMETER("PARAMETER"),
    XML_CELLTYPE("CELLTYPE"),
    XML_GRID("GRID"),
    XML_LOCATION("LOCATION"),
    XML_MAIN_TITLE("TITLE"),
    XML_MAIN_RULE("RULE"),
    XML_MAIN_AUTHOR("AUTHOR"),
    XML_PARAMETER_NAME("NAME"),
    XML_PARAMETER_MIN("MIN"),
    XML_PARAMETER_MAX("MAX"),
    XML_PARAMETER_VAL("CURRENT"),
    XML_CELLTYPE_ID("ID"),
    XML_CELLTYPE_NAME("NAME"),
    XML_GRID_WIDTH("WIDTH"),
    XML_GRID_HEIGHT("HEIGHT"),
    XML_GRID_DEFAULTID("DEFAULTID"),
    XML_GRID_FILLMETHOD("FILLMETHOD"),
    XML_LOCATION_ID("ID"),
    XML_LOCATION_ROW("ROW"),
    XML_LOCATION_COL("COL"),
    XML_LOCATION_PERCENT("PERCENT"),
    XML_LOCATION_MANUAL_METHOD("manual"),
    XML_LOCATION_PERCENTAGE_METHOD("percentage");


    private double resourceDouble;
    private String resourceString;

    AppResources(String resource) {
        resourceString = resource;
        resourceDouble = -1;
    }

    AppResources(double resource) {
        resourceString = null;
        resourceDouble = resource;
    }

    public String getResource() {
        return resourceString;
    }

    public double getDoubleResource() {
        return resourceDouble;
    }
}
