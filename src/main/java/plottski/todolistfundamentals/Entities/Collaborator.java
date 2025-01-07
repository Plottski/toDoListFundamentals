package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "collaborators")
public class Collaborator{

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonBackReference(value = "itemListCollaborators")
    private ItemList itemList;

    @Column(nullable = false)
    List<String> collaboratorNames;

    public Collaborator() {
    }

}
