package dev.feiyang.sereneme;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MeditationRecord {
    public int mID;
    public String mDate;
    public int mLength;
    public int mScore;

    public byte[] getBytes(){
        byte[] idBytes = ByteBuffer.allocate(4).putInt(mID).array();
        // this length refers to the time span of the meditation
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(mLength).array();
        byte[] scoreBytes = ByteBuffer.allocate(4).putInt(mScore).array();
        byte[] dateBytes = mDate.getBytes();
        return ByteBuffer.allocate(1 + idBytes.length
                + lengthBytes.length
                + scoreBytes.length
                + dateBytes.length + 1)
                .put((byte) 01)
                .put(idBytes)
                .put(lengthBytes)
                .put(scoreBytes)
                .put(dateBytes)
                .put((byte) 00)
                .array();
    }

    public static MeditationRecord getFromBytes(byte[] bytes){
        if (bytes[0] != (byte) 01 && bytes[bytes.length - 1] != (byte) 00)
            return null;

        MeditationRecord record = new MeditationRecord();
        record.mID = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 1, 5)).getInt();
        record.mLength = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 5, 9)).getInt();
        record.mScore = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 9, 13)).getInt();
        record.mDate = new String(Arrays.copyOfRange(bytes, 13, bytes.length - 1));
        return record;
    }
}
