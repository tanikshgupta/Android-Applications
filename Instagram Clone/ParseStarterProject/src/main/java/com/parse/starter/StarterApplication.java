/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Application;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    //OpHuJ9awfp6y



    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("c7c077a387698f191146634d496bbe429c480996")
            .clientKey("bd0ca0191da7416216a12625fb7b41db67e15d8c")
            .server("http://13.58.107.185:80/parse/")
            .build()
    );


    /*ParseObject object = new ParseObject("ExampleObject");
    object.put("myNumber", "123");
    object.put("myString", "taniksh");

    object.saveInBackground(new SaveCallback () {
      @Override
      public void done(ParseException ex) {
        if (ex == null) {
          Log.i("Parse Result", "Successful!");
        } else {
          Log.i("Parse Result", "Failed" + ex.toString());
        }
      }
    });*/


    //ParseUser.enableAutomaticUser();

    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
