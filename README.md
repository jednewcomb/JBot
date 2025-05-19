# JBot

**JBot** is a multipurpose Discord bot written in Java using JDA. It includes music playback via Lavalink-compatible tools, moderation utilities, fun commands using external APIs, and more.

## 🚀 Features

- 🎵 Play music from YouTube and other sources
- 🔧 Moderation commands (ban, unban, etc.)
- 🤖 Slash commands and event listening
- 😄 Jokes and fun commands using public APIs
- 🔐 Environment-based config using `.env`
- 🧱 Modular architecture with Gradle

## 🧰 Technologies & Libraries

| Library | Description |
|--------|-------------|
| [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA) | A wrapper for the Discord API |
| [Lavaplayer](https://github.com/sedmelluq/lavaplayer) | Audio player library used for loading tracks into Opus formats |
| [YouTube-Source](https://github.com/lavalink-devs/youtube-source) | Customized audio source manager for YouTube, maintained by Lavalink devs |
| [dotenv-java](https://github.com/cdimascio/dotenv-java) | Loads environmental variables from a `.env` file |
| [jokes4j](https://github.com/IAmNotHax/jokes4j) | A Java wrapper for [Sv443's Joke API](https://jokeapi.dev/) |
| [Logback](https://github.com/qos-ch/logback) | A modern logging framework and successor to log4j |

> ✅ **Built with [Gradle 8.10.2](https://docs.gradle.org/8.10.2/release-notes.html)**

## 🖼️ Screenshots

![Screenshot 2025-05-19 104102](https://github.com/user-attachments/assets/b2c341e2-1296-4121-9836-c51a3286bbb6)



## 🛠️ Setup & Usage

### Prerequisites

- Java 17+ recommended
- Gradle installed (or use the Gradle wrapper)
- A registered Discord bot token
