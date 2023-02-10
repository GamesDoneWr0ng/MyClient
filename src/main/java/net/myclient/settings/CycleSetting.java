package net.myclient.settings;

public class CycleSetting extends Setting {
    private final String[] options;
    private String value;

    public CycleSetting(String name, String[] options, String value) {
        super(name);
        this.options = options;
        this.value = value;
    }

    public String[] getOptions() {
        return options;
    }
    public final String getValue() {
        return value;
    }
    public final void setValue(String value) {
        this.value = value;
        update();
    }
}
