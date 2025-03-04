package m1.uasz.sn.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Query;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Note;

public class NoteDAO extends GenericDAO<Note, Long> {
   
    

    public NoteDAO() {
        super(Note.class);
    }

    public double calculerMoyenne(Long etudiantId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerMoyenne'");
    }

   public void saisirNote(Long etudiantId, Long moduleId, double cc, double examen) {
        Etudiant etudiant = new EtudiantDAO().findById(etudiantId);
        Module module = new ModuleDAO().findById(moduleId);
        
        Note note = new Note( cc,examen,etudiant,module);
        // note.setEtudiant(etudiant);
        // note.setModule(module);
        // note.setNoteCC(cc);
        // note.setNoteExamen(examen);
        // note.calculerMoyenne();
        
        // NoteDAO.addNote(note);
    }


    public void deliberer() {
       
    }

    public double calculerTauxReussite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerTauxReussite'");
    }

  

    public Map<String, Long> statistiquesMentions() {
        String jpql = "SELECT n.mention, COUNT(n) FROM Note n GROUP BY n.mention";
        Query query = em.createQuery(jpql);
        List<Object[]> results = query.getResultList();

        Map<String, Long> stats = new HashMap<>();
        for (Object[] result : results) {
            stats.put((String) result[0], (Long) result[1]);
        }

        return stats;
    }
}

   

