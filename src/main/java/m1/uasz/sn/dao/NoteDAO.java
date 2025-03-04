package m1.uasz.sn.dao;

import jakarta.persistence.NoResultException;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Note;

public class NoteDAO extends GenericDAO<Note, Long> {
    public NoteDAO() {
        super(Note.class);
    }

    public Note findNoteByEtudiantAndModule(Etudiant etudiant, Module module) {
        try {
            return entityManager.createQuery(
                            "SELECT n FROM Note n WHERE n.etudiant = :etudiant AND n.module = :module",
                            Note.class)
                    .setParameter("etudiant", etudiant)
                    .setParameter("module", module)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
