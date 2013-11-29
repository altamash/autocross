package com.veriqual.gofast;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.veriqual.gofast.model.Comparison;
import com.veriqual.gofast.utilites.ListviewAdapter;
import com.veriqual.gofast.utilites.Utilities;

/**
 * A fragment representing a single Comparison detail screen. This fragment is
 * either contained in a {@link ComparisonListActivity} in two-pane mode (on
 * tablets) or a {@link ComparisonDetailActivity} on handsets.
 */
public class ComparisonDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Comparison comparison;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ComparisonDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			comparison = Utilities
					.getComparisonsList(
							ComparisonDetailFragment.this.getActivity())
					.getComparisons().get(getArguments().getInt(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comparison_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (comparison != null) {
//			((TextView) rootView.findViewById(R.id.comparison_detail))
//					.setText(comparison.getName());
			
			ListView lview = (ListView) rootView.findViewById(R.id.complistview);
			ListviewAdapter adapter = new ListviewAdapter(
					ComparisonDetailFragment.this.getActivity(),
					Utilities.populateList(comparison.getFirstVideo(),
							comparison.getSecondVideo()));
	        lview.setAdapter(adapter);
		}

		return rootView;
	}
}
