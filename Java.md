## <h3 id="0">目录</h3>
* [字符串比较](#1)
* [码点和代码单元](#2)
* [跳出多重嵌套循环](#3)
* [Java中总是采用按值调用](#4)
* [i *= i++ + i 的结果](#5)
* [📌关于Java中protected的作用范围](#6)
* [Java中静态方法中为什么不能使用this、super和直接调用非静态方法](#7)
* [📝有n步台阶，一次只能上1步或2步，共有多少种走法](#8)
* [📌并发编程的三大特性：原子性、可见性、有序性(搬运)](#9)
* [Java子类重写(覆盖)父类的方法必须满足的条件(搬运)](#10)
* [String类型赋值问题](#11)
* [Java代码块和静态代码块加载顺序](#12)
* [为什么在局部内部类中使用外部类方法的局部变量要加 final 呢](#13)
* [📌try(catch)块中的return语句和finally块中的语句执行顺序](#14)
* [📌this与super以及两者在内部类中的使用](#15)
* [📌构造器内部的多态方法和行为](#16)
* [Java包装类一些注意的点](#17)
* [📌泛型相关](#18)
* [杂记](#-1)
---

# <h4 id="1">字符串比较[⬆(返回目录)](#0)</h4>

**一定不要使用 == 运算符检测两个字符串是否相等!!!** 因为，Java文档中将String类对象称为是**不可变的(immutable)**，优点是**编译器可以让字符串共享**。所以，这个运算符只能够确定两个字符串是否存放再同一个位置上。当然，如果字符串再同一个位置上，他们必然相等。但是，完全有可能将内容相同的多个字符串副本放置在不同的位置上。==实际上只有字符串**字面量**是共享的 **（在常量池中共享一块空间，实际上比较的还是地址空间是否相同）**，而+或substring等操作得到的字符串并不共享。需要进行比较时需要使用**equals函数**或**compareTo函数**(函数具体用法见API)。 **（java核心技术卷1[11版] p46，47）**

```java
    // 错误例子
    String temp = "Hello";
    String temp_2 = "Hello";
    if (temp == temp_2) {
        // probably true
    }
    if (temp == "Hello") {
    	// probably true
    }
    if (temp.substring(0, 3) == "Hel") {
    	// probably false
    }
    
    //正确例子
    if (temp.equals("Hello") {
    	// true
    }
    if (temp.compareTo("Hello") == 0) {
		// true
		// 使用equals更为清晰
	}
```
---

# <h4 id="2">码点和代码单元[⬆(返回目录)](#0)</h4>

关于**码点(Code Point)** 和 **代码单元** [**链接1**](https://blog.csdn.net/diehuang3426/article/details/83422309) [**链接2**](https://www.jianshu.com/p/668356dd8089)**（Java核心技术卷1[11版] p48，49）**

> **代码点（Code Point）**：在 Unicode 代码空间中的一个值，取值 0x0 至 0x10FFFF，代表一个字符。
    
> **代码单元（Code Unit）**：在具体编码形式中的最小单位。比如 UTF-16 中一个 code unit 为 16 bits，UTF-8 中一个 code unit 为 8 bits。一个 code point 可能由一个或多个 code unit(s) 表示。在 U+10000 之前的 code point 可以由一个 UTF-16 code unit 表示，U+10000 及之后的 code point 要由两个 UTF-16 code units 表示
---

# <h4 id="3">跳出多重嵌套循环[⬆(返回目录)](#0)</h4>

尽管Java设计者将**goto**作为保留字，但实际上并没有打算在语言中使用它，因为通常认为使用**goto**很拙劣，通常会使代码的逻辑难以阅读及维护。但是，Java提供了一种**带标签的break**语句，用于跳出多重嵌套循环。示例代码如下：**(java核心技术卷1[11版] p75)**

```java
Scanner in = new Scanner(System.in);
int n;
// set the label
read_data:
// this loop statement is tagged with the label
while (...) {
    ...
    // this loop is not labeled
    for (...) {
        System.out.println("Enter a number >= 0: ");
        n = in.nextInt();
        // should never happen-can't go on
        if (n < 0) {
            break read_data;
            // break out of read_data loop
        }
        ...
    }
}
// this statement is executed immediately affer the labeled break
if (n < 0) {
    // check for bad situation
    // deal with bad situation
} else {
    // carry out normal processing
}
```

如果输入有误，执行带标签的break会跳转到带标签的语句块末尾。与任何使用break语句的代码一样，然后需要检测循环是正常结束，还是由break跳出。
> 事实上，可以将标签应用到任何语句，甚至可以将其应用到if语句或者块语句，如下所示：

```java
// set the label
label:
{
    ...
    if (codition) break label; // exits block
    ...
}
// jumps here when the break statement executes
```

> 因此，如果确实希望使用goto语句，而且一个代码块恰好在你想要跳到的位置之前结束，就可以使用break语句！当然，**并不提倡使用这种方式**。另外需要注意，只能**跳出**语句块，而不能**跳入**语句块。
---

# <h4 id="4">Java中总是采用按值调用[⬆(返回目录)](#0)</h4>

Java程序设计语言总是采用**按值调用**。也就是说，方法得到的是所有参数值的**一个副本**。具体来讲，方法不能修改传递给它的任何参数变量的内容。**(Java核心技术卷1[11版] p121-p124)**
1. 当传递的参数是基本数据类型时：

```java
public static void tripleValue(double x) {
    x = 3 * x;
}
double percent = 10.0;
tripleValue(percent);
// 结果x的值为30，但percent的值仍未10
// 因为x初始化为percent值的一个副本，但这个方法结束后，参数变量x不再使用
```

2. 当传递的参数是引用对象时：（类比cpp中**恶心**的指针）

```java
public static void swap(Employee x, Employee y) {
    Employee temp = x;
    x = y;
    y = temp;
}
// Java10 引入了 var 关键字来声明变量
var a = new Employee("Alice", ...);
var b = new Employee("Bob", ...);
swap(a, b);
// 此时，没有卵用。在方法结束时参数变量 x 和 y 被丢弃了
// 原来的变量 a 和 b 仍然引用这个方法调用之前所引用的对象
```

3. 但是当想改变对象参数的状态时是可以的：

```java
public static void tripleSalary(Employee x) {
    x.raiseSalary(200);
}
harry = new Employee(...);
tripleSalary(harry);
// 1. x 初始化为 harry值的一个副本，这里就是一个对象引用
// 2. raiseSalary方法应用于这个对象引用。x和 harry同时引用的那个Employee对象的工资提高了200%
// 3. 方法结束后，参数变量 x不再使用。当然，对象变量 harry继续引用那个工资增至3倍的员工对象
```

> **总结：** 方法不能修改基本数据类型的参数(即数值型或布尔型)。但是，方法可以改变对象参数的状态。同时，方法不能让一个对象参数引用一个新的对象。
---

# <h4 id="5">i *= i++ + i 的结果[⬆(返回目录)](#0)</h4>

[**参考资料**](https://zhuanlan.zhihu.com/p/40645506)

```java
// 如下代码的运算结果是：
int i = 2, j = 2;
i *= i++ + i;
j *= ++j + j;
System.out.println("i=" + i); // i=10
System.out.println("j=" + j); // j=12
```
用javap -c 指令看一下JVM执行过程如下：
```dos
public static void main(java.lang.String[]);
    Code:
       0: iconst_2
       1: istore_1
       2: iconst_2
       3: istore_2
       4: iload_1
       5: iload_1
       6: iinc          1, 1
       9: iload_1
      10: iadd
      11: imul
      12: istore_1
      13: iload_2
      14: iinc          2, 1
      17: iload_2
      18: iload_2
      19: iadd
      20: imul
      21: istore_2
      ...
```

- ```iconst_i```: 当int取值 -1~5 时，JVM采用iconst指令将常量压入栈中
- ```istore_i```: 弹出操作数栈栈顶元素，保存到局部变量表第i个位置
- ```iload_i```: 加载局部变量表的第i个变量到操作数栈顶
- ```iinc i, j```: 把局部变量i,增加j
- ```iadd```: 操作数栈中的前两个int相加，并将结果压入操作数栈顶
- ```imul```: 操作数栈中的前两个int相乘，并将结果压入操作数栈顶
> **工作中谁这样写，多半是脑子有泡**，目的是为了面试 ，是为了给需要这个问题的人看
---

# <h4 id="6">📌关于Java中protected的作用范围[⬆(返回目录)](#0)</h4>

很多Java的书中对protected的作用范围的描述都是 **“对本包和所有子类可见”。** 实际上，描述的很准确，但是关于**子类**部分的描述过于简略。而实际情况却很复杂（**当然，出现这种复杂的情况极有可能是写代码的思路有问题，没有很好地运用Java的编程思想**），比如：在```com.parent```包中定义一个```Parent```类并声明一个```protected String str = "Whatever";```，在```com.child```包中定义一个```Child```类并且继承自```Parent```类，之后在```Child```类中使用```new Parent().str```就会报一个**属性不可见**的错误。

- 先看一个解释：
> 6.6.2 Details on protected Access
> 
> A protected member or constructor of an object may be accessed from outside the package in which it is declared only by code that is responsible for the implementation of that object.

这个解释其实很清楚了，但是如果思考过后还是没有很明白，看下面这个示例：

```java
//in Parent.java
package parentpackage;
public class Parent {
    protected String parentVariable = "whatever"; // define protected variable
}

// in Children.java
package childenpackage;
import parentpackage.Parent;

class Children extends Parent {
    Children(Parent withParent ){
        System.out.println( this.parentVariable ); // works well.
        System.out.print(withParent.parentVariable); // doesn't work
    } 
}
// It is accessible, but only to its own variable.
```

> **总结：** 对包可见很好理解，对子类可见或许解释为对子类的实例对象可见更好理解。[**StackOverflow关于这个问题的链接**(被采纳的答案中包含了出现这种问题时更好的代码设计模式)](https://stackoverflow.com/questions/3071720/why-cant-my-subclass-access-a-protected-variable-of-its-superclass-when-its-i/3071889#)
---

# <h4 id="7">Java中静态方法中为什么不能使用this、super和直接调用非静态方法[⬆(返回目录)](#0)</h4>

这个要从java的内存机制去分析，首先当你New 一个对象的时候，并不是先在堆中为对象开辟内存空间，而是先将类中的静态方法（带有static修饰的静态函数）的代码加载到一个叫做方法区的地方，然后再在堆内存中创建对象。所以说静态方法会随着类的加载而被加载。当你new一个对象时，该对象存在于对内存中，this关键字一般指该对象，但是如果没有new对象，而是通过类名调用该类的静态方法也可以。

程序最终都是在内存中执行，变量只有在内存中占有一席之地时才会被访问，类的静态成员（静态变量和静态方法）属于类本身，在类加载的时候就会分配内存，可以通过类名直接去访问，非静态成员（非静态变量和非静态方法）属于类的对象，所以只有在类的对象创建（实例化）的时候才会分配内存，然后通过类的对象去访问。

在一个类的静态成员中去访问非静态成员之所以会出错是因为在类的非静态成员不存在的时候静态成员就已经存在了，访问一个内存中不存在的东西当然会出错。

**静态成员变量虽然独立于对象，但是不代表不可以通过对象去访问，所有的静态方法和静态变量都可以通过对象访问（只要访问权限足够）**

**static是不允许用来修饰局部变量。不要问为什么，这是Java语法的规定**(static修饰的变量或者方法是在编译的时候放入方法区的，是共享的；但局部变量是运行时放入栈帧中的，是线程所特有的，只有所在线程才能访问到)
[参考链接](https://www.cnblogs.com/jxldjsn/p/11410329.html)

---

# <h4 id="8">📝有n步台阶，一次只能上1步或2步，共有多少种走法[⬆(返回目录)](#0)</h4>

```java
public static int upstairs(int n) {
    /*
    * 1级：1
    * 2级：2   1步1步  和 2步
    * 3级：上到1级的总走法（因为从1级上到3级直接跨2步）  + 上到2级的总走法（从2级上到3级直接跨1步）
    * 					因为从1级走1步就归到上到2级的走法里面了
    * 4级：上到2级的总走法 + 上到3级的总走法
    * ...
    * 即最后一步要么跨1步，要么跨2步
    */
    if (n <= 2) {
        return n;
    }
    return upstairs(n - 1) + upstairs(n - 2);
}
```
---

# <h4 id="9">📌并发编程的三大特性：原子性、可见性、有序性(搬运)[⬆(返回目录)](#0)</h4>

[**文章搬运地址**](https://zhuanlan.zhihu.com/p/141744632) （搬运请注明原出处，我只是大自然的搬运工，原作者码字不易🍖）

**Java内存模型**

在讲三大特性之前先简单介绍一下Java内存模型（Java Memory Model，简称JMM），了解了Java内存模型以后，可以更好地理解三大特性。

Java内存模型是一种抽象的概念，并不是真实存在的，它描述的是一组规范或者规定。JVM运行程序的实体是线程，每一个线程都有自己私有的工作内存。Java内存模型中规定了所有变量都存储在主内存中，主内存是一块共享内存区域，所有线程都可以访问。但是线程对变量的读取赋值等操作必须在自己的工作内存中进行，在操作之前先把变量从主内存中复制到自己的工作内存中，然后对变量进行操作，操作完成后再把变量写回主内存。线程不能直接操作主内存中的变量，线程的工作内存中存放的是主内存中变量的副本。

**原子性（Atomicity）**

**什么是原子性**

原子性是指：在一次或者多次操作时，要么所有操作都被执行，要么所有操作都不执行。

一般说到原子性都会以银行转账作为例子，比如张三向李四转账100块钱，这包含了两个原子操作：在张三的账户上减少100块钱；在李四的账户上增加100块钱。这两个操作必须保证原子性的要求，要么都执行成功，要么都执行失败。不能出现张三的账户减少100块钱而李四的账户没增加100块钱，也不能出现张三的账户没减少100块钱而李四的账户却增加100块钱。

**原子性示例**

**示例一**

```i = 1;```

根据上面介绍的Java内存模型，线程先把```i=1```写入工作内存中，然后再把它写入主内存，就此赋值语句可以说是具有原子性。

**示例二**

```i = j;```

这个赋值操作实际上包含两个步骤：线程从主内存中读取j的值，然后把它存入当前线程的工作内存中；线程把工作内存中的 i 改为 j 的值，然后把i的值写入主内存中。虽然这两个步骤都是原子性的操作，但是合在一起就不是原子性的操作。

**示例三**

**i++;**

这个自增操作实际上包含三个步骤：线程从主内存中读取i的值，然后把它存入当前线程的工作内存中；线程把工作内存中的i执行加1操作；线程再把i的值写入主内存中。和上一个示例一样，虽然这三个步骤都是原子性的操作，但是合在一起就不是原子性的操作。

从上面三个示例中，我们可以发现：简单的读取和赋值操作是原子性的，但把一个变量赋值给另一个变量就不是原子性的了；多个原子性的操作放在一起也不是原子性的。

**如何保证原子性**

在Java内存模型中，只保证了基本读取和赋值的原子性操作。如果想保证多个操作的原子性，需要使用```synchronized```关键字或者```Lock```相关的工具类。如果想要使 int、long 等类型的自增操作具有原子性，可以用```java.util.concurrent.atomic```包下的工具类，如：```AtomicInteger```、```AtomicLong```等。另外需要注意⚠的是，**```volatile```关键字不具有保证原子性的语义。**

**可见性（Visibility）**

**什么是可见性**

可见性是指：当一个线程对共享变量进行修改后，另外一个线程可以立即看到该变量修改后的最新值。

**可见性示例**

```java
package onemore.study;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisibilityTest {
    public static int count = 0;

    public static void main(String[] args) {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

        //读取count值的线程
        new Thread(() -> {
            System.out.println("开始读取count...");
            int i = count;//存放count的更新前的值
            while (count < 3) {
                if (count != i) {//当count的值发生改变时，打印count被更新
                    System.out.println(sdf.format(new Date()) + " count被更新为" + count);
                    i = count;//存放count的更新前的值
                }
            }
        }).start();

        //更新count值的线程
        new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                //每隔1秒为count赋值一次新的值
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(sdf.format(new Date()) + " 赋值count为" + i);
                count = i;

            }
        }).start();
    }
}
```

在运行代码之前，先想一下运行的输出是什么样子的？在更新count值的线程中，每一次更新count以后，在读取count值的线程中都会有一次输出嘛？让我们来看一下运行输出是什么：

```
开始读取count...
17:21:54.796 赋值count为1
17:21:55.798 赋值count为2
17:21:56.799 赋值count为3
```

从运行的输出看出，读取count值的线程一直没有读取到count的最新值，这是为什么呢？因为在读取count值的线程中，第一次读取count值时，从主内存中读取count的值后写入到自己的工作内存中，再从工作内存中读取，之后的读取的count值都是从自己的工作内存中读取，并没有发现更新count值的线程对count值的修改。

**如何保证可见性**

在Java中可以用以下**3种**方式保证可见性。

**使用```volatile```关键字**

当一个变量被```volatile```关键字修饰时，其他线程对该变量进行了修改后，会导致当前线程在工作内存中的变量副本失效，必须从主内存中再次获取，当前线程修改工作内存中的变量后，同时也会立刻将其修改刷新到主内存中。

**使用```synchronized```关键字**

```synchronized```关键字能够保证同一时刻只有一个线程获得锁，然后执行同步方法或者代码块，并且确保在锁释放之前，会把变量的修改刷新到主内存中。

**使用```Lock```相关的工具类**

```Lock```相关的工具类的```lock```方法能够保证同一时刻只有一个线程获得锁，然后执行同步代码块，并且确保执行```Lock```相关的工具类的```unlock```方法在之前，会把变量的修改刷新到主内存中。

**有序性（Ordering）**

**什么是有序性**

有序性指的是：程序执行的顺序按照代码的先后顺序执行。

在Java中，为了提高程序的运行效率，可能在编译期和运行期会对代码指令进行一定的优化，不会百分之百的保证代码的执行顺序严格按照编写代码中的顺序执行，但也不是随意进行重排序，它会保证程序的最终运算结果是编码时所期望的。这种情况被称之为**指令重排（Instruction Reordering）**。

**有序性示例**

```java
package onemore.study;

public class Singleton {
    private Singleton (){}

    private static boolean isInit = false;
    private static Singleton instance;

    public static Singleton getInstance() {
        if (!isInit) {//判断是否初始化过
            instance = new Singleton();//初始化
            isInit = true;//初始化标识赋值为true
        }
        return instance;
    }
}
```

这是一个有问题的[**单例模式**](https://www.runoob.com/design-pattern/singleton-pattern.html)示例，假如在编译期或运行期时指令重排，把```isInit = true;```重新排序到```instance = new Singleton();```的前面。在单线程运行时，程序重排后的执行结果和代码顺序执行的结果是完全一样的，但是多个线程一起执行时就极有可能出现问题。比如，一个线程先判断```isInit```为**false**进行初始化，本应在初始化后再把```isInit```赋值为**true**，但是因为指令重排没后初始化就把```isInit```赋值为**true**，恰好此时另外一个线程在判断是否初始化过，```isInit```为**true**就执行返回了```instance```，这是一个没有初始化的```instance```，肯定造成不可预知的错误。

**如何保证有序性**

这里就要提到Java内存模型的一个叫做**先行发生（Happens-Before）** 的原则了。如果两个操作的执行顺序无法从 **Happens-Before**原则推到出来，那么可以对它们进行随意的重排序处理了。**Happens-Before**原则有哪些呢？

- 程序次序原则：一段代码在单线程中执行的结果是有序的。
- 锁定原则：一个锁处于被锁定状态，那么必须先执行```unlock```操作后面才能进行```lock```操作。
- **volatile**变量原则：同时对```volatile```变量进行读写操作，写操作一定先于读操作。
- 线程启动原则：```Thread```对象的```start```方法先于此线程的每一个动作。
- 线程终结原则：线程中的所有操作都先于对此线程的终止检测。
- 线程中断原则：对线程```interrupt```方法的调用先于被中断线程的代码检测到中断事件的发生。
- 对象终结原则：一个对象的初始化完成先于它的```finalize```方法的开始。
- 传递原则：操作A先于操作B，操作B先于操作C，那么操作A一定先于操作C。

除了**Happens-Before**原则提供的天然有序性，我们还可以用以下几种方式保证有序性：

- 使用```volatile```关键字保证有序性。
- 使用```synchronized```关键字保证有序性。
- 使用```Lock```相关的工具类保证有序性。

**总结**

- 原子性：在一次或者多次操作时，要么所有操作都被执行，要么所有操作都不执行。
- 可见性：当一个线程对共享变量进行修改后，另外一个线程可以立即看到该变量修改后的最新值。
- 有序性：程序执行的顺序按照代码的先后顺序执行。

```synchronized```关键字和```Lock```相关的工具类可以保证原子性、可见性和有序性，```volatile```关键字可以保证可见性和有序性，⚠**不能**保证原子性。

---

# <h4 id="10">Java子类重写(覆盖)父类的方法必须满足的条件(搬运)[⬆(返回目录)](#0)</h4>
[**文章搬运地址**](https://www.jianshu.com/p/6ceb4c581d3b) （搬运请注明原出处，我只是大自然的搬运工，原作者码字不易🍖）

1. 父类中的方法在子类中必须可见，即子类继承了父类中的该方法（可以显式的使用 super 关键字来访问父类中的被重写的方法），如果父类中的方法为```private```类型的，那么子类虽能继承，但无法覆盖

2. 子类和父类的方法必须是实例方法，如果父类是```static```方法而子类是实例方法，或者相反都会报错。如果父类和子类都是```static```方法，那么子类隐藏父类的方法，而不是重写父类方法

3. 子类和父类的方法必须要具有相同的函数名称、参数列表，并且子类的返回值与父类相同或者是父类返回类型的子类型（jdk1.5 之后）。如果方法名称相同而参数列表不同（返回类型可以相同也可以不同），那么只是方法的重载，而非重写。如果方法名称和参数列表相同，返回值类型不同，子类返回值类型也不是父类返回值类型的子类，编译器就会报错

4. 子类方法的访问权限不能小于父类方法的访问权限（可以具有相同的访问权限或者子类的访问权限大于父类）。访问权限由高到低：```public```、```protected```、```(default)```、```private```。如果子类方法的访问权限低于父类，则编译器会给出错误信息

5. 子类方法不能比父类方法抛出更多的编译时异常（不是运行时异常），即子类方法抛出的编译时异常或者和父类相同或者是父类异常的子类。当然，子类也可以不抛出异常，或者抛出`RuntimeException`异常

6. 不能重写被标识为`final`的方法。`final`方法可以被继承，但不能被重写。一个方法如果被`final`修饰就意味着这个方法不会被改动

- **为何子类重写方法的访问权限不能低于父类中权限**

    因为向上转型及 Java 程序设计维护的原因

    假设一个父类 A 拥有的方法`public void setXXX() {}`可以被其他任意对象调用
    这个方法被子类 B 覆写后为`void setXXX() {}`即默认的访问权限只能被本包极其子类所访问

    假设其他包中的对象 C 调用方法：

    ```java
    void get(A a) {
        a.setXXX();
    }
    ```

    假设此时传入的对象为 B 类对象 b，此时 b 将转型为 a 但是 b 中的`setXXX()`调用权限已经被缩小了这将造成错误。所以子类对象不能比父类对象访问权限小

- **为何子类重写方法的返回类型必须与父类相同或者是父类返回类型的子类型**

    也是因为向上转型

    假设 B 是 A 的子类，初始化 a 为 B 类型，并调用 B 类重写过的方法` func()：`

    ```java
    A a = new B();
    C aa = a.func();
    ```

    假设原本 A 类的`func()`方法返回类型为 C，那么 B 类的`func()`方法返回类型就只能是 C 类或 C 类的子类型了

    > 其实，我个人认为这里这个例子举得并不是很好。换个思路，从代码复用性角度去想，假设有一个场景如下：

    ```java
    A[] arr = new A[len];
    // 添加一些A子类的对象进数组，但并不只有一种子类对象
    arr[0] = new B(); // B extends A
    arr[1] = new C(); // C extends A
    ...
    for (A a : arr) {
        // 这里可以统一用 C 类接受
        // 因为返回的都是 C 类的实例对象或 C 类的子类的实例对象
        C aa = a.func();
        ...
    }
    ```

    > 这样就会比较方便了，如果有更好的例子或更合理的解释，欢迎留言

- **为何子类方法不能比父类方法抛出更多的编译时异常**

    子类在覆盖父类方法的时候，父类的引用可以调用该方法。如果父类的引用调用子类的方法，那么这个多抛出来的异常，就可能处于一种无法被处理的状态
---

# <h4 id="11">String类型赋值问题[⬆(返回目录)](#0)</h4>

`String`类特点：
1. 字符串`String`类型本身是`final`声明的，意味着我们不能继承`String`
2. 字符串的对象也是**不可变对象**，意味着一旦进行修改，就会产生**新对象**，因为`String`类中存储数据采用的是`private final char value[];`，**所有对字符串的操作(拼接，截取，赋值...)都是先创建一个新的字符串对象，然后再把新对象的引用赋给老对象，保证了字符串对象的不可变性**(反射除外，*反射先挖个坑*)
3. `String`对象内部是用字符数组进行保存的
4. 就因为字符串对象设计为不可变，那么所以字符串有**常量池**来保存很多常量对象

`String`的拼接操作：
1. 用'+'拼接  变量+变量  结果在**堆内存**
2. 用'+'拼接  变量+常量、常量+变量  结果也在**堆内存**
3. 用'+'拼接  常量+常量  结果在**常量池**
4. 用`concat`方法拼接，无论是常量还是变量都在**堆内存**

拼接操作示例代码：

```java
    String str1 = "shangguigu"; // str1是从常量池中拿的地址
    String str2 = "shang";
    final String str3 = "guigu"; // final声明后变为常量
    String str4 = str2 + str3; // str4的地址是堆内存的地址
    String str5 = "shang" + "guigu"; // str5是从常量池中拿的地址
    String str6 = "shang" + str3; // str6的地址是从常量池中拿的地址
    System.out.println(str1 == str4); // false
    System.out.println(str1 == str5); // true
    System.out.println(str1 == str6); // true
```

看一下`String`类型的源码：

```java
...
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    // 在JDK1.9中，String的value被设计成byte[]，不再是char[]了
    // 这个是JDK1.8的源码
    private final char value[];
    ...
}
```

发现其中有个`final`修饰的`char`类型数组`value`，而且这个数组存有`String`类型变量的值（切分为多个字符存入这个字符数组），但是考虑如下赋值语句：

```java
String str = "aaa";
str = "bbb";
```

有没有感觉到有什么不对，是的，好像`final`修饰的`value`数组值被改变了，那好像也没什么不对。那再考虑下面代码：

```java
String str = "aaa";
str = "bbbbb";
```

好像又不对了，这里`final`修饰的`value`是个静态数组啊，在内存中应该是连续存储的啊，这里数组长度应该被改变了，就算是做了扩容操作，也应该要重新申请一个连续的地址空间重新赋给`value`啊，那不相当于修改了`final`修饰的引用类型变量的地址了嘛。

是的，这里不仅仅改变了`value`的地址，连`str`其实都已经不是之前的`str`了，实际上，这里恰好体现了`String`是一个引用类型的事实，`str = "bbbbb";`首先从常量池寻找是否存在`"bbbbb"`，如果没有就创建一个，然后返回一个`String`类型对象的副本，然后再把这个副本赋值给`str`。这个看着很像基本数据类型的赋值语句，实则是非常典型的引用类型的赋值语句。所以，用`final`修饰的`String`类型变量因无法修改其指向的地址，所以就无法修改。

```java
final String str = "aaa";
str = "bbbbb";
// error: Cannot assign a value to final variable 'str'
```

**但是**，当使用`String`类型有参构造器创建对象时，会和其他引用类型创建实例对象一样，会把值保存在堆空间中，而不是常量池。代码如下：

```java
String str = "aaa";
System.out.println(str == "aaa"); // true
System.out.println(new String("aaa") == "aaa"); // false
System.out.println(new String("aaa") == str); // false
```
---

# <h4 id="12">Java代码块和静态代码块加载顺序[⬆(返回目录)](#0)</h4>

先看如下代码：

```java
public class Person() {
    private String name = aaa();
    private static String tel = bbb();

    {
        System.out.println("实例初始化块");
    }
    static {
        System.out.println("静态初始化块");
    }

    public Person() {
        System.out.println("person 无参构造");
    }

    public String aaa() {
        System.out.println("this is aaa func()");
        return "aaa";
    }
    public static String bbb() {
        System.out.println("this is bbb func()");
        return "bbb";
    }
}

public class HelloWord {
    public static void main(String[] args) {
        Person person = new Person();
    }
}
```

输出结果如下：

```bash
this is bbb func()
静态初始化块
this is aaa func()
实例初始化块
person 无参构造
```

再看下面的代码：

```java
public class Person() {
    {
        System.out.println("实例初始化块");
    }
    static {
        System.out.println("静态初始化块");
    }

    private String name = aaa();
    private static String tel = bbb();

    public Person() {
        System.out.println("person 无参构造");
    }

    public String aaa() {
        System.out.println("this is aaa func()");
        return "aaa";
    }
    public static String bbb() {
        System.out.println("this is bbb func()");
        return "bbb";
    }
}

public class HelloWord {
    public static void main(String[] args) {
        Person person = new Person();
    }
}
```

输出结果如下：

```bash
静态初始化块
this is bbb func()
实例初始化块
this is aaa func()
person 无参构造
```

**所以**，静态代码块和其他静态资源在类加载时按代码顺序加载，非静态代码块和其他非静态资源在实例化时按代码顺序加载。

---

# <h4 id="13">为什么在局部内部类中使用外部类方法的局部变量要加 final 呢[⬆(返回目录)](#0)</h4>

```java
public class TestInner {
	public static void main(String[] args) {
		A obj = Outer.method();
		// 因为如果c不是final的，那么method方法执行完，method的栈空间就释放了，那么c也就消失了
		obj.a(); // 这里打印c就没有中可取了，所以把c声明为常量，存储在方法区中
	}
}

interface A {
	void a();
}

class Outer {
	public static A method() {
		final int c = 3;
		class Sub implements A {
			@Override
			public void a() {
				System.out.println("method.c = " + c);
			}
		}
		return new Sub();
	}
}
```

**局部内部类**中还可以使用所在方法的局部常量，即用```final```声明的局部变量
JDK1.8之后，如果某个局部变量在局部内部类中被使用了，自动加```final```.


其实**匿名内部类**是一种特色的**局部内部类**，只不过没有名称而已

---

# <h4 id="14">📌try(catch)块中的return语句和finally块中的语句执行顺序[⬆(返回目录)](#0)</h4>

看下面这段程序：（[参考资料](https://blog.csdn.net/sinat_22594643/article/details/80509266)）

```java
public class Test {

    public static void main(String[] args) {
        int res = func();
        System.out.println("res = " + res); // res = 1
    }

    public static int func() {
        int i = 1;
        try {
            System.out.println("try block");
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            System.out.println("finally block");
            i++;
            // return ++i; 这里如果打开这个注释就会返回3
        }
    }
}
```

- 首先，Java方法是在栈中执行的，栈是线程私有栈的单位，执行方法的线程会为每一个方法分配一小块栈空间来作为该方法执行时的内存空间，栈分为三个区域：
  1. **操作数栈**：用来保存正在执行的表达式中的操作数，数据结构中学习过基于栈的多项式求值算法，操作数栈的作用和这个一样
  2. **局部变量区**：用来保存方法中使用的变量，包括方法参数，方法内部声明的变量，以及方法中使用到的对象的成员变量或类的成员变量（静态变量），最后两种变量会复制到局部变量区，因此在多线程环境下，这种变量需要根据需要声明为```volatile```类型
  3. **字节码指令区**：这个不用解释了，就是方法中的代码翻译成的指令

- 其次，关于```return```和```finally```：

    要记住的就是在```return [expression];```这里跟在```return```后面的表达式返回的是```return```指令执行的时刻，操作数栈顶的值，不管```[expression]```是一个怎样的表达式，究竟做了些什么工作，对于```return```指令来说都不重要，他只负责把操作数栈顶的值返回。

    而return expression是分成两部分执行的:
    
    执行：```[expression];```

    执行：```return```指令;

    例如：```return x+y;```
    
    这句代码先执行```x+y```，再执行```return```；首先执行**将x以及y从局部变量区复制到操作数栈顶**的指令，然后**执行加法指令**，这个时候结果```x+y```的值会**保存在操作数栈的栈顶**，最后执行```return```指令，**返回操作数栈顶的值**。

    对于```return x;```先执行x，x也是一个表达式，这个表达式只有一个操作数，会执行**将变量x从局部变量区复制到操作数栈顶**的指令，然后执行```return```，**返回操作数栈顶的值**。因此```return x;```实际返回的是```return```指令执行时，x在操作数栈顶的**一个快照或者叫副本，而不是x这个值**。

- 而当存在```finally```语句块的时候：

    首先我们知道，```finally```语句是一定会执行（除一种情况外，即在```try```或```catch```中执行了```System.exit(status);```使JVM终止），但他们的执行顺序是怎么样的呢？他们的执行顺序如下：
    
    1. 执行：```expression```，计算该表达式，结果保存在操作数栈顶；
    2. 执行：操作数栈顶值（```expression```的结果）复制到局部变量区作为返回值；
    3. 执行：```finally```语句块中的代码；
    4. 执行：将第2步复制到局部变量区的返回值又复制回操作数栈顶；
    5. 执行：```return```指令，返回操作数栈顶的值；

    可以看到，在第一步执行完毕后，整个方法的返回值就已经确定了，由于还要执行```finally```代码块，因此程序会**将返回值暂存在局部变量区**，腾出操作数栈用来执行```finally```语句块中代码，等```finally```执行完毕，再将暂存的返回值又复制回操作数栈顶。所以无论```finally```语句块中执行了什么操作，都无法影响返回值，所以试图在```finally```语句块中修改返回值是徒劳的。因此，```finally```语句块设计出来的**目的只是为了让方法执行一些重要的收尾工作（释放/关闭资源）**，而不是用来计算返回值的。

    所以在```finally```中更改返回值是无效的，因为它只是**更改了操作数栈顶端复制到局部变量区的快照，并不能真正的更改返回值**，但是如果在```finally```中使用```return```的话则是会将新的操作数栈的顶端数据返回，而不是之前复制到局部变量区用作返回内容快照的值返回，所以这样是可以返回的，同样的在```catch```语句块里也是这样，只有重新出现了```return```才有可能更改返回值。

---

# <h4 id="15">📌this与super以及两者在内部类中的使用[⬆(返回目录)](#0)</h4>

[**```super```和```this```的异同**](https://www.runoob.com/w3cnote/the-different-this-super.html)：

- super(参数)：调用基类中的某一个构造函数（应该为构造函数中的第一条语句）
- this(参数)：调用本类中另一种形成的构造函数（应该为构造函数中的第一条语句）
- super:　它引用当前对象的直接父类中的成员（用来访问直接父类中被隐藏的父类中成员数据或函数，基类与派生类中有相同成员定义时如：super.变量名 super.成员函数据名（实参） this：它代表当前对象名（在程序中易产生二义性之处，应使用 this 来指明当前对象；如果函数的形参与类中的成员数据同名，这时需用 this 来指明成员变量名）
- 调用super()必须写在子类构造方法的第一行，否则编译不通过。每个子类构造方法的第一条语句，都是隐含地调用 super()，如果父类没有这种形式的构造函数，那么在编译的时候就会报错。
- super() 和 this() 类似,区别是，super() 从子类中调用父类的构造方法，this() 在同一类内调用其它方法。
- super() 和 this() 均需放在构造方法内第一行。
- 尽管可以用this调用一个构造器，但却不能调用两个。
- this 和 super 不能同时出现在一个构造函数里面，因为this必然会调用其它的构造函数，其它的构造函数必然也会有 super 语句的存在，所以在同一个构造函数里面有相同的语句，就失去了语句的意义，编译器也不会通过。
- this() 和 super() 都指的是对象，所以，均不可以在 static 环境中使用。包括：static 变量,static 方法，static 语句块。
- 从本质上讲，**`this`是一个指向本对象的指针, 然而`super`是一个 Java 关键字**。

先看下面这个程序，先自己想一下输出是什么：

```java
public class Test{
    public static void main(String[] args){
        Son s = new Son();
        s.func();
    }
}

class Person {
    Person() {
        System.out.println("Person Constructor");
    }
    public int getNumber(){
        System.out.println("Person:getNumber()");
        return 1;
    }
}

class Father extends Person {
    private int a = super.getNumber();
    Father() {
        System.out.println("Father Constructor");
        a =this.getNumber();
        func();
    }
    {
        System.out.println(this.hashCode());
        System.out.println(super.hashCode());
    }
    private int b = getNumber();
    public void func() {
        System.out.println("Father func");
        super.getNumber();
    }
    public int getNumber(){
        System.out.println("Father:getNumber()");
        return 1;
    }
}
class Son extends Father{
    private int a = super.getNumber();
    Son() {
        System.out.println("Son Constructor");
    }
    public int getNumber(){
        System.out.println("Son:getNumber()");
        return 1;
    }
}
```

输出如下：

```
Person Constructor
Person:getNumber()
1836019240
1836019240
Son:getNumber()
Father Constructor
Son:getNumber()
Father func
Person:getNumber()
Father:getNumber()
Son Constructor
Father func
Person:getNumber()
```

主要关注`private int a = super.getNumber();`这段代码，这里的`super.getNumber()`调用的是`Person`类的`getNumber()`方法，而不是运行时的实例的类型的父类（即`Father`类）的方法；类似的，`Father`类的`func()`方法中的`super.getNumber();`，虽然运行时对象实例的类型是`Son`类型，但是这里调用的也是`Person`中的`getNumber()`方法，而不是`Father`类中的。但是，`this`调用的都是运行时对象实例（即`Son`类的实例）中的方法。而且注意到这里对`this`和`super`调用`hashCode()`方法，返回值是相同的，验证了 **`this`是一个指向本对象的指针, 然而`super`是一个 Java 关键字**，相当于都在获取当前`Son`类型实例对象的哈希值。

所以，在JVM中`this`永远指向当前运行时的实例对象，而`super`则是以当前代码所在类的空间为基准去找父类。见下图：

![super作用空间](https://user-images.githubusercontent.com/35959679/101376394-7f59e580-38eb-11eb-967f-6ae124e657eb.png)

再看下面这个程序，先自己想一下输出是什么：

```java
public class SubClass extends SuperClass {

    public static void main(String[] args) throws Exception {
        SuperClass s = new SubClass();
//        SubClass s = new SubClass();
        s.show();
    }

    SubClass() throws Exception {
        System.out.println("SubClass constructor -> " + super.getClass().getName());
    }

    @Override
    void show() {
        super.show();
        System.out.println("SubClass show -> " + super.getClass().getName());
    }

    @Override
    void show2() {
        System.out.println("SubClass show2 -> " + super.getClass().getSuperclass().getName());
    }
}

class SuperClass {

    SuperClass() throws Exception {
        System.out.println("SuperClass constructor -> " + this.getClass().getName());
    }

    void show() {
        System.out.println("SuperClass show -> " + super.getClass().getName());
        show2();
    }
    
    void show2() {
        System.out.println("SuperClass show2");
    }
}
```

输出如下：

```
SuperClass constructor -> com.atguigu.mytest.SubClass
SubClass constructor -> com.atguigu.mytest.SubClass
SuperClass show -> com.atguigu.mytest.SubClass
SubClass show2 -> com.atguigu.mytest.SuperClass
SubClass show -> com.atguigu.mytest.SubClass
```

发现，`super.getClass().getName()`打印的是`SubClass`，这里其实是因为`Object`类下的`getClass()`方法的返回值返回的是**运行时实例对象的具体类型**。所以，这里`super`关键字虽然指向父类，但是此时实际的实例对象还是`SubClass`类型的实例，所以会打印`SubClass`。

```java
// Object.java
...
/**
 * Returns the runtime class of this {@code Object}. The returned
 * {@code Class} object is the object that is locked by {@code
 * static synchronized} methods of the represented class.
 *
 * <p><b>The actual result type is {@code Class<? extends |X|>}
 * where {@code |X|} is the erasure of the static type of the
 * expression on which {@code getClass} is called.</b> For
 * example, no cast is required in this code fragment:</p>
 *
 * <p>
 * {@code Number n = 0;                             }<br>
 * {@code Class<? extends Number> c = n.getClass(); }
 * </p>
 *
 * @return The {@code Class} object that represents the runtime
 *         class of this object.
 * @jls 15.8.2 Class Literals
 */
public final native Class<?> getClass();
...
```

---

# <h4 id="16">📌构造器内部的多态方法和行为[⬆(返回目录)](#0)</h4>

构造器调用的层次带来了一个有趣的两难问题。如果一个构造器的内部调用正在构造的对象的某个动态绑定方法，那会发什么什么呢？

在一般的方法内部，动态绑定的调用是在运行时才决定的，因为对象无法知道它是属于方法所在的那个类，还是属于那个导出类。

如果要调用构造器内部的一个动态绑定方法，就要用到那个方法的被覆盖后的定义。然而，这个调用的效果可能相当难于预料，因为被覆盖的方法在对象被完全构造之前就会被调用。这可能会造成一些难于发现的隐藏错误。

从概念上讲，构造器的工作实际上是创建对象（这并非是一件平常的工作）。在任何构造器内部，整个对象可能只是部分形成——我们只知道基类对象已经进行初始化。如果构造器只是在构建对象过程中的一个步骤，并且该对象所属的类是从这个构造器所属的类导出的，那么导出部分在当前构造器正在被调用的时刻仍旧是没有被初始化的。然而，一个动态绑定的方法调用却会向外深入到继承层次结构内部，它可以调用导出类里的方法。如果我们是在构造器内部这样做，那么就可能会调用某个方法，而这个方法所操纵的成员可能还未进行初始化——这肯定会招致灾难。

通过下面这个例子，我们会看到问题所在：

```java
// Constructors and polymorphism
// don't produce what you might expect
import static net.mindview.util.Print.*;

class Glyph {
    void draw() { print("Glyph.draw()"); }
    Glyph() {
        print("Glyph() before draw()");
        draw();
        print("Glyph() after draw()");
    }
}

class RoundGlyph extends Glyph {
    private int radius = 1;
    RoundGlyph(int r) {
        radius = r;
        print("RoundGlyph.RoundGlyph(). radius = " + radius);
    }
    void draw() {
        print("RoundGlyph.draw(). radius = " + radius);
    }
}

public class PolyConstructors {
    public static void main(String[] args) {
        new RoundGlyph(5);
    }
}

/*
Output:
Glyph() before draw()
RoundGlyph.draw(). radius = 0
Glyph() after draw()
RoundGlyph.draw(). radius = 5
*/
```

`Glyph.draw()`方法设计为将要被覆盖，这种覆盖是在`RoundGlyph`中发生的。但是`Glyph`构造器会调用这个方法，结果导致了对`RoundGlyph.draw()`的调用，这看起来似乎是我们的目的。但是如果看到输出结果，我们会发现当`Glyph`的构造器调用`draw()`方法时，`radius`不是默认初始值 1，而是 0。这可能导致在屏幕上只画了一个点，或者根本什么东西都没有；我们只能干瞪眼，并试图找出程序无法运转的原因所在。

初始化的实际过程是：
1. 在其他任何事物发生之前，将分配给对象的存储空间初始化成二进制的零。
2. 如前所述那样调用基类构造器。此时，调用被覆盖后的`draw()`方法（要在调用`RoundGlyph`构造器之前调用），由于步骤1的缘故，我们此时会发现`radius`的值为 0。
3. 按照声明的顺序调用成员的初始化方法。
4. 调用导出类的构造器主体。

这样做有一个优点，那就是所有东西都至少初始化成零（或者是某些特殊数据类型中与“零”等价的值），而不是仅仅留作垃圾。其中包括通过“组合”而嵌入一个类内部的对象引用，其值是`null`。所以如果忘记为该引用进行初始化，就会在运行时出现异常。查看输出结果时，会发现其他所有东西的值都会是零，这通常也正是发现问题的证据。

另一方面，我们应该对这个程序的结果相当震惊。在逻辑方面，我们做的已经十分完美，而它的行为却不可思议地错了，并且编译器也没有报错。（在这种情况下，C++语言会产生更合理的行为。）诸如此类的错误会很容易被人忽略，而且要花很长的时间才能发现。

因此，编写构造器时有一条有效的准则：**“用尽可能简单的方法使对象进入正常状态；如果可以的话，避免调用其他方法”**。在构造器内唯一能够安全调用的那些方法是**基类中的`final`方法（也适用于`private`方法，他们自动属于`final`方法）**。这些方法不能被覆盖，因此也就不会出现上述令人惊讶的问题。你可能无法总是能够遵循这条准则，但是应该朝着它努力。(Java编程思想 162-164)

类似例子如下：

例子1

```java
class Test3{
    public static void main(String[] args){
        new Sub().test();
        // output: Parent无参构造  Sub无参构造 Rose Jack
        System.out.println("--------------------------------");
        new Sub("John").test();
        // output: Parent有参构造 Sub有参构造 John Jack
    }
}
class Parent{
    String name = "Rose";// john
    public Parent(){
        System.out.println("Parent无参构造");
    }
    public Parent(String name){
        this.name = name;
        System.out.println("Parent有参构造");
    }
}
class Sub extends Parent{
    String name="Jack";
    public Sub(){
        System.out.println("Sub无参构造");
    }
    public Sub(String s){
        super(s);
        System.out.println("Sub有参构造");
    }
    public void test(){
        System.out.println(super.name);
        System.out.println(this.name);
    }
}
```

例子2

```java
public class TestInner{
    public static void main(String[] args){
        Outer.Inner in = new Sub();
        in.method();
    }
}
class Outer {
    Outer() {
        System.out.println("outer constructor");
    }
    abstract class Inner{
        abstract void method();
    }
}
class Sub extends Outer.Inner{
    // 这里static是因为Inner依赖于Outer的实例对象，同时Sub又继承自Inner
    // 所以要保证类加载时(构造器执行之前)就要有Outer实例对象
    static Outer out = new Outer();
    Sub(){
        // 这里out.super()是在调用Inner的无参构造器，因为Inner依赖于
        // Outer实例对象，所以要用实例对象out来调用Inner的无参构造器
        out.super();
    }
    @Override
    void method() {
        System.out.println("hello inner");
    }
}
/* output:
outer constructor
hello inner */
```

---

# <h4 id="17">Java包装类一些注意的点[⬆(返回目录)](#0)</h4>

- 包装类有自动装箱和自动拆箱的功能。包装类的实例对象在进行数学运算或逻辑运算时会自动拆箱（自动转为基本数据类型）进行运算，例如：`int a = integerInstance + 20;`或者`integerInstance == 20`。在用基本类型数据创建包装类对象时会自动装箱，例如：`Integer i1=127;`或者`Double d1=12.5;`。
- `Integer`类比较特殊，它有缓存。`IntegerCache`它是`Integer`的一个静态成员内部类，`Integer`类初始化时也会将`IntegerCache`类初始化，而`IntegerCache`类会将 -127~128 这 256 个整形数据存在常量池，每次如下初始化语句`Integer i7=127;`会调用`Integer`类的`valueOf()`方法，如果赋的值在缓存范围内则返回值在常量池中的地址，否则，调用`Integer`的有参构造器，在堆内存中开辟一个空间创建新的实例对象。

    `Integer.valueOf(int i)`代码如下：

    ```java
    // Integer.valueOf(int i)
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
    ```

    示例代码如下：

    ```java
    public class Demo {
        public static void main(String[] args) {
            Integer i7=127;
            Integer i8=127;
            System.out.println(i7==i8); // true

            Integer i1=128;
            Integer i2=128;
            System.out.println(i1==i2); // false
            // Integer类缓存范围 -128~127，128超出范围，调用Integer类的valueOf()方法
            // 该方法会return new Integer(i); 所以，会在堆中创建两个实例对象
            // “==”比较的是地址，所以 i1 == i2 -> false

            Double d1=12.5;
            Double d2=12.5;
            System.out.println(d1==d2); // false - Double类没有缓存(Float也没)

            Integer i3=new Integer(100);
            Integer i4=100;
            System.out.println(i3==i4); // false - i3在堆空间，100在缓存范围内，所以i4指向常量池

            Integer i5=128;
            int i6=128;
            System.out.println(i5==i6);// true - i5在运算的时候，会自动拆箱
        }
    }
    ```

- 在包装类中，缓存的基本数据类型值的范围如下：

    | 基本数据类型 | 包装类型 | 缓存范围 |
    | :---: | :---: | :---: |
    | byte | Byte | -128 ~ 127 |
    | short | Short | -128 ~ 127 |
    | int | Integer | -128 ~ 127 |
    | long | Long | -128 ~ 127 |
    | char | Character | 0 ~ 127 |
    | boolean | Boolean | true, false |
    | float | Float | 无 |
    | double | Double | 无 |

---

# <h4 id="18">📌泛型相关[⬆(返回目录)](#0)</h4>

开始看书上讲的泛型，有点似懂非懂，听完课回来又看看，发现这鬼东西门道好多，想整理一下，又不知从何下手...就记一些我懂了一点的东西吧

> **不深究的话只看 3、5、6 就够了**

### 1. 为什么要使用泛型

泛型类和泛型方法有类型参数，这使得它们可以准确地描述特定类型实例化时会发生什么。在有泛型之前，程序员必须使用`Object`编写适用于多种类型的代码。这很繁琐，也很不安全。

随着泛型的引入，Java 有了一个表述能力很强的类型系统，允许设计者详细地描述变量和方法的类型要如何变化。对于简单的情况，你会发现实现泛型代码很容易。不过，在更高级的情况下，对于实现者来说这会相当复杂。其目标是提供让其他程序员可以轻松使用的类和方法而不会出现意外。

Java 5 中泛型的引入从成为 Java 自最初发行以来最显著的变化。Java 的一个主要设计目标是支持与之前版本的兼容性（导致了 Java 中存在大量“陪跑类”，不过为了兼容性也无可奈何）。因此，Java 的泛型有一些让人不快的局限性。（*Java Core Ⅰ*）

在没有泛型之前，从集合中读取到的每一个对象都必须进行转换。如果有人不小心插入了类型错误的对象，在运行时的转换处理就会出错。有了泛型之后，你可以告诉编译器每个集合中接受哪些对象类型。编译器自动为你的插入进行转换，并在**编译时**告知是否插入了类型错误的对象。这样可以使程序更加安全，也更加清楚。

### 2. 泛型中的术语

| 术 语 | 范 例 |
| :---: | :---: |
| 参数化的类型 | `List<Sring>` |
| 实际类型参数 | `String` |
| 泛型 | `List<E>` |
| 形式类型参数 | `E` |
| 无限制通配符类型 | `List<?>` |
| 原生态类型 | `List` |
| 有限制类型参数 | `<E extends Number>` |
| 递归类型限制 | `<T extends Comparable<T>>` |
| 有限制通配符类型 | `List<? extends Number>` |
| 泛型方法 | `static <E> List<E> asList(E[] a)` |
| 类型令牌 | `String.class` |

### 3. 万恶之源——类型擦除

#### 3.1 类型擦除

无论何时定义一个泛型类型，都会自动提供一个相应的**原始类型**（*raw type*）。这个原始类型的名字就是去掉类型参数后的泛型类型名。类型变量会被**擦除**（*erased*），并替换为其**限定类型**（或者，对于无限定的变量则替换为`Object`）。

例如，`Pair<T>`的原始类型如下所示：

```java
public class Pair {
    private Object first;
    private Object second;

    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    public Object getFirst() { return first; }
    public Object getSecond() { return second; }

    public void setFirst(Object newValue) { first = newValue; }
    public void setSecond(Object newValue) { second = newValue; }
}
```

因为`T`是一个无限定的变量，所以直接用`Object`替换。

结果是一个普通的类，就好像 Java 语言中引入泛型之前实现的类一样。

在程序中可以包含不同类型的`Pair`，例如，`Pair<String>`或`Pair<LocalDate>`。不过擦除类型后，它们都会变成原始的`Pair`类型。

原始类型用**第一个**限定来替换类型变量，或者，如果没有给定限定，就替换为`Object`。例如，类`Pair<T>`中的类型变量没有显式的限定，因此，原始类型用`Object`替换`T`。假定我们声明了一个稍有不同的类型：

```java
public class Interval<T extends Comparable & Serializable> implements Serializable {
    private T lower;
    private T upper;
    ...
    public Interval(T first, T second) {
        if (first.compareTo(second) <= 0) { lower = first; upper = second; }
        else { lower = second; upper = first; }
    }
}
```

原始类型`Interval`如下所示：

```java
public class Interval implements Serializable {
    private Comparable lower;
    private Comparable upper;
    ...
    public Interval(Comparable first, Comparable second) { ... }
}
```

> 如果限定切换为`class Interval<T extends Serializable & Comparable>`，原始类型会用`Serializable`替换`T`，而编译器在必要时要向`Comparable`插入强制类型转换。为了提高效率，应该将标签（tagging）接口（即没有方法的接口，`Serializable`就是一个方法接口）放在限定列表的末尾。

#### 3.2 转换泛型表达式

编写一个泛型方法调用时，如果擦除了返回类型，编译器会插入强制类型转换。例如，对于下面这个语句序列，

```java
Pair<Employee> buddies = ...;
Employee buddy = buddies.getFirst();
```

`getFirst()`擦除类型后的返回类型是`Object`。编译器自动插入转换到`Employee`的强制类型转换。也就是说，编译器把这个方法调用转换为两条虚拟机指令：

- 对原始方法`Pair.getFirst`的调用。
- 将返回的`Object`类型强制转换为`Employee`类型。

当访问一个泛型字段时也要插入强制类型转换。假设`Pair`类的`first`字段和`second`字段都是公共的（也许这不是一种好的编程风格，但在 Java 中是合法的）。表达式

`Employee buddy = buddies.first;`

也会在结果字节码中插入强制类型转换。

#### 3.3 转换泛型方法

类型擦除也会出现在泛型方法中。程序员通常认为类似下面的泛型方法

```java
public static <T extends Comparable> T min(T[] a)
```

是整个一组方法，而擦除类型之后，只剩下一个方法：

```java
public static Comparable min(Comparable[] a)
```

注意，类型参数`T`已经被擦除了，只留下了限定类型`Comparable`。

方法的擦除带来了两个复杂问题。例如：

```java
class DateInterval extends Pair<LocalDate> {
    public void SetSecond(LocalDate second) {
        if (second.compareTo(getFirst()) >= 0)
            super.setSecond(second);
    }
    ...
}
```

日期区间是一对`LocalDate`对象，而且我们需要覆盖这个方法来确保第二个值永远不小于第一个值。这个类擦除后变成

```java
class DateInterval extends Pair { // after erasure
    public void setSecond(LocalDate second) { ... }
    ...
}
```

令人感到奇怪的是，还有另一个从`Pair`继承的`setSecond`方法，即

```java
public void setSecond(Object second)
```

这显然是一个不同的方法，因为它有一个不同类型的参数——`Object`，而不是`LocalDate`。不过，不应该不一样。考虑下面的语句序列：

```java
var interval = new DateInterval(...);
Pair<LocalDate> pair = interval; // OK -- assignment to superclass
pair.setSecond(aDate);
```

这里，我们希望`setSecond`调用具有多态性，会调用最合适的那个方法。由于`Pair`引用一个`DateInterval`对象，所以应该调用`DateInterval.setSecond`。问题在于类型擦除与多态发生了冲突。为了解决这个问题，编译器在`DateInterval`类中生成了一个**桥方法（bridge method）**：

```java
public void setSecond(Object second) { setSecond((LocalDate) second); }
```

请仔细跟踪以下语句的执行：

```java
pair.setSecond(aDate);
```

变量`pair`已经声明为类型`Pair<LocalDate>`，并且这个类型只有一个名为`setSecond`的方法，即`setSecond(Object)`。虚拟机在`pair`引用的对象上调用这个方法。这个对象是`DateInterval`类型，因而将会调用`DateInterval.setSecond(Object)`方法。这个方法是合成的**桥方法**。它会调用`DateInterval.setSecond(LocalDate)`，这正是我们想要的。

桥方法可能会变得更奇怪。假设`DateInterval`类也覆盖了`getSecond`方法：

```java
class DateInterval extends Pair<LocalDate> {
    public LocalDate getSecond() { return (LocalDate) super.getSecond(); }
    ...
}
```

在`DateInterval`类中，有两个`getSecond`方法：

```java
LocalDate getSecond() // defined in DateInterval
Object getSecond() // overrides the method defined in Pair to call the first method
```

不能这样编写 Java 代码（两个方法有相同的参数类型是不合法的，在这里，两个方法都没有参数）。但是，在虚拟机中，会由参数类型和返回类型共同指定一个方法。因此，编译器可以为两个仅返回类型不同的方法生成字节码，虚拟机能够正确地处理这种情况。

> **桥方法**不仅用于泛型类型。一个方法覆盖另一个方法时，可以指定一个更严格的返回类型，这是合法的。
> ```java
> public class Employee implements Cloneable {
>   public Employee clone() throws CloneNotSupportedException { ... }
> }
> ```
> `Object.clone`和`Employee.clone`方法被称为**有协变的返回类型（covariant return type）**。实际上，`Employee`类有**两个**克隆方法：
> ```java
> Employee clone() // defined above
> Object clone() // synthesized bridge method, overrides Object.clone
> ```
> 合成的桥方法会调用新定义的方法。

**总之，对于 Java 泛型的转换，需要记住以下几个事实：**
- **虚拟机中没有泛型，只有普通的类和方法。**
- **所有的类型参数都会替换为它们的限定类型。**
- **会合成桥方法来保持多态。**
- **为保持类型安全性，必要时会插入强制类型转换。**

### 4. 限制与局限性

太多了，挑一些应用场景多的写，这段摘自《*Java Core Ⅰ*》(11版) P338~P345。

#### 4.1 不能用基本类型实例化类型参数

不能用基本类型代替类型参数。因此，没有`Pair<double>`，只有`Pair<Double>`。当然，其原因就在于**类型擦除**。擦除之后，`Pair`类含有`Object`类型的字段，而`Object`不能存储`double`值。

#### 4.2 运行时类型查询只适用于原始类型

虚拟机中的对象总有一个特定的非泛型类型。因此，所有的类型查询只产生原始类型。例如，

```java
if (a instanceof Pair<String>) // ERROR
```

实际上仅仅测试 a 是否是任意类型的一个`Pair`。下面的测试同样如此：

```java
if (a instanceof Pair<T>) // ERROR
```

或强制类型转换：

```java
Pair<Sring> p = (Pair<String>) a; // warning -- can only test that a is a Pair
```

为提醒这一风险，如果试图查询一个对象是否属于某个泛型类型，你会得到一个编译器错误（使用`instanceof`时），或者得到一个警告（使用强制类型转换时）。

同样的道理，`getClass`方法总是返回原始类型。例如：

```java
Pair<String> stringPair = ...;
Pair<Employee> employeePair = ...;
if (stringPair.getClass() == employeePair.getClass()) // they are equal
```

其比较结果是`true`，这是因为两次`getClass`调用都返回`Pair.class`。

#### 4.3 不能创建参数化类型的数组

不能实例化参数化类型的数组，例如：

```java
var table = new Pair<String>[10]; // ERROR
```

擦除之后，`table`的类型是`Pair[]`。可以把它转换为`Object[]`：

```java
Object[] objarray = table;
```

**数组会记住它的元素类型**，如果试图存储其他类型的元素，就会抛出一个`ArrayStoreException`异常：

```java
objarray[0] = "Hello"; // ERROR -- component type is Pair
```

不过对于泛型类型，擦除会使这种机制无效。以下赋值

```java
objarray[0] = new Pair<Employee>();
```

尽管能够通过数组存储的检查，但仍会导致一个类型错误。出于这个原因，不允许创建参数化类型的数组。

需要说明的是，只是不允许**创建**这些数组，而**声明**类型为`Pair<String>[]`的变量仍是合法的。不过不能用`new Pair<String>[10]`初始化这个变量。

> **注释**：可以声明通配类型的数组，然后进行强制类型转换：
> ```java
> var table = (Pair<String>[]) new Pair<?>[10];
> ```
> 结果将是不安全的。如果在`table[0]`中存储一个`Pair<Employee>`，然后对`table[0].getFirst()`调用一个`String`方法，会得到一个`ClassCastException`异常。

> **提示**：如果需要收集参数化类型对象，简单地使用`ArrayList:ArrayList<Pair<String>>`更安全、有效。

#### 4.4 Varargs警告

略。（见《*Java Core Ⅰ*》(11版) P339）

#### 4.5 不能实例化类型变量

不能在类似`new T(...)`的表达式中使用类型变量。例如，下面的`Pair<T>`构造器就是非法的：

```java
public Pair() { first = new T(); second = new T(); } // ERROR
```

类型擦除将 T 变成`Object`，而你肯定不希望调用`new Object()`。

在 Java 8 之后，最好的解决办法是让调用者提供一个构造器表达式。例如：

```java
Pair<String> p = Pair.makePair(String::new);
```

#### 4.6 不能构造泛型数组（新的提案中已经部分可以了 @date 2020/12/13）

> 目前Project Valhalla的Model 3方案里，如果泛型类型参数T是原始类型（没错，T可以是原始类型了！），那么new T[size]就是可以支持的；如果T是引用类型则跟Java 5开始的规定一样，还是不能new T[size]。

就像不能实例化泛型实例一样，也不能实例化数组。不过原因有所不同，毕竟数组可以填充`null`值，看上去好像可以安全地构造。不过，数组本身也带有类型，用来监控虚拟机中的数组存储。这个类型会被擦除。例如，考虑下面的例子：

```java
public static <T extends Comparable> T[] minmax(T...a) {
    T[] mm = new T[2]; // ERROR
    ...
}
```

类型擦除会让这个方法总是构造`Comparable[2]`数组。

如果数组仅仅作为一个类的私有字段，那么可以将这个数组的元素类型声明为擦除的类型并使用强制类型转换。例如，`ArrayList`类可以如下实现：

```java
public class ArrayList<E> {
    private Object[] elements;
    ...
    @SuppressWarnings("unchecked") public E get(int n) { return (E) elements[n]; }
    public void set(int n, E e) { elements[n] = e; } // no cast needed
}
```

但实际的实现没有这么清晰：

```java
public class ArrayList<E> {
    private E[] elements;
    ...
    public ArrayList() { elements = (E[]) new Object[10]; }
}
```

这里，强制类型转换`E[]`是一个假象，而类型擦除使其无法察觉。

#### 4.7 泛型的静态上下文中类型变量无效

不能在静态字段或方法中引用类型变量。例如，下面的做法看起来很聪明，但实际上行不通：

```java
public class Singleton<T> {
    private static T singleInstance; // ERROR

    public static T getSingleInstance() { // ERROR
        if (singleInstance == null) { construct new instance of T }
        return singleInstance;
    }
}
```

如果这样可行，程序就可以声明一个`Singleton<Random>`共享一个随机数生成器，另外声明一个`Singleton<JFileChooser>`共享一个文件选择器对话框。但是，这样是行不通的。类型擦除之后，只剩下`Singleton`类，它只包含一个`singleInstance`字段。因此，禁止使用带有类型变量的静态字段和方法。

#### 4.8 不能抛出或捕获泛性类的实例

既不能抛出也不能捕获泛型类的对象。实际上，泛型类扩展`Throwable`甚至都是不合法的。例如，以下定义就不能正常编译：

```java
public class Problem<T> extends Exception { /*...*/ }
// ERROR -- can't extend Throwable
```

`catch`子句中不能使用类型变量。例如，以下方法将不能编译：

```java
public static <T extends Throwable> void doWork(Class<T> t) {
    try {
        do work
    } catch (T e) { // ERROR -- can't catch type variable
        Logger.global.info(...);
    }
}
```

不过，在异常规范中使用类型变量是允许的。以下方法是合法的：

```java
public static <T extends Throwable> void d {
    {
        do work
    }
    catch (Throwable realCause) {
        t.initCause(realCause);
        throw t;
    }
}
```

#### 4.9 可以取消对检查型异常的检查

略。（见《*Java Core Ⅰ*》(11版) P343）

#### 4.10 注意擦除后的冲突

略。（见《*Java Core Ⅰ*》(11版) P345）

### 5. 泛型类型的继承规则

考虑一个类和一个子类，如`Employee`和`Manager`。`Pair<Manager>`是`Pair<Employee>`的一个子类吗？或许人们会感到奇怪，答案是“不是”。例如，下面的代码将不能成功编译：

```java
Manager[] topHonchos = ...;
Pair<Employee> result = ArrayAlg.minmax(topHonchos); // ERROR
```

`minmax`方法返回`Pair<Manager>`，而不是`Pair<Employee>`，并且这样的赋值是不合法的。

无论 S 与 T 有什么关系，通常，`Pair<S>`与`Pair<T>`都**没有任何关系**（如下图所示）。

![pair类之间没有继承关系](https://github-production-user-asset-6210df.s3.amazonaws.com/35959679/102007332-0cc18d80-3d63-11eb-99ec-a29583395d3f.jpg)

这看起来是一个严格的限制，不过对于类型安全非常必要。假设允许将`Pair<Manager>`转换为`Pair<Employee>`。考虑下面的代码：

```java
var managerBuddies = new Pair<Manager>(ceo, cfo);
Pair<Employee> employeeBuddies = managerBuddies; // illegal, but suppose it wasn't
employeeBuddies.setFirst(lowlyEmplyee);
```

显然，最后一句是合法的。但是`employeeBuddies`和`managerBuddies`引用了**同样的对象**。现在我们会把CFO和一个普通员工组成一对，这对于`Pair<Manager>`来说应该是不可能的。

> **注释**：前面看到的是泛型类型与 Java 数组之间的一个重要区别。可以将一个`Manager[]`数组赋给一个类型为`Employee[]`的变量：
> ```java
> Manager[] managerBuddies = { ceo, cfo };
> Employee[] employeeBuddies = managerBuddies; // OK
> ```
> 不过，数组有特别的保护。如果试图将一个低级别的员工存储到`employeeBuddies[0]`，虚拟机将会抛出`ArrayStoreException`异常。

总是可以将参数化类型转换为一个原始类型。例如，`Pair<Employee>`是原始类型`Pair`的一个子类型。在与遗留代码交互时，这个转换非常比要。

转换成原始类型会产生类型错误！例如：

```java
var managerBuddies = new Pair<Manager>(ceo, cfo);
Pair rawBuddies = managerBuddies; // OK
rawBuddies.setFirst(new File("...")); // only a compile-time warning
```

当使用`getFirst`获得外来对象并赋值给`Manager`变量时，与以往一样，会抛出`ClassCastException`异常。这里失去的只是泛型程序设计提供的附加安全性。

最后，**泛型类可以扩展或实现其他的泛型类**。就这一点而言，它们与普通的类没有什么区别。例如，`ArrayList<T>`类实现了`List<T>`接口。这意味着，一个`ArrayList<Manager>`可以转换为一个`List<Manager>`。但是，如前面所见，`ArrayList<Manager>`不是一个`ArrayList<Employee>`或`List<Employee>`。下图展示了它们之间的这种关系。

![泛型列表类型中子类型间的关系](https://github-production-user-asset-6210df.s3.amazonaws.com/35959679/102082955-c8f78280-3e4d-11eb-9265-0ac680a39c0a.jpg)

### 6. 通配符类型

#### 6.1 通配符概念

在通配符类型中，允许类型参数发生变化。例如，通配符类型

`Pair<? extends Employee>`

表示任何泛型`Pair`类型，它的类型参数是`Employee`的子类，如`Pair<Manager>`，但不是`Pair<String>`。

假设要编写一个打印员工对的方法，如下所示：

```java
public static void printBuddies(Pair<Employee> p) {
    Employee first = p.getFirst();
    Employee second = p.getSecond();
    System.out.println(first.getName() + " and " + second.getName() + " are buddies.");
}
```

不能将`Pair<Manager>`传递给这个方法，这一点很有限制。不过解决的方法很简单：使用一个通配符类型：

`public static void printBuddies(Pair<? extends Employee> p)`

类型`Pair<Manager>`是`Pair<? extends Employee>`的子类型（如下图所示）。

![使用通配符的子类型关系.jpg](https://github-production-user-asset-6210df.s3.amazonaws.com/35959679/102084002-6901db80-3e4f-11eb-96d2-e4282b7470e0.jpg)

#### 6.2 通配符的超类型限定

可以指定一个超类型限定（supertype bound），例如：`? super Manager`，这个通配符限制为`Manager`的所有超类型。

![带有超类型限定的通配符](https://github-production-user-asset-6210df.s3.amazonaws.com/35959679/102084529-43290680-3e50-11eb-865e-6d76374f5327.jpg)

直观地讲，带有超类型限定的通配符允许你写入一个泛型对象，而带有子类型限定的通配符允许你读取一个泛型对象。

#### 6.3 无限定通配符

还可以使用根本无限定的通配符，例如，`Pair<?>`。初看起来，这好像与原始的`Pair`类型一样。实际上，这两种类型有很大的不同。类型`Pair<?>`有以下方法：

```java
? getFirst()
void setFirst(?)
```

`getFirst`的返回值只能赋给一个`Object`。`setFirst`方法不能被调用，甚至不能用`Object`调用。`Pair<?>`和`Pair`本质的不同在于：可以用任意`Object`对象调用原始`Pair`类的`setFirst`方法。

> **注释**：可以调用`setFirst(null)`。

它对于很多简单操作非常有用。例如，下面这个方法可用来测试一个对组是否包含一个`null`引用，它不需要实际的类型。

```java
public static boolean hasNulls(Pair<?> p) {
    return p.getFirst() == null || p.getSecond() == null;
}
```

通过将`hasNulls`转换成泛型方法，可以避免使用通配符类型：

`public static <T> boolean hasNulls(Pair<T> p)`

但是，带有通配符的版本可读性更好。

#### 6.4 通配符捕获

略。（*Java Core Ⅰ（11版）* P352）

### 7. 网上一些优质回答的链接

- [generic array creation ?](https://www.zhihu.com/question/393638991)
- [java为什么不支持泛型数组？](https://www.zhihu.com/question/20928981)

---

# <h4 id="-1">杂记[⬆(返回目录)](#0)</h4>

- Java实际上并没有多维数组，只有一维数组。多维数组被解释为“数组的数组”。**arr[i][j]** 中每个**arr[i]** 本身也是一个数组**arr[i][j]** 引用这个数组的第j个元素
- 在Java中，所有链表实际上都是**双向链接**的——即每个链接还存放着其前驱的引用
- 在需要动态数组时，一般有两个选择```Vector```和```ArrayList```。其中，```Vector```类的所有方法都是**同步的**。可以安全地从两个线程访问一个```Vector```对象。但是，如果只从一个线程访问```Vector```(这种情况更为常见)，代码就会在同步操作上白白浪费大量的时间。而与之不同，```ArrayList```方法不是同步的，因此，建议在不需要同步时使用```ArrayList```，而不要使用```Vector```。
- ⚠**不要**调用```Thread```类或```Runnable```对象的```run```方法。直接调用```run```方法只会在**同一个线程**中执行这个任务——而没有启动新的线程。实际上，应当调用```Thread.start```方法，这会创建一个执行```run```方法的新线程。
- 子类构造器默认第一行是```super();```，除非在子类构造器第一行显示调用父类有参构造器```super(some args);```。抽象类也是存在构造器的，为了实例化子类对象的时候调用。
- 调用Object.hashCode()方法会采用hash算法，将对象计算出一个hash值（int型数据）。**两个对象的hash值不同时，两个对象肯定不同；两个对象的hash值相同时，两个对象也不一定相同。**
- Java新开一个线程时，新线程的优先级和创建时所在线程的优先级相同。
- `synchronized`修饰普通方法时，默认锁对象是`this`；修饰静态方法时，默认锁对象是`this.getClass()`。
- 线程被挂起后，将释放当前锁资源；被挂起的线程被唤醒后会重新判断锁资源；被挂起的线程被唤醒后会从之前挂起的位置继续往下执行。
- 进程是操作系统调度和分配资源的最小单位，线程是CPU调度的最小单位。不同的进程之间是不共享内存的。进程之间的数据交换和通信的成本是很高。不同的线程是共享同一个进程的内存的。当然不同的线程也有自己独立的内存空间。对于方法区、堆中的同一个对象的内存，线程之间是可以共享的，但是栈的局部变量永远是独立的。
- 集合不能添加基本数据类型，添加基本数据类型时会自动装箱成对应的包装类。
- `String`类型重写了`+`操作符，使用`StringBuilder`类进行拼接操作。
- `ArrayList`类在`new`的时候不开辟空间，在第一次添加数据的时候才开辟10个空间，`ArrayList`是线程不安全的，效率较高；`Vector`类在`new`的时候就已经开辟了10个空间，`Vector`是线程安全的，效率较低。
- `List<E>`有序指的是**元素添加顺序**；`Set<E>`无序指的也是**元素添加顺序**；`HashSet<E>`数据顺序有序（根据hashCode值排序）及`TreeSet<E>`数据顺序有序指的是**数据顺序**；`linkedHashSet<E>`有序指的是**元素添加顺序**，而**数据顺序**无序（不明白这个的名字里为啥带个Hash...链式结构怎么搞哈希算法嘛...感觉这个东西应该设计为继承自`LinkedList<E>`更好一些...）
- 不要在使用`Iterator`迭代器进行迭代时，调用`Collection`的`remove(xx)`方法，否则会报异常`java.util.ConcurrentModificationException`，或出现不确定行为。要使用`Iterator`的`remove()`方法去删，（以`ArrayList`为例）因为`ArrayList`中有个`modCount`属性记录数据的版本信息，`ArrayList`的内部类`Itr`的`next()`方法首先会调用`checkForComodification()`方法比对数据版本是否匹配。而`Iterator`的`remove()`方法会更新版本信息，但是`ArrayList`的`remove()`方法不会更新版本信息，导致出错。
- `HashMap`的数组中的链表在链表长度达到8，并且数组长度大于64，链表会变为**红黑树**，以提高效率；当某`table[index]`下的红黑树结点个数少于6个，此时，如果继续删除`table[index]`下树结点，一直删除到2个以下时就会变回链表，如果继续添加映射关系到当前`map`中，如果添加导致了`map`的`table`重新`resize`，那么只要`table[index]`下的树结点仍然<=6个，那么会变回链表；`HsahMap`扩容机制是原容量的2倍，默认**装填因子**为0.75，在第一次添加前数组默认容量为16，临界值为 16 * 0.75 =12，刚`new`声明时容量为0，临界值为 0 * 0.75 = 0。
