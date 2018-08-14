package vas.com.currencyconverter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.Currency;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
        final ArrayAdapter<Currency> fromAdapter =
                new ArrayAdapter<Currency>(this, android.R.layout.simple_spinner_item, CurrencyConverter.getCurrencyList()) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        return getV(position, convertView, parent);
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        return getV(position, convertView, parent);
                    }

                    View getV(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TextView view = (TextView) super.getView(position, convertView, parent);
                        Currency currency = getItem(position);
                        if (currency != null)
                            view.setText(MessageFormat.format("{0} ({1})",
                                    currency.getCurrencyCode(), CurrencyConverter.getCurrencyLocale(currency).get(0).getDisplayCountry()));
                        return view;
                    }
                };
        final ArrayAdapter<Currency> toAdapter =
                new ArrayAdapter<Currency>(this, android.R.layout.simple_spinner_item, CurrencyConverter.getCurrencyList()) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        return getV(position, convertView, parent);
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        return getV(position, convertView, parent);
                    }

                    View getV(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TextView view = (TextView) super.getView(position, convertView, parent);
                        Currency currency = getItem(position);
                        if (currency != null)
                            view.setText(MessageFormat.format("{0} ({1})",
                                    currency.getCurrencyCode(), CurrencyConverter.getCurrencyLocale(currency).get(0).getDisplayCountry()));
                        return view;
                    }
                };

        sp_from.setAdapter(fromAdapter);
        sp_to.setAdapter(toAdapter);
        ed_value = findViewById(R.id.ed_value);
        ed_value.setText("1");

        sp_from.setSelection(fromAdapter.getPosition(Currency.getInstance("BRL")));
        sp_to.setSelection(toAdapter.getPosition(Currency.getInstance("USD")));
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

        sp_from.setOnItemSelectedListener(this);
        sp_to.setOnItemSelectedListener(this);
        findViewById(R.id.bt_swap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Currency from = (Currency) sp_from.getSelectedItem();
                Currency to = (Currency) sp_to.getSelectedItem();

                sp_from.setSelection(fromAdapter.getPosition(to));
                sp_to.setSelection(toAdapter.getPosition(from));
            }
        });

        findViewById(R.id.bt_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyConverter.reset();
            }
        });

        findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
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
                                textView.setText(e.getMessage());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String from = sp_from.getSelectedItem().toString();
        final String to = sp_to.getSelectedItem().toString();
        if (ed_value.getText().toString().length() > 0) {
            double value = Double.parseDouble(ed_value.getText().toString());
            CurrencyConverter.calculate(value, from, to, new CurrencyConverter.Callback() {
                @Override
                public void onValueCalculated(Double value, Exception e) {
                    if (e != null) {
                        textView.setText(e.getMessage());
                    } else {
                        // Do someting with value.
                        textView.setText(CurrencyConverter.formatCurrencyValue(to, value));
                    }
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
