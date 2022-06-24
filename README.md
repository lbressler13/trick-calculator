# Trick Calculator

This calculator exists purely for fun and chaos.
Instead of performing the expected computations, it can change the actions of the operators, the values of the numbers, and several other aspects of computation.
The full list of settings is below.

## Settings
Several settings are available to make the calculator more/less chaotic and difficult to use.
Here is the complete list:
| Setting               | Default | Description                                                                         |
|:----------------------|:--------|:------------------------------------------------------------------------------------|
| Apply decimals        | true    | Apply decimals entered in textbox                                                   |
| Apply parentheses     | true    | Apply parentheses entered in textbox                                                |
| Clear text on error   | false   | Clear textbox when error occurs. Error message is shown regardless of setting       |
| History               | 1       | Degree of randomness in computation history                                         |
| Show settings button  | false   | If settings button should be shown on main screen. Not accessible from main screen  |
| Shuffle computation   | false   | If the order of numbers and order of ops should be shuffled                         |
| Shuffle numbers       | false   | Shuffle value of numbers. No visual indication of new values                        |
| Shuffle operators     | true    | Shuffle action of operators. No visual indication of new actions                    |

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

Access to the settings menu is unintuitive to create added difficulty.
In the spirit of the app, this access is undocumented.

Additional information about developing settings can be found [here](https://github.com/lbressler13/trick-calculator/blob/main/app/src/main/java/com/example/trickcalculator/ui/settings/README.md).
However, this documentation does not provide any additional insight into the settings or how to access them.

## Available Operators
The calculator currently supports addition, subtraction, multiplication, and division of rational numbers.
It also supports exponentiation, using whole numbers as exponents.
Fractional exponents are not currently supported.

## Testing
**All** new computation needs unit tests.
The computation in here is messy and complicated, and manually testing everything is difficult and it **will** miss edge cases.
There is a huge sense of relief from being able to press a button and know that everything still works after refactoring the most important functions.
The hours writing tests will be worth it. 

You will gain a huge appreciation for testing and TDD.

## Dependencies
This app has a dependency on an [exact-fraction](https://github.com/lbressler13/exact-numbers) package.
This package must be built and placed in a local **libs** folder in order for a gradle build to succeed.

## Build Types
The app has 2 build types: a dev build, and a final/complete build.
The dev build should be used for pieces of functionality that are still under development and may not be fully functional.
The final build should be used for functionality that has been fully tested and merged into the main branch.

To aid in the development and debugging process, a menu of developer tools is available in the dev build.
This can be launched using the icon in the bottom left corner of the main screen.
This menu is unavailable in the final build.

See [here](https://developer.android.com/studio/build/build-variants) for information about configuring build variants in an Android app.

## Project Structure
TODO

## Serious Stuff
All images are taken from [Flaticon](https://www.flaticon.com/), which allows free use of icons for personal and commercial purposes with attribution.
This is the complete list of Flaticon images used within the app.
The list is also available within the app itself.

| Icon                                                        | Creator                                                                                                    | Link                                                                  |
|:------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------|:----------------------------------------------------------------------|
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_down.png)  | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/down-chevron_1633716>          |
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_left.png)  | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/left-chevron_1633718>          |
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_right.png) | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/right-chevron_1633719>         |
| ![img](app/src/main/res/drawable-hdpi/ic_chevron_up.png)    | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/up-chevron_1633717>            |
| ![img](app/src/main/res/drawable-hdpi/launcher.png)         | Icon made by [Pixel perfect](www.flaticon.com/authors/pixel-perfect) from <www.flaticon.com>               | <https://www.flaticon.com/free-icon/keys_2891382>                     |
| ![img](app/src/main/res/drawable-hdpi/ic_arrow_left.png)    | Icon made by [Ilham Fitrotul Hayat](www.flaticon.com/authors/ilham-fitrotul-hayat) from <www.flaticon.com> | <https://www.flaticon.com/premium-icon/left_3416141>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_close.png)         | Icon made by [Ilham Fitrotul Hayat](www.flaticon.com/authors/ilham-fitrotul-hayat) from <www.flaticon.com> | <https://www.flaticon.com/premium-icon/cross_4421536>                 |
| ![img](app/src/main/res/drawable-hdpi/ic_divide.png)        | Icon made by [Smashicons](www.flaticon.com/authors/smashicons) from <www.flaticon.com>                     | <https://www.flaticon.com/free-icon/divide_149702>                    |
| ![img](app/src/main/res/drawable-hdpi/ic_equals.png)        | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/free-icon/equal_56751>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_history.png)       | Icon made by [IconKanan](www.flaticon.com/authors/iconkanan) from <www.flaticon.com>                       | <https://www.flaticon.com/premium-icon/history_2901149>               |
| ![img](app/src/main/res/drawable-hdpi/ic_minus.png)         | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/free-icon/minus_56889>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_info.png)          | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/free-icon/info-button_64494>                |
| ![img](app/src/main/res/drawable-hdpi/ic_plus.png)          | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/plus_3524388>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_settings.png)      | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/gear_484613>                   |
| ![img](app/src/main/res/drawable-hdpi/ic_times.png)         | Icon made by [Freepik](www.flaticon.com/authors/freepik) from <www.flaticon.com>                           | <https://www.flaticon.com/free-icon/multiply-mathematical-sign_43823> |

See [here](https://support.flaticon.com/s/article/Attribution-How-when-and-where-FI?language=en_US&Id=ka03V0000004Q5lQAE) for more information about Flaticon attributions.
