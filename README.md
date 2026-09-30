SMART PANTRY MANAGER

This project is for the Mobile App Development 700 assignment. This is a java based Android application which will suggest 
recipes to the user based on strictly whatever they have in their pantry. 

Smart Pantry manager is a android application developed using Java in android studio. It helps users to reduce food waste by 
recording whatever they already have in their pantry and will suggest them recipes that they can make with only those
ingredients. 

Why SQLite? 

I chose SQLite because it is the most suitable for a mobile application for this size. It will store data locally onto the device.
The project uses SQLiteOpenHelper for the database creation. SQLite offers persistent local storage and it does all of the required 
CRUD operations that I needed.

Setup Instructions.

Install:
  Android studio
  Android SDK
  An android emulator or use physical android phone

Opening the project in Android Studio:
  Open android studio
  Select open and select the smart pantry manager folder
  Let Gradle finish syncing
  Select an emulator or android phone
  Click run

On the first run, the application should create an SQLite database and create the tables and seed the recipes into the application
and it should open the pantry screen directly.


