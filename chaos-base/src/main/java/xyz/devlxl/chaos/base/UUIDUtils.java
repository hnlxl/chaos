package xyz.devlxl.chaos.base;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * JDK内置UUID的工具
 * 
 * @author Liu Xiaolei
 * @date 2018/07/21
 */
public class UUIDUtils {
    public static UUID asUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
