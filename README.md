# NIT3213 Android Assignment: Build a Login -> Dashboard -> Details App
Build an Android app that uses Retrofit, Koin, and RecyclerView to authenticate against the NIT3213 REST API and display topic-specific data. You'll know:
- How to configure Retrofit to consume a REST API from Android
- How to define data models and a Repository-style API service interface
- How to use Koin for dependency injection across Activities and ViewModels
- How to use Coroutines + LiveData to manage async network calls and UI state
- How to use RecyclerView to display a dynamic list fetched from an API

For architecture reference, see:
> [Guide to Android app architecture (Android Developers)](https://developer.android.com/topic/architecture)

> [Retrofit documentation](https://square.github.io/retrofit/)

> [Koin for Android documentation](https://insert-koin.io/docs/quickstart/android/)

## Tech Stack & Key Dependencies

| Purpose | Library |
|---|---|
| Language | Kotlin |
| Networking | Retrofit 2.11.0 + Gson converter |
| HTTP client | OkHttp (with logging interceptor for debugging) |
| Async | Kotlin Coroutines |
| Dependency Injection | Koin 3.5.6 |
| UI lists | RecyclerView |
| UI components | Material Components for Android |
| ViewModel / LiveData | AndroidX Lifecycle |
| Unit testing | JUnit 4, Mockito |

## Architecture

```
Activity (UI)
   observes
   
ViewModel (holds UI state as a sealed class: Loading / Success / Error)
   calls
   
ApiService (Retrofit interface)
   provided by
   
RetrofitInstance (singleton, configured via Koin)
```

```
com.example.aS8131475assignment2/
- MainActivity.kt              # Login screen
- DashboardActivity.kt         # Dashboard screen (RecyclerView)
- DetailsActivity.kt           # Details screen
- MyApplication.kt             # Starts Koin on app launch
- adapter/EntityAdapter.kt     # RecyclerView adapter for dashboard entities
- data/                        # LoginRequest/Response, DashboardResponse
- di/AppModule.kt              # Koin dependency injection module
- network/                     # ApiService, RetrofitInstance
- util/Labels.kt               # Formats raw API field names for display
- viewmodel/                   # LoginViewModel, DashboardViewModel
```

## API

- **Base URL:** `https://nit3213apinew.onrender.com/`
- **Login:** `POST /sydney/auth` - body: `{ "username": "<studentID>", "password": "<firstName>" }` -> returns `{ "keypass": "<topic>" }`
- **Dashboard:** `GET /dashboard/{keypass}` - returns the entity list for the authenticated topic

> Note: the API is hosted on a free Render.com instance, which sleeps after inactivity. The first request can take 30-60 seconds while it wakes up; later requests are fast.

## Setup

1. Open the project in [Android Studio](https://developer.android.com/studio) (**File -> Open**, select the project root).
2. Let Gradle sync automatically - this downloads all dependencies listed above.
3. Create or select a device: **Device Manager -> Create Device** (or use a physical device with USB debugging enabled).

## Run application
```
Click the green Run ▶ button in Android Studio (Shift+F10 / Ctrl+R)
```
or from the command line:
```
./gradlew installDebug
adb shell am start -n com.example.aS8131475assignment2/.MainActivity
```

On the Login screen, enter your student ID without the leading `s` as the username, and your first name (case-sensitive) as the password.

## Run tests
```
./gradlew test
```

## Author
Student ID: 8131475
Unit: NIT3213 - Mobile Application Development
