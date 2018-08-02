package vas.com.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CurrencyConverter.calculate(10, "BRL", "USD", new CurrencyConverter.Callback() {
            @Override
            public void onValueCalculated(double value, Exception e) {

            }
        });
    }
}
