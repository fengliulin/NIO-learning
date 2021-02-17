package main.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class 阻塞式_客户端 {




    public static void main(String[] args) throws IOException {
        // 1、获取通道
        SocketChannel sChanel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        FileChannel inChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);

        // 2、分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 3、读取本地文件，并发送到服务端
        while (inChannel.read(buf) != -1) {
            buf.flip();
            sChanel.write(buf);
            buf.clear();
        }

        // 3、关闭通道
        inChannel.close();
        sChanel.close();

    }
}
