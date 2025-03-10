package src.java.parser;

import src.java.exceptions.CodeParsingException;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ParserMap implements ParadoxParserPart {
    private final LinkedHashMap<String, LinkedList<ParadoxParserPart>> map;

    public ParserMap() {
        map = new LinkedHashMap<>();
    }

    public ParserMap(String key, String[] array, int first, int last) {
        map = new LinkedHashMap<>();
        add(key, array, first, last);
    }

    public void add(String key, ParserValue value) {
        if (!map.containsKey(key)) {
            map.put(key, new LinkedList<>());
        }
        map.get(key).add(value);
    }

    public void add(String key, String[] array, int first, int last) {
        if (!array[first].equals("{") || !array[last].equals("}")) {
            throw new RuntimeException(new CodeParsingException(
                    "Tried to create a ParserMap with a list value without brackets on both sides."));
        }

        if (!map.containsKey(key)) {
            map.put(key, new LinkedList<>());
        }

        if (key.equalsIgnoreCase("color") || key.equalsIgnoreCase("color_ui")) {
            if (last - first  == 4) {
                map.get(key).add(new ParserColor(key, array[first + 1], array[first + 2], array[first + 3]));
            } else if (last - first == 5){
                map.get(key).add(new ParserColor(key, array[first + 1], array[first + 2], array[first + 3], array[first + 4]));
            } else {
                throw new RuntimeException(new CodeParsingException("Color has too many arguments"));
            }
            return;
        }

        map.get(key).add(ParadoxParser.loadFromStringArr(array, first, last));
    }

    public int size() {
        return map.size();
    }

    public ParadoxParserPart getFirstOfKey(String key) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(new CodeParsingException(String.format("ParserMap does not contain key '%s'", key)));
        }
        return map.get(key).getFirst();
    }
    public LinkedList<ParadoxParserPart> getList(String key) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(new CodeParsingException(String.format("ParserMap does not contain key '%s'", key)));
        }
        return map.get(key);
    }

    public LinkedHashMap<String, LinkedList<ParadoxParserPart>> getMap() {
        return map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append(key).append(": {");
            for (ParadoxParserPart part : map.get(key)) {
                sb.append(part.toString()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());

            sb.append("}; ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
