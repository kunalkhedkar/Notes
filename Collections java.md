# Collections java

> Collection is framework which defines several classes and interfaces to represent a group of objects as a single unit. 
>

The Collection interface (**java.util.Collection**) and Map interface (**java.util.Map**) are the two main “root” interfaces of Java collection classes. It define the most common methods which are applicable for any collection. 

Eg. add, addAll, remove, removeAll, clear, retainAll(e) `except e all element`, contains, isEmpty

Note: Collection interface doesn't contain any method to retrieve data.

```java
								Object
								/     \
                                  /       \
                               Array     Collections 
```

#### Array

- **fix in size**
  - once we created array , array can **not** be increase or decrease in **size**
  - need to know size in advance
- Can hold only homogenous data 
  - Student s [3] ;   // only student type data can be store  | sol- go Object type array
- Readymade function/features are not available

#### Collection

- collection is **growable** in nature
- Both homogenous and heterogeneous objects can be stored
- **Readymade** functions/feature are available 



##### Need for Collection Framework 

- Before Collection Framework **(or before JDK 1.2)** was introduced, the standard methods for grouping Java objects (or collections) were **Arrays** or **Vectors** or **Hashtables**. 
- All of these collections had no common interface.
- Accessing elements of these Data Structures was a hassle as each had a different method (and syntax) for accessing its members:

```java
class Test 
{ 
    public static void main (String[] args) 
    { 
        // Creating instances of array, vector and hashtable 
        int arr[] = new int[] {1, 2, 3, 4}; 
        
        Vector<Integer> v = new Vector(); 
        v.addElement(1); 
        v.addElement(2); 
        
        Hashtable<Integer, String> h = new Hashtable(); 
        h.put(1,"geeks"); 
        h.put(2,"4geeks"); 
  
        // Array instance creation requires [],
        //  while Vector and hastable require () 
        // Vector element insertion requires addElement(),  
        // but hashtable element insertion requires put() 
  
        // Accessing first element
        System.out.println(arr[0]); 
        //vector
        System.out.println(v.elementAt(0)); 
        //hashtable
        System.out.println(h.get(1)); 
    } 
}
```

As we can see, **none** of these collections **implement a standard member access interface**. It was very difficult for programmers to write algorithms that can **work for all kinds** of Collections. 

> Java developers decided to come up with a common interface to deal with the above mentioned problems and introduced the **Collection Framework in JDK 1.2**



## **Collection Framework in JDK 1.2**

The API has a basic set of interfaces like Collection, Set, List, or Map. 

All classes that implement these interfaces have *some* common set of methods. 

**Hierarchy of Collection Framework**

```haskell
             Collection (1.2)          Map                 legacy class (1.0)
         /     /    \      \            |                   Vector, Stack, HastTable
        /      /      \     \           |
     Set    List    Queue  Dequeue   SortedMap
     /
    /
 SortedSet 
                              Core Interfaces in Collections
```

Collection is root interface with basic methods like add(), remove(), contains(), isEmpty(), addAll() etc.



**collection vs collections**

| Collection Interface | Collections class |
| -------------------- | ----------------- |
|                      |                   |

**Note:** Collection is use to hold & transfer object , to transfer object over network object should Serializable and after receiving object at receiver end it clone the object to prevent from losing original object.

**So every collection by default implements Serializable and Cloneable.**

Collection class define several utility methods for collection object like sort, search, reverse, synchronized

##### 1. Sorting

collections class define two sort method

- public static void **sort(collection c)**
  - sort base on default natural sorting order
  - Object must be homogenous and comparable 
- public static void **sort(Collection c, Comparator com)**
  - It sort base on customize sorting order define by comparator

##### 2. Searching

​	Collection should be in sorted order else we may get unpredictable results

- ​	public static int **binarySearch(List l,Object target)**
	 	public static int **binarySearch(List l,Object target, Comparator com)**

**return**

​	On successful search it return target's index

​	on unsuccessful search it return insertion point (index where target should be place)

##### 3. Reverse

​	public static void reverse(Collection c)

Note: we can get reverserOrder comparator by using method reverserOrder 

Comparator rev= Collections.reverserOrder(comparatorAsc)



# 4 ways to retrieve elements from collection 

- **For-each loop**
- **Using cursors**
  - Iterator
  - ListIterator
  - EnumerationIterator

#### For - each loop

For each loop is meant for traversing items in a collection. 

```
for(Element e: data ){
    system.out.print(e)
}
```

here data is group of elements and e is particular single element. It will travels from first element to last element.

### Using cursors

Cursor is an **interface** and it is used to retrieve data from collection object, one by one. 

**Cursor has 3 types**

- Iterator
- ListIterator
- EnumerationIterator

#### 1. Iterator

​	Iterator is an **interface** provided by collection framework to traverse a collection and for a sequential access of items in the collection.

- Only forward direction cursor
- We can do read & remove But can not replace

**collection object**

```java
 HashSet<String> hs = new HashSet<String>() ; 
        // store some string elements 
        hs.add("India"); 
        hs.add ("America"); 
        hs.add("Japan");           
```

**Iterator**

```java
 // Add an Iterator to hs. 
 Iterator it = hs.iterator(); 
  
 // Display element by element using Iterator 
 while (it.hasNext()) 
      System.out.println(it.next()); 
 } 
```

**It has 3 methods:** 

> boolean **hasNext()** : This method returns true if the iterator has more elements.
>
> elements **next()** : This method returns the next elements in the iterator.
>
> void **remove()** : This method removes from the collection the last elements returned by the iterator.



#### 2. ListIterator

ListIterator is an interface that contains methods to retrieve the elements from collection object, 

Both in forward and reverse directions. This iterator is list base iterator.

- Bidirectional traverser
- Can do read, remove and replace

> - boolean **hasNext()**: This returns true if the ListIterator has more elements when traversing the list in the forward direction.
> - boolean **hasPerivous()**: This returns true if the ListIterator has more elements when traversing the list in the reverse direction.
> - element **next()**: This returns the next element in the list.
> - element **previous()**: This returns the previous element in the list.
> - void **remove()**: This removes from the list the last elements that was returned by the next() or previous() methods.
> - int **nextIndex()** Returns the index of the element that would be returned by a subsequent call to next(). 
> - int **previousIndex()** Returns the index of the element that would be returned by a subsequent call to previous(). (Returns -1 if the list iterator is at the beginning of the list.)



ListIterator code snippet

```java
	// set iterator on vector object v
	ListIterator lit = v.listIterator(); 

	while (lit.hasNext()){
        System.out.print(lit.next()+" ") ; 
    }
    //backward direction
    while (lit.hasPrevious()) {
        System.out.print(lit.previous()+" "); 
    }    
```
### 3. EnumerationIterator

EnumerationIterator is useful to retrieve one by one element. This iterator is based on data from Enumeration. To get EnumerationIterator we use obj.elements() method.

- Can only use for legacy class. (vector, stack)
- We can only read , we can not remove element



> - boolean **hasMoreElements()**: This method tests if the Enumeration has any more elements or not
> - element **nextElement()**: This returns the next element that is available in elements that is available in Enumeration



```java
    Vector dayNames = new Vector(); 
    dayNames.add("Sunday"); 

	// Creating enumeration  
   Enumeration days = dayNames.elements(); 

  // Retrieving elements of enumeration 
    while (days.hasMoreElements()) {
        System.out.println(days.nextElement()); 
	} 
```


## Differences

#### Iterator vs Foreach

Both are meant for traversing items in a collection. 

Internally Foreach creates an Iterator and iterates over the Collection. 

> In for-each loop, we **can’t modify collection**, it will throw a **ConcurrentModificationException** 
>
> on the other hand with **iterator we can modify** collection. 

```java
for (String i : list) {
    System.out.println(i);
    list.remove(i); // throws exception
} 

Iterator it=list.iterator();
while (it.hasNext()){
    System.out.println(it.next());
    it.remove(); // valid here
}
```

**Performance Analysis** :

Iterator and for-each loop are **faster than simple for loop** for collections **with no random access**,

 while in collections which **allows random access** there is **no performance change** with for-each loop/for loop/iterator. 



#### Iterator vs ListIterator

|                           Iterator                           |                         ListIterator                         |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
|       It is used for traversing `List` and `Set` both.       |         It is used to traverse `List` only (vector)          |
|            can traverse in only forward direction            | can traverse a List in both the directions forward and Backward |
|          cannot obtain indexes while using Iterator          | can obtain indexes at any point of time while traversing by methods like  nextIndex() and previousIndex() |
| cannot add element to collection while traversing it using Iterator |    can add element at any point of time while traversing     |
| cannot replace the existing element value when using Iterator | using set(Element e) method of ListIterator we can replace the last element |
|           Methods  :   hasNext()  next()  remove()           | Methods :  add(E e)  hasNext()  hasPrevious()  next()  nextIndex()  previous()  previousIndex()  remove()  set(E e) |

#### Iterator vs EnumerationIterator

| Iterator                                                     | EnumerationIterator                                          |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| It is introduced from JDK **1.2**                            | It is there from JDK 1.0                                     |
| Iterator **allows** us to **remove** an element              | *Enumeration* **only traverses** the *Collection* object     |
| Iterator is **not a legacy** code which is used to traverse most of the classes in the collection framework. For example, ArrayList, LinkedList, HashSet, LinkedHashSet, | *Enumeration* is a **legacy** interface used to traverse only the legacy classes like *Vector*, *HashTable* and *Stack* |
| *Iterator* is a **fail-fast in nature**. i.e it throws *ConcurrentModificationException* **if a collection is modified** while iterating other than it’s own *remove()* method. | Where as ***Enumeration* is fail-safe** in nature. It doesn’t throw any exceptions if a collection is modified while iterating. |
| As *Iterator* is fail-fast in nature and **doesn’t allow modification of a collection** by other threads while iterating, it is considered as **safe** and secure than *Enumeration*. | Enumeration is **not that safe**.                            |

#### Simple For loop vs Iterator 

if you use only array or data structure that support direct access(e.g. arraylist at java). "a[i]" is good. but, when you use other data structure, iterator is more efficient 



#### Iterator for Map

```java
for (Integer key : hm.keySet()) {
    System.out.println("Key = " + key + " - " + hm.get(key));
}
```

Iterating over the Entry set will be better performance wise:

```java
for (Map.Entry<Integer, String> entry : hm.entrySet()) {
    Integer key = entry.getKey();
    String value = entry.getValue();

}
```



# Comparable vs Comparator

Java provides two interfaces to sort objects using data members of the class:

1. Comparable
2. Comparator

| Comparable                                                   | Comparator                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| found in **lang** package.                                   | is found in the **util** package.                            |
| provides **compareTo() method**                              | provides **compare()** and **equal()***                      |
| sort the collection on the basis of a single element    public int compareTo(Student st){  return (this.id - emp.id);  } | sort the collection on the basis of multiple elements   public int compare(Student s1,Student s2){    return s1.getAge() - s2.getAge();  } |
| comparing itself with another object                         | comparing two different objects                              |





# Collections : List Interface

The java.util.List is a child interface of Collection.

- It is an **ordered** collection of objects
- where **duplicate** values are **allowed**
- **insertion** **order** is **preserves**
- it **allows** positional **access** and insertion 
- List Interface is implemented by **ArrayList**, **LinkedList**, **Vector** and **Stack** classes.
- we differentiate duplicate data by its index



**Generic List Object:**
After the **introduction of Generics in Java 1.5**, it is possible to restrict the type of object that can be stored in the List. The type-safe List can be defined in the following way:

```
// Obj is the type of object to be stored in List.
List<Obj> list = new List<Obj> ();  
```

List Interface **extends Collection**, hence it supports all the operations of Collection Interface, along with following additional operations 

`add`, `addAll`, `remove`, `get`, `set`, `indexOf` ,  `lastIndexOf`, `subList`  

## List Interface is implementations

### ArrayList

- ArrayList inherits AbstractList class and implements List interface.
- ArrayList is **growable** in nature, its size can be increase or decrease as objects add or remove
- Direct access the list 
- **Null** value allowed
- Heterogeneous objects are allowed 
- Can not be used for primitive types, like int, char

**Constructors in Java ArrayList**: 

- ArrayList()

- ArrayList(Collection c)

- ArrayList(int capacity)

  

**ArrayList** and **vector** implements **RandomAccess** interface so they can **retrieve** any object by index at super speed.

ArrayList is good option if frequently requirement is **retrieve random object**. And worst at insertion at middle and delete at middle.



### LinkedList 

LinkedList are **linear data structure** where the element are **not** **stored in contiguous locations** and every element is a separated object with a data part and address part.  It also has few disadvantages like the **nodes cannot be accessed directly** instead we need to start from the head and follow through the link to reach to a node we wish to access. 

To store the elements in a linked list we use a **doubly linked list** which provides a linear data structure and also used to inherit an abstract class and implement **list** and **deque** interfaces. 

Good option if frequent requirement is insertion & deletion in middle

**Constructors for Java LinkedList:**

1. LinkedList():
2. LinkedList(Collection C): 

**Methods**

Clone, addFirst, AddLast, getFirst, getLast, removeLast



### Vector

The Vector class implements a growable array of objects. Vectors basically falls in **legacy classes** but now it is fully compatible with collections. 

- Vector implements a dynamic array that means it can grow or shrink as required. 
- Elements can **directly access by index**
- Vector is **synchronized** and have **some legacy methods** which collection framework not contain
- vector implements **RandomAccess** interface


it extends **AbstractList** and implements **List** interface

**Constructor:**

- Vector(): Creates a default vector of initial capacity is 10.
- Vector(int size): Creates a vector whose initial capacity is specified by size.
- Vector(int size, int incr): 
- Vector(Collection c): 

**Increment of vector capacity:** 
If increment is specified, Vector will expand according to it in each allocation cycle but if increment is not specified then vector’s capacity get **doubled** in each allocation cycle. 

Vector defines three protected data member:

- **int capacityIncreament:** Contains the increment value.
- **int elementCount:** Number of elements currently in vector stored in it.
- **Object elementData[]:** Array that holds the vector is stored in it.

**Methods** 

add, addAll, clear, clone, contains, get, indexOf , isEmpty, lastIndexOf, remove,firstElement , toArray, retainAll, hashCode,     



### Stack 

Java Collection framework provides a Stack class which models and implements Stack data structure. 

with principle of **last-in-first-out** 

- stack is **synchronized** 
- Thread safe

With basic operations stack provide push, pop, empty, search, peek.

```java
                                    Iterable 		    /
                                        |			   /
                                        |extends	    /
                                    collection		    /     Interface
                                        |			   /
                                        |extends	    /
                                       List			    /
                                        |
                                        |implements
                                      Vector			\
                                        |			    \
                                        |extends		\     Class
                                      Stack				\
```



**Constructor**

​	class supports one *default constructor* **Stack()** 

**Methods**

​	push, pop, empty, search, peek



# Set Interface

Set is an interface which extends collection.

- It is **unordered** collection
- which **duplicate** value **not** allowed (add method returns false for duplicate element insertion)
- **Set** is implemented by **HashSet**, **LinkedHashSet**  
- Objects are store on **hash code way**
- Heterogeneous data allowed 
- Good for **searching** 



```java
                                        Collection   (I)             
                                             /          
                                           Set       (I)             
                                           /
                                     SortedSet       (I)             
                                         /
                                     NavigableSet    (I)             
                                        / 
                                     TreeSet         (Class)             
                             
```



```java
Set<String> mySet = new HashSet<String>(); 
mySet.add("Geeks"); 
```

### HashSet

- The HashSet class implements the **Set** interface.
- Underlying data structure is **hashtable**.
- **Duplicate** values are **not** allowed
- **Insertion** order is **not** preserved mainly it base of hash code
- **Null** elements are **allowed**.
- HashSet also implements **Searlizable** and **Cloneable** interfaces. 
- Good for searching object.



**Initial Capacity:** 

The initial capacity means the number of buckets when hashtable  is created. The number of buckets will be automatically increased if the current size gets full. 

**Load Factor** :

```javascript
  				Number of stored elements in the table
   load factor = -----------------------------------------
                        Size of the hash table 
```



 HashSet is not synchronized so to prevent data we should be wrapped using the 

Collection.synchronizedSet method.

```java
Set s = Collections.synchronizedSet(new HashSet(...));	
```

**Constructors in HashSet:**

1. **HashSet h = new HashSet();** 
   Default initial capacity is **16** and default load factor is **0.75**.

2. **HashSet h = new HashSet(int initialCapacity);** 
   default loadFactor of 0.75

3. **HashSet h = new HashSet(int initialCapacity, float loadFactor**);

4. **HashSet h = new HashSet(Collection C);**

   

```java
    HashSet<String> h = new HashSet<String>(); 
    // Adding elements into HashSet usind add() 
    h.add("India"); 
```
 HashSet uses **HashMap** for storing its object internally. Actually the **value** we insert in **HashSet** acts as **key** to the map Object and for its **value** java uses a **constant variable**. So in key-value pair all the keys will have same value. 

### **LinkedHashSet**  

**LinkedHashSet** ordered version of HashSet. Where iterating through a HashSet the order is unpredictable, while a LinkedHashSet lets us iterate through the elements in the order in which they were inserted. 

- It maintains a **doubly-linked** List internally.
- Contains **unique** elements only like extends HashSet class and implements Set interface.
- Maintains **insertion order**.

**constructors** : 

-  HashSet()
- HashSet(Collection C)
- LinkedHashSet(int size):
- LinkedHashSet(int capacity, float loadCapacity):

**Note:** Best use for cache base data 

# SortedSet Interface

SortedSet is an interface in collection framework. This interface **extends** **Set** and **provides a total ordering** of its elements.  

All elements of a SortedSet **must implement** the **Comparable** interface .

Normal set does not contains any extra methods.

**Methods** **of Sorted Set interface:**  

- **comparator()** : Returns the **comparator used to order the elements** in this set, or null if this set uses the natural ordering of its elements.
- **first()** : Returns the first (lowest) element currently in this set.
- **last()** : Returns the last (highest) element currently in this set.
- **subSet(E fromElement, E toElement)** : fromElement, inclusive, to toElement, exclusive
- **headSet(E toElement)** : Returns group of elements of this set whose elements are strictly less than toElement.
- **tailSet(E fromElement)** : Returns a view of the portion of this set whose elements are greater than or equal to fromElement.

​            

# NavigableSet 

NavigableSet represents a **navigable** set in Java Collection Framework.

NavigableSet interface inherits from the **SortedSet** interface.

It behaves like a **SortedSet** with the exception that we have **additional navigation methods**

Implementation of this interface are **TreeSet** and **ConcurrentSkipListSet**.

 

**Methods** **of NavigableSet**  (Extra)

- **Lower(E e)** : Returns element which is less than the given element .

- **Floor(E e )** : Returns element which is less than or equal to given element 

- **Ceiling(E e)** : Returns element  which is greater than or equal to given element 

- **Higher(E e)** : Returns  element which is greater than the given element

- **pollFirst()** : Retrieve and remove the first least element. 

- **pollLast()** : Retrieve and remove the last highest element. 

  or NULL if there is no such element.

  **Example**

```python
Normal order: [0, 1, 2, 3, 4, 5, 6]
Reverse order: [6, 5, 4, 3, 2, 1, 0]
3 or  more:  [3, 4, 5, 6]
lower(3): 2
floor(3): 3
higher(3): 4
ceiling(3): 3
pollFirst(): 0
Navigable Set:  [1, 2, 3, 4, 5, 6]
pollLast(): 6
```



### TreeSet

TreeSet is one of the most important implementations of the SortedSet interface in Java that uses a Tree for storage. Ordering of the elements is maintained by a set using their natural ordering or explicit comparator.

- **duplicate** values are **not** allowed 
- stored in a **sorted** and ascending order 
- **not preserve** the **insertion** order of elements but elements are sorted by keys 
- **not** allow to insert **Heterogeneous** objects 
- it has faster access and retrieval time  
- implementation of a **self-balancing binary search** tree like **Red-Black Tree**.
- For **heterogeneous** object it **throw** runtime classCastException 
- operations like add, remove and search take O(Log n) time.
- **printing** n elements in sorted order takes **O(n)** time. 

**Constructor**

- TreeSet t = new TreeSet();

- TreeSet t = new TreeSet(Comparator comp);

- TreeSet t = new TreeSet(Collection col); 

- TreeSet t = new TreeSet(SortedSet s);

  

  **Synchronized TreeSet** 																																																																																	

TreeSet is **not** synchronized by default we need to explicitly by wrapping Collections.synchronizedSortedSet 

```java
TreeSet ts = new TreeSet();
Set syncSet = Collections.synchronziedSet(ts); 
```

**Note:**

- **Insertion** of **null** into a TreeSet **throws** **NullPointerException** because while insertion of null, it gets compared to the existing elements and null cannot be compared to any value. 
- if we are depending on **default** natural sorting order, compulsory the object should be **homogeneous** and **comparable** otherwise we will get RuntimeException: ClassCastException
  - Eg. String is allowed but StringBuffer is not. 
- upto **JDK 6**, null will be accepted as first value 



**Differences**

**HashSet  vs LinkedHashSet** 

| HashSet                                     | LinkedHashSet                           |
| ------------------------------------------- | --------------------------------------- |
| The underlined data structure is Hashtable. | combination of LinkedList and Hashtable |
| Insertion order is not preserved.           | Insertion order is preserved.           |
| Introduced in 1.2 version.                  | Introduced in 1.4 version.              |

**HashSet vs TreeSet**

| Property                  | HashSet                             | TreeSet                       |
| :------------------------ | ----------------------------------- | ----------------------------- |
| Ordering or Sorting       | doesn't provide any order guarantee | data is store in sorted order |
| Comparison                | it uses equals method to compare    | it uses compareTo method      |
| Underlying data structure | hashtable                           | Red-black-tree                |
| Null element              | only one null element               | not allow                     |
| implementation            | using hashmap                       | using TreeMap                 |
| Performance               | faster                              | slower than hashset           |



# Queue Interface 

The Queue interface is available in java.util package and extends the Collection interface.  It is an **ordered** list of objects with its use limited to **FIFO**. 

- implementations are **PriorityQueue** and **LinkedList**. 
- both the implementations are not thread safe. *PriorityBlockingQueue* is one alternative implementation if thread safe implementation.

**Methods in Queue:**

1. **add()-**  add elements at the tail of queue.
2. **peek()-** This method is used to view the head of queue without removing it.
3. **element()-** This method is similar to peek(). It throws *NoSuchElementException* when the queue is empty.
4. **remove()-** This method removes and returns the head of the queue. It throws *NoSuchElementException* when the queue is impty.
5. **poll()-** This method removes and returns the **head** of the queue. It returns null if the queue is empty.



### Queue Categories

- **Bounded Queues**
  - Bounded Queues are queues which are bounded by capacity that means we need to provide the max size of the queue at the time of creation. Eg. **ArrayBlockingQueue** 
- **Unbounded Queues**
  - Unbounded Queues are queues which are NOT bounded by capacity that means we should not provide the size of the queue. Eg **LinkedList** 
- **Blocking Queues**
  - BlockingQueues **blocks** until it finishes it’s job or time out.
- **Non-Blocking Queues**

## PriorityQueue 

A PriorityQueue is used when the objects are supposed to be **processed based on the priority**. The elements of the priority queue are ordered according to the natural ordering, or by a **Comparator** provided at queue construction time, depending on which constructor is used.  

- **Null** value is **not** allowed

- **Object** must be **comparable**


**Constructors of PriorityQueue class**

- **PriorityQueue():** with the default initial capacity (11) and natural ordering.
- **PriorityQueue(Collection c):** 
- **PriorityQueue(int initialCapacity)**: 
- **PriorityQueue(int initialCapacity, Comparator comparator):** 
- **PriorityQueue(PriorityQueue c)**: 
- **PriorityQueue(SortedSet c)**: 



# Deque interface

The Deque is related to the **double-ended queue** that supports **addition** or **removal** of elements from **both side**. can be used as a queue (first-in-first-out/**FIFO**) or as a stack (last-in-first-out/**LIFO**).

```java
 									Iterable 		         /
                                        |			   		    /
                                        |extends	    		/
                                    collection		    		/     Interface
                                        |			   		    /
                                        |extends	    		/
                                       Queue			    	/
                                        |
                                        |extends
                                      Deque			   			
                                        |			    		\
                                        |implements				\     Class
                                      Abstract collection		 \
                                        |						\
                                        | extends				\
                                      ArrayDeque				\
```



- Not thread safe
- **Null** value is **not** allowed
- Implementation is ArrayDeque
- support of resizable array 



**Methods**

1. **add(element):** Adds an element to the tail.

2. **addFirst(element):** Adds an element to the head.

3. **addLast(element):** Adds an element to the tail.

4. **offer(element):** Adds an element to the tail and `returns a boolean` to explain if the insertion was successful.

5. **offerFirst(element):** Adds an element to the head and returns a boolean to explain if the insertion was successful.

6. **offerLast(element):** Adds an element to the tail and returns a boolean to explain if the insertion was successful.

7. **iterator():** Returna an iterator for this deque.

8. **descendingIterator():** Returns an iterator that has the reverse order for this deque.

9. **push(element):** Adds an element to the head.

10. **pop(element):** Removes an element from the head and returns it.

11. **removeFirst():** Removes the element at the head.

12. **removeLast():** Removes the element at the tail.

13. **poll():** Retrieves and removes the head of the queue represented by this deque 

14. **pollFirst():** Retrieves and removes the first element of this deque,

15. **pollLast():** Retrieves and removes the last element of this deque

16. **peek():** Retrieves, but does not remove, the head of the queue represented by this deque 

17. **peekFirst():** Retrieves, but does not remove, the first element of this deque, 

18. **peekLast():** Retrieves, but does not remove, the last element of this deque

    >  or returns null if this deque is empty



## ArrayDeque

ArrayDeque in Java provides a way to apply resizable-array in addition to the **implementation of the Deque** interface. It is also known as Array Double Ended Queue or Array Deck. This is a special kind of array that grows and allows users to add or remove an element from both the sides of the queue. 

- It growable as necessary to support usage 

- Not thread-safe 

- **Null** element is **not** allowed

- ArrayDeque class is likely to be faster than Stack when used as a stack 

- ArrayDeque class is likely to be faster than LinkedList when used as a queue.

  

  **Constructors :**

1. **ArrayDeque():** 
2. **ArrayDeque(Collection c):** 
3. **ArrayDeque(int numofElements)**



**Methods :** 

add, addFirst, addLast, clear, size, clone, getFirst, getLast, isEmpty, offer, OfferFirst, offerLast, peek, poll, pop, remove



# Map Interface

The java.util.Map interface represents a **mapping** between a **key** and a **value**. The Map interface is not a subtype of the Collection interface. Therefore it behaves a bit different from the rest of the collection types. 

```java
							 <Map>
							/  |  \
						   /   |   \
						  /    |   <SortedMap>
						 /     |     \
						/      |    <NaviagableMap>
					   /       |       \
					  /        |        \
				HashTable    HashMap    TreeMap
							   |
							   |
						LinkedHashMap 
```



- **duplicate** keys **not** allowed, values of keys can be duplicate


- some implementation allowed **null** value like **HashMap** and **LinkedHashMap** but some do **not like TreeMap**
- Order of a map depends on specific implementations like TreeMap and LinkedHashMap have predictable order, while HashMap does not.
- **Map is not child interface** of collection interface
- collection interface **method** can **not** be use for map (use put instead of add)



**Methods:** 

- **Object put(Object key, Object value)** 
- void putAll(Map map)
- Object remove(Object key)
- Object get(Object key)
-  boolean containsKey(Object key)
- Set keySet(): This method is used to return the group of containing all the keys.
- Set entrySet(): This method is used to return the group of containing all the keys and values.



## HashTable

Hashtable was part of the original java.util and is a concrete implementation of a Dictionary.

However, Java 2 re-engineered Hashtable so that it also implements the Map interface.

- It is a **legacy class** 
- It is similar to HashMap, but is **synchronised**.

- Hashtable stores data in key/value pair.
- Insertion **order** is **not** **preserved**
- Does **not** allow any **null** **key** or **value**. 
- Can be traversed by **Enumerator** and **Iterator**  
- Hashtable inherits Dictionary class 
- The key is then hashed, and the resulting hash code is used as the index at which the value is stored within the table. 

**Constructors:**

- **Hashtable():**
- **Hashtable(int size):** 
- **Hashtable(int size, float fillRatio):** 
  -  **fill ratio:** Basically it determines how full hash table can be before it is resized upward.and its Value lie between 0.0 to 1.0
- **Hashtable(Map m):**

**Methods**

- clear, clone, contains, containsValue, set, equals, get, hashCode
- **Enumeration elements() :**Returns an enumeration of the values obtained in hash table.  



## Hashmap

**HashMap** is a part of Java’s collection since Java 1.2. It provides the basic implementation of Map interface of Java. It stores the data in (Key, Value) pairs. 

- Insertion **order** is not **preserved**
- Allows **null** key also but only **once** and multiple null values are allowed.
- **Duplicate** key are **not** allowed, values can be duplicate



**Internal Structure of HashMap**  

Internally HashMap contains an array of Node and a node is represented as a class which contains 4 fields 

```java
int hash
K key
V value
Node next
```

**constructor:**

1. **HashMap() :**  initial capacity 16 and load factor 0.75.

2. **HashMap(int initial capacity) :** with specified initial capacity and load factor 0.75.

3. **HashMap(int initial capacity, float loadFactor) :**

4. **HashMap(Map map)** 

   

   **Methods:**

   clear, put, get, keySet, entrySet, putAll, remove, size

   **Note:** 

   To mitigate the above performance issue, JDK 8 has introduced balanced tree instead of linked list in case of frequent collision in HashMap. It internally switches to balanced tree from linked list if there are more than 8 entries in one bucket. 

### LinkedHashMap

LinkedHashMap is just like **HashMap** with an **additional feature** of maintaining an **order** of elements inserted into it. 

- It implements the Map interface and extends HashMap class.  
- Insertion **order is preserved**
- Allow **one** **null** key and multiple null values



**Constructors in LinkedHashMap:**

- LinkedHashMap(): 
- LinkedHashMap(int capacity): 
- LinkedHashMap(Map m_a_p): 
- LinkedHashMap(int capacity, float fillRatio): 
- LinkedHashMap(int capacity, float fillRatio, boolean Order): 
  - True is passed for last access order.
  - False is passed for insertion order.



**Methods**

- clear, containsKey, get,
-  **boolean removeEldestEntry(Map.Entry eldest)**:  The method is used to return true when the map removes its eldest entry from the map.

# SortedMap Interface 

The main characteristic of a SortedMap is that, it **orders the keys** by their natural ordering, or by a specified comparator. 

- **null** **key** or null **value** are **not** permitted.
- The keys are sorted either by natural ordering or by a specified comparator.

**Methods:**

- **subMap(K fromKey, K toKey)**
- **headMap(K toKey)** : Returns a group of the elements of this Map whose keys are strictly less than toKey.
- **tailMap(K fromKey)**: Returns a group of the elements of this Map whose keys are greater than or equal to fromKey.
- **firstKey()**
- **lastKey()**
- **comparator()**



# NavigableMap Interface

NavigableMap is an **extension** of **SortedMap** which provides convenient navigation method like lowerKey, floorKey, ceilingKey and higherKey.



**Methods:**

**Methods** of NavigableMap:

1. **lowerKey(Object key)** : Returns the key strictly **less** than the given key

2. **floorKey(Object key)** : Returns the  key **less than or equal** to the given key

3. **ceilingKey(Object key)** : Returns the least key **greater than or equal** to the given key

4. **higherKey(Object key)** : Returns the key strictly greater than the given key

5. **descendingMap()** : Returns a reverse order map.

6. **headMap(object toKey, boolean inclusive)** : Returns a view of the portion of this map whose keys are **less** than (or equal to, if inclusive is true) toKey.

7. **subMap(object fromKey, boolean fromInclusive, object toKey, boolean toInclusive)** : Returns a view of the portion of this map whose keys **range** from fromKey to toKey.

8. **tailMap(object fromKey, boolean inclusive)** : Returns a view of the portion of this map whose keys are **greater** than (or equal to, if inclusive is true) fromKey.

   ​	

## TreeMap

The TreeMap in Java is used to implement Map interface and NavigableMap interface.**Ordering** of the elements is maintained by a set using their natural ordering or explicit comparator.

- **Duplicate** keys are not allowed
- **Null** keys are **not** allowed
- Insertion **order** is **not** preserved
- store data on basis of **sorting order**
- TreeMap is based upon tree data structure 
-  guaranteed log(n) time cost for the **containsKey, get, put and remove** operations. 

**Constructors in TreeMap:**

- TreeMap()
- TreeMap(Comparator comp) 
- TreeMap(Map m) 
- TreeMap(SortedMap sm) 

**Methods:**

- containsKey, containsValue, firstKey, lastKey, get, remove, putAll, entrySet, keySet, 

- **SortedMap headMap(Object key_value):** The method returns a view of the portion of the map strictly less than the parameter key_value

- **Object put(Object key, Object value):** The method is used to insert a mapping into a map

- **SortedMap subMap((K startKey, K endKey):** The method returns the portion of this map whose keys range from startKey, inclusive, to endKey, exclusive.

  

## IdentityHashMap class 

This class is not a general-purpose Map implementation. While this class implements the Map interface, it intentionally **violates Map’s general contract**, which mandates the use of the **equals method** when comparing objects. 

IdentityHashMap is hashMap which compare key is already exist or not to avoid duplication on the basis of **'==' operator**. where hashmap use equals method.

**equals vs ==**

```java
Integer no1 = new Integer(10);
Integer no2 = new Integer(10);

no1==no2             // false  - Compare address
no1.equals(no2)     // true    - Compare value
```



## WeakHashMap class 

WeakHashMap is the Hash table based implementation of the Map interface, with **weak keys**.  

In HashMap those objects which does not have any reference, are still not eligible to garbage collector, HashMap dominates the GC.

Where as weakHashMap that object will be cleared by GC.

- **Both** **null** values and null keys are **supported** 

- This class is intended primarily for use with key objects whose equals methods test for object identity using the **== operator**.

**Constructors in WeakHashMap:**

1. **WeakHashMap():**  default initial capacity-(16) and load factor (0.75)
2. **WeakHashMap(int initialCapacity):**  default load factor (0.75)
3. **WeakHashMap(int initialCapacity, float loadFactor)** 
4. **WeakHashMap(Map m)**

**Methods:**

- clear, containsValue, containsKey, put, isEmpty



**Differences**

| Property                   | HashTable     | HashMap       | LinkedHashMap         | TreeMap                                         |
| -------------------------- | ------------- | ------------- | --------------------- | ----------------------------------------------- |
| Iteration order            | Not preserved | Not preserved | preserved             | Not preserved. Store data on some sorting order |
| Get/put remove containsKey | O(1)          | O(1)          | O(1)                  | O(log(n))                                       |
| Interfaces                 | Map           | Map           | Map                   | Map, NavigableMap, SortedMap                    |
| Null                       | Not allowed   | allowed       | allowed               | only values                                     |
| Implementation             | buckets       | buckets       | double-linked buckets | Red-Black Tree                                  |
| Is synchronized            | yes           | no            | no                    | no                                              |



# Concurrent Collections

**Need of Concurrent Collections**

- Traditional collections are **not thread safe** 
- Some collections are thread safe but performance is low (vector, hashtable)
- when one thread is iterating data, at same time if another thread changes collection data, then it will throw concurrentModification exception



Java.util.concurrent contains 

- **ConcurrentHashMap**
- **CopyOnWriteArrayList**
- **CopyOnWriteArraySet**



```java
								Map
								 |
						    ConcurrentMap
								 |
						   ConcurrentHashMap
```

## ConcurrentHashMap

- introduced in JDK 1.5 
- perform lock technique, segment lock, bucket level lock
- concurrency level is number of locks provided
-  Any number thread to read operation allowed  at a time
-  multiple update/write in safe way
-  ConcurrentHashMap does not lock the entire collection for synchronization. 

**Methods:**

- putIfAbsent 
- remove
- replace(key, old,new)

**Constructor:**

ConcurrentHashMap c=new ConcurrentHashMap()  // default cap-**16**, fill-ratio**-0.75**, concurrency level = **16**

ConcurrentHashMap c=new ConcurrentHashMap(initial_capacity, fill_ratio,concurrency level)

## **CopyOnWriteArrayList**

**CopyOnWriteArrayList** is thread safe version of ArrayList

Every write operation, clone copy is created 

and write operation done on clone copy, 

after that JVM sync these both copies.

```java
                                        collection   (I)
                                            |
                                           List      (I)
                                            |
                                    CopyOnWriteArrayList    (Class) 	
```

If less write and more read operation is there then **CopyOnWriteArrayList** is recommended

Because for 1000 write operation there will be 1000 copy of arraylist.

- Insertion is **preserved**
- **duplicates** are allowed 
- heterogeneous Objects are allowed
- Iterator of **CopyOnWriteArrayList** can't perform remove operation (UnsupportedOperationException  exception)



**Methods:**

- boolean **addIfAbsent**(Object o)

- **addAllAbsent**(collection c)

  

## **CopyOnWriteArraySet**

```
 									collection   (I)
                                            |
                                           Set      (I)
                                            |
                                    CopyOnWriteArraySet    (Class) 	
```

- duplicate **not** allowed 

- Insertion order is **preserved**

- **heterogeneous** data allowed

- **Null** value **allowed**

- Fails-safe

- Iterator can **not** **remove** data

- While one thread iterating the Set, other threads can perform updation, here we wont get any runtime exception like ConcurrentModificationException.

  

## **BlockingQueue** 

- providing **inbuilt blocking support** for put() and take() method 
- put() method will block if Queue is full 
- take() method will block if Queue is empty.  
- implement FIFO ordering of element 
- implementation ArrayBlockingQueue and LinkedBlockingQueue
- **ArrayBlockingQueue** is backed by Array and its **bounded** in nature 
- while **LinkedBlockingQueue** is **optionally bounded** 
- **PriorityBlockingQueue** ordered on **priority** and useful if you want to process elements on order other than FIFO
- using BlockingQueue to solve **producer Consumer problem** 

### ConcurrentSkipListMap and ConcurrentSkipListSet 

- **ConcurrentSkipListMap** and **ConcurrentSkipListSet** provide concurrent alternative for synchronized version of **SortedMap** and **SortedSet**.  
- instead of using TreeMap or TreeSet wrapped inside synchronized Collection, You can consider using ConcurrentSkipListMap or ConcurrentSkipListSet from java.util.concurrent package. 







=================================================================================

- peek -  retrieve data without modifying it (pop- remove|push - add )

- offer - Its like add method with difference add method throw exception when element can not be   added where offer method doesn't.

- headSet - return group of elements set whose elements are less than element

- tailSet - return group of elements set whose elements are grater than element

- poll() : Retrieve and remove the element

  ​	