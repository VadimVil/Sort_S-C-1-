import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EchoServer {

    public static void main(String[] args) throws IOException {

        InetAddress address = InetAddress.getLocalHost();
        String hostName = address.getHostName();
        if (args.length != 1) {
            System.err.print("Usage: java EchoServer <port>");
            System.exit(ExitCode.FAILURE.getCode());
        }
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error trying to listen on port "
                    + portNumber + " or listening to a connection");
        }

        Manager manager = new Manager();
        while (!manager.isJobDone()) {
            try {
                if (serverSocket != null) {
                    manager.addClient(serverSocket.accept());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("accept failed: " + portNumber);
                System.exit(ExitCode.FAILURE.getCode());
            }
        }

        try {
            ArrayList<String> list = manager.askForResults();
            ArrayList<Integer> fin = new ArrayList<>();
            for (String str1 : list) {
                System.out.println(str1);
                String[] items = str1.replaceAll("\\[", "")
                        .replaceAll("\\]", "")
                        .replaceAll("\\s", "").split(",");
                int[] results = new int[items.length];
                for (int l = 0; l < items.length; l++) {
                    try {
                        results[l] = Integer.parseInt(items[l]);
                    } catch (NumberFormatException nfe) {
                    }
                }

                for (int j = 0; j < 2; j++) {
                    fin.add(results[j]);
                }
            }
            System.out.println("Before Sorting:");
            for (int counter : fin) {
                System.out.println(counter);
            }
            Collections.sort(fin);
            System.out.println("After Sorting:");
            for (int counter : fin) {
                System.out.println(counter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

