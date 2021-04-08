package 学习的;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class NIO非阻塞网络_服务端 {
    public static void main(String[] args) throws IOException {

        // 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 切换成非阻塞模式
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.bind(new InetSocketAddress(6666));
        // 获取选择器
        Selector selector = Selector.open();

        // 将通道注册到选择器上，指定接收"监听通道"事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 轮训的获取选择器上已"就绪"的事件--》只要select>0,说明已就绪
        while (true) {
            // 调用阻塞的select,等待 selector上注册的事件发生
            selector.select();

            // 获取当前选择器所有注册的“选择键”(已就绪的监听事件)
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            // 获取已"就绪的事件"，不同的事情做不同的事
            while (iterator.hasNext()) {

                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                // 接收事件就绪
                if (selectionKey.isAcceptable()) {

                    ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();

                    // 获取客户端的连接(阻塞的)
                    SocketChannel socketChannel = server.accept();

                    if (socketChannel == null) {
                        continue;
                    }

                    // 切换成非阻塞状态
                    socketChannel.configureBlocking(false);

                    // 注册读事件（服务端一般不注册 可写事件）
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    byteBuffer.clear();
                    byteBuffer.put("和服务器建立连接".getBytes(StandardCharsets.UTF_8));
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);

                }

                // 服务端关心的可读，意味着有数据从client传来了数据
                if (selectionKey.isReadable()) { // 读就绪事件

                    // 获取当前选择器读就绪状态的通道
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

                    // 读取数据
                    // 得到文件通道，将客户端传递过来的图片写到本地项目下(写模式、没有则创建)
                    FileChannel outChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/未标题1.bmp"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
//
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//
//
//
//
                    // 将客户端传递过来的图片保存在本地
                    int read;
                    // 这里要大于0，不会-1。
                    while ((read = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        System.out.println(byteBuffer.mark());
                        outChannel.write(byteBuffer);
                        byteBuffer.clear();
                    }

                    outChannel.close();


                    // 服务器告诉客户端已经收到图片
                    byteBuffer.clear();
                    byteBuffer.put("已经收到信息".getBytes(StandardCharsets.UTF_8));
                    byteBuffer.flip();
                    while(byteBuffer.hasRemaining()){
                        //防止写缓冲区满，需要检测是否完全写入
                        System.out.println("写入数据:"+socketChannel.write(byteBuffer));
                    }
                }

                // 取消选择键

            }
        }







    }
}
