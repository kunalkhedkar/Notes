"create observables"
	observables.from
		-creates an observable from an Iterable
		Observable.from(listOf(1, 2, 3, 4, 5))

	observables.just
		-creates observable from an object
		Observable.just("Hello World!") 

	observables.create
		-reates an observable from scratch by means of a function. 
		Observable.create(object : Observable.OnSubscribe<Int> {
	    	override fun call(subscriber: Subscriber<in Int>) {
	        	for(i in 1 .. 5)
	            	subscriber.onNext(i)

	        		subscriber.onCompleted()
		    	}
			})

	observables.create = it is same as observables.just but observables.create have full control as to 
	what should be emmit and whrn emmit should end.


"Filtering Operators"
	Filtering operators will filter out the data stream objects based on some expression 
	and only emits data objects those satisfy the expression

	1. filter
	2. skip
		skip(2) -> skip first two elements and emit rest of data
	3. Take 
		Take(2) -> emit first 2 elemnts only and ignor rest all data
		takeLast, takeFirst

"Combining Operators"

	1. concat
		Combining operators combines more than two data streams emitted by different observable 
		and emit single data stream. 		

		Observable<Integer> observer1 = Observable.from(new Integer[]{1, 2, 3, 4, 5});  //Emit 1 to 5
		Observable<Integer> observer2 = Observable.from(new Integer[]{6, 7, 8, 9, 10}); //Emit 6 to 10

		Observable.concat(observer1, observer2)
		.subscribeOn()

	2. merge
		concat() operator merge() operator doesn’t wait for data from observable 1 to complete. 
		It emits data from both the observable simultaneously as soon as the data becomes available to emit.	

	3. Zip
		zip() operator combine the emissions of multiple Observables together via a specified function 
		and emit single items.




"subject" acts like both as an observer and as an Observable

"Publish Subject"
	It will emit data to subscriber from that movement, when it subscribed to it. Past emited data will not 
	emit again.

"Replay Subject"
	It emits all the items of the source Observable, regardless of when the subscriber subscribes.

"Behavior Subject"
	It emits the most recently emitted item and all the subsequent items of the source Observable 
	when an observer subscribes to it.	
	It will not emit from begaining just emit recent item and subsequent items

"AsyncSubject"	
	It only emits the last value of the source Observable
	only after that source Observable completes.


