# ![logo48](https://user-images.githubusercontent.com/47643827/232855048-dfc5ef01-71b1-4ab5-9a60-2b03986c3a87.svg) Premier League: standings, scores, teams 

An open-source full-stack application with English football league scores built with Kotlin Multiplatform (KMP)

![big_picture](https://user-images.githubusercontent.com/47643827/233084402-a84f5331-a881-4c8f-8f81-5800c4b9b1ec.png)

### Stack

💬 Backend: Ktor (Server) + Flow/Coroutines (Network) + Ktor (Network) + Kodein (DI) + Exposed (ORM) + PostgreSQL (DB) + kotlinx.serialization (Data)

🧱 Multiplatform: Custom Simple MVI (Model-View-Intent, inspired by arkivanov), Flow/Coroutines, Clean- Architecture ([What?](https://github.com/holdbetter/PremierLeague/new/main?readme=1#clean-)), Ktor + Abstraction (Network), DI.kt (DI), Napier (Logging), Deeplink Navigation, Local databases

📱 Android: Palette (Colors) + Glide + Room with ksp + Dynamic Navigation Component on routes + DI.kt (DI) + Coroutines/Flow + ViewBinding (xml) and bunch of custom views

🎨 Figma: Components + Themes + Prototypes

Also, you can just open `Deps.kt` file, but consider there are some unused constants (e.g MVICore, I’m not using it for now)

### Tech features
- Multiplatform abstractions at network, database, navigation, UI, DI layers
- Automatically colored UI based on team logo colors with custom adjusting on UI components demand
- Light and Dark theme are supported
- Module separation: core and feature module scheme
- Only cross-platform libraries / dependencies

### User features

- Check standings
- Add your favorites
- Discover teams twitter
- Watch live scores
- Team matches calendar
- Explore history at current season

### Demonstration and access

[Download apk](https://drive.google.com/file/d/1AKfiUJE2AHi7hqUM2WzLDsO2P_rV8qVO/view?usp=share_link)

[Watch demo](https://drive.google.com/file/d/1AKfiUJE2AHi7hqUM2WzLDsO2P_rV8qVO/view?usp=share_link)

### Credits

Hey, my name is Vilen. 

I am Android developer with 5+ year programming experience. I am passionate about UI/UX design and enjoy trying new things. This project expresses me well 😎

I am going to probably publish this app in Play Market for the next football season, but it mainly developed for educational purposes. If you are interested to see it in Play Market follow to roadmap.

You are welcome for asking any questions about the project!

[Contact](https://t.me/holdbetter)

### Design

[Figma](https://www.figma.com/file/63yqz3bw0qT4JouWrvfqJo/League2023-(Share)?node-id=0%3A1&t=xo6iBPGClRK1VWss-1)

### Roadmap: Features

- [ ]  Compare feature
- [ ]  Live notifications
- [ ]  Team subscriptions
- [ ]  Publishing (August 2023)

### Roadmap: Tech

- [ ]  Multiplatform test coverage
- [ ]  Analytics
- [ ]  Connectivity-aware
- [ ]  Backend authentication
- [ ]  K2 migration
- [ ]  MVICore branch
- [ ]  Android Compose branch
- [ ]  Decompose branch
- [ ]  Compose Multiplatform
- [ ]  Web client (2024)
- [ ]  iOS client - SwiftUI or Compose (2024)

## Architecture

This chapter describes how the project is designed and explains how the main multiplatform features are implemented.

### About

The project uses multimodule structure not only because it's KMM (Kotlin Multimodule). It’s like “microservices” - if you are not familiar with multimodule term.

All modules except `androidApp` and `backend` are KMM shared modules. Backend is JVM module with Ktor Server on board. AndroidApp is an application module of Android platform.

Any *-example module is independent android application which exposes one feature to play with.

The project itself working with gradle `kotlin-dsl` plugin, so there is `buildSrc` module, it resolves dependencies across modules.

### Modules

I’m using feature-separated structure. Communication between modules expressed in the chart below, also you can explore it with [Lucidchart](https://lucid.app/documents/view/3104509a-6501-44e6-9678-c67e7130fd07):

<details>
<summary>Big image chart:</summary>
![PremierLeague Module Map](https://user-images.githubusercontent.com/47643827/232858640-8293af19-d076-4e60-ba0e-6085e38c9ff7.png)
</details>

### Clean-

I’m aware of Clean architecture, but it doesn’t fit well with modern approaches since it adds a lot of boilerplate. I do use it, but not the way it was introduced.

For example, things like RxJava and Flow changed the way you communicate between Clean layers. Also I am avoiding use-cases, because my project mostly has `Repository` with one method and I prefer keep it simple.

As a result I don’t call it “Clean architurecture” even if I am applying it’s rules.

### Network (Shared Business Logic Example)

As you could see `core-network` module is shared module and it consists of 4 modules: common - contains shared code and others are platform-specific modules: Android, iOS, JVM. The last is using by backend service. So all project parts (even backend) consume same module, same logic and same API.

I’ve designed 2 UML diagrams which describe communication between entities.

First illustrates how platform-specific parts are implemented and how DI would provide it to feature-modules. If it seems hard to investigate please follow to second diagram.

Multiplatform Shared Network Logic Implementation: [(pdf)](https://drive.google.com/file/d/1D-e0a82YKKUSuwUjkkrLNCJciqb0w0Ym/view?usp=share_link)

<details>
<summary>Another big image chart:</summary>
![multiplatformNetworkSharedUMLi](https://user-images.githubusercontent.com/47643827/232858684-d9bf05ac-8d64-4147-ad51-2ba4d84dfdcf.png)
</details>

The second diagram shows how feature module consumes network module. At the diagram, feature module is common module, it defines business logic with repository to receive data from network or database and return it to UI. 

Although common module isn’t platform specific it provides repository to the presentation layer. Presentation (UI) is platform-specific so features common module implicitly exposes implementation code that’s why `android-network`  presented on chart.

Multiplatform Shared Network Logic Usage: [(pdf)](https://drive.google.com/file/d/1D-e0a82YKKUSuwUjkkrLNCJciqb0w0Ym/view?usp=share_link)

![multiplatformNetworkSharedUsageUMLi](https://user-images.githubusercontent.com/47643827/232858890-09b6887f-88bc-4046-99f1-1e33f4b6aa31.png)

### Build and Run

#### Backend

******Run******

To build and run backend service you need prerequisites an API key and PostgreSQL database. You can it do it locally. If you set up it correctly, then run following command with terminal at the project root folder: 

`./gradlew backend:run`

**********Debug**********

Alternatively, run gradle command in Android Studio to create configuration and debug it after

#### Other

Android app and feature example configs runs as it is from “Run configurations” menu, no additional steps required

### p.s What project isn’t about?

I’m focused on new things, so if I had experience in some topic before, I’ll drop it down of my list. That’s why clients are not connectivity-aware now; It doesn’t have retry policy, exception handling or any stuff like that. If you are reading this for hiring purposes or whatever you need to see more safe app examples please check out [this](https://github.com/holdbetter/FintechChatty) project and it’s sustainability demo
