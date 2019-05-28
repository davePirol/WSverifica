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
            System.out.print("inserisci il nome da cercare:");
            String daCercare=i.next();
            
            ws.put(daCercare);
            ws.printResult();
        }
        
        
    }
    
}
