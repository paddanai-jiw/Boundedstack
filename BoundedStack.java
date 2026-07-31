import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Menu — ADT แทนรายการอาหารที่ผู้ใช้จัดลำดับไว้
 *
 * ค่านามธรรม (A): ลำดับของอาหาร เช่น [อาหารA, อาหารB, อาหารC]
 *
 * ตัวอย่างการใช้งาน:
 * Menu p = new Menu();
 * p.add("Bohemian Rhapsody");
 * p.add("Imagine");
 * System.out.println(p.size()); // 2
 */
public class BoundedStack {

    public static final int MAX_FOODS = 100;

    // ===== representation =====
    // AF(foods) = รายการอาหารใน stack ตามลำดับ foods.get(0), foods.get(1), ...
    private final ArrayList<String> foods;

    // AF (Abstraction Function):
    // AF(foods) = รายการอาหารตามลำดับที่อยู่ใน foods ตั้งแต่ index 0 ถึง size - 1

    // RI (Representation Invariant):
    // 1. foods ไม่เป็น null
    // 2. foods มีจำนวนสมาชิกไม่เกิน MAX_FOODS
    // 3. ชื่ออาหารทุกตัวไม่เป็น null และไม่เป็นสตริงว่าง
    // 4. ชื่ออาหารใน foods ต้องไม่ซ้ำกัน

    // Safety from representation exposure:
    // foods เป็น private และเป็น final ไม่คืน foods โดยตรง และทำสำเนา
    // ของ list ที่รับเข้ามาใน constructor เพื่อป้องกันการแก้ representation

    /**
    * ตรวจสอบ Representation Invariant ของ BoundedStack
     */
    private void checkRep() {

        assert foods != null : "foods ต้องไม่เป็น null";
        assert foods.size() <= MAX_FOODS : "จำนวนอาหารเกิน MAX_FOODS";
        Set<String> seen = new HashSet<>();
        for (String s : foods) {
            assert s != null && !s.isEmpty()
                    : "ชื่ออาหารต้องไม่เป็น null หรือสตริงว่าง";
            assert seen.add(s) : "ชื่ออาหารซ้ำ: " + s;
        }
    }
    // ===== Creator =====
    // ใช้สร้าง BoundedStack ใหม่ทั้งแบบว่างและแบบมีรายการอาหารเริ่มต้น

    /**
    * สร้าง stack ว่าง
     */
    public BoundedStack() {
        this.foods = new ArrayList<>();
        checkRep();
    }

    /**
    * สร้าง stack จากรายชื่ออาหารที่ให้มา
     *
     * ระวัง: ห้ามเก็บ reference ของ initial ตรง ๆ (rep exposure!)
     *
    * @param initial รายชื่ออาหารเริ่มต้น ต้องไม่ซ้ำและไม่เกิน MAX_FOODS
     * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
     */
    public BoundedStack(List<String> initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial ต้องไม่เป็น null");
        }
        if (initial.size() > MAX_FOODS) {
            throw new IllegalArgumentException("มีอาหารเกิน MAX_FOODS");
        }
        Set<String> seen = new HashSet<>();
        for (String s : initial) {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("ชื่ออาหารไม่ถูกต้อง");
            }
            if (!seen.add(s)) {
                throw new IllegalArgumentException("ชื่ออาหารซ้ำ: " + s);
            }
        }
        // copy ขาเข้า ป้องกัน initial จากการเปลี่ยนแปลงภายนอก
        this.foods = new ArrayList<>(initial);
        checkRep();
    }
    // ===== Mutators =====
    // เมธอดกลุ่มนี้ใช้เปลี่ยนข้อมูลใน Menu

    /**
    * เพิ่มอาหารต่อท้าย stack
     *
    * @param food ชื่ออาหาร ต้องไม่เป็น null และไม่เป็นสตริงว่าง
    * @return true ถ้าเพิ่มสำเร็จ, false ถ้ามีอาหารนี้อยู่แล้วหรือเต็มแล้ว
    * @throws IllegalArgumentException ถ้า food เป็น null หรือสตริงว่าง
     */
    public boolean add(String food) {
        if (food == null || food.isEmpty()) {
            throw new IllegalArgumentException("ชื่ออาหารต้องไม่เป็น null หรือสตริงว่าง");
        }
        if (foods.contains(food) || foods.size() >= MAX_FOODS) {
            return false;
        }
        foods.add(food);
        checkRep();
        return true;
    }

    /**
    * ลบอาหารออกจาก stack
     *
    * @param food ชื่ออาหารที่ต้องการลบ
    * @return true ถ้าลบสำเร็จ, false ถ้าไม่พบอาหารนี้
     */
    public boolean remove(String food) {
        boolean removed = foods.remove(food);
        checkRep();
        return removed;
    }

    // ===== Observers =====

    /**
    * คืนจำนวนอาหารใน stack
     */
    public int size() {
        return foods.size();
    }

    /**
    * ตรวจว่ามีอาหารนี้อยู่หรือไม่
     */
    public boolean contains(String food) {
        return foods.contains(food);
    }

    /**
    * คืนรายชื่ออาหารทั้งหมดตามลำดับ
     *
    * ระวัง: ห้ามคืน reference ของ foods ตรง ๆ (rep exposure!)
     */
    public List<String> foods() {
        return new ArrayList<>(foods);
    }

    // ===== Producer =====

    /**
    * คืน stack ใหม่ที่มีอาหารเดียวกันแต่สลับลำดับ
     *
    * ระวัง: ห้ามแก้ stack เดิม (this) เด็ดขาด
     *
    * @return stack ใหม่ที่สลับลำดับแล้ว
     */
    public BoundedStack shuffled() {
        List<String> shuffledFoods = new ArrayList<>(foods);
        Collections.shuffle(shuffledFoods);
        return new BoundedStack(shuffledFoods);
    }

    @Override
    public String toString() {
        return foods.toString();
    }
}
