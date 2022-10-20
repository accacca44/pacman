import java.util.Comparator;

class RecordComparator implements Comparator<Record> {

    // override the compare() method
    @Override
    public int compare(Record o1, Record o2) {
        if (o1.getPlayerScore() == o2.getPlayerScore())
            return 0;
        else if (o1.getPlayerScore() < o2.getPlayerScore())
            return 1;
        else
            return -1;
    }
}