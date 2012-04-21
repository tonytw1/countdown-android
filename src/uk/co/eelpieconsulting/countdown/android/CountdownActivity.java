package uk.co.eelpieconsulting.countdown.android;

import uk.co.eelpieconsulting.countdown.android.daos.FavouriteStopsDAO;
import uk.co.eelpieconsulting.countdown.api.CountdownApi;
import uk.co.eelpieconsulting.countdown.exceptions.HttpFetchException;
import uk.co.eelpieconsulting.countdown.exceptions.ParsingException;
import uk.co.eelpieconsulting.countdown.model.StopBoard;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CountdownActivity extends Activity {

	private CountdownApi api;
	private FavouriteStopsDAO favouriteStopsDAO;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        api = new CountdownApi("http://countdown.tfl.gov.uk");
        favouriteStopsDAO = new FavouriteStopsDAO();
    }
	
	@Override
	protected void onResume() {
		super.onResume();

		final TextView arrivalsTextView = (TextView) findViewById(R.id.arrivals);
		try {
			StopBoard stopboard = loadArrivals();
			arrivalsTextView.setText(stopboard.getArrivals().toString());
			return;
			
		} catch (HttpFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		arrivalsTextView.setText("Failed to load arrivals");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Arrivals");
		menu.add(0, 2, 0, "Stops");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			this.startActivity(new Intent(this, CountdownActivity.class));
			return true;

		case 2:
			this.startActivity(new Intent(this, StopsActivity.class));
			return true;
		}
		return false;
	}
	
	private StopBoard loadArrivals() throws HttpFetchException, ParsingException {
		return api.getStopBoard(favouriteStopsDAO.getFavouriteStop());
	}
	
}