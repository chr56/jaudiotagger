package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;


public class FrameTMCLTest extends AbstractTestCase
{
    /**
     * Uses TMCL frame
     * @throws Exception
     */
    public void testWriteMusiciansIDv24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMusiciansv24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());

        f.setTag(new ID3v24Tag());
        ((ID3v24Tag)f.getTag()).setMultiValueField(FieldKey.MUSICIAN, "violinist", "Nigel Kennedy");
        ((ID3v24Tag)f.getTag()).addMultiValueField(FieldKey.MUSICIAN, "harpist", "Gloria Divosky");
        assertEquals(1, f.getTag().getFieldCount());
        assertEquals("violinist\u0000Nigel Kennedy\u0000harpist\u0000Gloria Divosky", f.getTag().getFirst(FieldKey.MUSICIAN));
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.MUSICIAN).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1, f.getTag().getFieldCount());
    }

    public void testWriteMusiciansAndDeleteIDv24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMusiciansAndDeletev24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());

        f.setTag(new ID3v24Tag());
        ((ID3v24Tag)f.getTag()).setMultiValueField(FieldKey.MUSICIAN,"violinist","Nigel Kennedy");
        ((ID3v24Tag)f.getTag()).addMultiValueField(FieldKey.MUSICIAN,"harpist","Gloria Divosky");
        assertEquals(1,f.getTag().getFieldCount());
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.MUSICIAN).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1, f.getTag().getFieldCount());

        f.getTag().deleteField(FieldKey.MUSICIAN);
        assertEquals(0,f.getTag().getFieldCount());
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(0,f.getTag().getFields(FieldKey.MUSICIAN).size());
        assertEquals(0,f.getTag().getFieldCount());
        assertEquals(0, f.getTag().getFieldCount());

    }

    public void testWriteMusiciansIDv23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMusiciansv23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());

        f.setTag(new ID3v23Tag());
        ((ID3v23Tag)f.getTag()).setMultiValueField(FieldKey.MUSICIAN,"violinist","Nigel Kennedy");
        ((ID3v23Tag)f.getTag()).addMultiValueField(FieldKey.MUSICIAN, "harpist", "Gloria Divosky");
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals("violinist\u0000Nigel Kennedy\u0000harpist\u0000Gloria Divosky", f.getTag().getFirst(FieldKey.MUSICIAN));
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.MUSICIAN).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1, f.getTag().getFieldCount());
    }

    public void testWriteMusiciansIDv22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMusiciansv22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());

        f.setTag(new ID3v22Tag());
        ((ID3v22Tag)f.getTag()).setMultiValueField(FieldKey.MUSICIAN,"violinist","Nigel Kennedy");
        ((ID3v22Tag)f.getTag()).addMultiValueField(FieldKey.MUSICIAN,"harpist","Gloria Divosky");
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals("violinist\u0000Nigel Kennedy\u0000harpist\u0000Gloria Divosky", f.getTag().getFirst(FieldKey.MUSICIAN));
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.MUSICIAN).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1, f.getTag().getFieldCount());
    }

    public void testArrangerIDv22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMusiciansv22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());

        f.setTag(new ID3v22Tag());
        ((ID3v22Tag)f.getTag()).setMultiValueField(FieldKey.ARRANGER, "arranger");
        f.commit();
    }
}