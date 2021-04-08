package 学习的;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIO实现浏览器请求 {
    public static void main(String[] args) throws IOException {
        new NIO实现浏览器请求().start();
    }

    public void start() throws IOException {

        // 打开服务器Socket管道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        System.out.println("启动web服务");

        // 绑定本机器端口
        serverSocketChannel.bind(new InetSocketAddress(8888));

//        ServerSocket socket = serverSocketChannel.socket();


//        socket.bind(new InetSocketAddress(8888));

        while (true) {
            // 接收监听端口来的信息
            SocketChannel channel = serverSocketChannel.accept();

            // 如果有信息，开启一个线程
            if (channel != null) {
                Thread thread = new Thread(new HttpServerThread(channel));
                thread.start();
            }
        }
    }

    private class HttpServerThread implements Runnable {

        SocketChannel channel;

        public HttpServerThread(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            if (channel != null) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                try {

                    // 获取远程客户端地址
                    InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
                    System.out.println("远程ip地址" + remoteAddress);

                    // 读取远程传输来的数据
                    channel.read(byteBuffer);
                    byteBuffer.flip();

                    while (byteBuffer.hasRemaining()) {
                        char c = (char) byteBuffer.get();
                        System.out.print(c);
                    }

                    System.out.println(Thread.currentThread().getName() + "开始向web返回消息...");

                    ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);

                    // 给客户端一个响应，即向输出流写入信息
                    String reply = "HTTP/1.1\n"; // 必须添加的响应头
                    reply += "Content-type:text/html;charset=utf-8\n\n"; // 必须添加的响应头
                    reply += "服务器返回的消息";

                    byteBuffer2.put(reply.getBytes());
                    byteBuffer2.flip();

                    channel.write(byteBuffer2);

                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}