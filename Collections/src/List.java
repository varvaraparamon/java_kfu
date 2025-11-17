/**
 * 13.03.2021
 * 21. Collections
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public interface List<T> extends Collection<T> {
    /**
     * Получение элемента коллекции по индексу
     * @param index индекс элемента
     * @return элемент, размещенный под этим индексом. Если элемент не найден - возвращается -1
     */
    T get(int index);

    /**
     * Удаляет элемент по заданному индексу
     * @param index индекс удаляемого элемента
     */
    void removeAt(int index);

    /**
     * Возвращает индекс элемента (первое вхождение)
     * @param element элемент
     * @return позиция элемента, либо -1 если элемент не обнаружен
     */
    int indexOf(T element);

    /**
     * Возвращает индекс элемента (последнее вхождение)
     * @param element элемент
     * @return позиция элемента, либо -1 если элемент не обнаружен
     */
    int lastIndexOf(T element);
}
