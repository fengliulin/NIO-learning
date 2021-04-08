package 学习的;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 使用内存映射文件的方式实现文件复制的功能(直接操作缓冲区)：
 */
public class 内存映射NIO使用直接缓冲区完成文件复制 {
    public static void main(String[] args) throws IOException {

        FileChannel inChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/2021春节合影副本1.jpg"), StandardOpenOption.READ);

        OpenOption[] openOptions = {StandardOpenOption.WRITE, StandardOpenOption.CREATE};
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/2021春节合影副本1.jpg"), openOptions);

        // 内存映射文件
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapperBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        // 直接对缓冲区进行数据读写操作
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMapperBuf.put(dst);
    }
}