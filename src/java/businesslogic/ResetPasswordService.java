package businesslogic;

import dataaccess.PasswordChangeRequestDB;
import domainmodel.PasswordChangeRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import keccak.FIPS202;


public class ResetPasswordService {

    private PasswordChangeRequestDB passDB;

    public ResetPasswordService() {
        passDB = new PasswordChangeRequestDB();
    }
  
    public PasswordChangeRequest get(String ID){
        return passDB.get(hash(ID));
    }

    public List<PasswordChangeRequest> getAll() {
        return passDB.getAll();
    }
    
    private String hash(String uuid) {
        return hashByKeccak512(uuid);
    }
    
    private String hashByKeccak512(String string) {
        byte[] stringBytes = string.getBytes();
        byte[] hashBytes = FIPS202.HashFunction.SHA3_512.apply(stringBytes);
        String hashHex = FIPS202.hexFromBytes(hashBytes);
        
        return hashHex;
    }
    
    private String hashBySHA512(String string, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(string.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            string = sb.toString();
        } 
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ResetPasswordService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return string;
    }
    
    
    public boolean delete(String ID) {
        PasswordChangeRequest deletedPCR = passDB.get(hash(ID));
        return passDB.delete(deletedPCR);
    }

    public boolean insert(String ID, String username) {
        PasswordChangeRequest pcr = new PasswordChangeRequest();
        UserService us = new UserService();
        Date date = new Date();
            
        try {
            pcr.setOwner(us.getByUsername(username));
        } catch (Exception ex) {
            Logger.getLogger(ResetPasswordService.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        pcr.setId(hash(ID));
        pcr.setTime(date);
    
        return passDB.insert(pcr);
    }
}
