# Settings

Settings allow the calculator to be configured in different ways, to increase or decrease the level of randomness.
Values are stored in a SharedViewModel, which can be accessed by different fragments.
Settings are changed through a settings menu, which contains switches, buttons, and other ui elements for modifying the values.

## Settings menus
There are 2 separate settings menus, a standard fragment and a dialog.
The dialog can be only be accessed through the developer tools, and the fragment will be opened when accessing the settings menu from any other location.
In order to manage 2 fragments with the same functionality, code is shared between fragments as much as possible.
This includes code to initialize the fragments, set the ui, and update the SharedViewModel when the fragment closes.

In addition, they contain layout elements with the same IDs.
Whenever a layout element is being referenced, a single val should be defined, and the value should be assigned based on the type of fragment.

## Adding a setting
Settings are referenced, modified, and observed in various places throughout the app.
When a new setting is added, it must be added in all those places.
To ensure that nothing is missed, add the setting in the following files:
* [ ] Add to the Settings object
* [ ] Add a key and label in the string resources
* [ ] Add to the SharedViewModel
* [ ] Add an observer in initSettingsObservers
* [ ] Add an appropriate ui element to the settings fragment and settings dialog
* [ ] Add to the args passed in the initSettingsFragments code
* [ ] Add to setUiFromArgs and saveToViewModel in settingsUtil
* [ ] Add to resetSettings and randomizeSettings in the SharedViewModel
* [ ] Add to the README
