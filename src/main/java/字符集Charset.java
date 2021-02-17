package main.java;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class 字符集Charset {
    public static void main(String[] args) throws CharacterCodingException {

        // 获取系统所有字符集
        SortedMap<String, Charset> map = Charset.availableCharsets();
        for (Map.Entry<String, Charset> stringCharsetEntry : map.entrySet()) {
            System.out.println(stringCharsetEntry.getKey() + "---" + stringCharsetEntry.getValue());
        }

        // 字符集
        Charset cs1 = Charset.forName("UTF-8");

        // 获取编码器
        CharsetEncoder ce = cs1.newEncoder();   /* 实际就是字符转成 byte 字节 */

        // 获取解码器
        CharsetDecoder cd = cs1.newDecoder();   /* 实际就是比把字节转成字符 */

        CharBuffer cBuf = CharBuffer.allocate(1024);
        cBuf.put("你好你好！！！");
        cBuf.flip();

        // 编码
        ByteBuffer bBuf = ce.encode(cBuf);

        /*for (int i = 0; i < 12; i++) {
            System.out.println(bBuf.get());
        }*/

        // 解码
        bBuf.flip();
        CharBuffer cBuf2 = cd.decode(ByteBuffer.wrap(bBuf.array()));
        System.out.println(cBuf2.toString());
    }
}
