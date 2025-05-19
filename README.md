JBot

JBot is a Discord bot built using the Java Discord API (JDA). This project serves as a sandbox for experimenting with Discord bot features and Java development.
GitHub
Features

    Responds to user commands.
     - Play Music from audio sources such as Youtube, SoundCloud, Bandcamp, and Vimeo
     - Moderation utilities to create various channel types or categories in your own server
     - Get information on current servers or members of the current server
     - Various text commands to "meme" quotes, reverse text, or hear a joke, etc
     
    Handles various Discord events.
     - Acknowledges JBot joining or leaving a server
     - Greets new users joining a server, as well as farewells leaving members
     - Tracks bans, unbans, and message deletion events
    

Technologies Used

    JDA (Java Discord API): A wrapper for the Discord API
    Lavaplayer: Audio player library used for loading tracks into Opus formats
    YouTube-Source: Customized audio library for loading youtube, maintained by Lavalink devs.
    dotenv: Loads environmental files from a .env
    Jokes4j: A Java Wrapper for Sv443's joke api
    Logback: A logging framework that picks up where log4j left off.

    Built with Gradle 8.10.2

Getting Started
Prerequisites

    Java Development Kit (JDK) 8 or higher.

    Gradle installed.

    A Discord account and a bot token.
