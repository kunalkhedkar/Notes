Dependency Injection

1. cearte module 
	class with annotation @module 

	1.1 Provider method 
		cearte method inside module class with annotation @Provider


2. Component
	create interface with anotation @Component with parameter of list of module related to this component


3. setup dagger in application class
	


4 Multiple dependency
		App => SharedprefHelper => 	Sharedpreference	=> context

		'AppComponent'
			1. Create 2 methods 
					- sharedpref
					- context	

		AppModule
			1.	provide 2 things
					- sharedpref
					- context				

		SharedPrefModule
			provide sharedPrefhelper which accepts 1 parameter SharedPref Object
					returns sharedPrefHelper object

		'SharedprefComponent'
			As this component dependent on AppComponent for Sharedpreference object and context object
				we need add AppComponent as dependencies in @Component tag
			next
			And normal inject method

		'Application'
			while creating SharedprefComponent as normal we add SharedPrefModule and as this component is dependent on AppModule
				we need to add appModule too this component.	


5. SubComponent
	
	we use anotation @SubComponent

	we add this component in parentComponent like normal method

	as like dpendencies we dont need to add this SubComponent to parentComponent while Instantiation.

