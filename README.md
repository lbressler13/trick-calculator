# Trick Calculator

This calculator exists purely for fun and chaos, which are both worthy goals.

Here are the settings:
| Setting               | Default | Description                                                                         |
|:----------------------|:--------|:------------------------------------------------------------------------------------|
| Apply decimals        | true    | Apply decimals entered in textbox                                                   |
| Apply parentheses     | true    | Apply parentheses entered in textbox                                                |
| Reset text on error   | false   | Clear textbox when error occurs. Error message is shown regardless of setting       |
| Show settings button  | false   | If settings button should be shown on main screen. Not accessible from main screen  |
| Shuffle numbers       | false   | Shuffle value of numbers. No visual indication of new values                        |
| Shuffle operator      | true    | Shuffle action of operators. No visual indication of new actions                    |

For standard calculator function, use the following settings:
| Setting               | Value  |
|:----------------------|:-------|
| Apply decimals        | true   |
| Apply parentheses     | true   |
| Clear text on error   | any    |
| Show settings button  | any    |
| Shuffle numbers       | false  |
| Shuffle operator      | false  |

Access to the settings menu is unintuitive to create added difficulty.
In the spirit of the app, this access is undocumented.
But you can read the code or click around in the app until you find it.

## Testing
**All** new logic needs unit tests.
The logic in here is messy and complicated, and manually testing everything is difficult and it **will** miss edge cases.
There is a huge sense of relief from being able to press a button and know that everything still works.
The hours writing tests will be worth it. 

You will gain a huge appreciation for testing and TDD.

## Dependencies
This app has a dependency on an [exact-fraction](https://github.com/lbressler13/exact-fraction) package.
This package must be built and placed in a local **libs** file in order for a gradle build to succeed.

## Serious Stuff
All images are taken from [Flaticon](https://www.flaticon.com/), which allows free use of icons for personal and commercial purposes with attribution.
This is the complete list of Flaticon images used within the app.
The list is also available within the app itself.

| Icon                                                        | Creator              | Link                                                                  |
|:------------------------------------------------------------|:---------------------|:----------------------------------------------------------------------|
| ![img](app/src/main/res/drawable-hdpi/launcher.png)         | Pixel perfect        | <https://www.flaticon.com/free-icon/keys_2891382>                     |
| ![img](app/src/main/res/drawable-hdpi/ic_arrow_left.png)    | Ilham Fitrotul Hayat | <https://www.flaticon.com/premium-icon/left_3416141>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_close.png)         | Ilham Fitrotul Hayat | <https://www.flaticon.com/premium-icon/cross_4421536>                 |
| ![img](app/src/main/res/drawable-hdpi/ic_divide.png)        | Smashicons           | <https://www.flaticon.com/free-icon/divide_149702>                    |
| ![img](app/src/main/res/drawable-hdpi/ic_equals.png)        | Freepik              | <https://www.flaticon.com/free-icon/equal_56751>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_history.png)       | IconKanan            | <https://www.flaticon.com/premium-icon/history_2901149>               |
| ![img](app/src/main/res/drawable-hdpi/ic_minus.png)         | Freepik              | <https://www.flaticon.com/free-icon/minus_56889>                      |
| ![img](app/src/main/res/drawable-hdpi/ic_info.png)          | Freepik              | <https://www.flaticon.com/free-icon/info-button_64494>                |
| ![img](app/src/main/res/drawable-hdpi/ic_plus.png)          | Freepik              | <https://www.flaticon.com/premium-icon/plus_3524388>                  |
| ![img](app/src/main/res/drawable-hdpi/ic_settings.png)      | Freepik              | <https://www.flaticon.com/premium-icon/gear_484613>                   |
| ![img](app/src/main/res/drawable-hdpi/ic_times.png)         | Freepik              | <https://www.flaticon.com/free-icon/multiply-mathematical-sign_43823> |
