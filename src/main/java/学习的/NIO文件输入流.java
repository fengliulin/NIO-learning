package 学习的;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIO文件输入流 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/fengliulin/Documents/2021春节合影副本.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/fengliulin/Documents/2021春节合影副本1.jpg");

        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 分配指定大小缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int read;
        while ((read = inputStreamChannel.read(buf)) != -1) {
            buf.flip(); // 设置 位置0 到limit位置
            System.out.println(buf.mark());
            outputStreamChannel.write(buf);
            buf.clear(); // 重置缓冲区
//            System.out.println(buf.mark());
        }

        System.out.println();
    }
}