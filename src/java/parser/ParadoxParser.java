package src.java.parser;

import src.java.exceptions.CodeParsingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParadoxParser {
    public static ParserList load(File file) {
        LinkedList<String> lines = fileToList(file);
        stripFromComments(lines);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line).append(" ");
        }
        Pattern pattern = Pattern.compile("\\p{C}");
        Matcher matcher = pattern.matcher(stringBuilder.toString());
        String[] arr = createArray(matcher.replaceAll(" "));
        ParadoxParserPart ppp = loadFromStringArr(arr, 0, arr.length - 1);
        return (ParserList) ppp;
    }

    public static ParadoxParserPart loadFromStringArr(String[] array, int first, int last) {
        if (first == last) {
            return new ParserValue(array[first]);
        }
        if (array[first].equals("{")) {
            return new ParserList(array, first, last);
        }
        if (array[first + 1].equals("=")) {
            return new ParserMap(array[0], array, first + 2, last);
        }
        throw new RuntimeException(
                new CodeParsingException("Unknown issue with code format encountered during parsing"));
    }

    public static LinkedList<String> fileToList(File file) {
        LinkedList<String> lines = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public static void stripFromComments(LinkedList<String> lines) {
        lines.replaceAll(s -> s.split("#")[0]);
        lines.removeIf(String::isEmpty);
    }
    public static String stripFromComments(String line) {
        return line.split(("#"))[0];
    }

    public static ProvinceData[] provinceParser (File file) {
        LinkedList<String> lines = fileToList(file);
        ProvinceData[] res = new ProvinceData[lines.size()];
        int i = 0;
        for (String line : lines) {
            res[i++] = new ProvinceData(line);
        }
        return res;
    }

    // TODO: pretty shitty code, should redo it in the future
    public static String[] createArray(String s) {
        StringBuilder lastString = new StringBuilder();
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<Integer> possibleColors = new LinkedList<>();

        boolean stringAdded = false;
        boolean inQuote = false;
        for (char ch : s.toCharArray()) {
            if (inQuote) {
                lastString.append(ch);
                if (ch == '"') {
                    queue.add(lastString.toString());
                    stringAdded = true;
                    lastString = new StringBuilder();
                    inQuote = false;
                }
                continue;
            }
            switch (ch) {
                case ' ', '\t', '\n', '\r':
                    if (!lastString.isEmpty()) {
                        if (!stringAdded) {
                            if (lastString.toString().equalsIgnoreCase("rgb") ||
                            lastString.toString().equalsIgnoreCase("hsv")) {
                                possibleColors.add(queue.size());
                            }
                            queue.add(lastString.toString().trim());
                            stringAdded = true;
                        }
                        lastString = new StringBuilder();
                    }
                    break;
                case '{','}','=':
                    if (!lastString.isEmpty() && !stringAdded) {
                        if (lastString.toString().equalsIgnoreCase("rgb") ||
                                lastString.toString().equalsIgnoreCase("hsv")) {
                            possibleColors.add(queue.size());
                        }
                        queue.add(lastString.toString());
                        lastString = new StringBuilder();
                        stringAdded = true;
                    }
                    queue.add(Character.toString(ch));
                    break;
                case '"':
                    if (!stringAdded) {
                        queue.add(lastString.toString());
                    }
                    inQuote = true;
                    lastString = new StringBuilder();
                    lastString.append('"');
                    break;
                default:
                    if (Character.isWhitespace(ch) || Character.isISOControl(ch)) {
                        System.err.println(":(");
                    }
                    lastString.append(ch);
                    stringAdded = false;
            }
        }

        for (int i : possibleColors) {
            if (queue.get(i - 2).equalsIgnoreCase("color")
            || queue.get(i - 2).equalsIgnoreCase("color_ui")) {
                queue.set(i + 1, queue.get(i));
                queue.set(i, "{");
            }
        }


        if (!lastString.isEmpty()) queue.add(lastString.toString().trim());
        queue.addFirst("{");
        queue.addLast("}");
        return queue.toArray(new String[0]);
    }
}
