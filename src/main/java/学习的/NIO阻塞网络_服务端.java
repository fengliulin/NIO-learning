package 学习的;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NIO阻塞网络_服务端 {
    public static void main(String[] args) throws IOException {

        // 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(6666));

        // 得到文件通道，将客户端传递过来的图片写到本地项目下(写模式、没有则创建)
        FileChannel outChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/2021春节合影副本1.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 获取客户端的连接(阻塞的)
        SocketChannel socketChannel = serverSocketChannel.accept();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将客户端传递过来的图片保存在本地
        int read;
        // 客户端不告诉服务器，传输完毕数据，那么这里读取到最后，就会变成阻塞
        while ((read = socketChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            outChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        // 服务器告诉客户端已经收到图片
        byteBuffer.put("已经收到信息".getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        byteBuffer.clear();

        // 关闭通道
        outChannel.close();
        socketChannel.close();
        serverSocketChannel.close();
    }
}
