package com.veriqual.gofast.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.veriqual.gofast.R;
import com.veriqual.gofast.model.Tagging;

public class TagDialog extends DialogFragment implements OnCheckedChangeListener {
	
	String tag;
	Context context;
	View input;
	RadioGroup group;
	RadioButton radio1;
	EditText edittag;
	
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
        context = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the TagDialogListener so we can send events to the host
            mListener = (TagDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TagDialogListener");
        }
    }
    
    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    input = inflater.inflate(R.layout.tag_input, null);
	    group = (RadioGroup) input.findViewById(R.id.group_tag);
	    if(getArguments().getString("hideinput") != null) {
	    	group.setVisibility(View.GONE);
	    	input.setVisibility(View.GONE);
	    } else {
	    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	               }
	           });
	    }
	    radio1 = (RadioButton) input.findViewById(R.id.radio1);
	    ((RadioButton) input.findViewById(R.id.radio2)).setChecked(true);
	    edittag = (EditText)input.findViewById(R.id.tag);
	    
	    group.setOnCheckedChangeListener(this);
	    // Set the dialog title
//	    String[] list = {"Start Tag", "First Tag", "list", "android", "item 3", "foobar", "bar", };
//	    ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list);
	    builder.setMessage(getArguments().getString("msg"))
	    		.setTitle("Tag")
	    		.setView(input)
//	    		.setIcon(R.drawable.)
//	    		.setAdapter(adapter, new DialogInterface.OnClickListener() {
//	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
//	               }
//	    		 })
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
	            	   mListener.onDialogPositiveClick(TagDialog.this);
	               }
	           });

		if (getArguments().getString("hideinput") != null) {
			group.setVisibility(View.GONE);
			input.setVisibility(View.GONE);
		} else {
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}
					});
		}

	    return builder.create();
	}
	
	public String getTg() {
		String tag = null;
		if(group.getVisibility() != View.GONE) {
			if (radio1.isChecked()) {
				tag = Tagging.FINISHTAG;
			} else {
				EditText edittag = (EditText)input.findViewById(R.id.tag);
				tag = edittag.getText().toString();
			}
		}
		return tag;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radio1:
			toggle(false);
			break;
		case R.id.radio2:
			toggle(true);
		default:
			break;
		}
	}
	
	private void toggle (boolean toggle) {
		edittag.setEnabled(toggle);
	}
	
	private void hideGroup() {
		group.setVisibility(View.GONE);
	}
}
