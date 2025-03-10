package src.java.main;

import src.java.exceptions.InvalidCodeFormatException;
import src.java.parser.*;
import src.java.structure.Country;
import src.java.structure.Province;
import src.java.structure.State;
import src.java.utils.Message;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MultiGenerator {
    private final File provincesFile;
    private final File provincesMap;
    private final File hisStatesDir;
    private final File hisCountriesDir;
    private final File comCountryTagsDir;
    private final File comCountriesDir;

    private static HashMap<Integer, Province> provinces;
    private static HashMap<Integer, State> states;
    private static HashMap<String, Country> countries;

    public MultiGenerator(GreatOrganizer greatOrganizer) {
        this.comCountryTagsDir = greatOrganizer.getComCountryTagsDir();
        this.comCountriesDir = greatOrganizer.getComCountriesDir();
        this.hisStatesDir = greatOrganizer.getHisStatesDir();
        this.hisCountriesDir = greatOrganizer.getHisCountriesDir();
        this.provincesFile = greatOrganizer.getProvincesFile();
        this.provincesMap = greatOrganizer.getProvincesMapFile();
        System.out.println(comCountryTagsDir.getAbsolutePath());
        System.out.println(comCountriesDir.getAbsolutePath());
        System.out.println(hisStatesDir.getAbsolutePath());
        System.out.println(hisCountriesDir.getAbsolutePath());
        System.out.println(provincesFile.getAbsolutePath());
        System.out.println(provincesMap.getAbsolutePath());
        try {
            generate();
        } catch (IOException e) {       // TODO pretty bad exception handling
            throw new RuntimeException(e);
        }
    }

    public void generate() throws IOException {
        Message mess = new Message(getClass());
        mess.sendStartMessage("Data generation in progress...");

        generateProvinces();
        mess.sendFormattedMessage("Provinces generated");

        generateCountries();
        mess.sendFormattedMessage("Countries generated");

        generateStates();
        mess.sendFormattedMessage("States generated");
        mess.sendEndMessage();
    }

    private void generateProvinces() {
        provinces = new HashMap<>();
        ProvinceData[] pd = ParadoxParser.provinceParser(provincesFile);
        for (ProvinceData p : pd) {
            Province province = new Province(p);
            provinces.put(p.getId(), province);
        }
    }
    private void generateCountries() {
        countries = new HashMap<>();

        for (File file : Objects.requireNonNull(comCountryTagsDir.listFiles())) {
            ParserMap pm = ParadoxParser.load(file).getParserMap();
            LinkedHashMap<String, LinkedList<ParadoxParserPart>> tempMap = pm.getMap();
            boolean dynamic_tags_now = false;
            for (String tag : tempMap.keySet()) {
                if (tag.equals("dynamic_tags")) {
                    if (deleteBrackets(tempMap.get(tag).toString()).equalsIgnoreCase("yes")) {
                        dynamic_tags_now = true;
                    } else if (deleteBrackets(tempMap.get(tag).toString()).equalsIgnoreCase("no")) {
                        dynamic_tags_now = false;
                    } else {
                        throw new RuntimeException(new InvalidCodeFormatException("Unknown dynamic_tags parameter", file));       // TODO check if dynamic_tags = yes exists
                    }
                    continue;
                }
                if (tag.length() != 3) {
                    throw new RuntimeException(new InvalidCodeFormatException("Wrong tag format", file));
                }
                if (dynamic_tags_now) {
                    continue;
                }

                String commonCountryFilename = tempMap.get(tag).toString();

                ParserList countryFile = ParadoxParser.load(new File(comCountriesDir,
                        clearComCountryFilename(commonCountryFilename)));
                ParserColor pc = (ParserColor) countryFile.getParserMap().getFirstOfKey("color");

                countries.put(tag, new Country(tag, tag, pc.getRGB()));
            }
        }

        ParserList colorsList = ParadoxParser.load(new File(comCountriesDir, "colors.txt"));
        LinkedHashMap<String, LinkedList<ParadoxParserPart>> tagMap = colorsList.getParserMap().getMap();
        for (String tag: tagMap.keySet()) {
            for (ParadoxParserPart p : tagMap.get(tag)) {
                ParserMap tempMap = ((ParserList) p).getParserMap();
                if (countries.get(tag) == null) {
                    continue;
                }
                countries.get(tag).setColorRGB(((ParserColor) tempMap.getFirstOfKey("color")).getRGB());
            }
        }
    }

    private void generateStates() {
        states = new HashMap<>();
        for (final File file : Objects.requireNonNull(hisStatesDir.listFiles())) {
            ParserList pl = ParadoxParser.load(file);

            pl = (ParserList) pl.getParserMap().getFirstOfKey("state");
            ParserMap tempMap = pl.getParserMap();

            int id = Integer.parseInt(tempMap.getFirstOfKey("id").toString());
            String name = tempMap.getFirstOfKey("name").toString();

            ParserList provinceList = (ParserList) tempMap.getFirstOfKey("provinces");
            LinkedList<Province> provincesToAdd = new LinkedList<>();
            for (ParadoxParserPart provStr : provinceList.getList()) {
                if (!(provStr instanceof ParserValue)) {
                    throw new RuntimeException(new InvalidCodeFormatException("Wrong province id", file));
                }
                provincesToAdd.add(provinces.get(Integer.parseInt(provStr.toString())));
            }

            String ownerTag = ((ParserList) tempMap.getFirstOfKey("history")).getParserMap().getFirstOfKey("owner").toString();
            Country owner = countries.get(ownerTag);

            State currentState = new State (id, name, provincesToAdd, owner);

            states.put(id, currentState);
            currentState.getCountry().addState(currentState);
            for (Province p : currentState.getProvinces()) {
                p.setState(currentState);
            }
        }
    }

    private String deleteBrackets(String str) {
        return str.replace("[", "").replace("]", "");
    }

    private String clearComCountryFilename(String filename) {
        return deleteBrackets(filename).replace("\"", "").replace("countries","");
    }

    public HashMap<Integer, Province> getProvinces() {
        return provinces;
    }
    public HashMap<Integer, State> getStates() {
        return states;
    }
    public HashMap<String, Country> getCountries() {
        return countries;
    }
}
