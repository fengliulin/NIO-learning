import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class UDP传输DatagramChannel {

    @Test
    public void send() throws IOException {
        DatagramChannel dc = DatagramChannel.open(); // 打开通道
        dc.configureBlocking(false);    // 切换非阻塞
        ByteBuffer buf = ByteBuffer.allocate(1024); // 创建缓冲区，好存放数据
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            String str = scan.next();
            buf.put((new Date().toString() + ":\n" + str).getBytes(StandardCharsets.UTF_8)); // 给缓冲区存放数据
            buf.flip();
            dc.send(buf, new InetSocketAddress("127.0.0.1", 9898)); // 把缓冲区的数据发送到目标主机监听的端口上
            buf.clear();
        }

        dc.close();
    }

    @Test
    public void receive() throws IOException {
        DatagramChannel dc = DatagramChannel.open(); // 打开通道
        dc.configureBlocking(false);    // 切换非阻塞
        dc.bind(new InetSocketAddress(9898)); // 监听本机端口

        Selector selector = Selector.open(); // 打开选择器

        dc.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();

                if (sk.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    dc.receive(buf); // 接收的数据放入，分配的缓冲区
                    buf.flip();

                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            }

            it.remove();
        }

        dc.close();
    }
}
