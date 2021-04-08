package 学习的;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.util.Iterator;

public class NIO非阻塞网络_客户端 {
    public static void main(String[] args) throws IOException {
        /*new Thread(()->{
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
            String s = null;
            try {
                s = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(s);
        }).start();*/

        // 客户端获取通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));



        // 发送一张图片给服务器
        FileChannel fileChannel = FileChannel.open(Paths.get("/Users/fengliulin/Documents/未标题.bmp"));

        // 要使用NIO，有了通道，必要要有Buffer，Buffer是在通道上传输数据的
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 读取本地文件，发送到服务器
        int read;
        while ((read = fileChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip(); // 在读之前都要切换成读模式
            socketChannel.write(byteBuffer);
            byteBuffer.clear();  // 读完切换成写模式，能让管道继续读取文件的数据
        }

        fileChannel.close();

        // 切换成非阻塞的模式
        socketChannel.configureBlocking(false);

        // 获取选择器
        Selector selector = Selector.open();

        // 将通道注册到选择器中，获取服务端返回的数据
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 在客户端上要想获取得到服务端的数据，也需要注册在register上(监听读事件)！
        while (true) {
            selector.select();

            // 获取当前选择器所有注册的选择键（已就绪的监听事件）
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            // 获取已就绪的事件(不同事件做不同的事)
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                // 读事件就绪
                if (selectionKey.isReadable()) {
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
                    // 得到对应的通道
                    SocketChannel socket = (SocketChannel)selectionKey.channel();

                    ByteBuffer responseBuffer = ByteBuffer.allocate(1024);

                    // 知道服务端要返回响应的数据给客户端，客户端在这里接收
                    socket.read(responseBuffer);
                    responseBuffer.flip();
                    String s = new String(responseBuffer.array());
                    System.out.println(s);
                }
                // 取消选择键(已经处理过的事件，就应该取消掉了)

            }
        }
    }
}
