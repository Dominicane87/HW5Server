package vladimir.gorin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket soc = new Socket("localhost", 39877);

        BufferedReader br =
                new BufferedReader(new InputStreamReader(soc.getInputStream()));
        BufferedWriter bw =
                new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
        BufferedReader cbr =
                new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Type your msg: ");
            String input = cbr.readLine();

            bw.write(input + "\n");
            bw.flush();
            if (input.contains("exit")) {
                break;
            }

            String line = br.readLine();
            String[] split = line.split(";");
            for (String s : split) {
                System.out.println(s);
            }

        }
        br.close();
        bw.close();
        soc.close();
        System.out.println("Client finish");

    }

}

