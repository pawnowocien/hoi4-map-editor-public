package src.java.parser;

import src.java.exceptions.CodeParsingException;
import src.java.structure.CountryColor;

import java.util.LinkedList;

// IMPORTANT: WORKS BUT ONLY WHEN COLORS ARE IN A LIST
// TODO rework in the future

public class ParserList implements ParadoxParserPart {
    private final LinkedList<ParadoxParserPart> list;
    private final ParserMap theOnlyMap;


    public ParserList() {
        list = new LinkedList<>();
        theOnlyMap = null;
    }

    public ParserList(String[] array, int first, int last) {
        if (!array[first].equals("{") || !array[last].equals("}")) {
            throw new RuntimeException(
                    new CodeParsingException("Tried to create ParserList without brackets on both ends."));
        }

        int inBrackets = 0;
        int firstBracket = -1;
        boolean creatingAList = false;
        boolean creatingAMap = false;
        theOnlyMap = new ParserMap();
        boolean mapAdded = false;               // to keep the (almost!) original order

        list = new LinkedList<>();
        for (int i = first + 1; i < last; i++) {
            switch (array[i]) {
                case "{":
                    if (inBrackets == 0)
                        firstBracket = i;

                    if (!creatingAMap)
                        creatingAList = true;

                    inBrackets++;
                    break;
                case "}":
                    inBrackets--;
                    if (inBrackets != 0)
                        break;

                    if (creatingAMap) {
                        theOnlyMap.add(array[firstBracket - 2], array, firstBracket, i);
                    } else if (creatingAList) {
                        list.addLast(new ParserList(array, firstBracket, i));
                    } else {
                        throw new RuntimeException(
                                new CodeParsingException("Brackets ended without creating a list or a map."));
                    }
                    creatingAMap = false;
                    creatingAList = false;
                    break;
                case "=":
                    if (inBrackets != 0)
                        break;

                    creatingAMap = true;
                    if (!mapAdded) {
                        list.add(theOnlyMap);
                        mapAdded = true;
                    }

                    break;
                default:
                    if (inBrackets != 0)
                        break;

                    // single value to map
                    if (creatingAMap) {
                        theOnlyMap.add(array[i - 2], new ParserValue(array[i]));
                        creatingAMap = false;
                        break;
                    }

                    // single value
                    if (!array[i + 1].equals("=")) {
                        list.addLast(new ParserValue(array[i]));
                        break;
                    }
            }
        }
    }

    public ParserList(LinkedList<ParadoxParserPart> list) {
        this.list = list;
        for (ParadoxParserPart part : list) {
            if (part instanceof ParserMap) {
                theOnlyMap = (ParserMap) part;
                return;
            }
        }
        theOnlyMap = null;
    }

    public LinkedList<ParadoxParserPart> getList() {
        return list;
    }

    public ParserMap getParserMap() {
        return theOnlyMap;
    }

    public CountryColor findColorOfType(String type) {
        for (ParadoxParserPart ppp : list) {
            if (!(ppp instanceof ParserColor)) {
                continue;
            }
            if (((ParserColor) ppp).getType().equals(type)) {
                return ((ParserColor) ppp).getCountryColor();
            }
        }
        return null;
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (ParadoxParserPart part : list) {
            sb.append(part.toString()).append(", ");
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }
}
