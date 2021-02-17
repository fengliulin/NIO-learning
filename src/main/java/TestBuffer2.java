package main.java;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestBuffer2 {

    public static void main(String[] args) {
       String str = "adbce";
        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(str.getBytes());

        buf.flip(); // 读模式

        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        System.out.println(new String(dst, 0, dst.length));

        System.out.println(buf.position());

        // 标记
        buf.mark();

        buf.get(dst, 2, 2);
        System.out.println(new String(dst, 2, 2));
        System.out.println(buf.position());

        // reset() 恢复到mark位置
        buf.reset();
        System.out.println(buf.position());

    }
}
