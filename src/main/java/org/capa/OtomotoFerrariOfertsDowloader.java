package org.capa;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class OtomotoFerrariOfertsDowloader {
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(15);

            ArrayList<String> ferrariLinks = readLinksFromWebsite("https://www.otomoto.pl/osobowe/ferrari");


            ferrariLinks = new ArrayList(new HashSet(ferrariLinks));
            AtomicInteger itr= new AtomicInteger(1);

        for (int i = 0; i < ferrariLinks.size(); i++) {
            int finalI = i;
            ArrayList<String> finalFerrariLinks = ferrariLinks;
            executorService.submit(() -> {
                        try {
                            createHtmlFiles(finalFerrariLinks.toArray()[finalI].toString(),finalI + ".html");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        executorService.shutdown();
    }

    public static ArrayList<String> readLinksFromWebsite(String webUrl) throws IOException {
        URL url = new URL(webUrl);
        BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputText;
        StringBuilder stringBuilder = new StringBuilder();

        while ((inputText = input.readLine()) != null) {
            stringBuilder.append(inputText);
            stringBuilder.append(System.lineSeparator());
        }
        input.close();
        List<String> links = new ArrayList<String>();
        String fullPage = stringBuilder.toString();

        for(int i=0; i<fullPage.length();i++){
            int foundLinkIndex = fullPage.indexOf("https://www.otomoto.pl/oferta/",i);
            if(foundLinkIndex < 0) break;
            String link = fullPage.substring(foundLinkIndex).split(".html")[0];
            link+=".html";
            links.add(link);

            i=foundLinkIndex;
        }
        return (ArrayList<String>) links;
    }

    public static void createHtmlFiles(String link, String fileName) throws IOException {
        URL otomoto = new URL(link);
        BufferedReader in = new BufferedReader(new InputStreamReader(otomoto.openStream()));

        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        in.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,false));
        bw.write(stringBuilder.toString());
        bw.close();
    }
}
