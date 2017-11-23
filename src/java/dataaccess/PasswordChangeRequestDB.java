package dataaccess;

import domainmodel.PasswordChangeRequest;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class PasswordChangeRequestDB {

    public boolean insert(PasswordChangeRequest passwordChangeRequest) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.persist(passwordChangeRequest);
            trans.commit();
            em.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(PasswordChangeRequestDB.class.getName()).log(Level.SEVERE, "Cannot insert " + passwordChangeRequest.toString(), ex);
            em.close();      
            try {
                trans.rollback();
            } catch (Exception ex2) {  
            }
        }
        return false;
    }
    
    public boolean update(PasswordChangeRequest passwordChangeRequest) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.merge(passwordChangeRequest);
            trans.commit();
            em.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(PasswordChangeRequestDB.class.getName()).log(Level.SEVERE, "Cannot update " + passwordChangeRequest.toString(), ex);
            em.close();
            try {
                trans.rollback();
            } catch (Exception ex2) {  
            }
        }
        return false;
    }

    public List<PasswordChangeRequest> getAll() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try {
            List<PasswordChangeRequest> passwordChangeRequest = em.createNamedQuery("PasswordChangeRequest.findAll", PasswordChangeRequest.class).getResultList();
            em.close();
            return passwordChangeRequest;
        } catch (Exception ex) {
            em.close();
            Logger.getLogger(PasswordChangeRequestDB.class.getName()).log(Level.SEVERE, "Cannot read passwordChangeRequests", ex);
        }
        return null;
    }

    public PasswordChangeRequest get(String ID) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try {
            PasswordChangeRequest passwordChangeRequest = em.find(PasswordChangeRequest.class, ID);
            em.close(); 
            return passwordChangeRequest;
        } catch (Exception ex) {
            em.close(); 
            Logger.getLogger(PasswordChangeRequestDB.class.getName()).log(Level.SEVERE, "Cannot read passwordChangeRequests", ex);     
        }
        return null;
    }

    public boolean delete(PasswordChangeRequest passwordChangeRequest) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.remove(em.merge(passwordChangeRequest));
            trans.commit();
            em.close();
            return true;
        } catch (Exception ex) {
            em.close();
            Logger.getLogger(PasswordChangeRequestDB.class.getName()).log(Level.SEVERE, "Cannot delete " + passwordChangeRequest.toString(), ex);
            try {
                trans.rollback();
            } catch (Exception ex2) {  
            }
        }
        return false;
    }
}
