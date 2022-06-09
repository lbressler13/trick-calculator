# Settings

Settings allow the calculator to be configured in different ways, to increase or decrease the level of randomness.
Values are stored in a SharedViewModel, which can be accessed by different fragments.
Settings are changed through a settings menu, which contains switches, buttons, and other UI elements for modifying the values.

## Documentation
A full list of settings and their definitions can be found in the [main README](https://github.com/lbressler13/trick-calculator/blob/main/README.md).
However, the explicit purpose of this app is to create chaos, so there are some specifics that are not documented in any README files.
For example, there is no description of the history randomness values or what they correspond to.

Even if information is not documented in a README, it will be available through comments in the code.
Creating chaos does not excuse messy code.

## Settings menus
There are 2 separate settings menus, a standard fragment and a dialog.
The dialog can only be accessed through the developer tools, and the fragment can be accessed through two locations.
The primary access will remain undocumented.
However, once the fragment has been opened from the primary location, there is an option to display a settings button on the main screen.
This settings button, if enabled, also opens the fragment.

In order to manage 2 fragments with the same functionality, the vast majority of code is shared between fragments using util functions, which sometimes take the fragment and/or ViewBinding as parameters
The shared code includes initializing the fragments, setting the UI, and updating the SharedViewModel when the fragment closes.

A large portion of the functionality is in setting the UI based on the current settings, and updating values based on the final UI configuration.
Because the fragment and the dialog have different layout files, they also have separate ViewBindings, which means all UI elements are separate.
In order to resolve this issue, the same names are used for elements in both layout files.
When the element is accessed in the code, a single variable is defined, and the value is pulled from the appropriate ViewBinding.

## Adding a setting
Settings are referenced, modified, and observed in various places throughout the app.
When a new setting is added, it must be added in all of these places.
To ensure that nothing is missed, add the setting in the following files:
* Settings
  * As a field in the Settings object
  * With a new observer in initSettingsObservers
  * In the args passed in initSettingsFragments
  * With an appropriate UI element in the settings fragment and dialog
  * In setUiFromArgs and saveToViewModel in settingsUtil
* SharedViewModel
  * As a property
  * In resetSettings and randomizeSettings
* Other  
  * With a key and label in the string resources
  * In both tables in the main README
