package com.veriqual.gofast.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class TagDialog extends DialogFragment {
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface TagDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    TagDialogListener mListener;
    
 // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TagDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TagDialogListener");
        }
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setMessage(getArguments().getString("msg"))
	    		.setTitle("Tag")
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
//	           .setMultiChoiceItems(R.array.toppings, null,
//	                      new DialogInterface.OnMultiChoiceClickListener() {
//	               @Override
//	               public void onClick(DialogInterface dialog, int which,
//	                       boolean isChecked) {
//	                   if (isChecked) {
//	                   }
//	                   else if (mSelectedItems.contains(which)) {
//	                   }
//	               }
//	           })
	    // Set the action buttons
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	               }
	           });

	    return builder.create();
	}
}
