# Supporting Multiple Android Device Definitions



## Responsive layout 

​	How to make sure that our code respond to the particular device that you are running. 

##### Declarative layout

 - be declarative like do not tell android to draw view on (x,y) coordinate, instead of tell android directly that i want draw view at center. Android will manage to find center of application screen no matter which device is going to use.

   we can use proportion of layout using attribute weight.

##### Density-independent pixels

​	Instead of using pixel or any other unit to specify amount use `dp` .    

##### Resource folder

we can create different resource folder to tell android to pick this specific folder contain when such kind of situation. Like we can put layout-land and keep same layout name then when device turn to landscape mode android will choose layout to draw from layout-land folder likely we can create layout-large to tell android use this folder layout when device screen is large.

Resource Folders Type Variation layout values drawable menu

-  **Language & Region**: en, fr, fr-rCA 
- **Screen size**: small, large, xlarge, sw320dp, h720dp 
- **Screen orientation**: port, land Screen 
- **density**: ldpi, mdpi, hdpi, xhdpi, nodpi, tvdpi 
- **Platform version**: v4, v11, v14, v21 
- **UI mode**: car, desk, television, appliance  

##### XML drawables

For background (gradient image) we normally put png files but if device get rotated or large device image will be starch. Android gives xml drawable where we can specify shape, corner, look, gradient and android will automatically scale up or down as per device. android also provide tile option to draw repeated image pattern background.





## Progressive functionality

means you are going add functionality on device that have it, but all device will still work. 

##### Resource folder

lets take example of ripple functionality it introduce in android api version-21 (lollipop)

if we want to use it we can create drawable file with attribute ripple and put it in resource folder call drawable-21 and create another simple color changer drawable in drawable folder android automatically take ripple drawable when it is using android os version21. And below 21 device it use simple normal color changer drawable file which has same name as ripple drawable file has. 

##### version check 

In java file we can detect api level by using constant

```java
int sdk = Build.VERSION.SDK_INT
if(sdk>= Build.VERSION_CODE_ECLAIR){
    // do stuff
}

```



##### support library

Android provides components with support libraries so that is can support in lower level device as well.

like Fragment, material designing.