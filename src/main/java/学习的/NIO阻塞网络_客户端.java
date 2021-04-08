package 学习的;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

public class NIO阻塞网络_客户端 {
    public static void main(String[] args) throws IOException {

        // 客户端获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        // 发送一张图片给服务器
        FileChannel fileChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/2021春节合影副本.jpg"));

        // 要使用NIO，有了通道，必要要有Buffer，Buffer是在通道上传输数据的
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 读取本地文件，发送到服务器
        int read;
        while ((read = fileChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip(); // 在读之前都要切换成读模式
            socketChannel.write(byteBuffer);
            byteBuffer.clear();  // 读完切换成写模式，能让管道继续读取文件的数据
        }

        // 阻塞的要告诉服务器，发送我数据了，关闭，服务器放行
        socketChannel.shutdownOutput();

        // 客户端接收服务器发送来的数据
        int len;
        while ((len = socketChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, len));
            byteBuffer.clear();
        }



        // 关闭流
        fileChannel.close();
        socketChannel.close();
    }
}
