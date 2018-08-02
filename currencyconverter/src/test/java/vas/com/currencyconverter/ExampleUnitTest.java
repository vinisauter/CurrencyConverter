package vas.com.currencyconverter;

import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String EU_BANK_XML_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    @Test
    public void addition_isCorrect() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(EU_BANK_XML_URL).build();
//                            Request request = new Request.Builder().url(MessageFormat.format("http://api.fixer.io/latest?base={0}&symbols={1}", valueCurrency, desiredCurrency)).build();
        Response response = client.newCall(request).execute();
        if (response != null && response.body() != null) {
            String responceString = response.body().string();
            assertNotNull(responceString);
        }
    }
}