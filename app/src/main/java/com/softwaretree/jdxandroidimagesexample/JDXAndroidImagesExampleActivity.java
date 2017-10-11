/**
 * Copyright (c) 2011-2017 Software Tree, LLC.
 */
package com.softwaretree.jdxandroidimagesexample;

import java.util.ArrayList;
import java.util.List;

import com.softwaretree.jdx.JDXS;
import com.softwaretree.jdx.JDXSetup;
import com.softwaretree.jdxandroid.DatabaseAndJDX_Initializer;
import com.softwaretree.jdxandroid.Utils;
import com.softwaretree.jdxandroidimagesexample.model.Person;
import com.softwaretree.jx.JXResource;
import com.softwaretree.jx.JXSession;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This project exemplifies how JDXA ORM and associated utilities can be used to easily develop 
 * an Android application that exchanges data of domain model objects with an SQLite database.
 * In particular, this project demonstrates the following:
 * <p>
 * 1) How an ORM Specification (mapping) for domain model classes can be defined textually using
 * simple statements.  The mapping is specified in a text file \res\raw\images_example.jdx identified 
 * by the resource id R.raw.images_example.
 * <p>
 * 2) The mapping file shows that nothing special needs to be specified for a byte array attribute
 * of the Person class holding the image/picture data.  However, since a picture or image may be absent for
 * a person, it has the following declaration:
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp;  <code>SQLMAP FOR picture NULLABLE</code>
 * <p>
 * 3) Use of {@link AppSpecificJDXSetup} and {@link DatabaseAndJDX_Initializer} classes to easily: 
 *   <br>&nbsp;&nbsp;&nbsp;&nbsp;  a) create the underlying database, if not already present.
 *   <br>&nbsp;&nbsp;&nbsp;&nbsp;  b) create the schema (tables and constraints) corresponding to the JDXA ORM specification 
 *      the very first time an application runs; the subsequent runs will reuse the 
 *      existing database schema and data. 
 * <p>
 * 4) Examples of how just a few lines of object-oriented code incorporating JDX APIs 
 * can be used to easily interact with relational data.  This avoids tedious and 
 * time-consuming coding/maintenance of low-level SQL statements.  See how the 
 * image (picture) data with a resource id can easily be converted into a byte 
 * array and stored in the database through an instance of a Person object.
 * <p>
 * 5) Although, in the code below, the images of the Person objects are obtained from the 
 * res\drawable folder through resource ids, there could be other sources of the pictures 
 * also (e.g. URLs or file names). If the image data is persistently and reliably 
 * available from non-database sources (e.g. resource ids, file names, or URLs),
 * one may just save the source location in the database instead of the actual image
 * data.  The code here is intended to illustrate how some binary (image) data, as part 
 * of an object, can easily be stored and retrieved using an RDBMS and JDXA ORM.   
 * <p>  
 * @author Damodar Periwal
 */
public class JDXAndroidImagesExampleActivity extends Activity {

    JDXSetup jdxSetup = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setTitle(getResources().getString(R.string.activity_title));

        List<Person> persons = null;
        try {        
            AppSpecificJDXSetup.initialize();  // must be done before calling getInstance()
            jdxSetup = AppSpecificJDXSetup.getInstance(this);
            persons = populateAndGetPersons(this.getResources(), jdxSetup);
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Exception: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            cleanup();
            return;
        }
        
        TextView tvName = (TextView) findViewById(R.id.tvName);
        ImageView ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        
        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new NextPersonButtonListener(this.getResources(), tvName, ivPhoto, persons));  
    }

    /**
     * Do the necessary cleanup.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }
    
    private void cleanup() {
        AppSpecificJDXSetup.cleanup(); // Do this when the application is exiting.
        jdxSetup = null;
    }
    
    /**
     * A class to handle showing of Person data in response to "Next" button click.
     */
    private class NextPersonButtonListener implements Button.OnClickListener {          
        Resources resources;
        TextView personNameView;
        ImageView personImageView;
        List<Person> persons;
        int currIndex = 0;
        int totalPersons = 0;
        
        public NextPersonButtonListener(Resources resources, TextView personNameView, ImageView personImageView, List<Person> persons) {
            this.resources = resources;
            this.personNameView = personNameView;
            this.personImageView = personImageView;
            this.persons = persons;
            // Show the first image to get started
            if (null != persons && persons.size() > 0) {
                currIndex = 0;
                totalPersons = persons.size();
                showPerson(persons.get(currIndex));
            }
        }
        
        /**
         * Shows the next person.
         */
        @Override
        public void onClick(View v) {
            if (totalPersons <= 1) {
                return;
            }
            currIndex = (currIndex + 1) % totalPersons;
            showPerson(persons.get(currIndex));          
        }
        
        private void showPerson(Person person) {
            if (null == person) {
                return;
            }
            personNameView.setText(person.getName());
            
            byte[] personImage = person.getPicture();
            if (null != personImage) {
                personImageView.setImageDrawable(Utils.getBitmapDrawbleFromByteArray(resources, personImage));
            }
        }
    }

    /**
     * Populates some Person objects in the database and then retrieves them using JDX ORM APIs.
     * 
     * @param jdxSetup
     * @throws Exception
     */
    private List<Person> populateAndGetPersons(Resources resources, JDXSetup jdxSetup) throws Exception {

        if (null == jdxSetup) {
            return null;
        }
        
        List <Person> persons = null;

        // Obtain ORM handles.
        JXResource jxResource = jdxSetup.checkoutJXResource();
        JXSession jxSessionHandle = jxResource.getJXSessionHandle(); // May be used for creating transaction scopes
        JDXS jdxHandle = jxResource.getJDXHandle();

        String personClassName = Person.class.getName();

        try {
            // First delete all existing persons from the database.
            System.out.println("Deleting all persons");
            jdxHandle.delete2(personClassName, null, 0);
                                        
            // In the code below, the pictures of the Person objects are obtained from the res\drawable folder.
            // There could be other sources of the picture also - the code is more to illustrate how binary (image)
            // data, as part of an object, can easily be stored and retrieved using an RDBMS and JDX ORM.  
            
            // Now create and insert new Persons         
            List<Person> newPersons = new ArrayList<Person> ();
            newPersons.add(new Person("Donal Trump", Utils.getByteArrayFromBitmapResource(resources, R.drawable.trump)));
            newPersons.add(new Person("Barack Obama", Utils.getByteArrayFromBitmapResource(resources, R.drawable.obama)));
            newPersons.add(new Person("George Bush", Utils.getByteArrayFromBitmapResource(resources, R.drawable.bush)));
            newPersons.add(new Person("Bill Clinton", Utils.getByteArrayFromBitmapResource(resources, R.drawable.clinton)));
            newPersons.add(new Person("George Bush Sr", Utils.getByteArrayFromBitmapResource(resources, R.drawable.bush_senior)));
            newPersons.add(new Person("Ronald Regan", Utils.getByteArrayFromBitmapResource(resources, R.drawable.regan)));
            newPersons.add(new Person("Jimmy Carter", Utils.getByteArrayFromBitmapResource(resources, R.drawable.carter)));         
            jdxHandle.insert(newPersons, 0, null);
            
            // Retrieve all persons from the database
            System.out.println("Querying persons");
            persons = jdxHandle.query(personClassName, null, JDXS.ALL, JDXS.FLAG_DEEP, null);
            // JXUtilities.printQueryResults(persons);                  
        } catch (Exception ex) {
            System.out.println("JDX Error " + ex.getMessage());
            Log.e("JDX", "Error",  ex);
            throw ex;
        } finally {
            jdxSetup.checkinJXResource(jxResource);
        }

        return persons;
    }
}



