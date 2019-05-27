/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mioconsumer;

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
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner i=new Scanner(System.in);
        
        while(true){
            System.out.print("inserisci la targa:");
            String targa=i.next();
            System.out.print("inserisci la data di scadenza assicurazione:");
            Date ass=new Date(i.next());
            System.out.print("inserisci la data di scadenza bollo:");
            Date bollo=new Date(i.next());
            System.out.print("inserisci la classe di inquinamento:");
            int classe=i.nextInt();
            System.out.print("inserisci true se ricercata o false se non lo è:");
            boolean ricercata=i.nextBoolean();
            
            String s="{targa: "+targa+", scadenzaAssicurazione: "+ass+", scadenzaBollo: "+bollo+", classeInquinamento: "+classe+", ricercata: "+ricercata+" }";
            
            System.out.println(s);
            
            ws.put(s);
            ws.printResult();
        }
        
        
    }
    
}
