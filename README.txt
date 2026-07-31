ADT: BoundedStack (Menu)
หน้าที่ : เก็บรายชื่ออาหารที่ผู้ใช้เพิ่มเข้ามาไว้เป็นลำดับ อาหารแต่ละชื่อห้ามซ้ำ และเก็บได้สูงสุด 100 รายการ

Representation : private ArrayList<String> foods

AF :
ตำแหน่งใน foods(index 0,1,2,...) แทนลำดับอาหารที่ถูกเพิ่มเข้ามาก่อน-หลัง

RI :
foods ต้องไม่ null
ขนาดของ foods ต้องอยู่ระหว่าง 0 ถึง MAX_FOODS
ทุกชื่อใน foods ต้องไม่เป็น null และไม่เป็น string ว่าง
ไม่มีชื่ออาหารซ้ำกันใน foods

Operation:

constructor เปล่า - สร้าง stack ที่ยังไม่มีอาหาร
constructor รับ list - สร้าง stack จากรายการที่กำหนดมาให้
add - ใส่อาหารเพิ่มเข้าไปท้าย stack
remove - เอาอาหารที่ระบุออกจาก stack
size - เช็คว่าตอนนี้มีอาหารกี่รายการ
contains - เช็คว่าอาหารที่ถามมามีอยู่ใน stack รึเปล่า
foods - เอารายการอาหารทั้งหมดออกมาดู
shuffled - เอาอาหารชุดเดิมมาสลับที่แบบสุ่มแล้วส่งเป็น stack ก้อนใหม่

Spec :
param pre post throws

BoundedStack()
param -
pre -
post stack ที่ได้ต้องว่างเปล่า size เท่ากับ 0
throws -

BoundedStack(List<String> initial)
param initial - รายชื่ออาหารที่ต้องการตั้งต้น
pre initial ต้องไม่เป็น null , สมาชิกข้างในต้องไม่มีตัวไหนเป็น null หรือค่าว่าง , ห้ามมีชื่อซ้ำกันเอง , จำนวนสมาชิกต้องไม่เกิน MAX_FOODS
post stack ที่สร้างขึ้นมามีอาหารตรงกับ initial ครบทุกตัวและเรียงลำดับเหมือนเดิม โดยข้อมูลถูกคัดลอกมาใหม่ทั้งหมด แก้ initial ภายหลังจะไม่กระทบ stack
throws IllegalArgumentException เมื่อ initial ไม่ผ่านเงื่อนไขใน pre

add(String food)
param food - ชื่ออาหารที่ต้องการเพิ่ม
pre food ต้องไม่เป็น null และไม่เป็น string ว่าง
post กรณี stack ยังไม่เต็มและยังไม่เคยมี food ตัวนี้: food จะถูกเพิ่มเข้าไปที่ท้าย stack, size เพิ่มขึ้น 1 ผลลัพธ์คืนเป็น true
กรณีมี food นี้อยู่แล้ว หรือ stack เต็มพอดี: stack ไม่มีการเปลี่ยนแปลงใดๆ ผลลัพธ์คืนเป็น false
throws IllegalArgumentException เมื่อ food เป็น null หรือ string ว่าง

remove(String food)
param food - ชื่ออาหารที่ต้องการนำออก
pre -
post ถ้าพบ food อยู่ใน stack: นำออกจาก stack, size ลดลง 1 ผลลัพธ์คืนเป็น true
ถ้าไม่พบ: stack ไม่เปลี่ยนแปลง ผลลัพธ์คืนเป็น false
throws -

size()
param -
pre -
post คืนจำนวนอาหารทั้งหมดที่มีอยู่ ณ ตอนนั้น(เป็นเพียงการอ่านค่า ไม่กระทบ stack)
throws -

contains(String food)
param food - ชื่ออาหารที่ต้องการตรวจสอบ
pre -
post คืน true หาก food อยู่ใน stack คืน false หากไม่มี(เป็นเพียงการอ่านค่า ไม่กระทบ stack)
throws -

foods()
param -
pre -
post คืนรายการอาหารทั้งหมดตามลำดับที่อยู่ใน stack ในรูปแบบ list ใหม่ที่แยกออกจาก rep ภายใน(แก้ list ที่ได้ไม่กระทบของจริง)
throws -

shuffled()
param -
pre -
post ได้ stack ใหม่ที่มีอาหารครบชุดเดียวกับตัวเดิมแต่เรียงลำดับสุ่มใหม่ ส่วนตัวเดิม(this) ยังคงเดิมทุกประการไม่ถูกแตะต้อง
throws -

นายพัชร์ดนัย จิวศิริตระกูล 6821601275