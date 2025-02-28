package m1.uasz.sn.dao;

import m1.uasz.sn.models.Note;

public class NoteDAO extends GenericDAO<Note, Long> {
    public NoteDAO() {
        super(Note.class);
    }
}
