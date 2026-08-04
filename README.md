# ![logo48](https://user-images.githubusercontent.com/47643827/232855048-dfc5ef01-71b1-4ab5-9a60-2b03986c3a87.svg) Premier League: standings, scores, teams 

An open-source full-stack application with English football league scores built with Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP). 

**This project DOESN'T contain AI-generated code.**

![big_picture](https://user-images.githubusercontent.com/47643827/233084402-a84f5331-a881-4c8f-8f81-5800c4b9b1ec.png)

<img width="4218" height="1446" alt="Frame 88(2)" src="https://github.com/user-attachments/assets/a88f66fc-62f4-49cd-a435-146b597969c7" />


### DISCLAIMER
Hey! Repository is under development right now, but core-features are ready, check [Active Branches?](https://github.com/holdbetter/PremierLeague/tree/main#active-branches).

#### Active Branches

| Name | Core description | Versions |
|---|---|---|
| `dev` | Migrating whole codebase to Compose with new approaches. Introduced Metro. Custom MVI rewritten in Compose. Standings Screen in Compose (Android + iOS). Detail screen is in Progress. | `minSdk = 26`, `targetSdk = 36`, `kotlin = 2.3.21`, `gradle-version = 8.13`, `agp = 8.13.2` |
| `released/xml-dikt-di-kmp`| Production ready Backend and Android App. UI built with Android Views (XML). Used DI.kt as Dependency Injection framework. | `minSdk = 26`, `targetSdk = 36`, `kotlin = 2.1.0`, `gradle-version = 8.11.1`, `agp = 8.9.1` |

#### Working Status History

| Season | Status |
|---|---|
| 2022/2023 | Online |
| 2023/2024 | Online |
| 2024/2025 | Offline |
| 2025/2026 | Offline |
| Since May 2026 | Online in Dev Env |

### Stack

💬 Backend: Ktor (Server), Flow/Coroutines, Ktor (Network), Kodein (DI), Exposed (ORM), PostgreSQL (DB), kotlinx.serialization (Data, REST)

🧱 Multiplatform: Compose, Custom MVI (Model-View-Intent, inspired by arkivanov), Flow/Coroutines, Clean- Architecture ([What?](https://github.com/holdbetter/PremierLeague/tree/main#clean-)), Ktor + Abstraction (Network), Metro (DI), Napier (Logging), Room3, Coil

🎨 Figma: Components + Themes + Prototypes

Also, you can just open `Deps.kt` file, but aware that there are some unused constants (e.g MVICore, I’m not using it for now)

### Tech features

- Multiplatform abstractions at network, database, navigation, UI, DI layers
- Multiplatform UI implementation: screens, styles
- Simple Custom MVI Architecture over Composable
- Backend included and uses shared models contact
- Light and Dark theme are supported
- Automatically colored UI, based on team logo colors with custom adjusting on UI components demand
- Modules separation: Core and Feature modules scheme
- Only cross-platform libraries / dependencies

### User features

- Check standings
- Add your favorites
- Discover teams twitter
- Watch live scores
- Team matches calendar
- Explore history at current season

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
- [x]  Android Compose branch
- [x]  Compose Multiplatform
- [ ]  Web client (In Progress)
- [x]  iOS client - SwiftUI or Compose

## Architecture

This chapter describes how the project is designed and explains how the main multiplatform features are implemented.

### About

The project uses multimodule structure not only because it's KMP (Kotlin Multiplatform). It’s like “microservices”.

Backend is JVM module with Ktor Server on board. AndroidApp is an application module of Android platform.

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

Alternatively, run gradle command in your IDE to create configuration and debug it after

#### Other

Android app as runs as it is from “Run configurations” menu. Setup your backend domain (or localhost) at LeagueBackendService.kt

### p.s What project isn’t about?

I’m focused on new things, so if I had experience in some topic before, I’ll drop it down of my list. That’s why clients are not connectivity-aware now; It doesn’t have retry policy, exception handling or any stuff like that. If you are reading this for hiring purposes or whatever you need to see more safe app examples please check out [this](https://github.com/holdbetter/FintechChatty) project and it’s sustainability demo
