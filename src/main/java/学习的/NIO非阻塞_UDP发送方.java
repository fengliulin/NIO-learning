package 学习的;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

public class NIO非阻塞_UDP发送方 {
    public static void main(String[] args) throws IOException {
        DatagramChannel dc = DatagramChannel.open();
        dc.configureBlocking(false);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            String str = scan.next();
            buf.put((new Date().toString() + ":\n" + str).getBytes(StandardCharsets.UTF_8));
            buf.flip();
            dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
            buf.clear();
        }

        dc.close();
    }
}
