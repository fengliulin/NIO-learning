package main.java;

import java.nio.ByteBuffer;

public class 直接缓冲区与非直接缓冲区 {
    public static void main(String[] args) {
        // 分配直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        System.out.println(byteBuffer.isDirect());
    }

}
