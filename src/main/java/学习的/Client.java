package 学习的;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Client{

    private final ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
    private final ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
    private Selector selector;
    private SocketChannel socketChannel;

    public Client()throws IOException {
        this.socketChannel = SocketChannel.open();
        this.socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(),10000));
        this.socketChannel.configureBlocking(false);
        System.out.println("连接建立成功");
        this.selector=Selector.open();
        this.socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws Exception{
        final Client client=new Client();
        Thread sendMsg=new Thread(client::sendInputMsg);
        sendMsg.start();

        client.start();
    }

    private void start()throws IOException {
        while (selector.select() > 0 ){

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                it.remove();

                if (key.isReadable()) {
                    System.out.println("isReadable");
                    receive(key);
                }

            }

        }
    }

    /**
     * 接收服务端发送的内容
     * @param key
     * @throws IOException
     */
    private void receive(SelectionKey key)throws IOException{
        SocketChannel socketChannel=(SocketChannel)key.channel();
        socketChannel.read(receiveBuffer);
        receiveBuffer.flip();
        String receiveData= Charset.forName("UTF-8").decode(receiveBuffer).toString();

        System.out.println("receive server message:"+receiveData);
        receiveBuffer.clear();
    }

    /**
     * 发送控制台输入内容至服务器
     */
    private void sendInputMsg() {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            String msg;
            while ((msg = bufferedReader.readLine()) != null){
                synchronized(sendBuffer){
                    sendBuffer.put((msg+"\r\n").getBytes());
                    sendBuffer.flip();
                    while(sendBuffer.hasRemaining()){
                        socketChannel.write(sendBuffer);
                    }
                    sendBuffer.compact();

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}