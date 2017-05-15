import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static final int ARRAY_SIZE = 10;
    private static final int CLIENTS_NUMBER_NEEDED = 5;

    private static List<Socket> clients = new ArrayList<>();
    ArrayList<Integer> array = new ArrayList<>();

    private boolean jobDone = false;

    public Manager() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        for (int i = 0; i < ARRAY_SIZE; i++) {
            String s;
            try {
                System.out.print("> ");
                s = reader.readLine();
                if (s.isEmpty()) {
                    break;
                }
                array.add(Integer.parseInt(s));
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Incorrect input.");
                break;
            }
        }
    }

    public void addClient(Socket client) {
        clients.add(client);
        tryToStartSort();
    }

    private void tryToStartSort() {
        if (clients.size() < CLIENTS_NUMBER_NEEDED) {
            return;
        }
        jobDone = false;
        try {
            System.out.println("Greater than " + CLIENTS_NUMBER_NEEDED + " so start");
            int amount = clients.size();
            int onePart = array.size() / amount;
            int pointer = 0;
            for (Socket client : clients) {
                ArrayList<Integer> newArray = new ArrayList<>(array.subList(pointer, pointer + onePart));
                pointer += onePart;
                PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
                printWriter.println(newArray);
            }
            System.out.println("finita");
            jobDone = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> askForResults() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for(Socket client : clients) {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(client.getInputStream()));
            list.add(in.readLine());
        }
        return list;
    }

    public boolean isJobDone() {
        return jobDone;
    }
}
