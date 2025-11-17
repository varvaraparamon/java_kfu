public class Main {

    public static void main(String[] args) {
        List<Integer> list = new DoublyLinkedList<>();
        list.add(7);
        list.add(15);
        list.add(10);
        list.add(15);
        list.add(16);
        list.add(17);
        list.add(18);
        list.add(19);
        list.add(20);
        list.add(26);
        list.add(36);
        list.add(46);
        list.add(56);
        list.add(6546);
        list.add(136);
        list.add(1336);
        list.add(163);

//        list.removeFirst(10);
//        list.removeFirst(7);
        System.out.println(list.indexOf(15) + " " + list.lastIndexOf(15) + " " + list.contains(15));
    
        list.removeAt(16);
        list.removeFirst(15);
        list.removeLast(15);
        list.removeAt(1);
        list.removeAt(0);
        list.removeAll(136);

        // System.out.println(list.get(0));
        // System.out.println(list.get(1));
        // System.out.println(list.get(2));
        // System.out.println(list.get(3));
        // System.out.println(list.get(4));

        Iterator<Integer> iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
