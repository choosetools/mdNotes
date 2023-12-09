# IntelliJ IDEA 常用快捷键一览表

***

## 1-IDEA的日常快捷键

### 第1组：通用型

| 说明            | 快捷键           |
| --------------- | ---------------- |
| 复制代码-copy   | ctrl + c         |
| 粘贴-paste      | ctrl + v         |
| 剪切-cut        | ctrl + x         |
| 撤销-undo       | ctrl + z         |
| 反撤销-redo     | ctrl + shift + z |
| 保存-save all   | ctrl + s         |
| 全选-select all | ctrl + a         |

### 第2组：提高编写速度（上）

| 说明                                                         | 快捷键           |
| ------------------------------------------------------------ | ---------------- |
| 智能提示-edit                                                | alt + enter      |
| <font color="red">**提示代码模板-insert live template**</font> | ctrl+j           |
| <font color="red">**使用xx块环绕-surround with ...**</font>  | ctrl+alt+t       |
| <font color="red">**调出生成getter/setter/构造器等结构-generate ...**</font> | alt+insert       |
| 自动生成返回值变量-introduce variable ...                    | ctrl+alt+v       |
| 复制指定行的代码-duplicate line or selection                 | ctrl+d           |
| 删除指定行的代码-delete line                                 | ctrl+y           |
| 切换到下一行代码空位-start new line                          | shift + enter    |
| 切换到上一行代码空位-start new line before current           | ctrl +alt+ enter |
| 向上移动代码-move statement up                               | ctrl+shift+↑     |
| 向下移动代码-move statement down                             | ctrl+shift+↓     |
| 向上移动一行-move line up                                    | alt+shift+↑      |
| 向下移动一行-move line down                                  | alt+shift+↓      |
| <font color="red">**方法的形参列表提醒-parameter info**</font> | ctrl+p           |

### 第3组：提高编写速度（下）

| 说明                                                         | 快捷键       |
| ------------------------------------------------------------ | ------------ |
| 批量修改指定的变量名、方法名、类名等-rename                  | shift+f6     |
| <font color="red">**抽取代码重构方法-extract method ...**</font> | ctrl+alt+m   |
| <font color="red">**重写父类的方法-override methods ...**</font> | ctrl+o       |
| 实现接口的方法-implements methods ...                        | ctrl+i       |
| 选中的结构的大小写的切换-toggle case                         | ctrl+shift+u |
| 批量导包-optimize imports                                    | ctrl+alt+o   |

### 第4组：类结构、查找和查看源码

| 说明                                                         | 快捷键                          |
| ------------------------------------------------------------ | ------------------------------- |
| 如何查看源码-go to class...                                  | ctrl + 选中指定的结构 或 ctrl+n |
| **显示当前类结构，支持搜索指定的方法、属性等-file structure** | ctrl+f12                        |
| 退回到前一个编辑的页面-back                                  | ctrl+alt+←                      |
| 进入到下一个编辑的页面-forward                               | ctrl+alt+→                      |
| 打开的类文件之间切换-select previous/next tab                | alt+←/→                         |
| 光标选中指定的类，查看继承树结构-Type Hierarchy              | ctrl+h                          |
| 查看方法文档-quick documentation                             | ctrl+q                          |
| 类的UML关系图-show uml popup                                 | ctrl+alt+u                      |
| 定位某行-go to line/column                                   | ctrl+g                          |
| 回溯变量或方法的来源-go to implementation(s)                 | ctrl+alt+b                      |
| 折叠方法实现-collapse all                                    | ctrl+shift+ -                   |
| 展开方法实现-expand all                                      | ctrl+shift+ +                   |

### 第5组：查找、替换与关闭

| 说明                                               | 快捷键       |
| -------------------------------------------------- | ------------ |
| 查找指定的结构                                     | ctlr+f       |
| 快速查找：选中的Word快速定位到下一个-find next     | ctrl+l       |
| 查找与替换-replace                                 | ctrl+r       |
| 直接定位到当前行的首位-move caret to line start    | home         |
| 直接定位到当前行的末位 -move caret to line end     | end          |
| 查询当前元素在当前文件中的引用，然后按 F3 可以选择 | ctrl+f7      |
| 全项目搜索文本-find in path ...                    | ctrl+shift+f |
| 关闭当前窗口-close                                 | ctrl+f4      |

### 第6组：调整格式

| 说明                                         | 快捷键           |
| -------------------------------------------- | ---------------- |
| 格式化代码-reformat code                     | ctrl+alt+l       |
| 使用单行注释-comment with line comment       | ctrl + /         |
| 使用/取消多行注释-comment with block comment | ctrl + shift + / |
| 选中数行，整体往后移动-tab                   | tab              |
| 选中数行，整体往前移动-prev tab              | shift + tab      |

## 2-Debug快捷键

| 说明                                                  | 快捷键        |
| ----------------------------------------------------- | ------------- |
| 单步调试（不进入函数内部）- step over                 | F8            |
| 单步调试（进入函数内部）- step into                   | F7            |
| 强制单步调试（进入函数内部） - force step into        | alt+shift+f7  |
| 选择要进入的函数 - smart step into                    | shift + F7    |
| 跳出函数 - step out                                   | shift + F8    |
| 运行到断点 - run to cursor                            | alt + F9      |
| 继续执行，进入下一个断点或执行完程序 - resume program | F9            |
| 停止 - stop                                           | Ctrl+F2       |
| 查看断点 - view breakpoints                           | Ctrl+Shift+F8 |
| 关闭 - close                                          | Ctrl+F4       |



## 3-IDEA查看一个类中的所有结构

在IDEA中查看一个类中的所有方法，可以使用以下方法：

1. 打开该类文件
2. 在编辑器中使用快捷键Ctrl + F12打开类文件的结构试图。
3. 在结构视图中可以看到该类中的所有方法与属性，
4. 可以通过快捷键 Ctrl + 鼠标左键点击方法名或者属性名来跳转到定义。
5. 另外，在编辑器中输入方法/属性名的前几个字符，IDEA会自动提示该类中的方法名，可以快速定位到需要查看的方法。

![](.\images\Snipaste_2023-10-31_09-47-00.png)



