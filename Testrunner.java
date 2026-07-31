import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test runner 
 */
public class Testrunner {

    private static int passed = 0;
    private static int failed = 0;

    /** helper กลาง — พิมพ์ PASS/FAIL และนับผลให้เอง */
    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea MenuTest\n");
        }

        System.out.println("=== Menu Test Suite ===\n");

        testCreators();
        testAdd();
        testRemove();
        testObservers();
        testProducer();
        testExposure();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // --- Partition: ว่าง / มีอาหาร / input ที่ผิดเงื่อนไข ---
    private static void testCreators() {
        System.out.println("-- Creators --");

        BoundedStack empty = new BoundedStack();
        check("new() -> empty", empty.size() == 0);

        BoundedStack p = new BoundedStack(Arrays.asList("A", "B", "C"));
        check("new(list) -> size 3", p.size() == 3);
        check("new(list) -> preserves order",
                p.foods().equals(Arrays.asList("A", "B", "C")));

        // boundary: list ว่างคือขอบล่างที่ถูกต้อง
        BoundedStack fromEmpty = new BoundedStack(new ArrayList<String>());
        check("new(empty list) -> empty", fromEmpty.size() == 0);

        // input ที่ผิดเงื่อนไขต้องโยน exception ไม่ใช่ปล่อยผ่าน
        boolean threwDup = false;
        try {
            new BoundedStack(Arrays.asList("A", "A"));
        } catch (IllegalArgumentException e) {
            threwDup = true;
        }
        check("new(duplicates) -> throws IllegalArgumentException", threwDup);

        boolean threwNull = false;
        try {
            new BoundedStack(Arrays.asList("A", null));
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("new(list with null) -> throws IllegalArgumentException", threwNull);

        boolean threwNullList = false;
        try {
            new BoundedStack(null);
        } catch (IllegalArgumentException e) {
            threwNullList = true;
        }
        check("new(null) -> throws IllegalArgumentException", threwNullList);

        List<String> maxFoods = new ArrayList<>();
        for (int i = 0; i < BoundedStack.MAX_FOODS; i++) {
            maxFoods.add("food" + i);
        }
        BoundedStack maxMenu = new BoundedStack(maxFoods);
        check("new(list at MAX_FOODS) -> succeeds",
                maxMenu.size() == BoundedStack.MAX_FOODS);

        maxFoods.add("too many");
        boolean threwTooMany = false;
        try {
            new BoundedStack(maxFoods);
        } catch (IllegalArgumentException e) {
            threwTooMany = true;
        }
        check("new(list over MAX_FOODS) -> throws IllegalArgumentException",
                threwTooMany);
    }

    // --- Mutator: add ต้องรักษาลำดับและกันอาหารซ้ำ ---
    private static void testAdd() {
        System.out.println("\n-- Add --");

        BoundedStack s = new BoundedStack();
        check("add(A) -> works", s.add("A"));
        check("add(A) -> size and contains", s.size() == 1 && s.contains("A"));

        s.add("B");
        s.add("C");
        check("add preserves insertion order",
                s.foods().equals(Arrays.asList("A", "B", "C")));

        // อาหารซ้ำไม่ใช่ error — คืน false เฉย ๆ
        check("add duplicate -> returns false", !s.add("A"));

        // input ที่ผิดเงื่อนไขต้องโยน exception
        boolean threwEmpty = false;
        try {
            s.add("");
        } catch (IllegalArgumentException e) {
            threwEmpty = true;
        }
        check("add(empty string) -> throws IllegalArgumentException", threwEmpty);

        boolean threwNull = false;
        try {
            s.add(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        // boundary: เติมจนเต็มพอดีแล้วเติมเพิ่ม
        BoundedStack full = new BoundedStack();
        for (int i = 0; i < BoundedStack.MAX_FOODS; i++) {
            full.add("food" + i);
        }
        check("full BoundedStack cannot add more",
            full.size() == BoundedStack.MAX_FOODS && !full.add("one more"));
    }

    // --- Mutator: remove ทั้งกรณีพบและไม่พบ ---
    private static void testRemove() {
        System.out.println("\n-- Remove --");

        BoundedStack s = new BoundedStack(Arrays.asList("A", "B", "C"));
        check("remove(B) -> works",
            s.remove("B") && s.size() == 2
                && !s.contains("B")
                && s.foods().equals(Arrays.asList("A", "C")));

        // ลบอาหารที่ไม่มีไม่ใช่ error — คืน false เฉย ๆ
        check("remove missing food -> false", !s.remove("nope"));

        // boundary: ลบจนหมด
        s.remove("A");
        s.remove("C");
        check("remove all -> empty", s.size() == 0);
        check("remove on empty BoundedStack -> returns false", !s.remove("A"));
    }

    // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");

        BoundedStack s = new BoundedStack(Arrays.asList("A", "B"));
        check("size and contains work", s.size() == 2 && s.contains("A"));
        check("contains rejects a missing food", !s.contains("Z"));
        check("foods returns the full list in order",
            s.foods().equals(Arrays.asList("A", "B")));

        int before = s.size();
        s.size();
        s.contains("A");
        s.foods();
        check("observers have no side effects", s.size() == before);
    }

    // --- Producer ต้องคืนตัวใหม่ ไม่แก้ตัวเดิม ---
    private static void testProducer() {
        System.out.println("\n-- Producer (shuffled) --");

        BoundedStack original = new BoundedStack(Arrays.asList("A", "B", "C", "D"));
        BoundedStack shuffled = original.shuffled();

        List<String> a = new ArrayList<String>(original.foods());
        List<String> b = new ArrayList<String>(shuffled.foods());
        Collections.sort(a);
        Collections.sort(b);
        check("shuffled contains exactly the same foods", a.equals(b));

        check("shuffled does not mutate the original",
                original.foods().equals(Arrays.asList("A", "B", "C", "D")));

        // mutate ตัวใหม่ต้องไม่กระทบตัวเดิม
        shuffled.add("E");
        check("mutating the result does not affect the original",
                original.size() == 4);

        // boundary: shuffle stack ว่างต้องไม่พัง
        BoundedStack emptyShuffled = new BoundedStack().shuffled();
        check("shuffling an empty BoundedStack is safe", emptyShuffled.size() == 0);
    }

    // --- ทดสอบว่าไม่เกิด representation exposure ---
    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        // ขาออก: แก้ list ที่ได้จาก foods() ต้องไม่กระทบ rep
        BoundedStack s = new BoundedStack();
        s.add("A");

        List<String> got = s.foods();
        got.clear();
        check("clearing result of foods() does not affect Menu",
                s.size() == 1);

        got = s.foods();
        got.add("injected");
        check("adding to result of foods() does not affect Menu",
                s.size() == 1 && !s.contains("injected"));

        // สองครั้งต้องเป็นคนละ object
        // ขาเข้า: แก้ list ที่ส่งให้ constructor ต้องไม่กระทบ rep
        List<String> input = new ArrayList<String>(Arrays.asList("A", "B"));
        BoundedStack p = new BoundedStack(input);

        input.clear();
        input.add("injected");
        check("changing constructor argument does not affect BoundedStack",
            p.size() == 2 && !p.contains("injected"));
    }
}