package dev.feiyang.sereneme.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;

import java.nio.ByteBuffer;
import java.util.Arrays;

@Entity(tableName = "MEDITATION_RECORDS")
public class MeditationRecord{
    @PrimaryKey
    public long mID;

    @ColumnInfo(name = "Date")
    public String mDate;

    @ColumnInfo(name = "Length")
    public int mLength;

    @ColumnInfo(name = "Score")
    public int mScore;

    public byte[] getBytes(){
        byte[] idBytes = ByteBuffer.allocate(8).putLong(mID).array();
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
        record.mID = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 1, 9)).getLong();
        record.mLength = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 9, 13)).getInt();
        record.mScore = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 13, 17)).getInt();
        record.mDate = new String(Arrays.copyOfRange(bytes, 17, bytes.length - 1));
        return record;
    }
}
