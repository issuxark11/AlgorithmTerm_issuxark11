package example.Android.issuxark11.simplelockscreen;

import android.app.Fragment;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;


public class FragmentFour extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        CalendarView calendar = (CalendarView)view.findViewById(R.id.calendarView);

        return view;
    }
}
