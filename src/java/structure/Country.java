package src.java.structure;

import java.util.LinkedList;

// TODO do sth about dynamic countries (probably just ignore their existence to save resources)

public class Country {
    private String name;
    private String tag;
    private LinkedList<State> states;
    private int colorRGB;
    private boolean dynamic = false;

    public Country(String name, String tag, LinkedList<State> states, int colorRGB) {
        this.name = name;
        this.tag = tag;
        this.states = states;
        this.colorRGB = colorRGB;
    }
    public Country(String name, String tag, int colorRGB) {
        this(name, tag, new LinkedList<>(), colorRGB);
    }

    public String getName() {
        return name;
    }
    public String getTag() {
        return tag;
    }
    public LinkedList<State> getStates() {
        return new LinkedList<>(states);
    }
    public int getColorRGB() {
        return colorRGB;
    }
    public void setColorRGB(int colorRGB) {
        this.colorRGB = colorRGB;
    }
    public boolean isDynamic() {
        return dynamic;
    }

    public void addState(State state) {
        states.add(state);
    }
    public void removeState(State state) {
        states.remove(state);
    }
    public Boolean containsState(State state) {
        return states.contains(state);
    }
}
