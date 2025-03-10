package src.java.utils;

public class Message {
    private long startTime;
    private final String name;

    public Message(Class<?> callingClass){
        this.startTime = System.currentTimeMillis();
        this.name = callingClass.getSimpleName();
    }
    public Message(String name){
        this.startTime = System.currentTimeMillis();
        this.name = name;
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public void sendStartMessage(String str) {
        System.out.printf("[%s]: %s\n", name, str);
    }

    public void sendFormattedMessage(String str) {
        System.out.printf("[%s]: %s [%dms]\n", name, str, System.currentTimeMillis() - startTime);
    }

    public void sendEndMessage() {
        System.out.printf("[%s]: Done [%dms]\n\n", name, System.currentTimeMillis() - startTime);
    }
}
