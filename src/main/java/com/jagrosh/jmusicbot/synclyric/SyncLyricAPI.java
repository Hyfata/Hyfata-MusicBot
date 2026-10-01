package com.jagrosh.jmusicbot.synclyric;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.github.topi314.lavalyrics.LyricsManager;
import com.github.topi314.lavalyrics.lyrics.AudioLyrics;
import com.github.topi314.lavasrc.lrclib.LrcLibLyricsManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SyncLyricAPI {
    private List<String> translations = null;
    private List<String> tlits = null;

    // Load only Papago-supported language profiles (zh-cn/zh-tw map to Papago code zh-CN/zh-TW)
    private static final LanguageDetector DETECTOR = createDetector();

    // Double time(seconds), String lyric
    public LinkedHashMap<Double, String> getLyric(AudioTrack track) throws Exception {
        LinkedHashMap<Double, String> lyric;
        LyricsManager lyricsManager = new LyricsManager();
        try {
            lyricsManager.registerLyricsManager(new LrcLibLyricsManager());
            AudioLyrics lyrics = lyricsManager.loadLyrics(track);
            if (lyrics == null || lyrics.getLines() == null) {
                throw new LyricNotFoundException();
            }
            lyric = (LinkedHashMap<Double, String>) getSyncedLyrics(lyrics);
        } catch (RuntimeException e) {
            throw new LyricNotFoundException();
        } finally {
            lyricsManager.shutdown();
        }

        String lang = detectLanguage(String.join("\n", lyric.values()));
        if (lang.equals("ja")) {
            translations = PapagoAPI.getTranslation(lang, lyric.values().toArray(new String[0]));
            tlits = PapagoAPI.getTlit(lang, lyric.values().toArray(new String[0]));
        } else if (!lang.equals("ko")) {
            translations = PapagoAPI.getTranslation(lang, lyric.values().toArray(new String[0]));
        }
        return lyric;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public List<String> getTlits() {
        return tlits;
    }

    private static LanguageDetector createDetector() {
        // Load only Papago-supported languages (simplified/traditional Chinese both map to CHINESE)
        return LanguageDetectorBuilder.fromLanguages(
                Language.KOREAN, Language.JAPANESE, Language.ENGLISH,
                Language.CHINESE,
                Language.SPANISH, Language.FRENCH, Language.GERMAN,
                Language.ITALIAN, Language.PORTUGUESE, Language.RUSSIAN,
                Language.VIETNAMESE, Language.THAI, Language.INDONESIAN,
                Language.HINDI, Language.ARABIC
        ).build();
    }

    public static String detectLanguage(String text) {
        Language language = DETECTOR.detectLanguageOf(text);
        return switch (language) {
            case CHINESE -> "zh-CN";
            case UNKNOWN -> "en";
            default -> language.getIsoCode639_1().toString();
        };
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
                if (content.isEmpty()) {
                    if (i == lines.size() - 1) {
                        content = ""; // end
                    } else {
                        content = "♪"; // interlude
                    }
                }
                result.put(time, content);
            }
        }
        return result;
    }
}
