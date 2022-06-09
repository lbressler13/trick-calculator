# Settings

## Settings menus
There are 2 separate settings menus, a standard fragment and a dialog fragment.
The dialog is accessible solely through the developer tools, and the fragment is used for all other methods to access settings.
In order to manage 2 fragments with the same functionality, code is shared between fragments as much as possible.
This includes code to initialize the fragments, set the ui, and update the SharedViewModel when the fragment closes.

In addition, they contain layout elements with the same ID names.
In the code, any ui element should be defined once, and assigned to the element from the appropriate fragment.

## Adding a setting
Settings get referenced/modified/observed in a variety of places in the app.
As the app has expanded in complexity, the number of places that interact with settings has also expanded.
Therefore, this document will serve as a list of everything that needs to happen when a new setting is added.

[ ] Add to the Settings object
[ ] Add a key and label in the string resources
[ ] Add to the SharedViewModel
[ ] Add an observer in initSettingsObservers
[ ] Add an appropriate ui element to the settings fragment and settings dialog
[ ] Add to the args passed in the initSettingsFragments code
[ ] Add to setUiFromArgs and saveToViewModel in settingsUtil
[ ] Add to resetSettings and randomizeSettings in the SharedViewModel
[ ] Add to the README
