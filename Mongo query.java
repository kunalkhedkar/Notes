

1.	use databse 		'use <database name>' 		
						if database not exist, it will be created

2.	show database		'show dbs'
						database will not list down until it has atleast one collection

3. show collections		'show collections'

4.	create collection	'createCollection("<collection_name>")'			

5.	drop database       'db.dropDatabase()'
						select database to use and fire above cmd. Currently using database will be drop

---------------------------------------------------------------------------------------------------------------------------
6.	insert				'db.<collection_name>.inser({object} or [array of object])'
						Eg.  db.stud.insert({name:"kunal",class:"fybcs",roll:123});

						
		->multiple document		db.stud.insert([
								...{name:"ketan",	class:"mech",	roll:324},
								...{name:"swap",	class:"ele",	roll:001},
								...{name:"abhi",	class:"ssc",	roll:007}	]);
---------------------------------------------------------------------------------------------------------------------------
7. Select (find)		'db.<collection_name>.find({<filter>})'
						Eg.	db.stud.find({});
						o/p: { "_id" : ObjectId("5b0e2d095117efa82063d866"), "name" : "kunal", "class" : "fybcs", "roll" : 123 }

		->pretty				'pretty()'
								Eg. db.stud.find({}).pretty();
								beautify the output result

		-> find specific		select-> '{<field_name>:1 or 0 ,f2,f3}'
				['select']		Eg. db.stud.find({},{name:1});
								add select in find function field_name:1|0 	1: to include 	0: to  Exclude
								here only name field will be show.

				['multiple select']      Eg. db.stud.find({},{name:1,class:1});		
---------------------------------------------------------------------------------------------------------------------------
8.	count 				'count()'
						Eg. db.stud.find({}).count();				
---------------------------------------------------------------------------------------------------------------------------

9. filter ('where')			'db.<collection_name>.find({<filter>})'
							Eg.	db.stud.find({});

							1. where name is kunal
								db.stud.find({name:"kunal"});

							2. where id
								db.stud.find({_id:ObjectId("5b0e2f0b5117efa82063d86a")});

	
			['less than'->lt]	where roll is less than 10 	  ->-> db.stud.find({roll:{$lt:10}});
			['less than'->gt]	where roll is gretter than 21 ->-> db.stud.find({roll:{$gt:21}});
								note:[lt-> less than or equal]


			['pattern']			where name contains letter 'k'
			('like')				Eg.	db.stud.find({name:/k/});
								
					('start')	where name start with letter 's'		// s%
									Eg. db.stud.find({name:/^s/});

								where name ends with letter 'al'		// %al
									Eg.	db.users.find({name: /ro$/}) 


			[AND]				where name contains letter 'a' AND roll number is greater than 100			
									Eg. db.stud.find({name: /a/,roll:{$gt:100}});


			[OR]				where name ends with 'l' or 'n'	
								db.getCollection('stud').find({$or:[ {name:/l$/},{name:/n$/} ] } );					


			[Sort]				sort by name ascending order	[1:asc | -1:desc]
									Eg.  db.stud.find({}).sort({name:1});

---------------------------------------------------------------------------------------------------------------------------
10.	Update 				'update({<filter>},{$set:{<field_name>:<new_value>}})'
						update name set:'arambh' where id=xxx
							Eg.	db.stud.update({_id:ObjectId("5b0e2dfc5117efa82063d867")},{$set:{name:"Arambh"}});

		['multiple update']		'{multi:true}'	we need to add this property to update
								update set class to MCA where roll is gretter than 100
								Eg.		db.stud.update({roll:{$gt:100}},{$set:{class:"MCA"}},{multi:true});

---------------------------------------------------------------------------------------------------------------------------

11.	Delete	('remove')			'remove({<filter>})'
								remove entry where id=x
								Eg.		db.stud.remove({_id:ObjectId("5b0e2e035117efa82063d868")});


---------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------

	Graphical tool is also avalible call robomongo
	- https://robomongo.org/download