package vas.com.currencyconverter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    String time;
    HashMap<String, Double> rates = new HashMap<>();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        InputStream stream = url.openStream();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();

        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(new DefaultHandler() {

            @Override
            public void startDocument() throws SAXException {
                Log.d(TAG, "***** start document *****");
            }

            @Override
            public void endDocument() throws SAXException {
                Log.d(TAG, "***** end document *****");
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                Log.d(TAG, "start element: localname=" + localName);
                for (int i = 0; i < attributes.getLength(); i++) {
                    Log.d(TAG, "start element: attr=" + attributes.getLocalName(i) + " value=" + attributes.getValue(i));
                }

                if ("Cube".equals(localName)) {
                    String name = null;
                    Double rate = null;
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if ("time".equals(attributes.getLocalName(i))) {
                            time = attributes.getValue(i);
                        } else if ("currency".equals(attributes.getLocalName(i))) {
                            name = attributes.getValue(i);
                        } else if ("rate".equals(attributes.getLocalName(i))) {
                            rate = Double.parseDouble(attributes.getValue(i));
                        }
                    }
                    // add new element in the list
                    if (name != null)
                        rates.put(name, rate);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                Log.d(TAG, "end element: localname=" + localName);
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                //String content = new String(ch, start, length);
                //Log.d(TAG, "content=" + content);
            }
        });
        xr.parse(new InputSource(stream));

        String valueCurrency = "USD";
        String desiredCurrency = "BRL";
        double value = 1;
        // get currency rates
        Double rateValue = rates.get(valueCurrency);

        Double rateDesired = rates.get(desiredCurrency);

        double result;
        if (rateValue == 0) {
            result = 0;
        } else {
            result = rateDesired / rateValue * value;
        }
        assertTrue(result > 0);
    }
}
