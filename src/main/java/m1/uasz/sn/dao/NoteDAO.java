package m1.uasz.sn.dao;

import m1.uasz.sn.models.Note;

public class NoteDAO extends GenericDAO<Note, Long> {
    public NoteDAO() {
        super(Note.class);
    }

    public double calculerMoyenne(Long etudiantId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerMoyenne'");
    }

    public void saisirNote(Long etudiantId, Long moduleId, double noteCC, double noteExam) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saisirNote'");
    }
}
