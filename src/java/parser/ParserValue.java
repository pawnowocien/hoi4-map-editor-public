package src.java.parser;

public class ParserValue implements ParadoxParserPart {
    private String value;

    public ParserValue(String value) {
        this.value = value;
        if (value.equals("-2")) {
            System.err.println("A");
        }
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        if (value.isEmpty()) {
            return "null";
        }
        return value;
    }

    public int size() {
        return 1;
    }
}
