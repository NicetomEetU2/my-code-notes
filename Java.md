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
* [try(catch)块中的return语句和finally块中的语句执行顺序](#14)
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

5. 子类方法不能比父类方法抛出更多的编译时异常（不是运行时异常），即子类方法抛出的编译时异常或者和父类相同或者是父类异常的子类。当然，子类也可以不抛出异常，或者抛出```RuntimeException```异常

6. 不能重写被标识为```final```的方法。```final```方法可以被继承，但不能被重写。一个方法如果被```final```修饰就意味着这个方法不会被改动

- **为何子类重写方法的访问权限不能低于父类中权限**

    因为向上转型及 Java 程序设计维护的原因

    假设一个父类 A 拥有的方法```public void setXXX() {}```可以被其他任意对象调用
    这个方法被子类 B 覆写后为```void setXXX() {}```即默认的访问权限只能被本包极其子类所访问

    假设其他包中的对象 C 调用方法：

    ```java
    void get(A a) {
        a.setXXX();
    }
    ```

    假设此时传入的对象为 B 类对象 b，此时 b 将转型为 a 但是 b 中的```setXXX()```调用权限已经被缩小了这将造成错误。所以子类对象不能比父类对象访问权限小

- **为何子类重写方法的返回类型必须与父类相同或者是父类返回类型的子类型**

    也是因为向上转型

    假设 B 是 A 的子类，初始化 a 为 B 类型，并调用 B 类重写过的方法``` func()：```

    ```java
    A a = new B();
    C aa = a.func();
    ```

    假设原本 A 类的```func()```方法返回类型为 C，那么 B 类的```func()```方法返回类型就只能是 C 类或 C 类的子类型了

    > 其实，我个人认为这里这个例子举得并不是很好。换个思路，从代码复用性角度去想，假设有一个场景如下：

    ```java
    A[] arr = new A()[len];
    // 添加一些A子类的对象进数组，但并不只有一种子类对象
    arr[0] = new B(); // B extends A
    arr[1] = new C(); // C extends A
    ...
    for (A a : arr) {
        // do something
        ...
    }
    ```

    > 这样就会比较方便了，如果有更好的例子或更合理的解释，欢迎留言

- **为何子类方法不能比父类方法抛出更多的编译时异常**

    子类在覆盖父类方法的时候，父类的引用可以调用该方法。如果父类的引用调用子类的方法，那么这个多抛出来的异常，就可能处于一种无法被处理的状态
---

# <h4 id="11">String类型赋值问题[⬆(返回目录)](#0)</h4>

先看一下```String```类型的源码：

```java
...
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];
    ...
}
```

发现其中有个```final```修饰的```char```类型数组```value```，而且这个数组存有```String```类型变量的值（切分为多个字符存入这个字符数组），但是考虑如下赋值语句：

```java
String str = "aaa";
str = "bbb";
```

有没有感觉到有什么不对，是的，好像```final```修饰的```value```数组值被改变了，那好像也没什么不对。那再考虑下面代码：

```java
String str = "aaa";
str = "bbbbb";
```

好像又不对了，这里```final```修饰的```value```是个静态数组啊，在内存中应该是连续存储的啊，这里数组长度应该被改变了，就算是做了扩容操作，也应该要重新申请一个连续的地址空间重新赋给```value```啊，那不相当于修改了```final```修饰的引用类型变量的地址了嘛。

是的，这里不仅仅改变了```value```的地址，连```str```其实都已经不是之前的```str```了，实际上，这里恰好体现了```String```是一个引用类型的事实，```str = "bbbbb";```首先从常量池寻找是否存在```"bbbbb"```，如果没有就创建一个，然后返回一个```String```类型对象的副本，然后再把这个副本赋值给```str```。这个看着很像基本数据类型的赋值语句，实则是非常典型的引用类型的赋值语句。所以，用```final```修饰的```String```类型变量因无法修改其指向的地址，所以就无法修改。

```java
final String str = "aaa";
str = "bbbbb";
// error: Cannot assign a value to final variable 'str'
```

**但是**，当使用```String```类型有参构造器创建对象时，会和其他引用类型创建实例对象一样，会把值保存在堆空间中，而不是常量池。代码如下：

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

# <h4 id="14">try(catch)块中的return语句和finally块中的语句执行顺序[⬆(返回目录)](#0)</h4>

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

# <h4 id="-1">杂记[⬆(返回目录)](#0)</h4>

- Java实际上并没有多维数组，只有一维数组。多维数组被解释为“数组的数组”。**arr[i][j]** 中每个**arr[i]** 本身也是一个数组**arr[i][j]** 引用这个数组的第j个元素
- 在Java中，所有链表实际上都是**双向链接**的——即每个链接还存放着其前驱的引用
- 在需要动态数组时，一般有两个选择```Vector```和```ArrayList```。其中，```Vector```类的所有方法都是**同步的**。可以安全地从两个线程访问一个```Vector```对象。但是，如果只从一个线程访问```Vector```(这种情况更为常见)，代码就会在同步操作上白白浪费大量的时间。而与之不同，```ArrayList```方法不是同步的，因此，建议在不需要同步时使用```ArrayList```，而不要使用```Vector```。
- ⚠**不要**调用```Thread```类或```Runnable```对象的```run```方法。直接调用```run```方法只会在**同一个线程**中执行这个任务——而没有启动新的线程。实际上，应当调用```Thread.start```方法，这会创建一个执行```run```方法的新线程。
- 子类构造器默认第一行是```super();```，除非在子类构造器第一行显示调用父类有参构造器```super(some args);```。抽象类也是存在构造器的，为了实例化子类对象的时候调用。
- 调用Object.hashCode()方法会采用hash算法，将对象计算出一个hash值（int型数据）。**两个对象的hash值不同时，两个对象肯定不同；两个对象的hash值相同时，两个对象也不一定相同。**
