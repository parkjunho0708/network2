package kr.co.itcen.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread{
	private Socket socket; // Blocking <- 찌르면 실행됨
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		EchoServer.log("connected from client[" 
				+ inetRemoteSocketAddress.getAddress().getHostAddress() 
				+ " : " + inetRemoteSocketAddress.getPort() + "]");

		try {
			// 4. I/OStream 생성
			// socket.close() 를 하면 그 안에 있는 input & output stream은 자동으로 닫힘.
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			while (true) {
				// 5. 데이터 읽기(수신)
				String data = br.readLine();
				
				if(data == null) {
					EchoServer.log("closed by client");
					break;
				}
				EchoServer.log("received : " + data);

				// 6. 데이터 쓰기(송신)
				pw.println(data);
			}
		} catch (SocketException e) { // 통신과 관련된 socket에 대한 처리
			// 비정상적으로 종료됨. (TCP의 4way hand shake 방법이 아닌 방법으로 종료됨.)
			EchoServer.log("abnormal closed by client");
		} catch (IOException e) { // 통신과 관련된 socket에 대한 처리
			e.printStackTrace();
		} finally {
			// 7. Socket 자원정리
			if (socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
