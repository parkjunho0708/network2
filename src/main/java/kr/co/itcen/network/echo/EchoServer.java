package kr.co.itcen.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

// XShell5
// [c:\~]$ telnet 192.168.246.1 8000
public class EchoServer {
	private static final int PORT = 8000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		// 1. 서버소켓 생성
		try {
			serverSocket = new ServerSocket();

			// 2. Binding : Socket에 SocketAddress(IPAddress + Port) 바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();

			// InetSocketAddress.InetSocketAddress(String hostname, int port)
			InetSocketAddress inetSocketAddress = new InetSocketAddress(localhostAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			log("binding " + inetAddress.getHostAddress() + " : " + PORT);

			// 3. accept : 클라이언트로 부터 연결요청(Connect)을 기다린다.
			// accept() 를 빠져나오기 위해서는 무조건 kill process를 해야 한다.
			while (true) {
				Socket socket = serverSocket.accept(); // Blocking <- 찌르면 실행됨.
				new EchoServerReceiveThread(socket).start();
			}
			
		} catch (IOException e) { // 이 catch문은 server socket의 Exception을 위해 존재함.
			e.printStackTrace();
		} finally {
			// 8. Server Socket 자원정리
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(String log) {
		System.out.println("[Echo Server#" + Thread.currentThread().getId() + "] " + log);
	}
}
