package com.alvarosantisteban.contentprovidertryout;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mContactsTextView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactsTextView = (TextView)findViewById(R.id.contacts);

        context = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getContacts(View view) {
        // Get contacts
        // A "projection" defines the columns that will be returned for each row
        String[] mProjection =
                {
                        UserDictionary.Words._ID,    // Contract class constant for the _ID column name
                        UserDictionary.Words.WORD,   // Contract class constant for the word column name
                        UserDictionary.Words.LOCALE,  // Contract class constant for the locale column name
                        UserDictionary.Words.APP_ID  // Contract class constant for the app_id that inserted the word column
                };

        // Defines a string to contain the selection clause
        String mSelectionClause = null;

        // Initializes an array to contain selection arguments
        //String[] mSelectionArgs = {""};

        // Does a query against the table and returns a Cursor object
        Cursor mCursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,  // The content URI of the words table
                mProjection,                       // The columns to return for each row
                mSelectionClause,                   // Either null, or the word the user entered
                null,                    // Either empty, or the string the user entered
                UserDictionary.Words.WORD + " ASC");                       // The sort order for the returned rows

        // Some providers return null if an error occurs, others throw an exception
        if (null == mCursor) {
            Log.e(TAG, "The cursor is null!");
        // If the Cursor is empty, the provider found no matches
        } else if (mCursor.getCount() < 1) {

            Toast.makeText(context, "No words found", Toast.LENGTH_LONG).show();

        } else {
            // Change the textView
           int index = mCursor.getColumnIndex(UserDictionary.Words.WORD);

            /*
            * Moves to the next row in the cursor. Before the first movement in the cursor, the
            * "row pointer" is -1, and if you try to retrieve data at that position you will get an
            * exception.
            */
            String words = "";
            while (mCursor.moveToNext()) {

                // Gets the value from the column.
                words += mCursor.getString(index) + " - ";
            }
               mCursor.close();
            mContactsTextView.setText(words);
        }
    }
}
