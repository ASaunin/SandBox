import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XMLTransformer {

    public static void convert(File inputFile, File outputFile, File xsltFile) throws TransformerException {
        final Source input = new StreamSource(inputFile);
        final StreamResult output = new StreamResult(outputFile);
        final Source xslt = new StreamSource(xsltFile);

        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer = factory.newTransformer(xslt);
        transformer.transform(input, output);
    }

}
