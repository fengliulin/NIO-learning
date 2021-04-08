
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class 通道Channel_直接缓冲区_内存映射 {
    public static void main(String[] args) throws IOException {

        // 1、使用直接缓冲区完成文件的复制(内存映射文件)
        FileChannel inChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);/* 不用流FileInputStream获取通道，用打开一个文件，建立一个通道传输数据 */
        FileChannel outChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        // 内存映射文件, 存放在内存中
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        // 直接对缓冲区进行数据的读写操作
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        inChannel.close();
        outChannel.close();
    }
}
