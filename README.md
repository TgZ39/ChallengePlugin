# ChallengePlugin
Minecraft Plugin for 1.20+ with Challenges

[![GitHub all releases](https://img.shields.io/github/downloads/TgZ39/ChallengePlugin/total?style=flat&color=green)](https://www.github.com/TgZ39/ChallengePlugin/releases) [![GitHub release (with filter)](https://img.shields.io/github/v/release/TgZ39/ChallengePlugin)](https://www.github.com/TgZ39/ChallengePlugin/releases) [![Discord](https://img.shields.io/discord/961347168235585546?style=flat&logo=discord&label=Discord&link=https%3A%2F%2Fdiscord.gg%2FFrHSKEwBkv)](https://discord.gg/FrHSKEwBkv)


## Features
- Random Mob Challenge
- Random Effect Challenge
- Random Block Drop Challenge
- Lava Challenge
- Health Challenge
- Setting UI (`/settings` or `/settings <CHALLENGE> <OPTION> <VALUE>`)
- Timer (`/timer [resume, pause, reset, set]`)
- highly customizable configuration 


## Social
Discord: https://discord.gg/FrHSKEwBkv

## Installation
1. Download the latest version of the [Plugin](https://github.com/TgZ39/ChallengePlugin/releases/).
2. Put the Plugin the the `plugins` folder of your Server.
3. Start the Server.

## Contributing
1. Fork the repository.
2. Clone the repository.
   ```
    git clone https://github.com/YOUR_USERNAME/ChallengePlugin
   ```
3. Open the created `ChallengePlugin` folder in [IntelliJ](https://www.jetbrains.com/de-de/idea/)
4. Download Java 17 (Zulu Recommended) if IntelliJ tells you to.
5. Change the `outputDirectory` in the `pom.xml` to your preferred folder.
   ```xml
   <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <version>3.3.0</version>
      <configuration>
         <outputDirectory>YOUR_OUTPUT_DIRECTORY_HERE</outputDirectory>
      </configuration>
   </plugin>
   ```
6. Edit and test the code.
   - To build the plugin open the Maven tab and click `ChallengePlugin -> Lifecycle -> package`.
7. Create a [pull request](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork).
