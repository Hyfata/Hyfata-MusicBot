package com.jagrosh.jmusicbot;

import com.github.topi314.lavalyrics.LyricsManager;
import com.github.topi314.lavalyrics.lyrics.AudioLyrics;

public class LavaLyricTest {
    static void main() {
        var lyricsManager = new LyricsManager();
// 이미 등록된 LavaSrc 소스 매니저 인스턴스를 그대로 등록
        lyricsManager.registerLyricsManager(spotifySourceManager); // spDc 쿠키 필요
        lyricsManager.registerLyricsManager(youtubeSourceManager);
// deezer 등 다른 소스도 가능

// 트랙 재생 시작 시
        AudioLyrics lyrics = lyricsManager.loadLyrics(track);
        if (lyrics != null && lyrics.getLines() != null) {
            for (AudioLyrics.Line line : lyrics.getLines()) {
                double time = line.getTimestamp() / 1000.0; // 밀리초 int → 기존 double time
                String content = line.getLine();            // 기존 content
            }
        }

    }
}
