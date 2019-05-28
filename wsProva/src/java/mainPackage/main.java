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
    final private String database = "verifica02";
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
            String provincia,nome,regione,abitanti;
            String s="";
            String url = request.getRequestURL().toString();
            String[] url_section = url.split("/");
            String parametro = url_section[url_section.length - 1];
            String comando=url_section[url_section.length - 2];
            
            if (!connected) {
                response.sendError(500, "DBMS server error!");
                return;
            }
            
            
            if(comando.equals("ricerca")){
                String sql="SELECT * FROM comuni WHERE comune='"+parametro+"'";
                Statement statement = phonebook.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if(result.next()){
                    nome=result.getString(2);
                    provincia=result.getString(3);
                    regione=result.getString(4);
                    abitanti=result.getString(5);
                }else{   
                    response.sendError(404, "Entry not found!");
                    result.close();
                    statement.close();
                    return;
                }
                result.close();
                statement.close();
                s="{'comune': '"+nome+"', 'provincia': '"+provincia+"', 'regione': '"+regione+"', 'abitanti': '"+abitanti+"'}";
            }
            else if(comando.equals("provincia")){
                String sql="SELECT * FROM comuni WHERE provincia='"+parametro+"'";
                Statement statement = phonebook.createStatement();
                ResultSet result = statement.executeQuery(sql);
                int count=0;
                s="{  'comuni':[";                
                while(result.next()){
                    nome=result.getString(2);
                    count+=result.getInt(5);
                    s+="'"+nome+"',";
                }
                s+="],'popolazione_totale': '"+count+"'}";
                
                result.close();
                statement.close();
            }
            else if(comando.equals("regione")){
                String sql="SELECT DISTINCT provincia FROM comuni WHERE regione='"+parametro+"'";
                Statement statement = phonebook.createStatement();
                ResultSet result = statement.executeQuery(sql);
                s="{  'provincie':[";                
                while(result.next()){
                    nome=result.getString(1);
                    s+="'"+nome+"',";
                }
                s+="]'}";
                                result.close();
                statement.close();
                
            }
            else{
                response.sendError(404, "Comando errato");
                return;
            }
            
            try (PrintWriter out = response.getWriter()) {
                out.print(s);
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
                
        try {
            String url = request.getRequestURL().toString();
            String[] url_section = url.split("/");
            String param = url_section[url_section.length - 1];
            String esito;
            
            Object obj = new JSONParser().parse(param);
            JSONObject jo = (JSONObject) obj;
            String comune=(String) jo.get("comune");
            int abitanti=(int) jo.get("abitanti");
            
            String sql="UPDATE comuni SET pop_residente='"+abitanti+"' WHERE comune='"+comune+"'";
            
            Statement statement = phonebook.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            if(result.next()){
                esito="operazione effettuata con successo";
            }else{   
                response.sendError(404, "Entry not found!");
                result.close();
                statement.close();
                return;
            }
            result.close();
            statement.close();
                        
            String s="{esito:'"+esito+"'}";
            
            try (PrintWriter out = response.getWriter()) {
                out.print(s);
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
