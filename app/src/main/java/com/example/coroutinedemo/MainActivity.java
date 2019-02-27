package com.example.coroutinedemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Fragment fragName;
    private Fragment fragAge;
    private String name;
    private String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragName = new FragName();
        fragAge = new FragAge();
        switchToFragment(1);
    }

    public void switchToFragment(int s) {
        switch(s) {
            case 1:
                switchToFragment(fragName, "Get Name Fragment.");
                break;
            case 2:
                switchToFragment(fragAge, "Get Age Fragment.");
                break;
            default:
                getSupportFragmentManager().popBackStack();
                Toast.makeText(this, "Name: " + name + ", Age: " + age, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void switchToFragment(Fragment fragment, String fragName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root, fragment, fragName)
                .commit();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public static class FragName extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment, container, false);
            TextView label = view.findViewById(R.id.label);
            label.setText("Your name?");
            Button button = view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "FragName Button clicked", Toast.LENGTH_LONG).show();
                    EditText editText = view.findViewById(R.id.content);
                    getMainActivity().setName(editText.getText().toString());
                    getMainActivity().switchToFragment(2);
                }

            });
            return view;
        }

        private MainActivity getMainActivity() {
            return (MainActivity)getActivity();
        }
    }

    public static class FragAge extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment, container, false);
            TextView label = view.findViewById(R.id.label);
            label.setText("Your age?");
            Button button = view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "FragAge Button clicked", Toast.LENGTH_LONG).show();
                    EditText editText = view.findViewById(R.id.content);
                    getMainActivity().setAge(editText.getText().toString());
                    getMainActivity().switchToFragment(3);
                }
            });
            return view;
        }

        private MainActivity getMainActivity() {
            return (MainActivity)getActivity();
        }
    }

}
