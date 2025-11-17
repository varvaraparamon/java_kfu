/**
 * 13.03.2021
 * 21. Collections
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public class ArrayList<T> implements List<T> {
    private static final int DEFAULT_SIZE = 10;

    private T elements[];
    private int count;

    public ArrayList() {
        this.elements = (T[]) new Object[DEFAULT_SIZE];
        this.count = 0;
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index < count) {
            return elements[index];
        }
        return null;
    }

    @Override
    public void removeAt(int index) {
        if (index >= 0 && index < count) {
            for (int i = index+1; i < count; i++){
                elements[i-1] = elements[i];
            }
            elements[count-1] = null;
            count--;
        }
    }

    @Override
    public int indexOf(T element) {
        for (int i = 0; i < count; i++){
            if (elements[i] == element){
                return i;
            }
        }
        return 0;
    }

    @Override
    public int lastIndexOf(T element) {
        for (int i = count-1; i >=0; i--){
            if (elements[i] == element){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void add(T element) {
        // если мы хотим добавить элемент, но массив уже заполнен
        if (count == elements.length) {
            T newArray[] = (T[])new Object[elements.length + elements.length / 2];
            for (int i = 0; i < count; i++) {
                newArray[i] = elements[i];
            }
            elements = newArray;
        }
        elements[count] = element;
        count++;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean contains(T element) {
        for (int i = 0; i < count; i++){
            if (elements[i] == element){
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeFirst(T element) {
        int positionOfRemovingElement = -1;
        for (int i = 0; i < count; i++) {
            if (elements[i] == element) {
                positionOfRemovingElement = i;
                break;
            }
        }
        if (positionOfRemovingElement == -1){
            return;
        }
        for (int i = positionOfRemovingElement; i < count-1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[count-1] = null;
        count--;
    }

    @Override
    public void removeLast(T element) {
        int positionOfRemovingElement = -1;
        for (int i = count-1; i >= 0; i--){
            if (elements[i] == element) {
                positionOfRemovingElement = i;
                break;
            }
        }
        if (positionOfRemovingElement == -1){
            return;
        }
        for (int i = positionOfRemovingElement; i < count-1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[count-1] = null;
        count--;
    }

    @Override
    public void removeAll(T element) {
        int j = 0;
        while (j < count){
            int positionOfRemovingElement = -1;
            if (elements[j].equals(element)) {
                System.out.println("element for delete found: " + element + "==" + elements[j]);
                positionOfRemovingElement = j;
            }

            if (positionOfRemovingElement == -1){
                j += 1;
                continue;
            }

            for (int i = positionOfRemovingElement; i < count-1; i++) {
                
                elements[i] = elements[i + 1];
            }

            elements[count-1] = null;
            count--;
        }
    }

    private class ArrayListIterator implements Iterator<T> {

        private int currentPosition;

        @Override
        public T next() {
            T nextValue = elements[currentPosition];
            currentPosition++;
            return nextValue;
        }

        @Override
        public boolean hasNext() {
            return currentPosition < count;
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }
}
