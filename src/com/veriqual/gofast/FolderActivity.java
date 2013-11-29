package com.veriqual.gofast;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.veriqual.gofast.interfaces.IFolderItemListener;
import com.veriqual.gofast.utilites.FolderLayout;

public class FolderActivity extends Activity implements IFolderItemListener {

    FolderLayout localFolders;
    String url;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        localFolders = (FolderLayout)findViewById(R.id.localfolders);
        localFolders.setIFolderItemListener(this);
        localFolders.setDir(Environment.getExternalStorageDirectory().getAbsolutePath());

    }

    //Your stuff here for Cannot open Folder
    public void OnCannotFileRead(File file) {
        // TODO Auto-generated method stub
//        new AlertDialog.Builder(this)
//        .setTitle(
//                "[" + file.getName()
//                        + "] folder can't be read!")
//        .setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog,
//                            int which) {
//
//
//                    }
//                }).show();
        
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);        
        finish();

    }


    //Your stuff here for file Click
    public void OnFileClicked(File file) {
        // TODO Auto-generated method stub
//        new AlertDialog.Builder(this)
//        .setTitle("[" + file.getName() + "]")
//        .setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                            int which) {
//
//
//                    }
//
//                }).show();
        
        Intent returnIntent = new Intent();
        returnIntent.putExtra("path",file.getAbsolutePath());
        setResult(RESULT_OK,returnIntent);     
        finish();
    }
    
    public String getUrl() {
    	return url;
    }

}
