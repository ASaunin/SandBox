import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class XmlToHtmlDriver {

    public static void main(String[] args) throws IOException, URISyntaxException, TransformerException {
        final ClassLoader classLoader = XmlToHtmlDriver.class.getClassLoader();
        final Source text = new StreamSource(new File(classLoader.getResource("catalog.xml").toURI()));
        final Source xslt = new StreamSource(new File(classLoader.getResource("transform.xsl").toURI()));
        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer = factory.newTransformer(xslt);

        final File htmlFile = File.createTempFile("catalog", ".html");
        transformer.transform(text, new StreamResult(htmlFile));
        Desktop.getDesktop().browse(htmlFile.toURI());
        htmlFile.deleteOnExit();
    }

}
