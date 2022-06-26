# Settings

Settings allow the calculator to be configured in different ways, to increase or decrease the level of randomness.
Values are stored in a SharedViewModel, which can be accessed by different fragments, and can be viewed/changed via a settings menu.

## Documentation
This README does not explain the settings or their meanings. 
It is a guide to how the settings are represented, viewed, and modified in the code.

A full list of settings and their basic definitions can be found in the [main README](https://github.com/lbressler13/trick-calculator/blob/main/README.md).
However, the explicit purpose of this app is to create chaos, so there are some specifics that are not documented in any README files.
For example, there is no description of the history values or what they mean.

Even if information is not documented in a README, it will be available through comments in the code.
Creating chaos does not excuse messy code.

## Settings menus
There are 2 different settings menus: the SettingsFragment and SettingsDialog, which extend the Fragment and DialogFragment classes.
The dialog can only be launched from the developer tools menu in the dev build variant.
The fragment can be accessed from the main screen, if the settings button is enabled. By default, it is not.
There is another way to access the fragment, but it will remain undocumented.

In order to maintain 2 completely separate fragments with identical functionality, the vast majority of code is shared using util functions.
The shared code includes functions to initialize the fragments, modify the UI, and update the SharedViewModel when the fragment closes.

Because the shared code frequently interacts with the UI, both fragments implement a SettingsUI interface.
This interface includes properties for all UI elements that are necessary when viewing and modifying settings.

## Adding a setting
Settings are referenced, modified, and observed in various places throughout the app.
When a new setting is added, it must be added in all of these places.
To ensure that nothing is missed, add the setting in the following places:
* Settings class: both constructors
* SettingsUI: property
* settingsUtil: initObservers, saveToViewModel
* initSettingsObservers: new observer
* SharedViewModel: property, randomizeSettings, resetSettings
* Layouts: fragment_setting and dialog_settings
* String resources: label
* Main README: both tables

When multiple settings are referenced in the code, they should be listed in alphabetical order.
In some cases, it may make sense to separate settings into groups (i.e. settings with the same type of UI element), but elements within each group should remain alphebetized.
This ordering does not need to be followed in layout files.
