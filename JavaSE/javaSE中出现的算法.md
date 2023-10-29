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

顺序查找其实就是一个一个地去遍历。

案例：查找value第一次在数组中出现的index

```
public class TestArrayOrderSearch {
    //查找value第一次在数组中出现的index
    public static void main(String[] args){
        int[] arr = {4,5,6,1,9};
        int value = 1;
        int index = -1;

        for(int i=0; i<arr.length; i++){
            if(arr[i] == value){
                index = i;
                break;
            }
        }
    
        if(index==-1){
            System.out.println(value + "不存在");
        }else{
            System.out.println(value + "的下标是" + index);
        }
    }

}
```

### 2、二分法查找

二分法查找要求数组**必须是有序**的，这样才能判断当前索引所指向的元素与要查找元素的位置关系。

**二分法查找图示**：

![](.\images\image-20220317230955644.png)

二分查找指的是每一个都要进行二分操作，然后进行查找；如果只有第一步进行二分的话，那和顺序查找的时间复杂度没啥区别。

**实现步骤**：

<img src=".\images\image-20220623210601915.png" style="zoom:60%;">

示例代码：

```
//二分法查找：要求此数组必须是有序的。
int[] arr3 = new int[]{-99,-54,-2,0,2,33,43,256,999};
boolean isFlag = true;
int value = 256;
//int value = 25;
int head = 0;//首索引位置
int end = arr3.length - 1;//尾索引位置
while(head <= end){
    int middle = (head + end) / 2;
    if(arr3[middle] == value){
        System.out.println("找到指定的元素，索引为：" + middle);
        isFlag = false;
        break;
    }else if(arr3[middle] > value){
        end = middle - 1;
    }else{//arr3[middle] < value
        head = middle + 1;
    }
}

if(isFlag){
    System.out.println("未找打指定的元素");
}
```



顺序查找：

* 优点：算法简单
* 缺点：效率低，执行的事件复杂度是O(N)

二分法查找：

* 优点：执行效率高，事件复杂度是O(logN)
* 缺点：算法相较于顺序查找难一点，而且数组必须有序

为了实现数组有序，可以先对数组进行排序。

## 五、数组的排序算法

### 1、排序算法概述

* **定义**：
  * 排序：假设含有n个记录的序列为{R1, R1, ..., Rn}，其相应的关键字序列为{K1, K2, ...,Kn}。将这些记录重新排序为{Ri1, Ri2, ..., Rin}，使得相应的关键字值满足条件Ki1 <= Ki2 <= ... <= Kin，这样的操作叫作排序。
  * 通常来说，排序的目的是为了快速查找。

* **衡量排序算法的优劣：**

  * `时间复杂度`：分析关键字的比较次数和记录的移动次数
    * 常见的算法时间复杂度有小到大依次是：O(1) < O(log2n) < O(n) < O(nlog2n) < O(n²) < O(n³) < ... < O(2ⁿ) < O(n!) < O(nⁿ)
  * `空间复杂度`：分析排序算法需要多少辅助内存

  ```
  一个算法的空间复杂度S（n)定义为该算法所耗费的存储空间，它也是问题规模n的函数。
  ```

  * `稳定性`：若两个记录A和B的关键字值相等，但排序后A、B的先后次序保持不变，则称这种排序算法是稳定的。

![](.\images\image-20211222113701365.png)

### 2、排序的分类

* **排序算法分类：内部排序和外部排序**
  * `内部排序`：整个排序过程不需要借助于外部存储器（如磁盘等），所有排序操作都在内存中完成。
  * `外部排序`：参与排序的数据非常多，数据量非常大，计算机无法把整个排序过程放在内存中完成，必须借助于外部存储器（如磁盘）。外部排序最常见的是多路归并排序，可以认为外部排序是多次内部排序组成。
* **十大内部排序算法**：

​	数组的排序算法很多，实现方式各不相同，时间复杂度、空间复杂度、稳定性也各不相同：

![](.\images\image-20211222111142684.png)

常见时间复杂度所消耗的时间从小到大排序：

**O(1) < O(logn) < O(n) < O(nlogn) < O(n^2) < O(n^3) < O(2^n) < O(n!) < O(n^n)**

注意，经常将以2为底的n的对数简写成logn。

![](.\images\image-20220824003440106.png)

### 3、冒泡排序（Bubble Sort)

**时间复杂度：O(n²)**

![](.\images\image-20220516094637228.png)

**排序思想**：

1. 比较相邻的元素。如果第一个比第二个大（升序），就交换他们两个。
2. 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这一步做完后，最后的元素会是最大的数。
3. 针对所有的元素重复以上的步骤，除了最后一个。
4. 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较为止。

![](.\images\BubbleSort.png)

```
/*
1、冒泡排序（最经典）
思想：每一次比较“相邻（位置相邻）”元素，如果它们不符合目标顺序（例如：从小到大），
     就交换它们，经过多轮比较，最终实现排序。
	 （例如：从小到大）	 每一轮可以把最大的沉底，或最小的冒顶。
	 
过程：arr{6,9,2,9,1}  目标：从小到大

第一轮：
	第1次，arr[0]与arr[1]，6>9不成立，满足目标要求，不交换
	第2次，arr[1]与arr[2]，9>2成立，不满足目标要求，交换arr[1]与arr[2] {6,2,9,9,1}
	第3次，arr[2]与arr[3]，9>9不成立，满足目标要求，不交换
	第4次，arr[3]与arr[4]，9>1成立，不满足目标要求，交换arr[3]与arr[4] {6,2,9,1,9}
	第一轮所有元素{6,9,2,9,1}已经都参与了比较，结束。
	第一轮的结果：第“一”最大值9沉底（本次是后面的9沉底），即到{6,2,9,1,9}元素的最右边

第二轮：
	第1次，arr[0]与arr[1]，6>2成立，不满足目标要求，交换arr[0]与arr[1] {2,6,9,1,9}
	第2次，arr[1]与arr[2]，6>9不成立，满足目标要求，不交换
	第3次：arr[2]与arr[3]，9>1成立，不满足目标要求，交换arr[2]与arr[3] {2,6,1,9,9}
	第二轮未排序的所有元素 {6,2,9,1}已经都参与了比较，结束。
	第二轮的结果：第“二”最大值9沉底（本次是前面的9沉底），即到{2,6,1,9}元素的最右边
第三轮：
	第1次，arr[0]与arr[1]，2>6不成立，满足目标要求，不交换
	第2次，arr[1]与arr[2]，6>1成立，不满足目标要求，交换arr[1]与arr[2] {2,1,6,9,9}
	第三轮未排序的所有元素{2,6,1}已经都参与了比较，结束。
	第三轮的结果：第三最大值6沉底，即到 {2,1,6}元素的最右边
第四轮：
	第1次，arr[0]与arr[1]，2>1成立，不满足目标要求，交换arr[0]与arr[1] {1,2,6,9,9}
	第四轮未排序的所有元素{2,1}已经都参与了比较，结束。
	第四轮的结果：第四最大值2沉底，即到{1,2}元素的最右边

*/
public class Test19BubbleSort{
    public static void main(String[] args){
        int[] arr = {6,9,2,9,1};

        //目标：从小到大
        //冒泡排序的轮数 = 元素的总个数 - 1
        //轮数是多轮，每一轮比较的次数是多次，需要用到双重循环，即循环嵌套
        //外循环控制 轮数，内循环控制每一轮的比较次数和过程
        for(int i=1; i<arr.length; i++){ //循环次数是arr.length-1次/轮
            for(int j=0; j<arr.length-i; j++){
                //希望的是arr[j] < arr[j+1]
                if(arr[j] > arr[j+1]){
                    //交换arr[j]与arr[j+1]
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    
        //完成排序，遍历结果
        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i]+"  ");
        }
    }

}
```

**冒泡排序优化**

```
/*
思考：冒泡排序是否可以优化
*/
class Test19BubbleSort2{
	public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9};

        //从小到大排序
        for (int i = 0; i < arr.length - 1; i++) {
            boolean flag = true;//假设数组已经是有序的
            for (int j = 0; j < arr.length - 1 - i; j++) {
                //希望的是arr[j] < arr[j+1]
                if (arr[j] > arr[j + 1]) {
                    //交换arr[j]与arr[j+1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
    
                    flag = false;//如果元素发生了交换，那么说明数组还没有排好序
                }
            }
            if (flag) {
                break;
            }
        }
    
        //完成排序，遍历结果
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "  ");
        }
    }

}
```

### 4、快速排序

快速排序（Quick Sort）由`图灵奖`获得者`Tony Hoare`发明，被列为`20世纪十大算法之一`，是迄今为止所有内排序算法中速度最快的一种，快速排序的时间复杂度为O(nlog(n))。

快速排序通常明显比同为O(nlogn)的其他算法更快，因此常被采用，而且快排采用了分治法的思想，所以在很多笔试面试中能经常看到快排的影子。

排序思想：

1. 从数列中挑出一个元素，称为"基准"（pivot），

2. 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区结束之后，该基准就处于数列的中间位置。这个称为分区（partition）操作。

3. 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。

4. 递归的最底部情形，是数列的大小是零或一，也就是永远都已经被排序好了。虽然一直递归下去，但是这个算法总会结束，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。

图示1：

![](.\images\image-20220317235922776.png)

图示2：

第一轮操作：

![](.\images\image-20221117205612230.png)

第二轮操作：

![](.\images\image-20221117205719427.png)

### 5、排序性能的比较与选择

* 性能比较：

  * **从平均时间而言**：快速排序最佳。但在最坏情况下时间性能不如堆排序和归并排序。
  * **从算法简单性看**：由于直接选择排序、直接插入排序和冒泡排序的算法比较简单，将其认为是简单算法。对于Shell排序、堆排序、快速排序和归并排序算法，其算法比较复杂，认为是复杂排序。
  * **从稳定性来看**：直接插入排序、冒泡排序和规定排序是稳定的；而直接选择排序、快速排序、Shell排序和堆排序是不稳定的。
  * **从待排序的记录数n的大小来看**，n较小时，宜采用简单排序；而n较大时，宜采用复杂排序。

* 选择：

  * 若n较小时（n<50），可采用直接插入或直接选择排序。

    当记录规模较小时，直接插入排序较好；否则因为直接选择移动的记录数小于直接插入，应选择直接排序为宜。

  * 若文件初始状态基本有序（指正序），则应选用直接插入、冒泡和随机的快速排序为宜。

  * 若n较大，则应采用时间复杂度为O(nlogn)的排序方法：快速排序、堆排序或归并排序。
