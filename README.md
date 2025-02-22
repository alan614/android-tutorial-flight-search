#Android-Kotlin Flight Search Codelab Project

The App uses Room database to maintain a list of airports. A departure airport is searchable using parts of its airport code or name, when selected will then list all available flights. Searching for the departure airport as well as saving the favorites is reactive via StateFlows

The user can tap at a flight to save it as their favorite. When the user returns to the home screen and clears the search keywords, their selected favorites will be shown.

The search keyword state is maintained using the Prefernces DataStore.
