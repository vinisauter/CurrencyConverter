# CurrencyConverter

Convert from:
```

 List<Currency> clist = CurrencyConverter.getCurrencyList();
 ...
 CurrencyConverter.calculate(10, "BRL", "USD", new CurrencyConverter.Callback() {
            @Override
            public void onValueCalculated(Double value, Exception e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }else{
                    //Do someting with value.
                    ...
                    textView.setText(CurrencyConverter.formatCurrencyValue(to, value));
                }
            }
        });
```