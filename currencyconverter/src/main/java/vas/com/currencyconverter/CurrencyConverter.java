package vas.com.currencyconverter;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by Vinicius Sauter on 28/09/2016.
 * ...
 */

@SuppressWarnings("unused")
public class CurrencyConverter {

    private static SortedMap<Currency, Locale> currencyLocaleMap;
    private static String time = null;
    private static HashMap<String, Double> rates = null;

    static {
        currencyLocaleMap = new TreeMap<>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleMap.put(currency, locale);
            } catch (Exception ignored) {
            }
        }
    }

    public interface Callback {
        void onValueCalculated(Double value, Exception e);
    }

    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    public static List<Currency> getCurrencyList() {
        return new ArrayList<>(currencyLocaleMap.keySet());
    }

    public String getStringFormat(String currencyCode, Number number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(Currency.getInstance(currencyCode));
        return format.format(number);
    }

    public static void calculate(final double value, final String valueCurrency, final String desiredCurrency, final Callback callback) {
        if (rates == null) {
            AsyncTask<Void, Void, Exception> task = new AsyncTask<Void, Void, Exception>() {
                Double returnValue = null;

                @Override
                protected Exception doInBackground(Void... params) {
                    try {
                        generateRates();
                        returnValue = calculate(value, valueCurrency, desiredCurrency);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Exception e) {
                    callback.onValueCalculated(returnValue, e);
                }
            };
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else
            try {
                callback.onValueCalculated(calculate(value, valueCurrency, desiredCurrency), null);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onValueCalculated(null, e);
            }

    }

    private static void generateRates() throws Exception {
        // EU Bank Currency Rate data source URL
        URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        InputStream stream = url.openStream();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();

        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
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

        });
        xr.parse(new InputSource(stream));
    }

    private static Double calculate(Double value, String valueCurrency, String desiredCurrency) {
        Double rateValue = rates.get(valueCurrency);
        Double rateDesired = rates.get(desiredCurrency);

        return rateValue == 0 ? 0 : rateDesired / rateValue * value;
    }

}
