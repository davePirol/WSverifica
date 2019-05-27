/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pirolo_davide
 */
public class main extends HttpServlet {
    
    
    final private String driver = "com.mysql.jdbc.Driver";
    final private String dbms_url = "jdbc:mysql://localhost/";
    final private String database = "preverifica";
    final private String user = "root";
    final private String password = "";
    private Connection phonebook;
    private boolean connected;

    // attivazione servlet (connessione a DBMS)
    public void init() {
        String url = dbms_url + database;
        try {
            Class.forName(driver);
            phonebook = DriverManager.getConnection(url, user, password);
            connected = true;
        } catch (SQLException e) {
            connected = false;
        } catch (ClassNotFoundException e) {
            connected = false;
        }
    }

    // disattivazione servlet (disconnessione da DBMS)
    public void destroy() {
        try {
            phonebook.close();
        } catch (SQLException e) {
        }
    }
    private JSONObject toJson(Vector<Utente> lista){
        
        
        JSONArray array = new JSONArray();
        JSONObject e=null;
        
        for(Utente i:lista){
            JSONObject obj=new JSONObject();
            obj.put("nome", i.getNome());
            obj.put("cognome", i.getCognome());
            obj.put("numero", i.getNumero());
            
            System.out.println(obj.toJSONString());
            
            array.add(i);
        }
        
        
        
        
        return e;
        
    }
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet main</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet " + request.getContextPath() + "</h1>");
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //processRequest(request, response);
            String cognome,nome,numero;
            String url = request.getRequestURL().toString();
            String[] url_section = url.split("/");
            String nomeDaCercare = url_section[url_section.length - 1];
            
            if (!connected) {
                response.sendError(500, "DBMS server error!");
                return;
            }
            String sql="SELECT * FROM rubrica WHERE nome='"+nomeDaCercare+"'";
            
            Statement statement = phonebook.createStatement();
            ResultSet result = statement.executeQuery(sql);
            Vector<Utente> lista=new Vector<Utente>();
            
            
            if(!result.next()){
                response.sendError(404, "Entry not found!");
                result.close();
                statement.close();
                return;
            }else{   
                result.previous();
                while (result.next()) {
                    nome=result.getString(2);
                    cognome=result.getString(4);
                    numero = result.getString(3);
                    lista.add(new Utente(nome,cognome,numero));
                                   
                }
            }
            result.close();
            statement.close();
            
            
            JSONObject lis=toJson(lista);
            
            
            try (PrintWriter out = response.getWriter()) {
                out.print(lis.toJSONString());
                out.close();
            }
            finally{
                response.setStatus(200);
            }
            
            
            
            
        }   catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override 
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String url = request.getRequestURL().toString();
            String[] url_section = url.split("/");
            String param = url_section[url_section.length - 1];
            
            
            //string ricevuta {targa:"ty768jh", scadenzaAssicurazione:"28/04/2000", scadenzaBollo:"28/04/2000", "classeInquinameto":"2", ricercata:"0"}
            Object obj = new JSONParser().parse(param);
            JSONObject jo = (JSONObject) obj;
            String targa=(String) jo.get("targa");
            int classe=(int) jo.get("classeInquinamento");
            Date ass=(Date) jo.get("scadenzaAssicurazione");
            Date bollo=(Date) jo.get("scadenzaBollo");
            boolean ricercata=(boolean) jo.get("ricercata");
            
            String sql="INSERT INTO `automobili` (`targa`, `scadenzaAssicurazione`, `scadenzaBollo`, `classeInquinamento`, `ricercata`) VALUES('"+targa+"','"+ass+"','"+bollo+"','"+classe+"','"+ricercata+"')";
            Statement statement = phonebook.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            try (PrintWriter out = response.getWriter()) {
                out.print("operazione eseguita con successo");
                out.close();
            }
            finally{
                response.setStatus(200);
            }
            
            
        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
