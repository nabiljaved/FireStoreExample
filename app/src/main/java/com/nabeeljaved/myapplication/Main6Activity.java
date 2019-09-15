package com.nabeeljaved.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

public class Main6Activity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.collection("Notebook").document("Example Note");
    private DocumentSnapshot lastResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);


    }

    //**************************************** ON START *****************************************************************
    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    NoteClass note = documentSnapshot.toObject(NoteClass.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();

                    int priority = note.getPriority();

                    data += "ID: " + documentId
                            + "\nTitle: " + title + "\nDescription: " + description
                            + "\nPriority: " + priority + "\n\n";
                }

                textViewData.setText(data);
            }
        });


    }

    //**************************************** ADD NOTE*****************************************************************
    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

        NoteClass note = new NoteClass(title, description, priority);

        noteRef.set(note);

    }

    //**************************************** LIKES*****************************************************************

    public void likes(View view)
    {
        executeTransaction();

    }

    private void executeTransaction()
    {
        db.runTransaction(new Transaction.Function<Long>() {
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference exampleNoteRef = notebookRef.document("Example Note");

                DocumentSnapshot exampleNoteSnapshot = transaction.get(exampleNoteRef);
                long newPriority = exampleNoteSnapshot.getLong("priority") + 1;
                transaction.update(exampleNoteRef, "priority", newPriority);
                return newPriority;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                Toast.makeText(Main6Activity.this, "New Priority: " + result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //**************************************** BATCH WRITE*****************************************************************
    public void executeBatchedWrite(View view)
    {
        WriteBatch batch = db.batch();
        DocumentReference doc1 = notebookRef.document("English");
        batch.set(doc1, new NoteClass("English", "IELTS is Great", 1));

        DocumentReference doc2 = notebookRef.document("Math");
        batch.set(doc2, new NoteClass("Math", "I love Maths", 1));

        DocumentReference doc3 = notebookRef.document("Math");
        batch.update(doc3, "title", "Science");

        DocumentReference doc4 = notebookRef.document("For Delete");
        batch.set(doc4, new NoteClass("deleted", "This will be deleted", 1));

        DocumentReference doc5 = notebookRef.document("For Delete");
        batch.delete(doc5);

        DocumentReference doc6 = notebookRef.document();
        batch.set(doc6, new NoteClass("Random", "this is Random Post", 1));

        batch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                textViewData.setText(e.toString());
            }
        });
    }

    //**************************************** GO TO ARRAY ACTIVITY*****************************************************************


    public void arrayActivity(View view)
    {
        Intent arrayintent = new Intent(Main6Activity.this, Main7Activity.class);
        startActivity(arrayintent);
    }
}

