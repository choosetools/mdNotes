对于有内部类的类中，如果外部类与内部类中声明的同名的属性或同名同参的方法时，在内部类中如果想要调用内部类中本身的该同名结构，直接调用即可；如果想要调用外部类中该同名的结构，需要使用：

<font color="red">**`外部类名.this.同名的属性或方法`**</font>





如果在某个子类类中，父类与父接口或当前类与父接口之间声明了同名的属性或同名同参的默认方法时，在子类中想要调用父类中的该同名结构可以直接使用super.的方式，但是如果想要调用父接口中的该同名结构时，需要使用：

<font color="red">**`父接口名.super.同名的属性或方法`**</font>