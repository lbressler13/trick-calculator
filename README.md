# Trick Calculator

This calculator exists purely for fun and chaos.
Instead of performing the expected computations, it can change the actions of the operators, the values of the numbers, and several other aspects of computation.

More information can be found in the below sections:
- [Functionality](#functionality)
- [Project structure](#project-structure)
- [Build](#build)
- [Testing](#testing)
- [Serious stuff](#serious-stuff)

## Functionality

### Available operators
The calculator currently supports addition, subtraction, multiplication, and division of rational numbers.
It also supports exponentiation, using whole numbers as exponents.
Fractional exponents are not currently supported.

Only rational numbers are supported. 
Support for irrational numbers is planned for the future.

### Available settings
Several settings are available to make the calculator more/less chaotic and difficult to use.
Here is the complete list:
| Setting               | Default | Description                                                      |
|:----------------------|:--------|:-----------------------------------------------------------------|
| Apply decimals        | true    | Apply decimals entered in textbox                                |
| Apply parentheses     | true    | Apply parentheses entered in textbox                             |
| Clear text on error   | false   | Clear textbox when error occurs                                  |
| History               | 1       | Degree of randomness in computation history                      |
| Show settings button  | false   | Show settings button on main screen                              |
| Shuffle computation   | false   | Shuffle order of numbers and order of ops within the computation |
| Shuffle numbers       | false   | Shuffle value of numbers                                         |
| Shuffle operators     | true    | Shuffle action of operators                                      |

The app can also function as a standard calculator, which performs computation correctly.
Here are the necessary configurations:
| Setting               | Value  |
|:----------------------|:-------|
| Apply decimals        | true   |
| Apply parentheses     | true   |
| Clear text on error   | any    |
| History               | 0      |
| Show settings button  | any    |
| Shuffle computation   | false  |
| Shuffle numbers       | false  |
| Shuffle operators     | false  |

With the exception of showing the settings button, no visual indication is given in the calculator of which settings are applied.

Access to the settings menu is unintuitive to create added difficulty.
In the spirit of the app, this access is undocumented.

Additional information about developing settings can be found [here](https://github.com/lbressler13/trick-calculator/blob/main/app/src/main/java/com/example/trickcalculator/ui/settings/README.md).
However, this documentation does not provide any additional insight into the settings or how to access them.

## Project structure
```project
├── app
│   ├── build <-- autogenerated build files
│   ├── libs <-- local libraries
│   ├── src
│   │   ├── androidTest <-- not implemented
│   │   ├── dev <-- code and resources that are specific to dev build variant
│   │   ├── final <-- code and resources that are specific to final build variant
│   │   ├── main
│   │   │   ├── java <-- main source code
│   │   │   │   ├── compute <-- code to perform calculation
│   │   │   │   ├── ext <-- extension methods for existing classes
│   │   │   │   ├── ui <-- app UI
│   │   │   │   ├── utils
│   │   │   ├── res <-- app resources, including strings, layouts, and images
│   │   │   ├── AndroidManifest.xml
│   │   ├── test <-- unit tests
│   ├── build.gradle <-- module level gradle file, contains app dependencies
├── build.gradle <-- project level gradle file
├── gradle.properties
└── settings.gradle
```

## Build

### Build variants
The app has 2 build variants: a dev variant, and a final variant.
The dev variant should be used for pieces of functionality that are still under development and may not be fully functional.
The final variant should be used for functionality that has been fully tested and merged into the main branch.

To aid in the development and debugging process, a menu of developer tools is available in the dev variant.
This can be launched using the icon in the bottom left corner of the screen.
This menu is unavailable in the final variant.

See [here](https://developer.android.com/studio/build/build-variants) for information about configuring build variants in an Android app.

### Dependencies
This app has dependencies on the following packages:
* [exact-fraction](https://github.com/lbressler13/exact-numbers) v0.1.0-dev
* [kotlin-utils](https://github.com/lbressler13/kotlin-utils) v0.0.1

These package must be built and placed in a local **libs** folder in order for a gradle build to succeed.
Package versions can be updated in the module-level build.gradle.

## Testing

### Unit tests
**All** new computation needs unit tests.
The computation in here is messy and complicated, and manually testing everything is difficult and it **will** miss edge cases.
The tests are annoying and time consuming to write, but it will all be worth it when you refactor the most important code and can test that it still works.
The hours writing tests will be worth it. 

You will gain a huge appreciation for testing and TDD.

### UI tests
Not yet implemented

## Serious stuff
All images are taken from [Flaticon](https://www.flaticon.com/), which allows free use of icons for personal and commercial purposes with attribution.
This is the complete list of Flaticon images used within the app.
The list is also available within the app itself.

| Icon                                                        | Creator                                                                                                                    | Link                                                                  |
|:------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------|
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_down.png)  | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/down-chevron_1633716>          |
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_left.png)  | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/left-chevron_1633718>          |
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_right.png) | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/right-chevron_1633719>         |
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_up.png)    | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/up-chevron_1633717>            |
| ![img](app/src/main/res/drawable-hdpi/launcher.png)         | Icon made by [Freepik](https://www.flaticon.com/authors/freepik)  from <https://www.flaticon.com>                          | <https://www.flaticon.com/premium-icon/calculator_2838917>            |
| ![img](app/src/main/res/drawable-hdpi/ic_arrow_left.png)    | Icon made by [Ilham Fitrotul Hayat](https://www.flaticon.com/authors/ilham-fitrotul-hayat) from <https://www.flaticon.com> | <https://www.flaticon.com/premium-icon/left_3416141>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_close.png)         | Icon made by [Ilham Fitrotul Hayat](https://www.flaticon.com/authors/ilham-fitrotul-hayat) from <https://www.flaticon.com> | <https://www.flaticon.com/premium-icon/cross_4421536>                 |
| ![img](app/src/main/res/drawable-hdpi/ic_divide.png)        | Icon made by [Smashicons](https://www.flaticon.com/authors/smashicons) from <https://www.flaticon.com>                     | <https://www.flaticon.com/free-icon/divide_149702>                    |
| ![img](app/src/main/res/drawable-hdpi/ic_equals.png)        | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/equal_56751>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_history.png)       | Icon made by [IconKanan](https://www.flaticon.com/authors/iconkanan) from <https://www.flaticon.com>                       | <https://www.flaticon.com/premium-icon/history_2901149>               |
| ![img](app/src/main/res/drawable-hdpi/ic_minus.png)         | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/minus_56889>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_info.png)          | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/info-button_64494>                |
| ![img](app/src/main/res/drawable-hdpi/ic_plus.png)          | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/plus_3524388>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_settings.png)      | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/gear_484613>                   |
| ![img](app/src/main/res/drawable-hdpi/ic_times.png)         | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/multiply-mathematical-sign_43823> |

See [here](https://support.flaticon.com/s/article/Attribution-How-when-and-where-FI?language=en_US&Id=ka03V0000004Q5lQAE) for more information about Flaticon attributions.
