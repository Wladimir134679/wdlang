package ru.wdeath.lang.lib;

public class Types {


    public static final int
            OBJECT = 0,
            NUMBER = 1,
            STRING = 2,
            ARRAY = 3,
            MAP = 4,
            FUNCTION = 5,
            CLASS = 6,
            IMPORT = 7;


    private static final int FIRST = OBJECT;
    private static final int LAST = IMPORT;
    private static final String[] NAMES = {"object", "number", "string", "array", "map", "function", "class", "import"};

    public static String typeToString(int type) {
        if (FIRST <= type && type <= LAST) {
            return NAMES[type];
        }
        return "unknown";
    }
}
