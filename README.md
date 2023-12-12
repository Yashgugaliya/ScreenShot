# Screenshot Viewer and OCR
Overview
A simple Android app that provides a seamless experience for viewing and managing screenshots. The app leverages modern Android development principles and integrates Google ML Kit for OCR and image labeling. It follows the MVVM architecture, uses Hilt for dependency injection, Room for local data storage, and employs Kotlin Coroutines for asynchronous programming.

## Features
- Image Sync
  - Automatically syncs images from the device's screenshot gallery.
  - Utilizes Android's Content Provider to fetch and display images.
- Text Recognition with Google's ML Kit
  - Implements Google's ML Kit for on-device text recognition (OCR).
  - Populates the description field in the 'Screenshot Details' page with OCR data extracted from the selected image.
- Image Labeling
  - Uses Google's ML Kit for on-device image labeling.
  - Collections in the app are based on the labels generated from image labeling.
- User-Friendly UI
  - Horizontal image strip for easy navigation.
  - Full image preview on the 'Screenshot Details' page.
  - Intuitive design for a smooth user experience.

## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Google ML Kit](https://developers.google.com/ml-kit/vision/text-recognition/v2/android) - For OCR and Image Labeling.
- [Hilt](https://dagger.dev/hilt/) - Hilt provides a standard way to incorporate Dagger dependency injection into an Android application.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.

 # Package Structure
    
    com.example.screenshot  # Root Package
    
    ├── data                # For data handling.
    │   ├── local           # Local Persistence Database. Room (SQLite) database   
    │   └── model           # Model classes
    |
    ├── di                  # Dependency Injection
    ├── repository          # Single source of data.           
    ├── util                # Utility Classes / Kotlin extensions  
    ├── view                # Activity/View layer
    │   ├── activity        # Activity layer
    │   ├── adaptor         # Adaptor View 
    │   ├── bottomsheet     # BottomSheet layer
    │   └── fragments       # Fragments layer
    |
    ├── viewmodel           # ViewHolder for RecyclerView
    └── app                 # Application Class

  ## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


