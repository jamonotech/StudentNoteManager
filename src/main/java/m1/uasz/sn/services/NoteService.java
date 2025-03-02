package m1.uasz.sn.services;

import m1.uasz.sn.dao.NoteDAO;
import m1.uasz.sn.models.*;
import java.util.List;

public class NoteService {
    private final NoteDAO noteDAO;

    public NoteService() {
        this.noteDAO = new NoteDAO();
    }

    public void ajouterNote(Note note) {
        noteDAO.create(note);
    }

    public void modifierNote(Note note) {
        noteDAO.update(note);
    }

    public void supprimerNote(Long id) {
        noteDAO.delete(id);
    }

    public Note trouverNote(Long id) {
        return noteDAO.findById(id);
    }

    public List<Note> listerNotes() {
        return noteDAO.findAll();
    }

    public void saisirNote(Long etudiantId, Long moduleId, double noteCC, double noteExam) {
        noteDAO.saisirNote(etudiantId, moduleId, noteCC, noteExam);
    }

    public double calculerMoyenne(Long etudiantId) {
        return noteDAO.calculerMoyenne(etudiantId);
    }
}
