# Trick Calculator

[![All Checks](https://github.com/lbressler13/trick-calculator/actions/workflows/all_checks.yml/badge.svg?branch=main)](https://github.com/lbressler13/trick-calculator/actions/workflows/all_checks.yml)

This calculator exists purely for fun and chaos.
Instead of performing the expected computations, it can change the actions of the operators, the values of the numbers, and several other aspects of computation.

More information can be found in the below sections:
- [Functionality](#functionality)
- [Project structure](#project-structure)
- [Build](#build)
- [Testing](#testing)
- [Linting](#linting)
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
| Setting               | Default | Description                                                       |
|:----------------------|:--------|:------------------------------------------------------------------|
| Apply decimals        | true    | Apply decimals entered in textbox                                 |
| Apply parentheses     | true    | Apply parentheses entered in textbox                              |
| Clear text on error   | false   | Clear textbox when error occurs                                   |
| History mode          | 1       | Degree of randomness in computation history                       |
| Randomize signs       | false   | Randomize the sign of each number                                 |
| Show settings button  | false   | Show settings button on main screen                               |
| Shuffle computation   | false   | Shuffle order of numbers and order of ops within the computation  |
| Shuffle numbers       | false   | Shuffle value of numbers                                          |
| Shuffle operators     | true    | Shuffle action of operators                                       |

The app can also function as a standard calculator, which performs computation correctly.
This can be enabled via a button in the settings menu.

Outside of the settings menu, there is no visual indication of which settings are applied to computation.
Randomization settings are re-applied each time the equals button is clicked, so repetitions of a computation may not produce the same result.

Some details about the settings are not documented. The definition of the "history mode" is one example of this.
However, all details are available through comments in the code. Creating chaos does not excuse messy code.

Similarly, access to the settings menu is unintuitive to create added difficulty.
In the spirit of the app, this access is undocumented.

## Project structure

```project
├── app
│   ├── build                <-- autogenerated build files
│   ├── src
│   │   ├── dev              <-- code and resources that are specific to dev product flavor
│   │   ├── espresso         <-- UI tests
│   │   ├── espressoDev      <-- UI tests that are specific to dev product flavor
│   │   ├── espressoFinal    <-- UI tests that are specific to final product flavor
│   │   ├── final            <-- code and resources that are specific to final product flavor
│   │   ├── main
│   │   │   ├── kotlin       <-- main source code
│   │   │   │   ├── compute  <-- code to perform calculation
│   │   │   │   ├── ext      <-- extension methods for existing classes
│   │   │   │   ├── ui       <-- app UI
│   │   │   │   ├── utils    <-- utility functions and useful typealiases
│   │   │   ├── res          <-- app resources, including strings, layouts, and images
│   │   │   ├── AndroidManifest.xml
│   │   ├── test             <-- unit tests
│   ├── build.gradle.kts     <-- module level gradle file, contains app dependencies
├── build.gradle.kts         <-- project level gradle file
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

This app has dependencies on packages that are hosted in the GitHub Packages registry.
In order to build the project, you will need a GitHub access token with at least the `read:packages` scope.

**Do not commit your access token.**

You can add the following properties to a gradle.properties file in order to build:
```properties
github.username=GITHUB_USERNAME
github.token=GITHUB_PAT
```
This will allow you to build through an IDE or the command line.
To build in the command line, you can set:
```shell
USERNAME=GITHUB_USERNAME
TOKEN=GITHUB_PAT
```
However, this configuration may not allow you to build through an IDE.
If you have values set in both gradle.properties and in the environment, the values in gradle.properties will be used.

See [here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) for more information on importing GitHub packages.

## Testing

### Unit tests

**All** new computation needs unit tests.
The computation in here is messy and complicated, and manually testing everything is difficult and it **will** miss edge cases.
The tests are annoying and time consuming, but they will be worth it.
You will gain a huge appreciation for testing and TDD.

Unit tests can be run via an IDE, or with the following command:
```./gradlew test```

### UI tests

UI tests are implemented using the Espresso framework.
This includes tests for functionality in the app, as well as tests for any UI utils/extension functions.
Some tests are shared across build variants, and some are run only on specific variants.

Tests can be run via an IDE, or with the following commands:
* All tests for both variants: `./gradlew connectedCheck`
* Dev build variant only: `./gradlew connectedDevDebugAndroidTest`
* Final build variant only: `./gradlew connectedFinalDebugAndroidTest`

Espresso tests must be run on a physical device or an emulator.
See [here](https://developer.android.com/training/testing/espresso) for more information about testing with Espresso.

## Linting

Linting is done using [ktlint](https://ktlint.github.io/), using [this](https://github.com/jlleitschuh/ktlint-gradle) plugin.
See [here](https://github.com/pinterest/ktlint#standard-rules) for a list of standard rules.

To run linting and fix formatting issues if possible, run the following command in the terminal or via an IDE:
```./gradlew ktlintFormat```

To run linting without fixing issues, run the following command in the terminal or via an IDE:
```./gradlew ktlintCheck```

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
| ![img](app/src/main/res/drawable-hdpi/ic_download.png)      | Icon made by [joalfa](https://www.flaticon.com/authors/joalfa) from <https://www.flaticon.com>                             | <https://www.flaticon.com/free-icon/download_3502477>                 |
| ![img](app/src/main/res/drawable-hdpi/ic_divide.png)        | Icon made by [Smashicons](https://www.flaticon.com/authors/smashicons) from <https://www.flaticon.com>                     | <https://www.flaticon.com/free-icon/divide_149702>                    |
| ![img](app/src/main/res/drawable-hdpi/ic_equals.png)        | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/equal_56751>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_history.png)       | Icon made by [IconKanan](https://www.flaticon.com/authors/iconkanan) from <https://www.flaticon.com>                       | <https://www.flaticon.com/premium-icon/history_2901149>               |
| ![img](app/src/main/res/drawable-hdpi/ic_minus.png)         | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/minus_56889>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_info.png)          | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/info-button_64494>                |
| ![img](app/src/main/res/drawable-hdpi/ic_plus.png)          | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/plus_3524388>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_settings.png)      | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/premium-icon/gear_484613>                   |
| ![img](app/src/main/res/drawable-hdpi/ic_times.png)         | Icon made by [Freepik](https://www.flaticon.com/authors/freepik) from <https://www.flaticon.com>                           | <https://www.flaticon.com/free-icon/multiply-mathematical-sign_43823> |

See [here](https://support.flaticon.com/s/article/Attribution-How-when-and-where-FI?language=en_US&Id=ka03V0000004Q5lQAE) for more information about Flaticon attributions.
