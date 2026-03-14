# Hyfata-MusicBot (JMusicBot Fork) - Project Context

This project is a Discord music bot based on [JMusicBot](https://github.com/jagrosh/MusicBot), featuring enhanced capabilities such as SyncLyrics, TTS, and multi-platform music source support.

## Project Overview

- **Core Technologies:** 
  - **Language:** Java 25
  - **Frameworks:** [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA), [Lavaplayer](https://github.com/sedmelluq/lavaplayer)
  - **Command Handling:** [jda-chewtils](https://github.com/Chew/JDA-Chewtils)
  - **Build System:** Maven
  - **Configuration:** [Typesafe Config](https://github.com/lightbend/config)
- **Architecture:** 
  - Entry point: `com.jagrosh.jmusicbot.JMusicBot`
  - Command structure: Categorized into `admin`, `dj`, `music`, `owner`, and `general` packages.
  - Commands extend `SlashCommand` or specialized base classes like `MusicCommand`, `AdminCommand`, etc.
  - Audio handling is managed by `PlayerManager` and `AudioHandler`.

## Key Features

- **Music Playback:** Supports YouTube, SoundCloud, Bandcamp, Vimeo, Twitch, and local files.
- **Enhanced Sources:** Integrated support for Spotify and Apple Music via LavaSrc.
- **SyncLyrics:** Synchronized lyrics support via Musixmatch.
- **TTS (Text-to-Speech):** Supports multi-language TTS (English, Japanese, Korean) using Google Translate/Papago APIs.
- **FairQueue™:** Ensures fair play distribution among users.
- **Slash Commands:** Fully supports Discord Slash Commands.

## Building and Running

### Build Commands
- **Compile and Package:**
  ```bash
  mvn clean package
  ```
  This generates a shaded JAR in `target/MusicBot-1.1.5-All.jar`.

### Run Commands
- **Standard Execution:**
  ```bash
  java -jar target/MusicBot-1.1.5-All.jar
  ```
- **Run without GUI:**
  ```bash
  java -Dnogui=true -jar target/MusicBot-1.1.5-All.jar
  ```
- **Generate Default Config:**
  ```bash
  java -jar target/MusicBot-1.1.5-All.jar generate-config
  ```

## Configuration

- **File:** Defaults to `config.txt` in the root directory. Can be overridden using `-Dconfig=path/to/config.conf`.
- **Default Settings:** Defined in `src/main/resources/reference.conf`.
- **Key Settings:**
  - `token`: Discord Bot Token.
  - `owner`: Bot owner's Discord ID.
  - `prefix`: Command prefix (defaulting to @mention).
  - `spotifyId`/`spotifySecret`: For Spotify support.
  - `musixmatchToken`: For synchronized lyrics.

## Development Conventions

- **Code Style:** Follows standard Java naming conventions and the existing codebase patterns from JMusicBot.
- **Command Implementation:**
  - New commands should extend `SlashCommand` (or a relevant category base class).
  - Implement `execute(SlashCommandEvent event)` or `doCommand(SlashCommandEvent event)` depending on the base class.
- **Logging:** Use SLF4J `Logger` (initialized via `LoggerFactory.getLogger()`).
- **Testing:** JUnit 4 tests are located in `src/test/java`. Run tests with `mvn test`.

## Project Structure (Key Paths)

- `src/main/java/com/jagrosh/jmusicbot/`: Main source code.
  - `commands/`: Command implementations.
  - `audio/`: Lavaplayer integration and audio management.
  - `settings/`: Settings management and persistence.
  - `synclyric/`: Synchronized lyrics logic.
  - `utils/`: Utility classes (Audio, Time, Format, etc.).
- `src/main/resources/`: Configuration and static assets.
- `scripts/`: Utility scripts (e.g., `run_jmusicbot.sh`).
