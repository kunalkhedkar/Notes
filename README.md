# Notes

## Add External library/project to android studio

step 1) Go to file->new->Import Module -> <choose library or project folder>
step 2) add library to include section in settings.gradle file 
			(you see library folder is shown now)
step 3) Go to File->Project Structure->app->dependency tab-> click on plus button
	step 3.1)	
			select module dependency -> select library (your library name should appear there)
			and put scope (compile or implimentation)
