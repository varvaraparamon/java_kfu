package HW3;

public class SequenceObserver {
    public void logCreateSeq(Sequence seq) {
        System.out.print("Создана новая последовательность: ");
        seq.printSequence();
    }

    public void logAddElement(int value) {
        System.out.println("Добавлен новый элемент: " + value);
    }

    public void logRemoveByIndex(int index) {
        System.out.println("Удален элемент по индексу: " + index);
    }

    public void logRemoveByvalue(int value) {
        System.out.println("Удалено значение: " + value);
    }

    public void logReplace(int index, int value) {
        System.out.println("Элемент по индексу " + index + " заменен на значение: " + value);
    }

    public void logInsert(int index, int value) {
        System.out.println("По индексу " + index + " вставлено значение: " + value);
    }

    public void logError(){
        System.out.println("Ошибка, индекс вышел за границы массива");
    }
}
