package com.jagrosh.jmusicbot;

import com.github.topi314.lavalyrics.LyricsManager;
import com.github.topi314.lavalyrics.lyrics.AudioLyrics;
import com.github.topi314.lavasrc.lrclib.LrcLibLyricsManager;
import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.YoutubeSourceOptions;
import dev.lavalink.youtube.clients.AndroidVr;
import dev.lavalink.youtube.clients.Music;
import dev.lavalink.youtube.clients.TvHtml5Simply;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class LavaLyricTest
{
    @Test
    public void loadYouTubeLyrics()
    {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager yt = new YoutubeAudioSourceManager(
                new YoutubeSourceOptions().setAllowSearch(true),
                new Music(), new AndroidVr(), new TvHtml5Simply());
        playerManager.registerSourceManager(yt);

        // LrcLib can look up lyrics from track metadata without extra authentication
        LyricsManager lyricsManager = new LyricsManager();
        lyricsManager.registerLyricsManager(new LrcLibLyricsManager());

        // Sample YouTube track for testing (Rick Astley - Never Gonna Give You Up)
        AudioItem item = yt.loadItem(playerManager, new AudioReference("https://www.youtube.com/watch?v=dQw4w9WgXcQ", null));
        Assume.assumeTrue("Failed to load track (network/auth issue)", item instanceof AudioTrack);
        AudioTrack track = (AudioTrack) item;

        System.out.println(getSyncedLyrics(lyricsManager.loadLyrics(track)));
    }

    @Test
    public void loadSpotifyLyrics()
    {
        // Read Spotify credentials from config.txt (skip test if missing)
        Config config = ConfigFactory.parseFile(new File("config.txt"));
        Assume.assumeTrue("Spotify credentials missing in config.txt, skipping test",
                config.hasPath("spotifyId") && config.hasPath("spotifySecret") && config.hasPath("spotifyCountry"));

        String spotifyId;
        String spotifySecret;
        String spotifyCountry;
        try
        {
            spotifyId = config.getString("spotifyId");
            spotifySecret = config.getString("spotifySecret");
            spotifyCountry = config.getString("spotifyCountry");
        }
        catch (Exception e)
        {
            System.out.println("Spotify credentials unreadable in config.txt, skipping test: " + e.getMessage());
            Assume.assumeNoException(e);
            return;
        }
        Assume.assumeTrue("Spotify credentials not configured in config.txt, skipping test",
                spotifyId != null && !spotifyId.isEmpty() && !spotifyId.equals("CLIENT_ID_HERE")
                        && spotifySecret != null && !spotifySecret.isEmpty() && !spotifySecret.equals("CLIENT_SECRET_HERE")
                        && spotifyCountry != null && !spotifyCountry.isEmpty() && !spotifyCountry.equals("COUNTRY_CODE_HERE"));

        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        SpotifySourceManager spotify = new SpotifySourceManager(null, spotifyId, spotifySecret, spotifyCountry, playerManager);
        playerManager.registerSourceManager(spotify);

        LyricsManager lyricsManager = new LyricsManager();
        lyricsManager.registerLyricsManager(spotify);

        AudioItem item;
        try
        {
            // Sample Spotify track for testing (Rick Astley - Never Gonna Give You Up)
            item = spotify.loadItem(playerManager, new AudioReference("https://open.spotify.com/track/4uLU6hMCjMI75M1A2tKUQC", null));
        }
        catch (Exception e)
        {
            // Skip instead of failing when the Spotify API is unreachable (e.g. lyrics API 403 on free accounts)
            System.out.println("Failed to access Spotify API: " + e.getMessage());
            Assume.assumeNoException(e);
            return;
        }
        Assume.assumeTrue("Failed to load track", item instanceof AudioTrack);
        AudioTrack track = (AudioTrack) item;

        System.out.println(getSyncedLyrics(lyricsManager.loadLyrics(track)));
    }

    private Map<Double, String> getSyncedLyrics(AudioLyrics lyrics)
    {
        Map<Double, String> result = new LinkedHashMap<>();
        if (lyrics != null && lyrics.getLines() != null)
        {
            var lines = lyrics.getLines();
            for (int i = 0; i < lines.size(); i++)
            {
                AudioLyrics.Line line = lines.get(i);
                double time = line.getTimestamp().toMillis() / 1000.0; // Duration converted to legacy double time in seconds
                String content = line.getLine();                       // legacy content
                if (content.isEmpty() && i < lines.size() - 1)
                    content = "♪";                                     // Mark non-trailing empty lines as an interlude
                result.put(time, content);
            }
        }
        return result;
    }
}
