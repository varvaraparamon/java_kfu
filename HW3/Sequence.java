package HW3;
import java.util.Arrays;

public class Sequence {
    private int[] sequence;
    private SequenceObserver observer = new SequenceObserver();

    public Sequence(){
        this.sequence = new int[0];
        observer.logCreateSeq(this);
    }

    public Sequence(int[] sequence){
        this.sequence = Arrays.copyOf(sequence, sequence.length);
        observer.logCreateSeq(this);
    }
    
    public Sequence(Sequence other){
        int[] sequenceOther = other.getArray();
        this.sequence = Arrays.copyOf(sequenceOther, sequenceOther.length);
        observer.logCreateSeq(this);
    }

    public int[] getArray(){
        return this.sequence;
    }

    public void add(int value){
        int[] newSequence = Arrays.copyOf(sequence, sequence.length + 1);
        newSequence[sequence.length] = value;
        sequence = newSequence;

        observer.logAddElement(value);
    }

    public void removeByIndex(int index){
        if ((index < 0) || (index >= sequence.length)){
            observer.logError();
            return;
        }

        int[] newSequence = Arrays.copyOf(sequence, sequence.length - 1);
        for (int i = 0, j = 0; i < sequence.length; i++){
            if (i != index){
                newSequence[j++] = sequence[i];  
            } 
        }
        sequence = newSequence;

        observer.logRemoveByIndex(index);
    }

    public void removeByValue(int value){
        for (int i = 0; i < sequence.length; i++){
            if (sequence[i] == value){
                removeByIndex(i);
                return;
            }
        }

        observer.logRemoveByvalue(value);
    }

    public int getByIndex(int index){
        if ((index < 0) || (index >= sequence.length)){
            observer.logError();
            return -1;
        }

        return sequence[index];
    }

    public void replace(int index, int value){
        if ((index < 0) || (index >= sequence.length)){
            observer.logError();
            return;
        }

        sequence[index] = value;

        observer.logReplace(index, value);
    }

    public void insert(int index, int value){
        if ((index < 0) || (index >= sequence.length)){
            observer.logError();
            return;
        }

        int[] newSequence = Arrays.copyOf(sequence, sequence.length + 1);

        for (int i = sequence.length; i > index; i--){
            newSequence[i] = sequence[i-1];
        }
        newSequence[index] = value;

        sequence = newSequence;

        observer.logInsert(index, value);
    }

    public void printSequence(){
        System.out.println(Arrays.toString(sequence));
    }
}
