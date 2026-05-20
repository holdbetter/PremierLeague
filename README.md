# ![logo48](https://user-images.githubusercontent.com/47643827/232855048-dfc5ef01-71b1-4ab5-9a60-2b03986c3a87.svg) Premier League: standings, scores, teams 

An open-source full-stack application with English football league scores built with Kotlin Multiplatform (KMP)

![big_picture](https://user-images.githubusercontent.com/47643827/233084402-a84f5331-a881-4c8f-8f81-5800c4b9b1ec.png)

### DISCLAIMER
Hey! This repository in development right now, but core-features are developed. 
It has active backend working in dev environment. 
Some new approaches could be separated into different branches e.g branch with Compose and old XML UI version branch (current main).
Check the status below. If it's online in Production Environment then you can just download package.

Was online at 2022/2023 season

Was online at 2023/2024 season

Offline 2024/2025 season

Online in Dev Environment since May'26 (In Development)

### Stack

💬 Backend: Ktor (Server) + Flow/Coroutines (Network) + Ktor (Network) + Kodein (DI) + Exposed (ORM) + PostgreSQL (DB) + kotlinx.serialization (Data)

🧱 Multiplatform: Custom Simple MVI (Model-View-Intent, inspired by arkivanov), Flow/Coroutines, Clean- Architecture ([What?](https://github.com/holdbetter/PremierLeague/tree/main#clean-)), Ktor + Abstraction (Network), Metro (DI), Napier (Logging), Deeplink Navigation, Local databases

📱 Android: Palette (Colors) + Glide + Room with ksp + Dynamic Navigation Component on routes + Metro (DI) + Coroutines/Flow + ViewBinding (xml) and bunch of custom views

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

[Download apk](https://drive.google.com/file/d/1slZFDtRe3QMZI1i6tcjgce4gGQymuq7b/view?usp=sharing)

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
- [ ]  ~~Publishing (August 2023)~~ not planned for now

### Roadmap: Tech

- [ ]  Multiplatform test coverage
- [ ]  Analytics
- [ ]  Connectivity-aware
- [ ]  Backend authentication
- [x]  K2 migration
- [ ]  MVICore branch
- [ ]  Decompose branch
- [ ]  Android Compose branch (In Progress)
- [ ]  Compose Multiplatform (In Progress)
- [ ]  Web client (In Progress)
- [ ]  iOS client - SwiftUI or Compose

## Architecture

This chapter describes how the project is designed and explains how the main multiplatform features are implemented.

### About

The project uses multimodule structure not only because it's KMP (Kotlin Multiplatform). It’s like “microservices” - if you are not familiar with multimodule term.

All modules except `androidApp` and `backend` are KMP shared modules. Backend is JVM module with Ktor Server on board. AndroidApp is an application module of Android platform.

Any *-example module is independent android application which exposes one feature to play with.

The project itself working with gradle `kotlin-dsl` plugin, so there is `buildSrc` module, it resolves dependencies across modules.

### Modules

I’m using feature-separated structure. Communication between modules expressed in the chart below, also you can explore it with [Lucidchart](https://lucid.app/documents/view/3104509a-6501-44e6-9678-c67e7130fd07):

<details>
  <summary>Big image chart:</summary>
  
  ![PremierLeague Module Map](https://user-images.githubusercontent.com/47643827/232858640-8293af19-d076-4e60-ba0e-6085e38c9ff7.png)
</details>

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
