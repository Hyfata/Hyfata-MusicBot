package com.jagrosh.jmusicbot.synclyric;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DetectLanguageTest
{
    @Test
    public void detectKorean()
    {
        assertEquals("ko", SyncLyricAPI.detectLanguage(
                "사랑해요 그대를 영원히\n네 곁에 있을게 언제까지나\n함께 걷던 그 길을 기억해줘"));
    }

    @Test
    public void detectJapanese()
    {
        assertEquals("ja", SyncLyricAPI.detectLanguage(
                "君がいるから ここにいるよ\nありがとう ずっと忘れない\n桜の花が舞い散る季節に"));
    }

    @Test
    public void detectEnglish()
    {
        assertEquals("en", SyncLyricAPI.detectLanguage(
                "We're no strangers to love\nYou know the rules and so do I\nNever gonna give you up, never gonna let you down"));
    }

    @Test
    public void detectMixedJapaneseEnglish()
    {
        // Mixed lyrics with dominant Japanese content should be detected as ja
        assertEquals("ja", SyncLyricAPI.detectLanguage(
                "君がいるから ここにいるよ\nありがとう ずっと忘れない\n桜の花が舞い散る季節に\n夜が明けるまで待っているよ\nI love you"));
    }

    @Test
    public void detectChinese()
    {
        assertEquals("zh-CN", SyncLyricAPI.detectLanguage(
                "月亮代表我的心\n你问我爱你有多深\n我爱你有几分\n我的情也真 我的爱也真"));
    }

    @Test
    public void detectRomajiJapaneseIsNotJapanese()
    {
        // Romaji Japanese cannot be detected as Japanese — documented as detected as en or another language
        String detected = SyncLyricAPI.detectLanguage(
                "kimi ga iru kara koko ni iru yo\narigatou zutto wasurenai");
        System.out.println("Romaji Japanese detection result: " + detected);
        assertEquals(false, detected.equals("ja"));
    }
}
