package safecommute.main;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlphaMainMenu extends ListFragment {
	boolean mDualPane;
	int mCurCheckPosition = 0;
	
    public static final String[] TITLES = 
    {
            "Aaron is Beautiful",   
            "Mark is BIG",
            "Jordan takes 140",       
            "Kru is done",
            "Image Capture",
            "Bluetooth Bullshit"
    };

	// onActivityCreated() is called when the activity's onCreate() method
	// has returned.

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// You can use getActivity(), which returns the activity associated
		// with a fragment.
		// The activity is a context (since Activity extends Context) .

		// Populate list with our static array of titles in list TITLES
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				TITLES));

		// Check that a view exists and is visible
		// A view is visible (0) on the screen; the default value.
		// It can also be invisible and hidden, as if the view had not been
		// added.
		//


		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

		if (mDualPane) {
			// In dual-pane mode, the list view highlights the selected
			// item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			// Make sure our UI is in the correct state.
			showDetails(mCurCheckPosition);
		} else {
			// We also highlight in uni-pane just for fun
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			getListView().setItemChecked(mCurCheckPosition, true);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		showDetails(position);
	}

	// Helper function to show the details of a selected item, either by
	// displaying a fragment in-place in the current UI, or starting a whole
	// new activity in which it is displayed.

	void showDetails(int index) {
		mCurCheckPosition = index;
		
			// We need to launch a new activity to display
			// the dialog fragment with selected text.

			// Create an intent for starting the DetailsActivity
			Intent intent = new Intent();

			// explicitly set the activity context and class
			// associated with the intent (context, class)
			intent.setClass(getActivity(), MainActivity.DetailsActivity.class);

			// pass the current position
			intent.putExtra("index", index);
			intent.putExtra("title", TITLES[index]);

			startActivity(intent);
		
	}
}