import model.Resume;
import storage.ListStorage;

/**
 * Test for your ArrayStorage implementation
 */
public class MainTestArrayStorage {
    //    static final MapStorage ARRAY_STORAGE = new MapStorage();
//    static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();
//    static final SortedArrayStorage ARRAY_STORAGE = new SortedArrayStorage();
    static final ListStorage ARRAY_STORAGE = new ListStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume("uuid1");
        Resume r2 = new Resume("uuid2");
        Resume r3 = new Resume("uuid3");

        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(new Resume("uuid5"));
        ARRAY_STORAGE.save(new Resume("uuid4"));

//        ARRAY_STORAGE.save(r1);
        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());
//        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        ARRAY_STORAGE.update(new Resume("uuid1"));
//        ARRAY_STORAGE.update(new Resume("uuid7"));
//        ARRAY_STORAGE.delete("uuid20");
        printAll();
        ARRAY_STORAGE.delete(r2.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();
        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}

