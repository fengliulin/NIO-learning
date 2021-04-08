package 学习的;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class NIO非阻塞_UDP接收方 {
    public static void main(String[] args) throws IOException {
        DatagramChannel dc = DatagramChannel.open();
        dc.bind(new InetSocketAddress(9898));
        dc.configureBlocking(false);

        Selector selector = Selector.open();
        dc.register(selector, SelectionKey.OP_READ);

        while (true) {
            selector.select();

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    dc.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            }

        }
    }
}
