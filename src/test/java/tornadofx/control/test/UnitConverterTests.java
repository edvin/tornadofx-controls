package tornadofx.control.test;

import org.junit.Assert;
import org.junit.Test;
import tornadofx.converter.UnitConverter;

public class UnitConverterTests {
    private UnitConverter converter = new UnitConverter();

    @Test
    public void from1000() {
        converter.setBinary(false);
        Assert.assertEquals(42L * 1000L * 1000L, (long) converter.fromString("42M"));
        Assert.assertEquals(42L * 1000L * 1000L, (long) converter.fromString("42m"));
        Assert.assertEquals(4L * 1000L * 1000L, 1000L, converter.fromString("4G"));
    }

    @Test
    public void from1024() {
        converter.setBinary(true);
        Assert.assertEquals(42L * 1024L * 1024L, (long) converter.fromString("42M"));
        Assert.assertEquals(42L * 1024L * 1024L, (long) converter.fromString("42m"));
        Assert.assertEquals(4L * 1024L, (long) converter.fromString("4k"));
        Assert.assertEquals(4L * 1024L * 1024L * 1024L, (long) converter.fromString("4g"));
        Assert.assertEquals(4096L * 1024L * 1024L * 1024L, (long) converter.fromString("4t"));
        Assert.assertEquals(4L, (long) converter.fromString("4"));
    }

    @Test
    public void to1000() {
        converter.setSeparator(" ");
        converter.setBinary(false);
        Assert.assertEquals("42 M", converter.toString(42L * 1000L * 1000L));
        Assert.assertNotEquals("42 M", converter.toString(42L * 1000L * 1000L + 1L));
        Assert.assertEquals("2 G", converter.toString(2000L * 1000L * 1000L));
        converter.setSeparator("");
        Assert.assertEquals("2T", converter.toString(2000L * 1000L * 1000L * 1000L));
        Assert.assertEquals("42000001", converter.toString(42L * 1000L * 1000L + 1L));
    }

    @Test
    public void to1024() {
        converter.setSeparator(" ");
        converter.setBinary(true);
        Assert.assertEquals("43 M", converter.toString(43L * 1024L * 1024L));
        Assert.assertNotEquals("43 m", converter.toString(43L * 1024L * 1024L + 1L));
        Assert.assertEquals("2 G", converter.toString(2048L * 1024L * 1024L));
        converter.setSeparator("");
        Assert.assertEquals("2T", converter.toString(2048L * 1024L * 1024L * 1024L));
        Assert.assertEquals("44040193", converter.toString(42L * 1024L * 1024L + 1L));
        Assert.assertEquals("42", converter.toString(42L));
    }

}
