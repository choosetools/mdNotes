# 数组中出现的算法

## 一、数组的复制

案例：创建一个整数型数组array1，给array1赋值；然后给array1数组复制一个数组array2

实际上数组的复制就是：创建的数组，通过for循环给新数组赋上值

```
int[] array1 = {2, 3, 5, 7, 11 ,13, 17, 19};

int[] array2 = new int[array1.length];
for (int i = 0; i < array2.length; i++) {
    array2[i] = array1[i];
}
```

## 二、数组的反转

**实现思想**：数组对称位置的元素互换。

<img src=".\images\image-20221117195931777.png" alt="image-20221117195931777" style="zoom:67%;" />

```
@Test
public void test9(){
    int[] arr = new int[]{45,65,23,7,344,73,32,90, 1};
    System.out.println(Arrays.toString(arr));

    //反转方式1
    for (int i = 0; i < arr.length / 2; i++) {
        int temp = arr[i];
        arr[i] = arr[arr.length - i - 1];
        arr[arr.length - i - 1] = temp;
    }

    //反转方式2
    for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    System.out.println(Arrays.toString(arr));
}
```

## 三、数组的扩容与缩容

数组的长度是固定的，如果要给数组扩容或者缩容，只能新造一个数组，然后将现有的元素一个一个地复制过去。

**数组的扩容**：

案例：给数组int[] arr = {1, 2, 3, 4, 5}扩容一倍，并将10,20,30放到数组中

```java
@Test
public void test10(){
    int[] arr = new int[]{1, 2, 3, 4, 5};
    int[] newArr = new int[arr.length << 1];
    for (int i = 0; i < arr.length; i++) {
        newArr[i] = arr[i];
    }
    newArr[arr.length] = 10;
    newArr[arr.length + 1] = 20;
    newArr[arr.length + 2] = 30;
    System.out.println(Arrays.toString(newArr));
}
```

**数组的缩容**：

案例：现有数组int[] arr = {1, 2, 3, 4, 5, 6, 7}。现在需删除数组中索引为4的元素。

```java
@Test
public void test11(){
    int[] arr = {1, 2, 3, 4, 5, 6, 7};
    
    //方案一：新建一个数组
    int[] newArr = new int[arr.length - 1];
    for (int i = 0; i < newArr.length; i++) {
        if (i < 4){
            newArr[i] = arr[i];
        }else {
            newArr[i] = arr[i + 1];
        }

    }
    
    //方案二：在原数组的基础上进行修改
     for (int i = 4; i < arr.length - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[arr.length - 1] = 0;

    System.out.println(Arrays.toString(newArr));
}
```

## 四、数组元素的查找



### 1、顺序查找



### 2、二分法查找



## 五、数组的排序算法

### 1、排序算法的衡量标准

### 2、排序的分类

### 3、冒泡排序（Bubble Sort)

### 4、快速排序

### 5、排序性能的比较与选择

