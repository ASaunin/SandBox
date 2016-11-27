import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class XmlTransformerTest {

    private static File outputFile;

    @Before
    public void setUp() throws Exception {
        outputFile = File.createTempFile("catalog", ".html");
    }

    @Test
    public void checkTransformationIsCorrect() throws Exception {
        final ClassLoader classLoader = XmlTransformerTest.class.getClassLoader();
        final File inputFile = new File(classLoader.getResource("catalog.xml").toURI());
        final File xsltFile = new File(classLoader.getResource("transform.xsl").toURI());
        final File expectedFile = new File(classLoader.getResource("catalog.html").toURI());

        XMLTransformer.convert(inputFile, outputFile, xsltFile);

        assertEquals(FileUtils.readFileToString(expectedFile), FileUtils.readFileToString(outputFile));
    }

    @After
    public void tearDown() throws Exception {
        outputFile.deleteOnExit();
    }

}
