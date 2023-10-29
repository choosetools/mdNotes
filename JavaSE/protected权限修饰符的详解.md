# protected权限修饰符的详解

由于很多Java书籍对protected可见性的介绍都比较笼统，本文重点说明了protected关键字的可见性内涵。

问题：

在父类中创建了一个protected的属性或者方法，在不同包下的子类中按照以往的想法，都是可以访问的，但是事实并非如此，例：

```
package org.example.javabasics;

public class Father{
	protected void protectedMethod(){}
}

package org.example.javaadvance;
public class Son extends Father{
	@Test
	public void test(){
		protectedMethod();//编译成功
		
		Father f = new Father();
		f.protectedMethod();//编译错误
	}
}
```

对于上例中，在子类Son中，直接使用被继承过来的方法protectedMethod()就可以成功，但是使用父类对象去调用protectedMethod()却失败。

明明在不同包中的子类可以访问父类中的protected属性或方法，为什么这里会报错？



等学完多态之后再看

[详解Java的protected访问权限_protected访问权限范围-CSDN博客](https://blog.csdn.net/hyf24/article/details/122866042)