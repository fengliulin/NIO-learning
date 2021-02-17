package main.java;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class 通道之间的数据传输 {
    public static void main(String[] args) throws IOException {
        // main.java.通道之间的数据传输(直接缓冲区)
        FileChannel inChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);/* 不用流FileInputStream获取通道，用打开一个文件，建立一个通道传输数据 */
        FileChannel outChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

//        inChannel.transferTo(0, inChannel.size(), outChannel);  我这个通道的字节去哪个通道里

        outChannel.transferFrom(inChannel, 0, inChannel.size()); // 从哪个通道获取字节到本通道

        inChannel.close();
        outChannel.close();
    }
}
