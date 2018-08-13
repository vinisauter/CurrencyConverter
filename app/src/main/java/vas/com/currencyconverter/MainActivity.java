package vas.com.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Spinner sp_from;
    Spinner sp_to;
    EditText ed_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_text);
        sp_from = findViewById(R.id.sp_from);
        sp_to = findViewById(R.id.sp_to);

        ArrayAdapter<Currency> fromAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrencyConverter.getCurrencyList());
        ArrayAdapter<Currency> toAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrencyConverter.getCurrencyList());
        sp_from.setAdapter(fromAdapter);
        sp_to.setAdapter(toAdapter);
        sp_from.setSelection(fromAdapter.getPosition(Currency.getInstance("BRL")));
        sp_to.setSelection(toAdapter.getPosition(Currency.getInstance("USD")));
        ed_value = findViewById(R.id.ed_value);
        ed_value.setText("10");
        CurrencyConverter.calculate(10, "BRL", "USD", new CurrencyConverter.Callback() {
            @Override
            public void onValueCalculated(Double value, Exception e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Do someting with value.
                    textView.setText(CurrencyConverter.formatCurrencyValue("USD", value));
                }
            }
        });
        findViewById(R.id.bt_convert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String from = sp_from.getSelectedItem().toString();
                final String to = sp_to.getSelectedItem().toString();
                if (ed_value.getText().toString().length() > 0) {
                    double value = Double.parseDouble(ed_value.getText().toString());
                    CurrencyConverter.calculate(value, from, to, new CurrencyConverter.Callback() {
                        @Override
                        public void onValueCalculated(Double value, Exception e) {
                            if (e != null) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                // Do someting with value.
                                textView.setText(CurrencyConverter.formatCurrencyValue(to, value));
                            }
                        }
                    });
                }
            }
        });
    }
}
