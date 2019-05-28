
package mioconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author pirolo_davide
 */
public class WSConsumer {
   
    private String result;
    // 
    private String prefix = "http://localhost:8080/wsProva/test/";

    WSConsumer() {
        result = "";
    }

    public String getResult() {
        return result;
    }
    
    public int put(String param){
        int status = 0;
        result = "";

        try {
            URL serverURL;
            HttpURLConnection service;
            BufferedReader input;

            String url = prefix + URLEncoder.encode(param, "UTF-8");// + "=" + URLEncoder.encode(value, "UTF-8");
            serverURL = new URL(url);
            service = (HttpURLConnection) serverURL.openConnection();
            // impostazione header richiesta
            service.setRequestProperty("Host", "localhost");
            service.setRequestProperty("Accept", "application/text");
            service.setRequestProperty("Accept-Charset", "UTF-8");
            // impostazione metodo di richiesta GET
            service.setRequestMethod("PUT");
            // attivazione ricezione
            service.setDoInput(true);
            // connessione al web-service
            service.connect();
            // verifica stato risposta
            status = service.getResponseCode();
            if (status != 200) {
                return status; // non OK
            }
            // apertura stream di ricezione da risorsa web
            input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            // ciclo di lettura da web e scrittura in result
            String line;
            while ((line = input.readLine()) != null) {
                result += line + "\n";
            }
            input.close();

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
    public int post(String param){
        int status = 0;
        result = "";

        try {
            URL serverURL;
            HttpURLConnection service;
            BufferedReader input;

            String url = prefix + URLEncoder.encode(param, "UTF-8");// + "=" + URLEncoder.encode(value, "UTF-8");
            serverURL = new URL(url);
            service = (HttpURLConnection) serverURL.openConnection();
            // impostazione header richiesta
            service.setRequestProperty("Host", "localhost");
            service.setRequestProperty("Accept", "application/text");
            service.setRequestProperty("Accept-Charset", "UTF-8");
            // impostazione metodo di richiesta GET
            service.setRequestMethod("POST");
            // attivazione ricezione
            service.setDoInput(true);
            // connessione al web-service
            service.connect();
            // verifica stato risposta
            status = service.getResponseCode();
            if (status != 200) {
                return status; // non OK
            }
            // apertura stream di ricezione da risorsa web
            input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            // ciclo di lettura da web e scrittura in result
            String line;
            while ((line = input.readLine()) != null) {
                result += line + "\n";
            }
            input.close();

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public int get(String paramater) {
        int status = 0;
        result = "";

        try {
            URL serverURL;
            HttpURLConnection service;
            BufferedReader input;

            String url = prefix + URLEncoder.encode(paramater, "UTF-8");// + "=" + URLEncoder.encode(value, "UTF-8");
            serverURL = new URL(url);
            service = (HttpURLConnection) serverURL.openConnection();
            // impostazione header richiesta
            service.setRequestProperty("Host", "localhost");
            service.setRequestProperty("Accept", "application/text");
            service.setRequestProperty("Accept-Charset", "UTF-8");
            // impostazione metodo di richiesta GET
            service.setRequestMethod("GET");
            // attivazione ricezione
            service.setDoInput(true);
            // connessione al web-service
            service.connect();
            // verifica stato risposta
            status = service.getResponseCode();
            if (status != 200) {
                return status; // non OK
            }
            // apertura stream di ricezione da risorsa web
            input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            // ciclo di lettura da web e scrittura in result
            String line;
            while ((line = input.readLine()) != null) {
                result += line + "\n";
            }
            input.close();

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    String printResult() {
        String[] arrOfStr = result.split("\",\"");
        String s="";
        for (int i = 0; i < arrOfStr.length; i++) {
            System.out.println(arrOfStr[i]);
            s+=arrOfStr[i]+"\n";
        }
        return s;
    }
}
