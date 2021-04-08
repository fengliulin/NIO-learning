package 学习的;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 使用内存映射文件的方式实现文件复制的功能(直接操作缓冲区)：
 */
public class Transfer实现传输 {
    public static void main(String[] args) throws IOException {

        FileChannel inChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/2021春节合影副本.jpg"), StandardOpenOption.READ);

        OpenOption[] openOptions = {StandardOpenOption.WRITE, StandardOpenOption.CREATE};
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/2021春节合影副本1.jpg"), openOptions);

       inChannel.transferTo(0, inChannel.size(), outChannel);

       inChannel.close();
       outChannel.close();
    }
}