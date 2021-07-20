import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final int timeOut = 2000;

    public static void main(String[] args) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 3552);
        final SocketChannel socketChannel = SocketChannel.open();
        try (Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(socketAddress);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            while(true) {
                System.out.println("Введите строки с пробелами:");
                msg = scanner.nextLine();
                if ("end".equals(msg)) break;
                socketChannel.write(
                        ByteBuffer.wrap(
                                msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(timeOut);
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount,
                        StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}
