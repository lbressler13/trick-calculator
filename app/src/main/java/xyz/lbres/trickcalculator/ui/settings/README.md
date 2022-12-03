# Settings

Settings allow the calculator to be configured in different ways, to increase or decrease the level of randomness.
Values are stored in a SharedViewModel, which can be accessed by different fragments. 
The settings can be viewed/changed via a settings menu.

## Documentation
This README does not explain the settings or their meanings. 
It is a guide to the different UI components involved in the settings and how they fit together.
A full list of settings and their basic definitions can be found in the [main README](https://github.com/lbressler13/trick-calculator/blob/main/README.md).
However, the explicit purpose of this app is to create chaos, so there are some specifics that are not documented in any README files.
For example, there is no description of the history mode values or their meaning.

Even if information is not documented in a README, it will be available through comments in the code.
Creating chaos does not excuse messy code.

## Settings menus
There are 2 different settings menus: the SettingsFragment and SettingsDialog, which extend the Fragment and DialogFragment classes respectively.
The dialog can only be launched from the developer tools menu in the dev build variant.
Therefore, the code for the SettingsDialog is stored in the [dev sources](https://github.com/lbressler13/trick-calculator/tree/main/app/src/dev/java/xyz/lbres/trickcalculator/ui/settings/SettingsDialog.kt).
In all variants, the SettingsFragment can be accessed in two different ways.
The first way will remain undocumented.
The second way is through the settings button on the main screen.
By default, this button is hidden, and must be enabled through another settings menu.

In order to maintain 2 completely separate fragments with identical functionality, the vast majority of code is stored in a SettingsUI class, which is initialized in each fragment.
The shared code includes functions to initialize the fragments, modify the UI, and update the SharedViewModel when the fragment closes.
It also accesses UI elements in the fragments using their view resource IDs.
This requires both fragments to have the same UI elements for viewing and modifying settings.

## Adding a setting
Settings are referenced, modified, and observed in various places throughout the app.
New settings must be added in the following places:
* Settings class: both constructors, equals
* SettingsUI: property, collectUI, initObservers, saveSettingsToViewModel
* initSettingsObservers: new observer
* SharedViewModel: property, randomizeSettings, resetSettings, setStandardSettings
* Layouts: fragment_settings and dialog_settings
* String resources: label
* Main README: both tables

When multiple settings are referenced in the code, they should be listed in alphabetical order.
In some cases, it may make sense to separate settings into groups (i.e. settings with the same type of UI element), but elements within each group should remain alphebetized.
This ordering does not need to be followed in layout files.
