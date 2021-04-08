package 学习的;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServletHttp {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);

            while (true) {

                Socket accept = serverSocket.accept();


                try {
                    InputStream inputStream = accept.getInputStream();
                    InputStreamReader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(in);

                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    // 模拟浏览器直接请求过来获取数据
                    // 但是如果没有数据，在网络的情况下会阻塞，这里就一致死循环， 怎么解决
                    while((line = br.readLine()) != null) {
                        sb.append(line).append("\r\n");
                        // 根据读取浏览器请求头最后就是"" 为结束，如果在执行下次循环就阻塞了，这里判断退出结束阻塞
                        if (line.equals("")) {
                            break;
                        }
                    }
                    System.out.println(sb.toString());

                    OutputStream os = accept.getOutputStream();

                    // 没有这个响应行，浏览器是不现实任何信息
                    os.write("HTTP/1.1 200 OK\r\n".getBytes());

                    //响应头内容
                    os.write("Content‐Type:text/html\r\n".getBytes());

                    // 响应头和响应体之间要有换行
                    os.write("\r\n".getBytes());
                    os.write("浏览器显示内容ok".getBytes(StandardCharsets.UTF_8));

                    System.out.println("完成");

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    accept.close();
                }
            }


//        bufferedReader.close();
//        inputStreamReader.close();
//        inputStream.close();
//        accept.close();
//        serverSocket.close();
    }
}
