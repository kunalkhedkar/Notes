operator
Kotlin Android Extensions not caching the views - better way
Vetoable


This data class auto-generates all the fields and property accessors, as well as some
useful methods such as toString() . You also get equals() and hashCode() for free


// Will print only if artist != null
artist?.print()

// Use Elvis operator to give an alternative in case the object is null.
val name = artist?.name ?: "empty"

'Extension functions'
	An extension function is a function that adds a new behaviour to a class, even if we
	don’t have access to the source code of that class.

fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
	Toast.makeText(getActivity(), message, duration).show()
}

fragment.toast("Hello world!")



"Functional support (Lambdas)"
	view.setOnClickListener { toast("Hello world!") }

"access view"
	message.text = "Hello Kotlin!"

"Constructor"
	class Person(name: String, surname: String) {
	init {
			... // intilize values
		}
	}	
	

"Class inheritance"
Classes are closed by default (final), so we can only extend
a class if it’s explicitly declared as open or abstract.

"Functions"
	Functions in Kotlin always return a value. So if you don’t specify a return value, it
	will return Unit .Unit is similar to void in Java

	if the result can be calculated using a single expression, you can get rid of
	brackets and use equal:
		fun add(x: Int, y: Int) : Int = x + y

"function parameters"
	
	fun toast(message: String, length: Int = Toast.LENGTH_SHORT) {
		Toast.makeText(this, message, length).show()
	}
	// calling
	toast("Hello")
	toast("Hello", Toast.LENGTH_LONG)

	we can make parameter optional by specifying a default value.
	Default value prevents the need of function overloading

	// name parameters also allowed
	niceToast(message = "Hello", length = Toast.LENGTH_SHORT)

Default visibility is public

We don’t find primitive types, so we have an homogeneous way to deal with all the available types.

"type cast"
	functions are available like toDouble,toInt

"Bitwise arithmetical operations"
	// Java
	int bitwiseOr = FLAG1 | FLAG2;
	int bitwiseAnd = FLAG1 & FLAG2;
	// Kotlin
	val bitwiseOr = FLAG1 or FLAG2
	val bitwiseAnd = FLAG1 and FLAG2

A String can be accessed as an array and can be iterated
val s = "Example"
val c = s[2] // This is the Char 'a'

*Variables*
	Variables in Kotlin can be easily defined as 
		mutable   ( var ) 
		immutable ( val )

"getter & setter"
	class provide bydefault getter and setter
	But if we want to add custom logic we can do it like
	
	class Person {
		var name: String = ""
		get() = field.toUpperCase()
		set(value) {
			field = "Name: $value"
		}
	}

Backing field
	https://medium.com/@nomanr/backing-field-in-kotlin-explained-9f903f27946c

	Backing field is generated only if a property uses the default implementation of getter/setter.
	If the property needs access to its own value in a custom getter or setter, 
	it requires the creation of a backing field.


"Anko and Extension Functions"
	-	is a powerful library developed by JetBrains
	-	Its main purpose is the generation of UI layouts by using code instead of XML

	"Extension functions"

		toast("Hello world!")
		longToast(R.string.hello_world)

		"asynchrony"
			Anko provides a very easy DSL to deal with asynchrony,

"Data class"
POJO: classes which are used to keep state
	data class Forecast(val date: Date, val temperature: Float, val details: String)

	data class has additional feature methods like equals,hashCode, copy

	Copy 
		val f1 = Forecast(Date(), 27.5f, "Shiny day")
		val f2 = f1.copy(temperature = 30f)

"Destructuring Declarations"
	val (name, age) = person
	above statement compile down to
		val name = person.component1()
		val age = person.component2()
	Here, We have declared two new variables: name and age, and can use them independently


every function in Kotlin returns a value. By default,
if nothing is specified, it will return an object of the Unit class

"with"
	with(weekForecast.dailyForecast[position]) {
		holder.textView.text = "$date - $description - $high/$low"	
	}


"Visibility Modifiers"
	default modifier in this language is public
	-	private
	-	protected
	-	Internal 
			Module restrictive modifier (sub-project)
	-	public

"Constructors"
	By default Constructors are public which will allowed to access from anywhere.
	we can create Constructor private by following code

we can get rid of the return type are when we assign
the value to a function or a property using equals (=)
	fun get(position: Int) = dailyForecast[position]

"Kotlin Android Extensions"	
	app build.gradle
		apply plugin: 'kotlin-android-extensions'

	we can access view as property directly no need for findviewById
	Need to add synthetic import
	import kotlinx.android.synthetic.main.activity_main.*

"Lazy"
	The lazy operation is thread safe.
	lazy(LazyThreadSafetyMode.NONE) { ... } if you’re not worried about multi-threading

	It takes a lambda that is executed the first time getValue is called, so the initialisation
	of the property is delayed up to that moment. Subsequent calls will return the same value.	

"Vetoable"
	This is a special kind of observable that lets you decide whether the value must be
	saved or not.

"lateinit"
	-	It identifies that the property should have
		a non-nullable value, but its assignment will be delayed.

	-	If the value is requested before it is assigned, it will throw an exception

	lateinit var instance: App

"private set"
	private set will allowed only Test class to set value for var instance

	class Test{
		companion object {
		lateinit var instance: App
			private set
		}
	}


"loops"
	if (i in 0..10)   //checks exist or not 
	for (i in 0..10)	
	for (i in 10..0)
	for (i in 10 downTo 0)
	for (i in 1..4 step 2) 
	for (i in 4 downTo 1 step 2) 
	for (i in 0 until 4)
	


Everything is an expression, which means it returns a value.	

Kotlin standard functions
	https://medium.com/@elye.project/mastering-kotlin-standard-functions-run-with-let-also-and-apply-9cd334b0ef84

	Normal vs. extension function
		1. "With vs T.run"
			// Yack!
			with(webview.settings) {
			      this?.javaScriptEnabled = true
			      this?.databaseEnabled = true
			   }
			}
			// Nice.
			webview.settings?.run {
			    javaScriptEnabled = true
			    databaseEnabled = true
			}


		2. "T.run and T.let"
				both functions are similar except for one thing, the way they accept the argument.

				stringVariable?.run {
				      println("The length of this String is $length")
				}
				// Similarly.
				stringVariable?.let {
				      println("The length of this String is ${it.length}")
				}