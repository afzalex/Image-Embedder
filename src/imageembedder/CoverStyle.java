package imageembedder;

public enum CoverStyle {

    FIT("DO_FIT_SELECTED"),
    FILL("DO_FILL_SELECTED"),
    STRETCH("DO_STRETCH_SELECTED");
    public final String ACTION_COMMAND_STRING;

    private CoverStyle(String actionCommandString) {
        this.ACTION_COMMAND_STRING = actionCommandString;
    }

    public static CoverStyle parseEmbedImageCoverType(String str) {
        if (str.equalsIgnoreCase(FIT.ACTION_COMMAND_STRING)) {
            return FIT;
        } else if (str.equalsIgnoreCase(FILL.ACTION_COMMAND_STRING)) {
            return FILL;
        } else {
            return STRETCH;
        }
    }
}
