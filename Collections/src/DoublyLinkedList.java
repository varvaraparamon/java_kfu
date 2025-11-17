public class DoublyLinkedList<T> implements List<T> {

    public static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
        }
    }

    public class DoublyLinkedListIterator implements Iterator<T> {
        private Node<T> current;

        public DoublyLinkedListIterator() {
            this.current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T value = current.value;
            current = current.next;

            return value;
        }
    }

    private Node<T> first;

    private Node<T> last;

    private int count;

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (first == null) {
            this.first = newNode;
        } else {
            // следующий после последнего - новый узел
            newNode.prev = last;
            last.next = newNode;
        }
        // новый узел теперь последний
        this.last = newNode;
        this.count++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= count) {
            System.err.println("Вышли за пределы списка");
            return null;
        }

        Node<T> current;

        if (index <= count / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = count - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current.value;
    }

    @Override
    public void removeAt(int index) {
        if (index < count && index > -1) {
            if (index == 0){
                first = first.next;
                first.prev = null;
                count--;

                if (count == 0){
                    last = null;
                }
                return;
            }

            Node<T> current = first;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            
            if (index == count - 1){
                current.next = null;
                last = current;
            } else {
                current.next.next.prev = current;
                current.next = current.next.next;
            }
            count--;  
        }
    }

    @Override
    public int indexOf(T element) {
        Node<T> current = first;
        for (int i = 0; i < count; i++) {
            if (current.value == element){
                return i;
            }
            current = current.next;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(T element) {
        Node<T> current = last;
        for (int i = count - 1; i >= 0; i--) {
            if (current.value == element){
                return i;
            }
            current = current.prev;
        }

        return -1;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean contains(T element) {
        Node<T> current = first;
        for (int i = 0; i < count; i++) {
            if (current.value == element){
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public void removeFirst(T element) {
        // a -> b -> c -> d -> null
        // ^
        // f

        // current = a

        Node<T> current = first;

        // removeFirst(a)
        if (current.value == element) {
            // a -> b -> c -> d -> null
            //      ^
            //      f
            first = first.next;
            first.prev = null;
            return;
        }

        // removeFirst(c)
        // a -> b -> c -> d -> null
        //      ^
        //      c
        while (current.next != null && current.next.value != element) {
            current = current.next;
        }

        // a -> b -> -> d -> null
        //      ^
        //      c
        if (current.next != null) {
            current.next.next.prev = current;
            current.next = current.next.next;
        }

        if (current.next == null) {
            this.last = current;
        }

        count--;
    }

    @Override
    public void removeLast(T element) {
        int idx = lastIndexOf(element);
        if (idx != -1){
            this.removeAt(idx);
        }
    }

    @Override
    public void removeAll(T element) {
        while (first != null && first.value == element){
            first = first.next;
            first.prev = null;
            count--;
        }

        if (first == null){
            last = null;
            return;
        }

        Node<T> current = first;
        while (current.next != null){
            if (current.next.value.equals(element)){
                current.next.next.prev = current;
                current.next = current.next.next;
                count--;
            } else {
                current = current.next;
            }
        }
        last = current;
        
    }

    @Override
    public Iterator<T> iterator() {
        return new DoublyLinkedListIterator();
    }
}
