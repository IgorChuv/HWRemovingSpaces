import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    public static String deleteSpaces(String msg) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] withoutSpaces = msg.split(" ");
        for (String word : withoutSpaces) {
            stringBuilder.append(word);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 3552));
        while(true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while(socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1)
                        break;
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    socketChannel.write(ByteBuffer.wrap((deleteSpaces(msg)).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException err) {
                err.getStackTrace();
            } finally {
                serverChannel.close();
            }
        }
    }
}