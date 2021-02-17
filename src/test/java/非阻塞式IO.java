import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class 非阻塞式IO {

    @Test
    public void client() throws IOException {
        // 1、获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 2、切换非阻塞模式
        sChannel.configureBlocking(false);

        // 3、分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 4、发送数据给服务器
        buf.put(new Date().toString().getBytes());
        buf.flip();
        sChannel.write(buf);
        buf.clear();

        // 5、关闭通道
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        // 1、获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2、切换非阻塞模式
        ssChannel.configureBlocking(false);

        // 3、绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        // 4、获取选择器
        Selector selector = Selector.open();

        // 5、将通道注册到选择器上，并且指定"监听接收事件"
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6、轮询式的获取选择器上已经"准备就绪"的事件
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                // 获取准备就绪的事件
                SelectionKey sk = iterator.next();

                // 判断具体什么事件准备就绪
                if (sk.isAcceptable()) {
                    SocketChannel sChannel = ssChannel.accept();    /* 获取客户端连接 */

                    // 切换非阻塞模式
                    sChannel.configureBlocking(false);

                    // 将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                }else if(sk.isReadable()) {
                    // 获取当前选择器上"读就绪"状态的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();

                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }

                //取消选择键 SelectionKey
                iterator.remove();
            }
        }
    }
}
