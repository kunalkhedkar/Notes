Reference variable : it refers to where the actual object resides in memory 
instance variable  : A variable which belongs to perticuler instance

---------------

	free mem  												-- Heap --
---------------main																
egn---------------------------------------------------->	new Engine() <---
																			-			
car->---addr------------------------------------------->	new Car()		-
rank=10													  	hp=750			-		
main()														engine-----------
---------------
// stack


//program
class test{

	main(){

		int rank=10;
		Car mCar=new Car();					// mCar -> Reference variable
		mCar.hp=750;
		Engine egn=new Engine();			// egn -> Reference variable
		mCar.engine=eng;
	}
}


class Car{
	int hp;			//	instance variable
	Engine engine;	//	instance variable
}