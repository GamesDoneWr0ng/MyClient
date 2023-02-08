package net.myclient.settings;

public class Setting {
    private final String name;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void update() {}
}
