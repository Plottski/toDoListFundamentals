package plottski.todolistfundamentals.Utilities;

import plottski.todolistfundamentals.Entities.Item;

import java.util.Comparator;

public class ComparatorWrapper {

    private Comparator<Item> dueDateComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getDueDate().compareTo(item2.getDueDate());
        }
    };

    public Comparator<Item> getDueDateComparator() {
        return dueDateComparator;
    }

    public void setDueDateComparator(Comparator<Item> dueDateComparator) {
        this.dueDateComparator = dueDateComparator;
    }
}
