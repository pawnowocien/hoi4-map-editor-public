package src.java.main;

import src.java.exceptions.InvalidPathException;
import src.java.structure.Country;
import src.java.structure.Province;
import src.java.structure.State;

import java.io.File;
import java.util.*;

public class GreatOrganizer {
    private final File vanillaPath;
    private final File modPath;
    private final MultiGenerator multiGenerator;
    private HashMap<Integer, Province> provinces;
    private HashMap<Integer, State> states;
    private HashMap<String, Country> countries;
    private HashMap<Integer, Province> colorToProvince = new HashMap<>();
    private HashMap<Integer, State> colorToState = new HashMap<>();

    public GreatOrganizer(File vanillaPath, File modPath) throws InvalidPathException {
        this.vanillaPath = vanillaPath;
        this.modPath = modPath;

        validateVanillaPath();

        multiGenerator = new MultiGenerator(this);

        provinces = multiGenerator.getProvinces();
        states = multiGenerator.getStates();
        countries = multiGenerator.getCountries();

        for (Province p : provinces.values()) {
            colorToProvince.put(p.getColorRGB(), p);
        }
        for (State s : states.values()) {
            colorToState.put(s.getColorRGB(), s);
        }
    }
    public GreatOrganizer(File vanillaPath) throws InvalidPathException {
        this(vanillaPath, null);
    }

    private void validateVanillaPath() throws InvalidPathException {
        if (!vanillaPath.isDirectory()) throw new InvalidPathException("Path should lead to a directiory");

        List<String> files = Arrays.asList(Objects.requireNonNull(vanillaPath.list()));
        if (!files.contains("common")) throw new InvalidPathException("Path should contain 'common' directory");
        if (!files.contains("history")) throw new InvalidPathException("Path should contain 'history' directory");
        if (!files.contains("map")) throw new InvalidPathException("Path should contain 'map' directory");

        List<String> commonFiles = Arrays.asList(Objects.requireNonNull((vanillaPath.toPath().resolve("common").toFile().list())));
        if (!commonFiles.contains("country_tags")) throw new InvalidPathException("Path should contain 'common/country_tags' directory");
        if (!commonFiles.contains("countries")) throw new InvalidPathException("Path should contain 'common/countries' directory");

        List<String> historyFiles = Arrays.asList(Objects.requireNonNull((vanillaPath.toPath().resolve("history").toFile().list())));
        if (!historyFiles.contains("states")) throw new InvalidPathException("Path should contain 'history/states' directory");
        if (!historyFiles.contains("countries")) throw new InvalidPathException("Path should contain 'history/countries' directory");

        List<String> mapFiles = Arrays.asList(Objects.requireNonNull((vanillaPath.toPath().resolve("map").toFile().list())));
        if (!mapFiles.contains("definition.csv")) throw new InvalidPathException("Path should contain 'map/definition.csv' file");
        if (!mapFiles.contains("provinces.bmp")) throw new InvalidPathException("Path should contain 'map/provinces.bmp' file");
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

    public HashMap<Integer, Province> getColorToProvince() {
        return colorToProvince;
    }
    public HashMap<Integer, State> getColorToState() {
        return colorToState;
    }
    public File getComCountryTagsDir() {
        return vanillaPath.toPath().resolve("common/country_tags").toFile();
    }
    public File getComCountriesDir() {
        return vanillaPath.toPath().resolve("common/countries").toFile();
    }
    public File getHisStatesDir() {
        return vanillaPath.toPath().resolve("history/states").toFile();
    }
    public File getHisCountriesDir() {
        return vanillaPath.toPath().resolve("history/countries").toFile();
    }
    public File getProvincesFile() {
        return vanillaPath.toPath().resolve("map/definition.csv").toFile();
    }
    public File getProvincesMapFile() {
        return vanillaPath.toPath().resolve("map/provinces.bmp").toFile();
    }
}
