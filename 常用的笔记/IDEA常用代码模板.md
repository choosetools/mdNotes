## 常用代码模板

#### 1、非空判断

* 变量.null：if(变量 == null)
* 变量.nn：if(变量 != null) 
* 变量.notnull：if(变量 != null) 
* ifn：if(xx  == null)
* inn：if(xx  != null)

#### 2、遍历数组和集合

* 数组或集合变量.fori：for循环
* 数组或集合变量.for：增强for循环
* 数组或集合变量.forr：反向for循环
* 数组或集合变量.iter：增强for循环遍历数组或集合

#### 3、输出语句

- sout：相当于System.out.println
- soutm：打印当前方法的名称
- soutp：打印当前方法的形参及形参对应的实参值
- soutv：打印方法中声明的最近的变量的值
- 变量.sout：打印当前变量值
- 变量.soutv：打印当前变量名及变量值

#### 4、对象操作

- 创建对象
  - Xxx.new  .var ：创建Xxx类的对象，并赋给相应的变量
  - Xxx.new  .field：会将方法内刚创建的Xxx对象抽取为一个属性
- 强转
  - 对象.cast：将对象进行强转
  - 对象.castvar：将对象强转后，并赋给一个变量

#### 5、静态常量声明

* psf：public static final
* psfi：public static final int
* psfs：public static final String
* prsf：private static final