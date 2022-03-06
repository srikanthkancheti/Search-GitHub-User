# Search GitHub repositories
Kotlin + VIPER + Dagger2 + RxAndroid + RxJava + Retrofit + Mockito


## Concepts Included

* Test Driven Development
* REST API integration
* Data management by using Room
* Reactive programming
* Dependency injection
* API debugging with Stetho
* RecyclerView with PageList and load more
* Offline support 

#### View

View is responsible for displaying the user interface and sends events provided by the user to Presenter. 
Ideally, our View also not contain any logic, but only pass events to Presenter from the user and show what Presenter will say.
Due to this, the testability is being much improved

#### Interactor

Interactor will retrieve the data from the source, convert it into ready-to-work one, and return it to Presenter.
To share the work entrusted to them and increase testability,
another layer called Repo (Repository) was added which is responsible for obtaining the data.

#### Presenter

Presenter may be compared to a “director” who sends commands to Interactor and Router after receiving the data about the user’s actions from View,
and also sends the data prepared for display from Interactor to View.

#### Entity

Entity refers to model objects used by Interactor. It is the simplest element of our VIPER structure.

#### Router

Router handles commands from Presenter to navigate between the screens.
Everything is quite simple here: Router receives a command from Presenter and,
having a link to Activity, navigates through the app views.
It is also worth mentioning that Router is responsible for passing data between screens.

#### Architecture diagram

![Architecture diagram](https://miro.medium.com/max/1354/1*8iyNwD_ODMZfsTe0qrcInA.png)


#### To Run the app
* Android Studio -> Open an existing Android Studio project -> Navigate to the project folder and open build.gradle file
* Click on the run button(play) in the Android Studio 
* We need to have a device connected through data cable or an Android emulator to run the app

#### To execute the tests
* Project view -> Search GitHub repositories -> app -> src -> test -> right click and run the tests https://drive.google.com/file/d/1AJFvXGFDePx1VOPqVTn4cl6DkrITGzME/view
