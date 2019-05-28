/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mioconsumer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author pirolo_davide
 */
public class MioConsumer {

    
    private static WSConsumer ws=new WSConsumer();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        // TODO code application logic here
        Scanner i=new Scanner(System.in);
        
        while(true){
            String comando="";
            System.out.print("quale operazione vuoi fare?");
            int operazione=i.nextInt();
            
            switch(operazione){
                case 1:
                    System.out.print("inserisci il nome da cercare:");
                    String comune=i.next();
                    comando="ricerca";                    
                    ws.get(comando,comune);
                    ws.printResult();
                    break;
                case 2:
                    System.out.print("inserisci il nome da aggiornare:");
                    String daAggiornare=i.next();
                    System.out.print("inserisci gli abitanti:");
                    int abitanti=i.nextInt();
                    String s="{'comune': '"+daAggiornare+"', 'abitanti': '"+abitanti+"'}";
                    ws.post(s);
                    ws.printResult();
                    break;
                case 3:
                    System.out.print("inserisci la provincia da visualizzare:");
                    String provincia=i.next();
                    comando="provincia";
                    ws.get(comando,provincia);
                    ws.printResult();
                    break;
                case 4:
                    System.out.print("inserisci la regione per visualizzare le provincie:");
                    String regione=i.next();
                    comando="regione";
                    ws.get(comando,regione);
                    ws.printResult();
                    break;
                    
                    
            }
            
            
            
            
        }
        
        
    }
    
}
