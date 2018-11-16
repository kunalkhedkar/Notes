# Java 10



Object have  two major characteristic 

state 

behavior



Class is a template or blueprint for creating objects 







Super vs this

## This() call

**Bad Example of Constructor design**

```java
class Rectangle {

    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(){
        this.x = 0;
        this.x = 0;
        this.width = 0;
        this.height = 0;
    }

    public Rectangle(int width, int height){
        this.x = 0;
        this.x = 0;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int x,int y,int width, int height){
        this.x = x;
        this.x = y;
        this.width = width;
        this.height = height;
    }
}
```

**Good Constructor design**

```java
class Rectangle {

    private int x;
    private int y;
    private int width;
    private int height;

    // 1st
    public Rectangle(){
        this(0,0);    // it will call 2nd constructor
     }

     // 2nd
    public Rectangle(int width, int height){
       this(0,0,width,height);    // call 3rd constructor
    }

    // 3rd
    public Rectangle(int x,int y,int width, int height){
        this.x = x;
        this.x = y;
        this.width = width;
        this.height = height;
    }
}
```



## Super() call

```java
class Shape {

    private int x;
    private int y;

    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


class Rectangle extends Shape{

    private int width;
    private int height;

    // 1st
    public Rectangle(int x, int y){
        this(x,y,0,0);    // call 2nd constructor
    }

    // 2nd
    public Rectangle(int x,int y, int width, int height){
        super(x,y);		        // call constructor from parent (Shape)
        this.width = width;		// Rest initilization
        this.height = height;
    }
    
}
```

As parent class has only one parameterize constructor  we have to fulfill parent class constructor.

so we can call super(x,y) method which call parent class constructor.

Alternative solution is to declare default constructor with no parameter. 





### Method Overloading 

- Method overloading mean providing two or more separated methods in class with **same name** but **different parameters**.
- Method return type, access modifier does't matter 
- Because allowing same name for method we don't need to remember multiple method name
  - Eg. AreaOfCircle, AreaOfSqure, Aerosphere can be replace by area only
- some times overloading is relate with compile time polymorphism
- we can overload static and instance method
- usually overloading happens inside a single class, but a method can also be treated as overloaded in subclass . Because a subclass inherits one version of the method from the parent class and then subclass can have another version of that same method. 



### Method Overriding

Method overriding means 	defining a methods in child class that already exist in the parent class with same signature( same name, same signature)

method overriding is also known as runtime polymorphism and dynamic method dispatcher, because method that going to called is decided at runtime by JVM.

we can't override static methods. Only instance methods can be overridden.



**Method override rules**

- it must have same name and same argument
	 Method return type can be subclass of the return type in parent class  																																																			
- It can't have a lower access modifier.



- Only inherited methods can be overridden other words methods can be overridden only in child class.
- Constructor and private methods can not be override
- Methods  that are final cannot be override
	 Subclass can call super.method() to call 	superclass version of parent class



| Method Overloading                                           | Method Overriding                                            |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| provide a functionality to reuse a method name with different parameter | Used to override a behavior which the class has override from parent class |
| Usually in single class but may also used in a child class.  | Always in two classes that have a child-parent  or IS-A relationship |
| Must have different parameters                               | must have the same parameters and same name                  |
| may have return type different                               | must have same return type or covariant return type (child class) |
| May have different access modifier                           | Must not have a lower modifier but may have a higher modifier |
| May throw different exception                                | Must not throw a new or border check exception               |



## Static methods vs Instance Methods

### Static methods

static methods are declare using static modifier

static methods can't access instance methods and instant variables directly.

static method generally use for operations that don't required any data from instance of class

We can't use this keyword



### Instance Methods

Instance method is belong to an instance of a class

To use an instance method we need to first instantiate the class usually by using the new keyword 

instance methods can access instance method and variables directly 

instance methods can also access static methods and static variables directly



## Static variables vs Instance variables 

## Static variables

- static variables declare using static keywords

- static variables are also known as static member variables

- Every instance of that class shares the same static variable

  - If changes are made to that variable, all other instance will see the effect of that change.

- static variable is property of class so that we can directly access it using class, we don't need to create separate instance.

  

### Instance variables 

Instance variables are also known as field or member variables.

Instance variable belongs to its specific class instance

Each instance of class have its own copy of an instance variables 

Every instance variable can have different state



## Composition & Inheritance

Inheritance make IS-A relation. 

Eg.     class car is inherited from vehicle class then **car is a vehicle**

But Composition is HAS-A relations. 

**Computer has Cabinet**, motherboard, Monitor, Keyboard etc.

**but cabinet is not a computer**, motherboard is not computer, monitor is not a computer

```java
Cabinet cabinet = new Cabinet(12,7,iball,"2 USB port");
MotherBoard motherBoard = new MotherBoard("i5",8,"intel","32-64","2.64 ghz");
Monitor monitor = new Monitor(15,"LG","1080*720");
KeyBoard keyBoard = new KeyBoard(110,"thin design", "dell","backLight");


Computer computer = new Computer(cabinet,
                                 motherBoard,
                                 monitor,
                                 keyBoard);
```



so computer has a cabinet, has a motherBoard, has a monitor, has a keyBoard but 

alone cabinet is not a computer ...

we know that these are the part of computer, where they all together is a computer. so this is case where we have use composition not inheritance.

​                                 
​    

## Encapsulation                             

Lets take a example of player of game.

```java
public class Player {

    public String name;
    public int health;
    public String weapon;

    public void loseHealth(int damage) {
        this.health = this.health - damage;
        if (this.health <= 0) {
            System.out.print("Player knocked out");
        }
    }


    public int healthRemaining() {
        return this.health;
    }

}
```

Main Class

```java
public class main{

    public static void main(String[]args){

            Player player = new Player();

            player.name = "Tim";
            player.health = 20;
            player.weapon= "Gun";

            int damge = 10;
            player.loseHealth(damge);
            System.out.print("Player remaining health "+player.healthRemaining());


        damge= 11;
        player.loseHealth(damge);
        System.out.print("Player remaining health "+player.healthRemaining());

    }

}
```



**Problem 1**

we have direct ability to change the value of player. Health field is very imp for us. For we have created separated method with our custom formulas , custom message. 

But as field can be access directly outside the class. our method will useless. 

```java
player.health = 100;
```

**Problem 2**

```java
// player
//public String name;
public String nickName;

// main
//player.name = "Tim";
  player.nickName = "bob";

```

In player class if we decide to change instance variable name to nickName. this is the internal change in player class because we just have change internal variable name but it result into error in main class as it directly assessing name field of player class.

Every time we change in instance variable or method in player class we also need to change it in main class. 																								

**problem 3**

As we are setup values manually there might be chance of forgot some field  while building object of player.

here if forgot to setup default health our code may not work as we expected. in other word we can not ensure that data of class is valid or not. (player start health with 0 is invalid data)

we can solve this issue using constructor. But when your are giving control and getting people to manually get access to field there will no real guarantee that data will be valid.

**problem 4**

If we want to do some validation like max health must be 100 we cant do that because health field is open and can be change from any where and from multiple places.

Enhanced player class can be

```java
public class EnhancedPlayer {

    private String name;
    private int health = 100;
//  private int hitpoints = 100;
    private String weapon;

    public EnhancedPlayer(String name, int health, String weapon) {
        this.name = name;

        if (health > 0 && health < 200) {
            this.health = health;
        }
        this.weapon = weapon;
    }
    
    public void loseHealth(int damage) {
        this.health = this.health - damage;
        if (this.health <= 0) {
            System.out.print("Player knocked out");
        }
    }

    public int healthRemaining() {
        return this.health;
        // return hitpoints;
    }
}

```



- fields are declared as private so only player class can change values of fields.
- we have provided a constructor so initializing player object data always a valid data we my do validation and set default value to any particular field.
- updating health is going to done by only  `loseHealth` function so we can add validation at one place and we do have a control on it.
- we only provide getter for health so health field can only be read by others.
- If we decide to change any field name like `health` to `hitpoints`we don't need to change it in other class such as main class.



## Call by value or call by reference

Java is call by value for primitive data types that mean modification will not reflect on main object. value get copied in new variable. original value is not change.

> Java and C is **always call by value**. 

The method gets a copy of all parameter values, and the method cannot modify the contents of any parameter variables that are passed to it. it look very straight forward **but** when an **object** reference is passed to a method, the method gets a **copy of the object reference**, and **both the original and the formal copy refer to the same object**, therefore within from the method the state of an object parameter can be changed. 

if we assign new object to that reference and modify that object original object will not change.



# Autoboxing and Unboxing

**Autoboxing:** Converting a primitive value into an object of the corresponding wrapper class is called autoboxing. For example, converting int to Integer class.

**Unboxing:** Converting an object of a wrapper type to its corresponding primitive value is called unboxing. For example conversion of Integer to int. 

```java
		Integer iBox = new Integer(10); 
        // unboxing the Object 
        int ipre = iBox; 

        //Autoboxing of char 
        Character cBox = 'a'; 
        char ch = cBox; 

		// compiler does boxing-unboxing automatically
		Integer intObj = 25;   // Integer.valueOf(25)   // auto-boxing
		int intPre = intObj;   // intObj.intValue()     // unboxing
```



# Nested classes

In java, just like method, variables of the class too can have another class as its member. 

writing a class within another class is allowed in java. this class written inside class is called nested class.

and the class that holds the inner class is called outer class.

```java
class OuterDemo{
    int rank;
    String name;
    
    class InnerDemo{
        // inner class stuff
    }
   
}
```

Generally we create nested class if without having existing of one class there is no use of second class. If second class totally depend on first class then we can go for inner class. can make second class inner class for first class.

Nested classes divided into 2 types

- **Non-static inner class** 

  - **Inner class**
  - **Method a local inner class**
  - **Anonymous inner class**

- **static inner class**

  

  ## **Non-static inner class**

  Java inner class can be declared private, public, protected, or with  default access whereas an outer class can have only public or default  access. 

  ### 1. Inner class

  can access all the variables and methods of the outer class 

  Since inner classes are associated with instance, we can’t have any static variables in them.  

  Object of java inner class are part of the outer class object and to  create an instance of inner class, we first need to create instance of  outer class. 

  ```java
  OuterClass outerObject = new OuterClass();
  OuterClass.InnerClass innerObject = outerObject.new InnerClass();
  ```

  inner class example

  ```java
  class Outer_Demo {
     int num;
     
     // inner class
     private class Inner_Demo {
        public void print() {
           System.out.println("This is an inner class");
        }
     }
     
     // Accessing he inner class from the method within
     void display_Inner() {
        Inner_Demo inner = new Inner_Demo();
        inner.print();
     }
  }
     
  public class My_class {
  
     public static void main(String args[]) {
        // Instantiating the outer class 
        Outer_Demo outer = new Outer_Demo();
        
        // Accessing the display_Inner() method.
        outer.display_Inner();
     }
  }
  ```

  To access the private members of a class using inner class. 

```java
class Outer_Demo {
 
   private int num = 175;    // private variable of the outer class
   
   
   public class Inner_Demo {
      public int getNum() {
         
         return num;   // access outer class privte members
      }
   }
}

public class My_class2 {

   public static void main(String args[]) {
  
      Outer_Demo outer = new Outer_Demo();
      
      Outer_Demo.Inner_Demo inner = outer.new Inner_Demo();
      System.out.println(inner.getNum());      //access private members class using inner class.
   }
}
```

### **2. Method-local Inner Class**

### 

- In Java, we can write a class within a method 
- Like local variables, the scope of the inner class is restricted within the method. 
- A method-local inner class can be instantiated only within the method 
-  local inner class is not associated with Object, we can’t use private,  public or protected access modifiers with it. The only allowed modifiers are abstract or final.  

```java
public class Outerclass {
   
   void my_Method() {
      	int num = 23;
          class MethodInner_Demo {
             public void print() {
                System.out.println("This is method inner class "+num);	   
             }   
          }
	   
          // Accessing the inner class
          MethodInner_Demo inner = new MethodInner_Demo();
          inner.print();
   }
   
   public static void main(String args[]) {
      Outerclass outer = new Outerclass();
      outer.my_Method();	   	   
   }
}
```

### 3. Anonymous Inner Class

- An inner class declared without a class name is known as an **anonymous inner class**. 
- Anonymous inner class always extend a class or implement an interface. 
- Since an anonymous class has no name, it is not possible to define a constructor for an anonymous class. 
- Anonymous inner classes are accessible only at the point where it is defined.  

```java
abstract class AnonymousInner {
   public abstract void mymethod();
}

public class Outer_class {

   public static void main(String args[]) {
      AnonymousInner inner = new AnonymousInner() {
         public void mymethod() {
            System.out.println("This is an example of anonymous inner class");
         }
      };
      inner.mymethod();	
   }
}

//      new AnonymousInner(
//         public void mymethod() {
//            System.out.println("This is an example of anonymous inner class");
//         }
//      }).mymethod();	

```

Generally we use it if we need object that extends or implement some specific abstract implementation.

instead of creating class and then extend or implement from abstract thing and then create object of this newly created class and then pass it,

we can directly create object without class name and override essentials property. 

we can create Anonymous class without name, because we know that we are not going to use it anywhere else except current place (we are not going to reuse it).

## Static Nested Class

- it is a static member of the outer class 
- It can be accessed without instantiating the outer class
- static nested classes can access only static members of the outer class. 
- static nested class does not have access to the instance variables and methods of the outer class. 

```java
public class Outer {
    int score;
    
   static class Nested_Demo {
      public void my_method() {
         System.out.println("This is my nested class");
      }
   }
   
   public static void main(String args[]) {
      Outer.Nested_Demo nested = new Outer.Nested_Demo();	 
      nested.my_method();
   }
}
```



# Interface

In interface we are focusing on what needed to be done, not on how it's to be done.

interface we we know a purely abstract and they don't actually specify any actual aspect of implementation.

the actual implementation is left to class that implement the interface.

since java 8 interface can contain default methods. in other words methods with implementation.

The keyword default is use and static method also allowed

java 9 an interface can also contain private methods (commonly used when two default methods in an interface share common code. (must available for default method only) )



# Generics

java 1.5 introduce a generics. before that we can not create specific type of group of data

```java
ArrayList item = new ArrayList();
item.add(1);
item.add("kunal");
item.add(3.3);
```

Java **Generic** methods and generic classes enable programmers to  specify, with a single method declaration, a set of related methods, or  with a single class declaration, a set of related types, respectively.

Generics also provide compile-time type safety that allows programmers to catch invalid types at compile time.