package webserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import libcore.util.ClassPathUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebServerRun {
	protected final static Logger logger = LogManager.getLogger(WebServerRun.class);

	static Server server;
	static WebAppContext context;

	public static void start() {

		String webPath = ClassPathUtil.webPath();
		System.setProperty("WebappPath", webPath);
		// System.out.println(JSON.toJSONString(JSON.toJSON(System.getProperties()),
		// true) );

		server = new Server(8082);
		context = new WebAppContext();
		context.setDescriptor(webPath + "/WEB-INF/web.xml");
		context.setResourceBase(webPath);
		context.setContextPath("/");
		//context.setDisplayName("fucai");
		context.setConfigurationDiscovered(true);
		context.setParentLoaderPriority(true);
		//context.setClassLoader(Thread.currentThread().getContextClassLoader());

		server.setHandler(context);

		try {
			server.start();
			// server.join();
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	public static void stop() {
		try {
			server.stop();
			server.join();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				try {
					WebServerRunManager server = new WebServerRunManager();
					server.init();
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}.start();

		start();

	}

}

class WebServerRunManager {
	public static final int PORT = 18888;// 监听的端口号

//	public static void main(String[] args) {
//		System.out.println("服务器启动...\n");
//		WebServerRunManager server = new WebServerRunManager();
//		server.init();
//	}

	public void init() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				// 一旦有堵塞, 则表示服务器与客户端获得了连接
				Socket client = serverSocket.accept();
				System.out.println("client:"+client.getRemoteSocketAddress());
				// 处理这次连接
				new HandlerThread(client);
			}
		} catch (Exception e) {
			System.out.println("服务器异常: " + e.getMessage());
		}
	}

	private class HandlerThread implements Runnable {
		private Socket socket;

		public HandlerThread(Socket client) {
			socket = client;
			new Thread(this).start();
		}

		public void run() {
			try {
				// 读取客户端数据
				DataInputStream input = new DataInputStream(socket.getInputStream());
				String clientInputStr = input.readUTF();// 这里要注意和客户端输出流的写方法对应,否则会抛
				// 处理客户端数据
				System.out.println("客户端发过来的内容:" + clientInputStr);

				// 向客户端回复信息
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				String s = "~~~";
				out.writeUTF(s);

				out.close();
				input.close();
			} catch (Exception e) {
				System.out.println("服务器 run 异常: " + e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("服务端 finally 异常:" + e.getMessage());
					}
				}
			}
		}
	}
}
