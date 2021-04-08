package 学习的;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class 容量限制位置标记 {
    public static void main(String[] args) {
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("容量：" + byteBuffer.capacity());
        System.out.println("限制：" + byteBuffer.limit());
        System.out.println("位置：" + byteBuffer.position());
        System.out.println("标记：" + byteBuffer.mark());

        System.out.println("---------");

        // 添加了数据，位置就变了，变成了6
        // 容量不变，限制不变
        String s = "Java3y";
        byteBuffer.put(s.getBytes(StandardCharsets.UTF_8));

        System.out.println("容量：" + byteBuffer.capacity());
        System.out.println("限制：" + byteBuffer.limit());
        System.out.println("位置：" + byteBuffer.position());
        System.out.println("标记：" + byteBuffer.mark());

        System.out.println("-------------------");

        // 执行这个，容量不变
        // 位置变为0， 限制变为位置的6
        byteBuffer.flip();

        /*
         * limit变成了position的位置了，而position变成了0
         *
         * 当调用完flip()时：limit是限制读到哪里， 就会把位置放到限制上
         *
         * 而position是从哪里读一般我们称flip()为**“切换成读模式”**
         *
         * 每当要从缓存区的时候读取数据时，就调用flip()“切换成读模式”。
         */
        System.out.println("容量：" + byteBuffer.capacity());
        System.out.println("限制：" + byteBuffer.limit());
        System.out.println("位置：" + byteBuffer.position());
        System.out.println("标记：" + byteBuffer.mark());

        System.out.println("---------------------从缓冲区读数据------------------------");

        // 创建一个limit()大小的字节数组(因为就只有limit这么多个数据可读)
        byte[] bytes = new byte[byteBuffer.limit()];

        // get也会改变： 位置变成限制， 其他不变。
        /*
         * 容量：1024
         * 限制：6
         * 位置：6
         */
        byteBuffer.get(bytes); // 读取缓冲区数据放到bytes里
        System.out.println("容量：" + byteBuffer.capacity());
        System.out.println("限制：" + byteBuffer.limit());
        System.out.println("位置：" + byteBuffer.position());
        System.out.println("标记：" + byteBuffer.mark());

        System.out.println(new String(bytes, 0, bytes.length));

        byteBuffer.clear(); // 清除不清缓存区内容，把位置置为0，把限制置为容量
//        byteBuffer.position(6); // 设置没有数据的位置开始put，如果不设置直接put，因为位置为0，就覆盖了

        byteBuffer.put("张三".getBytes(StandardCharsets.UTF_8));


        System.out.println("---------------------从缓冲区读数据1------------------------");

        byteBuffer.flip();

        // 创建一个limit()大小的字节数组(因为就只有limit这么多个数据可读)
        byte[] bytes1 = new byte[byteBuffer.limit()];

        byteBuffer.get(bytes1); // 读取缓冲区数据放到bytes里

        System.out.println(new String(bytes1, 0, bytes1.length));

    }
}
