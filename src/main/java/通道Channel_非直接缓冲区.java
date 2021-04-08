

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class 通道Channel_非直接缓冲区 {
    public static void main(String[] args) throws IOException {
        // 1、利用通道完成文件的复制（非直接缓冲区）
        FileInputStream fis = new FileInputStream("1.png");
        FileOutputStream fos = new FileOutputStream("2.png");

        // 获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        // 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 将通道中的数据存入缓冲区中
        while (inChannel.read(buf) != -1) {
            buf.flip(); // 切换读取数据的模式

            // 将缓冲区中的数据写入通道
            outChannel.write(buf);
            buf.clear(); // 清空缓冲区
        }

        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
    }
}
