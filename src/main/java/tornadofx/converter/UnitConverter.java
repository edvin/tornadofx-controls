package tornadofx.converter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.util.UnknownFormatConversionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitConverter extends StringConverter<Long> {
    private static final String units = "kMGTPE";
    private StringProperty separatorProperty = new SimpleStringProperty("");
    private BooleanProperty binaryProperty = new SimpleBooleanProperty(this, "binary", false);

    private static final Pattern ValueWithUnit = Pattern.compile("(\\d+)\\s*([kmgtpe]?)$");

    public String toString(Long value) {
        long base = getBase();

        int unitIndex;

        for (unitIndex = 0; unitIndex < units.length(); unitIndex++) {
            if (value < base || (value % base != 0)) return out(value, unitIndex);
            value = value / base;
        }

        return out(value, unitIndex);
    }

    private String out(long value, int unitIndex) {
        return ("" + value) + (unitIndex == 0 ? "" : (getSeparator() + units.charAt(unitIndex - 1)));
    }

    public Long fromString(String string) {
        if (string == null || string.isEmpty()) return null;

        Matcher matcher = ValueWithUnit.matcher(string.toLowerCase());
        if (!matcher.matches()) throw new UnknownFormatConversionException(String.format("Invalid format %s", string));

        Long value = Long.valueOf(matcher.group(1));
        String unit = matcher.group(2);
        if (unit.isEmpty()) return value;

        int index = units.toLowerCase().indexOf(unit.toLowerCase());
        return value * (long) Math.pow(getBase(), (double) index + 1);
    }

    private long getBase() {
        return getBinary() ? 1024 : 1000;
    }

    public boolean getBinary() {
        return binaryProperty.get();
    }

    public BooleanProperty binaryProperty() {
        return binaryProperty;
    }

    public void setBinary(boolean binary) {
        this.binaryProperty.set(binary);
    }

    public String getSeparator() {
        return separatorProperty.get();
    }

    public StringProperty separatorProperty() {
        return separatorProperty;
    }

    public void setSeparator(String separator) {
        this.separatorProperty.set(separator);
    }
}
