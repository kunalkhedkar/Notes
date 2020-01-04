# RxJava

> **observable** is something that emits.
> **observer** consumes the items emitted by the observables.

### Observable vs Observer

**Observable** is for the consumer, it can be transformed and subscribed:

```
observable.map(x => ...).filter(x => ...).subscribe(x => ...)
```

**Observer** is the interface which is used to feed an observable source:

```
observer.next(newItem)
```

### Observable Interface 

observable is base bone of any reactive approach we have.

It passes down three types of evets

- onNext			: It passes down emission into the observer instance 
- onComplete   : called when the observable emission are finished 
- onError           : when an error occur in the onNext method

***Example of Observable***

```java
        Observable<Integer> source=Observable.just(1,2,3,4,5,6,7,8,9,10);
```

***Example of Observer***

```java
        Observer<Integer> observer=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe: ");
            }

            @Override
            public void onNext(Integer s) {
                System.out.println("onNext: "+s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete: ");
            }
        };
```



## Cold vs Hot Observable

cold and hot Observable depends upon which factory we use to create observable.

**Cold Observable ** `Just Factory` `FromIterable Fractory`

If we have multiple observer for our observable in this case it emits all values to first observer and called onComplete.

```java
//Output
observer 1 : one
observer 1 : two
observer 1 : three

observer 2 : one
observer 2 : two
observer 2 : three

```

**Hot Observer** `ConnectableObservable`

Is opposite to cold observable, it emits first element to all observer and then second element and called onComplete on all observers. 

```java
 		//just observable
 		Observable<String> source=Observable.just("one","two","three");
 		// converting just observable to ConnectableObservable
        ConnectableObservable<String> hotSource=source.publish();
        
        hotSource.subscribe(e->System.out.println("observer 1 : "+e));
        hotSource.subscribe(e->System.out.println("observer 2 : "+e));
        hotSource.connect();
```

```java
//Output
observer 1 : one
observer 2 : one
observer 1 : two
observer 2 : two
observer 1 : three
observer 2 : three
```



## Observable Factories

#### 1. Just

- It can only take up to 10 emission
- Observable.just(1,2,3,4,5,6,7,8,9,10);

#### 2. Create

```java
Observable<Integer> source =Observable.create(emitter->{
    emitter.onNext(11);
    emitter.onNext(22);
    emitter.onNext(33);
    emitter.onComplete()
});
```

#### 3. FromIterable

The `Observable.fromIterable` belongs to the family of `from` operators. we can create observable from any iterable object.

```java
    List<String> carsList = new ArrayList<>();
    carsList.add("Mazda");
    carsList.add("BMW");
    carsList.add("Toyota");
    
    Observable.fromIterable(carsList)
```

#### 4. FromArray

`Observable.fromArray` is similar to `fromIterable` but it accepts a variable number of arguments instead.

```java
   String[] cars = new String[] {"Mazda", "BMW", "Toyota"};
    Observable.fromArray(cars)
```

All the factories that handles time units, they are work on different thread than the main.

#### 5. Interval Factory

After each interval you specify it will emit a number beginning from 0. 

```java
// time unit work on different thread
Observable.interval(200, TimeUnit.MILLISECONDS).subscribe(System.out::println);
// If we dont sleep main thread, main thread will execute and get completed very quickly.
try {
	Thread.sleep(4000);
} catch (InterruptedException e) {
	e.printStackTrace();
}
```

#### 6. Range Factory

It will take 2 parameter `start` and `count`, It will start emitting from `start`, and emit `count` number of emission.

```java
        Observable.range(10, 5).subscribe(System.out::println);
```

```java
//Output
10
11
12
13
14
```

#### 7. Empty

It does not emit anything. It directly call onComplete.

> From Rxjava2 Null value emission is not allowed.

```
Observable.empty();
```

#### 8. Never

It does not emit anything, but also not call onComplete. so observer will observe this forver.

```
Observable.never();
```

#### 9. Error

```java
Observable.error(new Exception("My error"))
			.subscribe(System.out::println,
			Throwable::printstacktrace,
			()->System.out.println("Done"));
```

#### 10. Defer

```java
 1       int a=0, b=10;
 2       Observable<Integer> source =Observable.range(a,b);
 3       source.subscribe(System.out::println);
 4       b=15;
 5       source.subscribe(e->System.out.println("observer2 -> "+e));
```

Here, we expected second observer take updated value of b and print 0-14, But it **NOT**.

observable is created at line 2, so no matter what value change after creation of observable it will not take new value.

**Solution** for this is Defer Factory.

```java
Observable<Integer> source=Observable.defer(()->Observable.range(a,b));
```

Defer factory postpone the creation of observable instance until an observable subscribe.

note: At the end we will have 2 different observable object.



## Types of Observable

#### 1. Observable

This is the simplest Observable which can emit more than one value.

#### 1. Single

It will emit only once so, there is no onNext and onComplete, instead of that onSuccess is there with emit value.

```java
	Observables.just("a","b").first("default").subscribe(System.out::println);
```

#### 2. Maybe

Instead of onNext it has onSuccess to emit value, and also it has onComplete because maybe also except an empty observable.

If it has a value then it will call onSuccess and  also call onComplete, if it has nothing it call onComplete.

We need to deal with such type of observable because one of the main operator filter returns 

```java
        MaybeObserver<String> ob=new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {   }

            @Override
            public void onSuccess(String s) {
                // data emission
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {
                //complete
            }
        };
```

#### 3. Completable 

Completable observable doesn't care about any emission. It only have onComplete and onError method 

```java
Completable.fromRunnable(()-> System.out.println("Do some task"))
                .subscribe(()->System.out.println("from onComplete"));
```

output : Do some task | from onComplete

#### 4. Flowable 

Flowable  comes to picture when there is a case that the Observable is emitting  huge numbers of values which can’t be consumed by the Observer.

In this case, the Observable needs to skip some values on the basis of some strategy else it will throw an exception.

The Flowable Observable handles the exception with a strategy. The strategy is called **BackPressureStrategy** and the exception is called **MissingBackPressureException**.



## Dispose using Disposable

Sometimes we need to stop observer of subscriber manually, it is automatically when onError happens or onComplete is triggered. 

```java
 Observable<Long> source=Observable.interval(1,TimeUnit.SECONDS);
        Disposable disposable = source.subscribe(System.out::println);
        msleep(3000);
        disposable.dispose();
        System.out.println("observer disposed");
        msleep(3000);
        System.out.println("The End");

//outpout: 
0
1
2
observer disposed
The End
```

Above example we are emitting number after every 1 sec, when we want to stop subscriber we need to dispose the subscriber. so after dispose we have not seen any emissions.

```java
        Observer<Integer> source = new Observer<Integer>() {
            Disposable mydisposable;
            @Override
            public void onSubscribe(Disposable d) {
                mydisposable=d;
            }

            @Override
            public void onNext(Integer integer) {
            
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
```

Another way to declare disposable internally. 

​	

## Operators

#### Types of Operator

**1. Suppressing Operator** [Filter]
	It takes the emission and check it matches certain condition, if match it take will take it, else discard it.

**2. Collection Operator**
	If we want to make multiple emission to store in collection.

**3. Reducing** **Operator** [Max]
	We take all the emissions and and return only one value.

**4. Error Operator**
	We use them when error is occurred

**5. Action Operator**
	We use this when we want to perform certain action after certain type of emission.

**6. Transformative Operator** [String -> numberOfCharacters]
	If you receive one type of observable and you want to return another type of observable.	



> **Observable<String> source=Observable.just("Alpha","Beta","Zeta","Omega");**

#### 1. Filter

It take predicate as parameter, predicate is actually functional interface which takes a condition lambda expression and then return a boolean. Filter returns observable and does not make any transformation.

```java
        source.filter(e->e.length()>4).subscribe(System.out::println);

//Alpha
//Omega
```

#### 2. Take

It only takes number of emission we precise.

```java
        source.take(3).subscribe(System.out::println);
// "Alpha","Beta","Zeta"
```

#### 3. Skip

It skips emissions we precise.

```java
        source.take(3).subscribe(System.out::println);

//Omega
```

#### 4. First

It emits first value. it also accept default value.

#### 5. TakeWhile

It will emit up to first condition get satisfied.

```java
		source.takeWhile(e->e.length()>4).subscribe(System.out::println);

//Alpha
```

#### 6. SkipWhile

It will skip only first emission which satisfied condition.

```java
		source.skipWhile(e->e.length()>4).subscribe(System.out::println);
//"Beta","Zeta","Omega"
```

#### 7. Distinct

it will remove duplicate.

```java
Observable<String> source=Observable.just("Alpha","Beta","Zeta","Omega","to","sun","k");
source.distinct(String::length).subscribe(System.out::println);
//Alpha Beta to sun k
```

#### 8. Map

 Map transforms the items emitted by an Observable by applying a function to each item.

```java
source.map(e->e.length())
.subscribe(System.out::println);
// 5-4-4-5
```

Here we have emissions of Strings alpha, beta.. but we transform string observable to integer one.

#### 9. Cast

```java
source.cast(Objects.class)
      .subscribe(System.out::println);
```

#### 10. StartWith

startWith block will execute first, then emissions will.

```java
source.startWith("Do start with it")
                .subscribe(System.out::println);
```

#### 11. DefaultIfEmpty 

If Observable is empty it will emits default value.

```java
        Observable.empty().defaultIfEmpty("default item")
                .subscribe(System.out::println);
```

#### 12. SwitchIfEmpty

If observable is empty we can emit another observable.

```java
        Observable.empty().switchIfEmpty(anotherObservable)
                .subscribe(System.out::println);
```

#### 13.  Delay

If you want to emit observable after some delay, we can use delay operator.

```java
source.delay(5,TimeUnit.SECONDS)
                .subscribe(System.out::println);
```

#### 15. Sorted

```java
Observable<Integer>sourceNumber=Observable.just(5,7,2,7,3,8,1);       sourceNumber.sorted()
                .subscribe(System.out::println);
//for reverse
sourceNumber.sorted(Comparator.reverseOrder())
```

#### 16. Repeat

```javascript
			source.repeat(3)
                .subscribe(System.out::println);
```

#### 17. Scan

Its a rolling aggregator, what it does is  aggregate all element. Lets say we want to total to be added to in next element.

```java
Observable<Integer> sourceNumber=Observable.just(2,3,1);
    sourceNumber.scan((total, nextElement) -> total+nextElement)
     	.subscribe(System.out::println);

//output  2  5  6
```

#### 18. Reduce

apply a function to each item emitted by an Observable, sequentially, and emit the final value. It returns **maybe** observable.

```java
Observable<String> sourceChar = 
				Observable.just("k","u","n","a", "l");
sourceChar.reduce((preTotal, next) ->
                preTotal + (next.equals("") ? "" : "|" + next)
        ).subscribe(System.out::println);
//output k|u|n|a|l        
```

##### 18.1 All

```java
Observable.just("k","u","n","a", "l")
    .all(s -> s.length()==1)
                .subscribe(System.out::println);
// output: True                
```

##### 18.2 Any

```java
Observable.just("k","u","n","a", "kun")
                .any(s -> s.length()==3)
                .subscribe(System.out::println);
// output: True                
                
```

Note : For empty observable all return true and Any returns false.

##### 18.3 count

 It single observable returns a number of emissions.

```java
Observable.just("k","u","n","a", "l")
                .count()
                .subscribe(System.out::println);
//output : 5                
```

##### 18.4 contains

 It takes element as a argument and check it is present in observable or not and return single<Bolean>.

```java
			    source.contains("alpha")
                .subscribe(System.out::println);
//output : ture                
```

#### 19. Collection

##### 19.1 toList

##### 19.2 toSortedList

##### 19.3 toMap

```java
Observable.just("k","u2","n33","a444", "l5555")
                .toMap(String::length)
                .subscribe(System.out::println);
output : {1=k, 2=u2, 3=n33, 4=a444, 5=l5555}                
```

##### 19.4 toMultimap

​	In toMap if key found to be duplicate value get override but in it will store multiple value if key found duplicate.

```java
      Observable.just("k","u","n","a", "L")
                .toMultimap(String::length)
                .subscribe(System.out::println);
//output : {1=[k, u, n, a, L]}   
//toMap  : {1=L} 
```

##### 19.5 collect

It collects the emission, it takes two parameters, first one is what type of collection we going to use and second is add element.

```java
source.collect(HashSet::new,
                HashSet::add)
                .subscribe(System.out::println);
//output ; [Zeta, Alpha, Omega, Beta]                
```

#### 20. Error

##### 	20.1 onErrorReturnItem	

```java
        Observable.just(12,20,0,8,4)        
            .map(integer -> 100/integer)        
            .onErrorReturnItem(-1)        
            .subscribe(System.out::println);
//output : 8 5 -1
```

##### 	20.2 onErrorResumeNext

​		on error it can emit another observable.

```java
        Observable.just(12,20,0,8,4)
                .map(integer -> 100/integer)
                .onErrorResumeNext(Observable.just(1000,2000))
                .subscribe(System.out::println);
//output ; 8 5 1000 2000                
```

#### 21. Retry 

​	It will retry to emit element by specified times from beginning.

```java
  Observable.just(2,6,0,8,4)
                .map(integer -> 16/integer)
                .retry(2)
                .subscribe(System.out::println);
/*output
8 2	 [normal emission]
8 2	 [retry 1]
8 2  [retry 2] */
```

#### 22. Action

Action operator are use to perform certain action before onNext happens, before onComplete trigger and before onError occurs. 

```java
Observable.just(2,6,0,8,4)
     .map(integer -> 16/integer)
     .doOnNext(integer -> System.out.println("Preparing onNExt"))
     .doOnComplete(() -> System.out.println("Completed"))
     .doOnError(throwable -> System.out.println("Error occured"))
     .subscribe(System.out::println);
/*output : 
Preparing onNExt
8
Preparing onNExt
2
Error occured
//onComplete is not called as we got an error.
*/
```

### Combining Observables

#### 1. Merge

we can **merge** two or more observables. merge takes up to 4 observable, if we want to merge more than 4 we need to use **mergeArray**.  And for merging two observable we also have **mergeWith** which work with two observables.

```java
Observable<String> source1 = Observable.just("Alpha", "Beta");
Observable<String> source2 = Observable.just("Zeta", "Omega");
Observable<String> source3 = Observable.just("Eta","Theta");
         
         Observable.merge(source1,source2,source3)
                 .subscribe(System.out::println);
//output : Alpha Beta Zeta Omega Eta Theta                 

```

#### 2. FlatMap

​		Transform the items emitted by an Observable into Observables, then flatten 		the emissions from those into a single Observable

```java
    source.flatMap(new Function<String, Observable<String>>() {
       @Override
         public Observable<String> apply(String str){
                return Observable.fromArray(str.split(""));
            }
        }).subscribe(System.out::println);
//output : A l p h a B e t a Z e t a O m e g a        
```

#### 3. Concat

We use **concat** to merge up to 4 observable, but special thing of concat is it respect the order where as merge does not give any guarantee of order. Also **concatWith** for two observables. And **concatArray** for more than 4 observables.

```java
     Observable.concat(source1,source2,source3)
                .subscribe(System.out::println);
//output : Alpha Beta Zeta Omega Eta Theta    
```

#### 4. Zip

Zip combine the emissions of multiple Observables together via a 
specified function and emit single items for each combination based on 
the results of this function.

```java
Observable<String> source1 = 								Observable.just("Alpha","Beta","Eta","Meta");
Observable<String> source2 = Observable.just("Zeta", "Omega");

        Observable.zip(source1,source2,
                        (s, s2) -> s+"-"+s2)
                .subscribe(System.out::println);
//output : Alpha-Zeta
//Beta-Omega     [ignor Eta and Meta as they have not pair]
```

#### 5. Combine latest

```java
Observable<Long> source1=Observable.interval(1,TimeUnit.SECONDS);
Observable<Long>source2=Observable.interval(300,TimeUnit.MILLISEC);
```

If we have case there observables emitting data on different time.

Here source1 is emitting data after 1 sec where as source2 emitting data much more faster than source1.

**Zip**

```java
        Observable.zip(source1,source2,
                (s, s2) -> s+"-"+s2)
                .subscribe(System.out::println);
// output 0-0	1-1		2-2		3-3		4-4
```

In Zip case, zip will queue all emitted value which are unpair yet, wait for emission coming from another observable.

**Combine latest**

```java
        Observable.combineLatest(source1,source2,
                (s, s2) -> s+"-"+s2)
                .subscribe(System.out::println);

/* output : 
0-2
0-3
0-4
0-5
1-5
1-6
1-7
1-8
1-9
2-9
2-10
2-11
2-12
3-12
3-13
3-14
3-15
4-15 */

```

Source1 emit 0 at this time source2 is at 2, as source2 is faster it emit 3 but source1 has not emitted any value yet so it will pair with latest value which is 0. that's we found duplicate pairs.

**withLatestFrom**

```java
        source1.withLatestFrom(source2,
                (s, s2) -> s+"-"+s2)
                .subscribe(System.out::println);
/* output 
0-2
1-5
2-8
3-12
4-15		*/
```

As we have applied this on source1, so it will wait for each emission from source1 and combine it with latest value of source2.

#### 6. Group

```java
Observable<String> source = Observable.just("Red", "blue", "green", 							"black","grey","brown");
Observable<GroupedObservable<Character,String>> groupedSource = 						source.groupBy(s -> s.charAt(0));

        groupedSource.flatMapSingle(e -> e.toList())
                .subscribe(System.out::println);
/* output
[Red]
[blue, black, brown]
[green, grey]	*/
```

As we have provided group condition as a first character, it will group base on first character matching.

### Multicasting 

Multicasting is a key method for reducing duplicated work in RxJava. When you multicast an event, you send the *same* event to *all* downstream subscribers. 

Any work done before the `ConnectableObservable` or `Subject` will only happen once, then that work will be multicast to all downstream `Subscribers`.



[https://blog.danlew.net/2016/06/13/multicasting-in-rxjava/ Multicasting]: Multicasting

There are two ways to multicast:

1. Use a **ConnectableObservable** (via `publish()` or `replay()`[1](https://blog.danlew.net/2016/06/13/multicasting-in-rxjava/#footnote1))
2. Use a `Subject`

![Chart with correct publish usage](http://i.imgur.com/Yf1C8vu.png)

#### What is ConnectableObservable?

we use Connectable Observable if multiple Observers needs same data stream

It will **not start** if calls `subscribe` method, it will **start** if calls `connect` method

```java
Obervable<User> getUsers() { return Observalbe.just(User1, User2, User3);}

Obervable<User> users = getUsers();
users.subscribe(o1);
users.subscribe(o2);
users.subscribe(o3);
```

It has a performance problem. because it will create a new data stream when ever `subscribe` method is called. so getUsers will executes 3 times in this case.



It can be solved. We use Connectable Observable.

```java
ConnectableObservable<User> users = getUsers().publish();
users.subscribe(o1);
users.subscribe(o2);
users.subscribe(o3);
users.connect(); // it's important!!
```

Connectable Observable is created when we apply `publish` operator.

Observers subscribes a Connectable Observable that waits until `connect` method is called. It begins emitting items to those Observers.

##### AutoConnect

Another way to deal with connectable Observable is autoConnect

we can use publish on operation and apply autoConnect on publish then we dont need to use connectable observable and also we don't need to call connect after subscribe.

If we know how many observable we have then you should go with autoConnect else stick to connectable.

```java
        Observable<Integer> source=Observable.range(1,5)
                .map(integer -> integer+10)
                .publish()
                .autoConnect(2);

        source.subscribe(System.out::println);
        source.subscribe(System.out::println);
```

If you don't pass number of observable to `autoConnect` it assumes that there is only one observable. 

and if we use 2 observable then autoConnect will not emit values from beginning to 2nd observable.

2nd observable will get live emissions only.

solution for that is use `refCount` instated of `autoConnect`. `refCount` will emit from beginning once first observable get dispose.

there is alias for `publish`+`refCount`is `share`. It does same.

###  Concurrency and Parallelization

#### Schedulers

Schedulers are used to create new thread on which each observer works on. 

To use scheduler we applied on the factory function called **subscribeOn** and as a parameter we pass type of scheduler we want use.

- `Schedulers.computation`

  when we have some complex computation that uses CPU. 

- `Schedulers.io`

  io: input/output use this when you deal with databases,  web-request, file-operation 

- `Schedulers.newThread`

  If you want to delete thread after used you need to use `newThread`.

- `Schedulers.Single`

  If you want to work with sequentially, that means take  a first observable finish with it and then take a second one.



##### Schedulers.computation

```java
 Observable<String> source=Observable.just(getRandomString(), 
                                           getRandomString(), 																		getRandomString())
                					.subscribeOn(Schedulers.computation());
									// schedule on computation

        source.subscribe(s -> println("Observable 1 : \t"+s+"\t"+ LocalTime.now()));
        source.subscribe(s -> println("Observable 2 : \t"+s+"\t"+ LocalTime.now()));

        msleep(3000);
```

If you are done with operation then you want to do some work on another thread you can switch thread using method called **`observeOn()`.**

**1. call Three Apis Sequentially**

```java
    fun callThreeApisSequentially(){

        var list : MutableList<String> = ArrayList()

        DisposableManager.add(
            RetroClass.getNormalApiService().allPhotos
                .subscribeOn(Schedulers.io())
                .flatMap {photos ->
                    list.add(photos.toString())
                    RetroClass.getNormalApiService().firstPost }
                .flatMap {post ->
                    list.add(post.toString())
                    RetroClass.getNormalApiService().commentsForFirstPost }
                .map {response->
                    response}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {result ->
                        list.add(result.toString())
                        view.toggleProgressBar(false)
                        //Log.i(TAG, "result->$result")
                        Log.i(TAG, "list->$list")
                    },
                    {error ->
                        view.toggleProgressBar(false)
                        Log.i(TAG,"error->$error.message)")}
                )
        )
    }
```

**2. call Three Apis Parallelly**

```java
 fun callThreeApisParallely(){
        val errorPhotoList : MutableList<Photo> = ArrayList()
        errorPhotoList.add(Photo(1,2,"a","b","c"))
        DisposableManager.add(
            Single.zip(RetroClass.getNormalApiService().allPhotos
                , RetroClass.getNormalApiService().firstPost,
                RetroClass.getNormalApiService().commentsForFirstPost,
                Function3<List<Photo>, Post, List<Comment>, List<String>> { type1, type2, 					type3 ->
                    val list : MutableList<String> = ArrayList()
                    list.add(type1[0].toString())
                    list.add(type2.toString())
                    list.add(type3[0].toString())
                    list
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {result ->
                        view.toggleProgressBar(false)
                        Log.i(TAG, "result->$result")},
                    {error ->
                        view.toggleProgressBar(false)
                        Log.i(TAG,"error->$error.message)")}
                )
        )
    }
```

### Flowables and Backpressure

- Flowables are the alternative for observable. There are some circumstances where we prefer flowables over observables.
- Flowables supports **backpressure**. Backpressure happens when you have huge amount of data being emitted by the observable(flowable). this emission fired at a higher pace than what the observer can handle. In such case observer will not have enough time to handle all of the coming emission, then you will end up with some emissions been lost. To handle that you need backpressure and to use backpressure you need to use flowables. 
- We use flowables when we have asynchronous  operations and a higher concurrency usage.
- TCP/UDP requests and streaming APIs that needs backpressure.

```java
Flowable.range(0, 500000000)
                .doOnNext(integer -> Sout("emission num : " + integer + " is coming"))
                .subscribeOn(Schedulers.computation())
                .subscribe(new FlowableSubscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        msleep(200);
                        System.out.println(integer);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
        msleep(10*1000);
```

##### Backpressure Strategies 

[medium page]: https://medium.com/@jayphelps/backpressure-explained-the-flow-of-data-through-software-2350b3e77ce7

If we are using **create** factory on flowable, we need to manually implement the backpressure. Some factory  like **range** implement backpressure strategies internally

```java
Flowable<String> source = Flowable.create(emitter -> {
            emitter.onNext("one");
            emitter.onNext("two");
            emitter.onNext("three");
        }, BackpressureStrategy.MISSING);
        source.subscribe(System.out::println);
```

1. **BUFFER**  

   Buffer will tell the emissions it needs to cache all the emissions that can not handle right away by the observable or the subscriber.

2. **DROP**

   Drop will drop or omit all the emissions that can not be handle by observable.

3. **ERROR**

   When subscriber can not keep up the flowable, it will throw an error and error is backPressureMissingException.

4. **LATEST**

   It will take last emission and it will cache it each time until subscriber is ready to received.

5. **MISSING**

   If we specify missing, it will not implement backpressure and tells the flowable that we will use some backpressure operators.

##### OnBackPressure operators

If we don't specify any backpressure strategy or if we used MISSING backpressure strategy, we can use backpressure operators. 

It acts the same way as the backpressure strategies except it has a set of arguments we can specify.

```java
private static void backPressureOperator() {
   Flowable<Long> source = Flowable.interval(1, TimeUnit.SECONDS);
   source.onBackpressureBuffer(
          10,     // size of an buffer : up to 10 values
          () -> System.out.println("overflow"),       // action to be trailered
          BackpressureOverflowStrategy.DROP_LATEST);  //backPressure overflow strategy
        
   source.onBackpressureLatest();  // it will only take last emission from the flowable
   source.onBackpressureDrop();    // drop all the emissions been kept
}
```

### Switching, Buffering, Windowing and Throttling

These are not purely alternative, but they can do some work-around using backpressure on observables.

As we know observables do not implement backpressure, we need to use flowables if we want to work with backpressure. But some time we forced to work only with the observables.

If we are going to work with observables and we still need backpressure, we can use these opeartors.

1. Buffering
2. Switching
3.  Windowing 
4. Throttling

#### 1. Buffer

**i. FixedSize**

```java
	Observable.range(0,100)
              .buffer(10)
              .subscribe(System.out::println);
```

Buffer will take each element from the observable and it will filled list that it construct base on argument we pass as fixedsize. Buffer doesn't care about if observable is busy or not, it will take elements wait till list filled, then pass it down to the observer.

**ii. TimeBase**

```java
Observable.interval(300,TimeUnit.MILLISECONDS)
                .buffer(1,TimeUnit.SECONDS)
                .subscribe(System.out::println);
```

Here interval factory fires new emission at each 300 milliseconds, but if we want to buffer these emission and each second they should pass down to observer we need use time-based overloaded buffer method. 

It will wait for 1 second and buffer all the emissions from the interval factory and after 1 second life-time it will passes down all emissions to observer.

**iii. fixedSizeWithSkip**

```java
Observable.range(1,100)
                .buffer(4,5)
                .subscribe(System.out::println);
                
//output
[1, 2, 3, 4]
[6, 7, 8, 9]
[11, 12, 13, 14]
[16, 17, 18, 19]
[21, 22, 23, 24]
[26, 27, 28, 29]
[31, 32, 33, 34]
[36, 37, 38, 39]
[41, 42, 43, 44]
[46, 47, 48, 49]

```

**Skip > count**

We can add 2nd parameter in buffer as skip. Skip will skip element starting from count and up to skipth element. In above case it will create buffer of 4 elements and every 5th element will be skip. 

```java
buffer(4,10)
//output
[1, 2, 3, 4]
[11, 12, 13, 14]
[21, 22, 23, 24]
[31, 32, 33, 34]
[41, 42, 43, 44]
```

**Skip < count**

There is behavior change in skip if we pass skip value less than buffer count. It will cache (count-skip) elements and re-emits.

```java
.buffer(5,2)
//output
[1, 2, 3, 4, 5]
[3, 4, 5, 6, 7]		// 3, 4, 5 re-emited
[5, 6, 7, 8, 9]		// 5, 6, 7 re-emited
[7, 8, 9, 10, 11]	// 7, 8, 9 re-emited
```

use collection for extract emitted values in form of collection. 

```
.buffer(5,2,HashSet::new)
```

**iv. Boundary**

```java
Observable.interval(300,TimeUnit.MILLISECONDS)
                .buffer(Observable.interval(1,TimeUnit.SECONDS))
                .subscribe(System.out::println);
```

boundary buffer will accept another observable as a input parameter. buffer will wait for emission from interval and while its waiting it will cache these values.

As boundary accepts observable, it will have multiple behavior. Above one has fixed behavior, in another case of observable, it will wait for that behavior to get completed and then pass down the emissions.

#### 2. Window

Window works exactly as a buffer but instead of returning a list it return an observable.

```java
Observable.range(0,50)
			.window(10)
			.flatMapSingle(e->e.count())
			.subscribe(System.out::Println
//output
10
10
```

window also support timeBase and boundary overload methods.

#### 3. switching  - [switchMap]

switchMap is a cold operator. SwitchMap is an operator that will switch from observable to another and disposing any previous emissions.

```java
Observable<String> items = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(value -> value + " : switched to first observable");

        Observable.interval(1,TimeUnit.SECONDS)
                .switchMap(aLong -> 
                	items.doOnDispose(() -> println("Item observable is been dispose")))
                .subscribe(System.out::println);
                
//output
0 : switched to first observable
1 : switched to first observable
2 : switched to first observable
Item observable is been dispose
0 : switched to first observable
1 : switched to first observable
2 : switched to first observable
Item observable is been dispose
0 : switched to first observable
1 : switched to first observable
2 : switched to first observable
Item observable is been dispose
0 : switched to first observable
1 : switched to first observable
2 : switched to first observable
```

while interval is emitting, switchMap will dispose the item observable and once the interval is silence emissions from items will be pass down.     

Here, item observable emits those string message value at each 300 milliseconds, because the 2nd interval is not doing anything, it is silent. so while it is silent item emissions will be passed down.

when 2nd interval factory emits the first value, item observable been dispose. once 2nd interval is silent item observable will start emits from beginning i.e from 0.

we can use this, in case of ignoring multiple tap events, and handle only one event. Like if we want to omit some redundant requests.

#### 4. Throttling

Using throttling we can pick specific emitted value base on time window. can be use as a debounce.

**i. Throttle First**

Using Throttle First you immediately receive the first emitted item, but then next items will come not sooner than `windowDuration` is expired.
So when new item is ready to be emitted, `throttleFirst` checks whether 
`windowDuration` is expired and if yes, then emits that item, otherwise 
discards it.

An example of usage is to avoid multiple click on button which starts some user-facing feature like opening new screen.

```java
        Observable.interval(1,TimeUnit.SECONDS)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(System.out::println);
//output
0
2
4
```

**ii. Throttle Last**

Returns an Observable that emits only the last item emitted by the source  during  time windows of a specified duration.

As throttleLast is basically ticking good example is timer and updating some data related to that timer with that same interval.

**iii. Throttle Latest**

In some sense `throttleLatest` is some kind of combination of `throttleLast` and `throttleFirst`. 

It is similar to `throttleFirst` as it guarantees that first item (in the given timeframe) will be emitted immediately. It is similar to throttleLast as when we have multiple events in the same time window — only latest will be emitted and other discarded.

**iv. ThrottleWithTimeout**

Basically `throttleWithTimeout` is an alias to debounce. 

ThrottleWithTimeout is similar to throttleLast, but with dynamic timer. Instead of having static ticking, timer is reset when new item is received.

This is very good for delayed events which require some long operation to provide results, such as loading suggestions or updating search query.  Basically debounce is good for filtering out input changes in text fields.

### Transformation

Transformers are use to compose  our own operators using a build in operator of rxJava. We can put all our operation we want to perform inside `observableTranformer` which has a function called apply then we can come back to our observable and use function compose on observable and put tranformer function as a parameter.

```java
	private static void transformer() {
        //can make Transformer fun here for chain operation of map and filter 
        Observable.just("Red", "blue", "green", "black", "grey", "brown")
                .map(s -> s.length())
                .filter(integer -> integer > 3)
                .subscribe(System.out::println);
        
		//using compose and ObservableTransformer
        Observable.just("Red", "blue", "green", "black", "grey", "brown")
                .compose(mapToInteger())
                .subscribe(System.out::println)
	}                

    public static ObservableTransformer<String, Integer> mapToInteger() {
        return new ObservableTransformer<String, Integer>() {
            @Override
            public ObservableSource<Integer> apply(Observable<String> upstream) {
                return upstream
                        .map(s -> s.length())
                        .filter(integer -> integer > 3);
            }
        };
    }
```

##### Custom Operator

we can create our own custom operator using `ObservableOperator` class.

To use custom operator we need to use another operator called **lift** and it takes another `observableOperator` and apply it on former observable.

```java
    private static void customOpeartor() {
        Observable.empty()
        	.cast(String.class)
        	.lift(defaultValueOperator("My default value"))
            .subscribe(System.out::println);
    }

public static ObservableOperator<String, String> defaultValueOperator(String dValue) {
        return new ObservableOperator<String, String>() {
            @Override
            public Observer<? super String> 
            apply(Observer<? super String> observer) throws Exception {
                return new Observer<String>() {
                    boolean isEmptyFlag = true;
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        isEmptyFlag = false;
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        if (isEmptyFlag) {
                            observer.onNext(dValue);
                        } else {
                            observer.onComplete();
                        }
                    }
                };
            }
        };
    }
```

###  Testing and Debugging

#### Blocking

Some observable factories like **interval** factory runs on another thread in that case we need to block main thread, to get done interval observable work. we have another solution for it, **BlockingSubscribe**

BlockingSubscribe will block all running thread until BlockingSubscribe observer call onComplete

```java
 Observable.interval(1,TimeUnit.SECONDS).take(10)
                .blockingSubscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("onComplete  Called"));
```

##### Blocking Operators

Blocking operator looks likes a very good option for a testing.

**1. blockingFirst**

It will give first emission of observable with blocking. It will not return observable it will return value.

```java
Observable<Long> source=Observable.interval(300,TimeUnit.MILLISECONDS).take(10);
long value=source.blockingFirst();
System.out.println(value);
//output : 0
```

Like blockingFirst we also have **2. blockingLast** which returns last emission of observable with blocking.

**3. blockingForEach** will emit each value with the blocking.

```java
Observable<Long> source = Observable.interval(300, TimeUnit.MILLISECONDS)
							.take(10)
							.filter(aLong -> aLong > 5);
source.blockingForEach(value -> System.out.println(value));
//output : 6 7 8 9
```

#### TestObservers

TestObservers has a bunch of great methods used for testing.

```java
Observable<Integer> source=Observable.range(0,5);
TestObserver<Integer> testObserver=new TestObserver<();

testObserver.assertNotSubscribed();     // to check no subscription has occurre
dsource.subscribe(testObserver);        // SUBSCRIBE
testObserver.assertSubscribed();        // to check subscription has occurred
//Block and wait for observables to terminate
testObserver.awaitTerminalEvent();
testObserver.assertComplete();          // to check onComplete called
testObserver.assertNoErrors();          // to check no error has occurred
testObserver.assertValueCount(5);       // to check how many values been emitted
testObserver.assertValues(1,2,3,4,5);   // to check emitted values
```

#### Manipulating Time with Schedulers

Imagine we need deal with time related observable we can not always wait for specified time to trigger. so we can manipulate time with schedulers and we don't need to wait for specified  time to trigger to test.

```java

        TestScheduler testScheduler=new TestScheduler();
        Observable<Long> source=Observable.interval(1,TimeUnit.MINUTES);

        TestObserver<Long> testObserver= new TestObserver<>();

        source.subscribe(testObserver);
        testScheduler.advanceTimeBy(30,TimeUnit.SECONDS);	//add time
        testObserver.assertValueCount(0);
        testScheduler.advanceTimeTo(2,TimeUnit.MINUTES);	//set time
        testObserver.assertValueCount(2);
        testScheduler.advanceTimeTo(90,TimeUnit.MINUTES);
        testObserver.assertValueCount(2);
```

`advanceTimeBy` will add the specified time in currently running interval time, where as 

`advanceTimeTo` will set specified time as currently running interval time.

