import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        String hostName = address.getHostName();
        if (args.length != 1) {
            System.err.print("Usage: java EchoClient <port>");
            System.exit(ExitCode.FAILURE.getCode());
        }
        int portNumber = Integer.parseInt(args[0]);

        try (
                Socket echoSocket =
                        new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))
        ) {
            String string = in.readLine();
            List<String> unsortedInput;
            List<Integer> output = new ArrayList<>();
            if (string != null) {
                string = string.replace("[", "");
                string = string.replace("]", "");
                unsortedInput = Arrays.asList(string.split("\\s*,\\s*"));
                for (String str : unsortedInput) {
                    output.add(Integer.parseInt(str));
                }
                output.sort((object1, object2) -> object1 < object2 ? -1 : (object1 > object2 ? 1 : 0));
            }
            out.write(String.valueOf(output));
            out.flush();
            System.out.println(output); // TODO: Send to server
            System.err.println("Sorted successfully");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("Doesn't know about the host " + hostName);
            System.exit(ExitCode.FAILURE.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(ExitCode.FAILURE.getCode());
        }
        System.exit(ExitCode.SUCCESS.getCode());
    }
}