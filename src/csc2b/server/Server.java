package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server
{
	private ServerSocket serverSocket;
	private Socket socket;
	
	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started in port "+ port);
			while(true) {
				socket = serverSocket.accept();
				System.out.println("Client connected to port " + socket.getPort());
				BUKAHandler handler = new BUKAHandler(socket);
				Thread thread = new Thread(handler);
				thread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
    public static void main(String[] argv)
    {
	//Setup server socket and pass on handling of request
    	Server server = new Server(2018);
    }
}
