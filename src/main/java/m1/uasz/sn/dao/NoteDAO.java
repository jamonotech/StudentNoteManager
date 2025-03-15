package m1.uasz.sn.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Note;

public class NoteDAO extends GenericDAO<Note, Long> {
    public NoteDAO() {
        super(Note.class);
    }

    public Note findNoteByEtudiantAndModule(Etudiant etudiant, Module module) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Note> query = em.createQuery(
                    "SELECT n FROM Note n WHERE n.etudiant = :etudiant AND n.module = :module", Note.class);
            query.setParameter("etudiant", etudiant);
            query.setParameter("module", module);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
