package vladimir.gorin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
            String body;
            String header;


//            while (soc.isConnected()) {
                StringBuilder line = new StringBuilder();
                String text;
                try {

                   while ((!(text=Optional.ofNullable(br.readLine()).orElse("")).equals(""))){
                       line.append(text);
                   }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
                if (line.indexOf("GET")!=-1) {
                    try {
                        //files directory
                        body=getBody();
                        header=getHTTPHeader(body.length(),true);
                        bw.write(header+body);
                        bw.newLine();
                        bw.flush();


                    } catch (SocketException ex) {
                        ex.printStackTrace();
                        break;
                    }
                } else {
                    bw.write(getHTTPHeader(0,false));
                    bw.flush();
                }

//           }

            bw.close();
            br.close();
            soc.close();
        }
    }
    private static List<String> getListFiles(){
        ArrayList<String> arr = new ArrayList<>();
        String absolutePath = new File(".").getAbsolutePath();
        File file = new File(absolutePath);
        File[] files = file.listFiles();
        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    private static String getBody(){
        StringBuilder stringBuilder=new StringBuilder();

        stringBuilder.append("<html><body><h1>Hello ").append(System.getProperty("user.name")).append("! List of files in the directory<ul>\n");
        for (String str: getListFiles()) {
            stringBuilder.append(" <li>").append(str).append("</li>\n");
        }
        stringBuilder.append("</ul></h1></body></html>");
        return stringBuilder.toString();
    }

    private static String getHTTPHeader(int length, boolean isOk){
        String stOK = "HTTP/1.1 200 OK";
        String stNotOK = "HTTP/1.1 404 Not Found";

        return (isOk?stOK:stNotOK)+ "\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n";
    }

}
