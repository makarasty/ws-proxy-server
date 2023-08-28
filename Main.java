import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
	public static void main(String[] args) {
		Properties config = loadConfig();
		if (config == null) {
			System.err.println("Failed to load config file.");
			return;
		}

		int port = Integer.parseInt(config.getProperty("proxyPort"));
		String targetAddress = config.getProperty("targetAddress");
		int targetPort = Integer.parseInt(config.getProperty("targetPort"));

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Proxy server started on port " + port);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(() -> handleClient(clientSocket, targetAddress, targetPort)).start();
			}
		} catch (IOException e) {
			System.err.println("Error occurred: " + e.getMessage());
		}
	}

	private static Properties loadConfig() {
		Path configPath = Path.of("config.properties");
		Properties config = new Properties();
		try (InputStream is = Files.newInputStream(configPath)) {
			config.load(is);
			return config;
		} catch (IOException e) {
			System.err.println("Failed to load config file: " + e.getMessage());
			return null;
		}
	}

	private static void handleClient(Socket clientSocket, String targetAddress, int targetPort) {
		System.out
				.println("Ping, from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
		try (Socket targetSocket = new Socket(targetAddress, targetPort)) {
			System.out.println("Incoming connection from " + clientSocket.getInetAddress().getHostAddress() + ":"
					+ clientSocket.getPort());

			Thread forwardThread = new Thread(() -> forwardData(clientSocket, targetSocket));
			forwardThread.start();

			forwardData(targetSocket, clientSocket);

			forwardThread.join();
		} catch (IOException | InterruptedException e) {
			System.err.println("Error occurred during connection handling: " + e.getMessage());
		} finally {
			System.out.println(
					"Closing, from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
			closeQuietly(clientSocket);
		}
	}

	private static void forwardData(Socket source, Socket destination) {
		byte[] buffer = new byte[8192];
		int bytesRead;

		try (InputStream input = source.getInputStream();
				OutputStream output = destination.getOutputStream()) {

			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			System.err.println("Error occurred during data forwarding: " + e.getMessage());
		} finally {
			closeQuietly(destination);
		}
	}

	private static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			System.err.println("Error occurred during closing: " + e.getMessage());
		}
	}
}
