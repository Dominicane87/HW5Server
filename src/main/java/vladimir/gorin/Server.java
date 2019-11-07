package vladimir.gorin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(39877);
        System.out.println("Got port");
        while (true) {

            Socket soc = ss.accept();

            System.out.println("Client exist!");

            InputStream is = soc.getInputStream();
            OutputStream os = soc.getOutputStream();

            InputStreamReader isr = new InputStreamReader(is);
            OutputStreamWriter osw = new OutputStreamWriter(os);

            BufferedReader br = new BufferedReader(isr);
            BufferedWriter bw = new BufferedWriter(osw);

            while (soc.isConnected()) {
                String line = null;
                try {
                    line = br.readLine();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                if (line.equals("GET")) {
                    try {
                        //files directory
                        String absolutePath = new File(".").getAbsolutePath();
                        File file = new File(absolutePath);
                        File[] files = file.listFiles();

                        StringBuilder stringBuilder = new StringBuilder();
                        for (File file1 : files) {
                            stringBuilder.append(file1.getName());
                            stringBuilder.append(";");
                        }
                        bw.write(stringBuilder.toString());
                        bw.newLine();
                        bw.flush();


                    } catch (SocketException ex) {
                        ex.printStackTrace();
                        break;
                    }
                } else {
                    bw.write("404");
                    bw.newLine();
                    bw.flush();
                }

            }

            bw.close();
            br.close();
            soc.close();
        }
    }
}
