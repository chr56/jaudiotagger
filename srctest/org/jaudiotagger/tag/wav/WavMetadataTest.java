package org.jaudiotagger.tag.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.audio.wav.WavSaveOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavMetadataTest extends AbstractTestCase
{


    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testReadFileWithListInfoMetadata()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
            assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            assertEquals("conductor\0", tag.getFirst(FieldKey.CONDUCTOR));
            assertEquals("lyricist\0", tag.getFirst(FieldKey.LYRICIST));
            assertEquals("composer\0", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("albumArtist\0", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("100\0", tag.getFirst(FieldKey.RATING));
            assertEquals("encoder\0", tag.getFirst(FieldKey.ENCODER));
            assertEquals("ISRC\0", tag.getFirst(FieldKey.ISRC));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveBoth()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveBoth.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(10L, ((WavTag) tag).getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926560L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(18L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileWithMoreMetadataSaveBoth()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", tag.getFirst(FieldKey.ALBUM_ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926700L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(10L, ((WavTag) tag).getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926700L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(18L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Delete file with Info metadata
     */
    public void testDeleteFileInfoMetadata()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123DeleteMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertTrue(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            AudioFileIO.delete(f);


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull(((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Delete file with Id3 metadata
     */
    public void testDeleteFileId3Metadata()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test126.wav", new File("test126DeleteId3Metadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            //Ease of use methods for common fields
           assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull(((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(25L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(33L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            AudioFileIO.delete(f);


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull(((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testReadFileWithID3AndListInfoMetadata()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
            assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));

            assertTrue(tag.isInfoTag());
            WavInfoTag wit = (WavInfoTag) tag.getInfoTag();
            assertEquals("id3artistName\0", wit.getFirst(FieldKey.ARTIST));
            assertEquals("id3albumName\0", wit.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", wit.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", wit.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", wit.getFirst(FieldKey.YEAR));
            assertEquals("1\0", wit.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", wit.getFirst(FieldKey.GENRE));

            assertTrue(tag.isID3Tag());
            AbstractID3v2Tag id3tag = (AbstractID3v2Tag) tag.getID3Tag();
            assertTrue(id3tag instanceof ID3v23Tag);
            assertEquals("id3artistName", id3tag.getFirst(FieldKey.ARTIST));
            assertEquals("id3albumName", id3tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123", id3tag.getFirst(FieldKey.TITLE));
            assertEquals("comment", id3tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002", id3tag.getFirst(FieldKey.YEAR));
            assertEquals("1", id3tag.getFirst(FieldKey.TRACK));
            assertEquals("rock", id3tag.getFirst(FieldKey.GENRE));


            assertEquals(926508L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926662L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(146L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(235L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(243L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Delete file with Info and ID3 metadata
     */
    public void testDeleteFileInfoAndID3Metadata()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test125.wav", new File("test125DeleteMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertTrue(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));

            AudioFileIO.delete(f);


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testWavReadOptionsHasId3AndInfo()
    {

        Exception exceptionCaught = null;
        try
        {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002", tag.getFirst(FieldKey.YEAR));
                assertEquals("1", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock", tag.getFirst(FieldKey.GENRE));
            }


            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002", tag.getFirst(FieldKey.YEAR));
                assertEquals("1", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_UNLESS_ONLY_ID3);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testWavReadOptionsHasInfoOnly()
    {

        Exception exceptionCaught = null;
        try
        {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("", tag.getFirst(FieldKey.ARTIST));
                assertEquals("", tag.getFirst(FieldKey.ALBUM));
                assertEquals("", tag.getFirst(FieldKey.TITLE));
                assertEquals("", tag.getFirst(FieldKey.COMMENT));
                assertEquals("", tag.getFirst(FieldKey.YEAR));
                assertEquals("", tag.getFirst(FieldKey.TRACK));
                assertEquals("", tag.getFirst(FieldKey.GENRE));
            }


            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_UNLESS_ONLY_ID3);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                //Ease of use methods for common fields
                assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActive()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileWithMoreMetadataSaveActive()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataSaveActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", tag.getFirst(FieldKey.ALBUM_ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926700L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveExistingActiveInfo()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveExistingActiveId3Info()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(25L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926560L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(33L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActiveId3()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, ((WavTag) tag).getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, ((WavTag) tag).getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(0L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(0L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull( ((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(25L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(33L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActiveId32()
    {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test126.wav", new File("test126ModifyMetadataSaveActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            assertFalse(tag.isExistingInfoTag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull(((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(25L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(33L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag) tag).getInfoTag());

            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            assertNull(((WavTag) tag).getInfoTag().getStartLocationInFile());
            assertNull( ((WavTag) tag).getInfoTag().getEndLocationInFile());
            assertEquals(0L, ((WavTag) tag).getInfoTag().getSizeOfTag());
            assertEquals(25L, ((WavTag) tag).getSizeOfID3TagOnly());
            assertEquals(926264L, ((WavTag) tag).getStartLocationInFileOfId3Chunk());
            assertEquals(33L, ((WavTag) tag).getSizeOfID3TagIncludingChunkHeader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}