# CurrencyConverter

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CurrencyConverter-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7070) 
[![](https://jitpack.io/v/vinisauter/CurrencyConverter.svg)](https://jitpack.io/#vinisauter/CurrencyConverter)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)


# Integrating into your project
This library is available in [JitPack.io](https://jitpack.io/) repository.
To use it, make sure to add the below inside root build.gradle file

```
allprojects {
    repositories {
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

and add the repository's url to the app's build.gradle file.

```
dependencies {
   implementation 'com.github.vinisauter:CurrencyConverter:master'
    // Other dependencies your app might use
}
```

IMPLEMENTATION
----
Get currency list:
```
 List<Currency> clist = CurrencyConverter.getCurrencyList();
```
 
Calculate currency from:
```
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

Reset data if necessary:
```
 CurrencyConverter.reset();
```

# Donation

[![ko-fi](https://www.ko-fi.com/img/donate_sm.png)](https://ko-fi.com/S6S8JJNM)

