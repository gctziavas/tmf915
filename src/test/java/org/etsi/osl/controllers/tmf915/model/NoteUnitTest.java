package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NoteUnitTest {

    @Test
    public void testNoteGettersAndSetters() {
        Note note = new Note();
        
        String author = "TestAuthor";
        String text = "Test note text";
        OffsetDateTime date = OffsetDateTime.now();
        
        note.setAuthor(author);
        note.setText(text);
        note.setDate(date);
        
        assertEquals(author, note.getAuthor());
        assertEquals(text, note.getText());
        assertEquals(date, note.getDate());
    }
    
    @Test
    public void testNoteBuilderPattern() {
        OffsetDateTime date = OffsetDateTime.now();
        Note note = new Note()
            .author("TMF915")
            .text("Model files not found")
            .date(date);
            
        assertEquals("TMF915", note.getAuthor());
        assertEquals("Model files not found", note.getText());
        assertEquals(date, note.getDate());
    }
}
