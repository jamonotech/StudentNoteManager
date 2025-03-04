package m1.uasz.sn.services;

import java.util.List;
import java.util.Map;

import m1.uasz.sn.dao.ModuleDAO;
import m1.uasz.sn.dao.NoteDAO;
import m1.uasz.sn.models.Etudiant;
import m1.uasz.sn.models.Module;
import m1.uasz.sn.models.Note;

public class NoteService {
    private NoteDAO noteDAO;

    public NoteService() {
        this.noteDAO = new NoteDAO();
    }

    public void ajouterNote(Note note) {
        noteDAO.create(note);
    }

    public Note trouverNote(Long id) {
        return noteDAO.findById(id);
    }

    public List<Note> listerNote() {
        return noteDAO.findAll();
    }

    public void modifierNote(Note note) {
        noteDAO.update(note);
    }

    public void supprimerNote(Note note) {
        noteDAO.delete(note);
    }
}
