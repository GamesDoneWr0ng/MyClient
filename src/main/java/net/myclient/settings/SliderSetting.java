package net.myclient.settings;

public class SliderSetting extends Setting{
    private double value;
    private final double defaultValue;
    private final double minimum;
    private final double maximum;
    private final double increment;

    public SliderSetting(String name, double value, double minimum, double maximum, double increment) {
        super(name);
        this.value = value;
        defaultValue = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public final double getValue() {
        return value;
    }

    public final double getDefaultValue() {
        return defaultValue;
    }

    public double getRange() {
        return maximum - minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getMinimum() {
        return minimum;
    }

    public final void setValue(double value) {
        value = (int)Math.round(value / increment) * increment;
        value = Math.max(minimum, Math.min(maximum, value));

        this.value = value;
        update();
    }
}
