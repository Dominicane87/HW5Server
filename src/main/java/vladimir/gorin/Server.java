package vladimir.gorin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;


public class Server {

    public static void main(String[] args) {
        String body;
        String header;
        StringBuilder line;
        String text;

        while (true){
            try (ServerSocket ss = new ServerSocket(39877);
                 Socket soc = ss.accept()){
                try (
                        BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()))) {
                    System.out.println("Got port");
                    System.out.println("Client exist!");
                    line = new StringBuilder();

                    while ((!(text = Optional.ofNullable(br.readLine()).orElse("")).equals(""))) {
                        line.append(text);
                    }

                    if (line.indexOf("GET") != -1) {
                        body = getBody();
                        header = getHTTPHeader(body.length(), true);
                        bw.write(header + body);
                        bw.flush();
                    } else {
                        bw.write(getHTTPHeader(0, false));
                        bw.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static List<String> getListFiles() {
        List<String> arr =(List<String>)Collections.EMPTY_LIST;
        String absolutePath = new File(".").getAbsolutePath();
        File file = new File(absolutePath);
        File[] files = file.listFiles();
        return files==null?arr:Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    private static String getBody() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html><body><h1>Hello ").append(System.getProperty("user.name")).append("! List of files in the directory<ul>\n");
        for (String str : getListFiles()) {
            stringBuilder.append(" <li>").append(str).append("</li>\n");
        }
        stringBuilder.append("</ul></h1></body></html>");
        return stringBuilder.toString();
    }

    private static String getHTTPHeader(int length, boolean isOk) {
        String stOK = "HTTP/1.1 200 OK";
        String stNotOK = "HTTP/1.1 404 Not Found";

        return (isOk ? stOK : stNotOK) + "\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n";
    }

}
