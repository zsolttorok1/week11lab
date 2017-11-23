/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import dataaccess.UserDB;
import domainmodel.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.naming.NamingException;

/**
 *
 * @author awarsyle
 */
public class AccountService { 
    public User checkLogin(String username, String password, String path) {
        User user;
        
        UserDB userDB = new UserDB();

            user = userDB.getByUsername(username);
            
            if (user.getPassword().equals(password)) {
                // successful login
                Logger.getLogger(AccountService.class.getName())
                        .log(Level.INFO,
                                "A user logged in: {0}", username);
                String email = user.getEmail();
                try {
                    // WebMailService.sendMail(email, "NotesKeepr Login", "Big brother is watching you!  Hi " + user.getFirstname(), false);
                    
                    HashMap<String, String> contents = new HashMap<>();
                    contents.put("firstname", user.getFirstname());
                    contents.put("date", ((new java.util.Date()).toString()));
                    
                    try {
                        WebMailService.sendMail(email, "NotesKeepr Login", path + "/emailtemplates/login.html", contents);
                    } catch (IOException ex) {
                        Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } catch (MessagingException ex) {
                    Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NamingException ex) {
                    Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return user;
            }
        return null;
    }
    
    public int resetPassword(String email, String path, String url) {
        //generate new UUID
        String uuid = UUID.randomUUID().toString();
        String link = url + "?uuid=" + uuid;
        
        //find User by email
        UserService us = new UserService();
        User user = us.getByEmail(email);
        
        if (user != null) {
            //insert password change request
            ResetPasswordService rps = new ResetPasswordService();
            rps.insert(uuid, user.getUsername());

            try {
                HashMap<String, String> contents = new HashMap<>();
                contents.put("firstname", user.getFirstname());
                contents.put("lastname", user.getLastname());
                contents.put("username", user.getUsername()); 
                contents.put("link", link); 

                try {
                    WebMailService.sendMail(email, "Password Reset Request", path + "/emailtemplates/resetpassword.html", contents);
                } catch (IOException ex) {
                    Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (MessagingException ex) {
                Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //OK, email sent
            return 1;
        }
        else {
            //email not found in Database
            return 2;
        }
    }
    
    public boolean changePassword(String username, String password) {
        UserService us = new UserService();
        try {
            User user = us.getByUsername(username);
            user.setPassword(password);
            UserDB udb = new UserDB ();
            udb.update(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
