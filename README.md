# JDXAndroidImagesExample
### A Simple Android Project Demonstrating Persistence of Binary (e.g., image) Data Using JDXA ORM

Storing some binary (e.g., image or encoded bytes) data in a separate attribute along with 'normal' data in other attributes is an important object modeling requirement for many applications. This project exemplifies how some binary data, as part of an object, can easily be stored and retrieved using SQLite database and JDXA ORM. 

Some highlights:  
*	The object model consists of a Person class that contains a byte[] attribute (named picture) to hold binary (image) data. Some US Presidents are instances of the Person class in this project.
*	The binary data is stored in a 'blob' column of a database table. The other 'normal' data is stored in separate columns.
*	The declarative mapping specification (in the file .../res/raw/images_example.jdx) is simple, intuitive, non-intrusive, and succinct.  
*	API calls for CRUD operations are simple and flexible.
*	Although, in the example code, the images of the Person objects are obtained from the res\drawable folder through resource ids, there could be other sources of the pictures also (e.g. URLs or file names). 
*	If the image data is persistently and reliably available from non-database sources (e.g. resource ids, file names, or URLs), one may just save the source location in the database instead of the actual image data. This project is intended to illustrate how JDXA ORM can be used to easily save binary data in a database.

To run this app in your own setup, please do the following:
*	Clone this project on your desktop.
*	Get the JDXA SDK download instructions from [this link](http://softwaretree.com/v1/products/jdxa/download-jdxa.php).
*	You may download just the mini version of the SDK.
*	Add the libraries (JDXAndroid-nn.n.jar and sqldroid.jar) from the SDK to the app/libs directory and build the project.
*	Run the app.  

### About JDXA ORM 
JDXA is a simple yet powerful, non-intrusive, flexible, and lightweight Object-Relational Mapping (ORM) product that simplifies and accelerates the development of Android apps by providing intuitive, object-oriented access to on-device relational (e.g., SQLite) data.  

Adhering to some well thought-out [KISS (Keep It Simple and Straightforward) principles](http://softwaretree.com/v1/KISSPrinciples.html), JDXA boosts developer productivity and reduces maintenance hassles by eliminating endless lines of tedious SQL code.  

Some of the powerful and practical features of JDXA include: 
*	Declarative mapping specification between an object model and a relational model is done textually using a simple grammar (no XML complexity). 
*	Full flexibility in domain object modeling – one-to-one, one-to-many, and many-to-many relationships as well as class-hierarchies supported.
*	POJO (Plain Old Java Objects) friendly non-intrusive programming model, which does not require you to change your Java classes in any way:   

    - No need to subclass your domain model classes from any base class
    - No need to clutter your source code with annotations
    - No source code generation (No need for DAO classes)
    - No pre-processing or post-processing of your code  

*	Support for persistence of JSON objects.
*	A small set of intuitive APIs for object persistence.
*	Automatic generation of relational schema from an object model. 
*	A highly optimized metadata-driven ORM engine that is lightweight, dynamic, and flexible.   

JDXA ORM is a product of Software Tree. To get more information and a free trial version of JDXA SDK, please visit http://www.softwaretree.com.  

JDXA is used with the SQLDroid open source library. SQLDroid is provided under the licensing terms mentioned [here](https://github.com/SQLDroid/SQLDroid/blob/master/LICENSE).



