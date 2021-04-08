
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestBuffer {

    public static void main(String[] args) {
        // 1、分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("--------------allocate()------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 2、利用put()存入数据到缓冲区中
        String str = "我爱你abn";
        buf.put(str.getBytes(StandardCharsets.UTF_8));
        System.out.println();

        System.out.println("--------------put()------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 3、切换读取数据模式
        buf.flip();

        System.out.println("--------------flip()------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 4、利用get()读取缓冲区中的数据
        byte[] dst = new byte[buf.limit()];
        buf.get(dst);
        System.out.println(new String(dst, 0, dst.length));

        System.out.println("--------------get()------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 5、rewind()
        buf.rewind();

        System.out.println("--------------rewind()------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());


        // 5、clear() 清空缓冲区，但是缓冲区中的数据依然存在，但是处于被遗忘状态
        buf.clear();

        System.out.println("--------------clear()------------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

    }
}
