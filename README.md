# Chase Challenge
<h1>How to run: </h1>
Clone the app and test on device of choice I used a Pixel 6 Pro for testing. Click play button inorder to install and run the app.

<h1>Requirements & Nice to haves Completed </h1>
<ul>
  <li>Create a browser or native-app-based application to serve as a basic weather app</li>
  <li>Search Screen:
      <ul>
        <li>Allow customers to enter a US city</li>
        <li>Call the openweathermap.org API and display the information you think a user would be interested in seeing</li>
        <li>Be sure to have the app download and display a weather icon</li>
        <li>Have image cache if needed</li>
      </ul>
    </li>
  <li>Auto-load the last city searched upon app launch.</li>
  <li>Ask the User for location access, If the User gives permission to access the location, then  retrieve weather data by default  </li>
  <li>Kotlin Coroutines</li>
  <li>Jetpack Compose</li>
  <li>Hilt</li>
  <li>Some Java</li>
  <li>Junit</li>

</ul>

<h1>Usage: </h1>
Users can search for a city and it will fetch current weather details and forecast

<h1>Sample Gifs:</h1>
<ol>
  <li>First gif is on initial app create where we ask for user location </li>
  <li>Middle gif is loading user location on app launch</li>
  <li>Last video is searching for a city</li>
</ol>
<p float="left">
    <img src="https://github.com/Giocern5/ChaseChallenge/assets/38301046/da98f468-d4d9-4582-944f-3be2d9352541" width="240" height="450" />
  <img src="https://github.com/Giocern5/ChaseChallenge/assets/38301046/ef1ba2bc-8652-4503-b93a-9cafd0f71a67" width="240" height="450" />
  <img src="https://github.com/Giocern5/ChaseChallenge/assets/38301046/335a13f1-e2e2-4483-ac63-f7cce8b74dfc" width="240" height="450" />
</p>

<h1>Project Overview:</h1>

Utilized MVVM architecture entirely in Jetpack Compose.

Incorporated Hilt for Dependency Injection.

Used Navigation to help with app navigation of screen.

<h2>API Call Optimization:</h2>

Avoided chaining API calls when fetching forecast information to reduce network load.

API gave some relevant information but more details like future forecast needed a separate API call. Chose to implement 2 individual network requests and use available information to increase user experience. API would need to be refactored to include neccessary data instead of having to chain or make multiple network requests for how I constructed the view.

After details is given for a city, I used the long and lat to make A seperate API call for weather forecast.

<h2>Application Flow:</h2>
WeatherSearchScreen: Initial screen displaying information on current location (if given acceess) or last city search. On user search, it updates the UI which great for utilizing single page app


<h1>Built Using</h1>
<li>Retrofit: Used to easily and safely make HTTP requests to RESTFUL Apis </li>
<li>Hilt: Used for dependency injection to help modularize the code for testability and readability</li>
<li>Coroutines: Useed for its ansynchronous porgamming</li>
<li>Jetpack Compose: Used per project requirements</li>
<li>Jetpack Navigation: Used to simplify navigation</li>
<li>Coil: Helps with image caching and loading</li>
