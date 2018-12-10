Android Jetpack

	Foundation
			AppCompact
				The appcompat library has migrated into the AndroidX library.
				library depends on the v4 Support Library.

				palette library
					com.android.support:palette-v7:28.0.0
					we can extract color from image can be use for dynamic toolbar or set imageName
						get image in bimap from

						palette = Palette.from(oreoBitMapImage).generate();
						vibrantSwatch = palette.getDarkMutedSwatch();
						int color=vibrantSwatch.getRgb()

						https://developer.android.com/training/material/palette-colors

			Android KTX
				Android KTX is a set of Kotlin extensions
			Multidex	
				minSdkVersion is set to 21 or higher
					you just need to add multiDexEnabled true inside defaultConfig in build.gradle
				minSdkVersion is set to 20 or lower
					you need to add multiDexEnabled true inside defaultConfig in build.gradle
					AND need to add dependencies 
						compile 'com.android.support:multidex:1.0.3'	
					
					MultiDex.install(this);
					extend application with MultiDexApplication
			Test

	Architecture	
		Data Binding
			allows you to bind UI components in your layouts to data sources
		Life Cycle
			We dont need to manage state ourself manually as per activitys state. (Eg. register loctaion listener on onstart and unrisger on onstop)
			we can create lifecycleaware component to work with activity/fragment state
				we jsut need to implement our component with LifecycleObserver
					It will provide annotation to manage our component with respect to activity/fragment
					we can write method and annotate it with following options
						Eg. @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
							ON_CREATE
							ON_RESUME
							ON_DISTROY
							ON_START
				finally we need add this component to activity/fragment
					getLifecycle().addObserver(new TestLifecycle());

		LiveData
			LiveData is an observable data holder class
				You can register an observer without an associated LifecycleOwner object 
					using the observeForever(Observer) method. 
					In this case, the observer is considered to be always active and is therefore always notified about modifications. 
					You can remove these observers calling the removeObserver(Observer) method.	
				Custom LiveData by extending our class with LiveData (onActive,onInactive, setValue)
				Mergh liveData - MediatorLiveData  (addsource)

		Navigation
			vedio
		Paging

				return data from database  DataSource.Factory<Integer, Concert>
					ViewModel
						LiveData<PagedList<Concert>> livedata =
									new LivePagedListBuilder<>(
            							mConcertDao.concertsByDate(), /* page size */ 20).build();

				


		Room
			abstraction over sqlite
		ViewModel
			lifecycleaware component we can use to keep our data in ViewModel to servive config changes
		Workmanager
			customize task scheduler

	Behavior
	UI