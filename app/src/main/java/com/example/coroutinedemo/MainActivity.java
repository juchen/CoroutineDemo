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

public class MainActivity extends AppCompatActivity {

    private Fragment fragName;
    private Fragment fragAge;
    private FragOutput fragResult;
    private String name;
    private String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragName = new FragName().setLabel("Your name?");
        fragAge = new FragAge().setLabel("Your age?");
        fragResult = new FragOutput();
        switchToFragment(fragName, "Get Name Fragment.");
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

    abstract public static class FragInput extends Fragment {
        private TextView labelTextView;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.frag_input, container, false);
            labelTextView = view.findViewById(R.id.label);
            labelTextView.setText(getLabel());
            Button button = view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = view.findViewById(R.id.content);
                    onButtonClicked(editText.getText().toString());
                }

            });
            return view;
        }

        abstract public void onButtonClicked(String content);

        public static String KEY_LABEL = "The text to be shown in the label.";
        public String getLabel() {
            Bundle bundle = getArguments();
            if (null == bundle) return "(empty)";
            String s = bundle.getString(KEY_LABEL);
            if (s == null) {
                return "(empty)";
            } else {
                return s;
            }
        }

        public FragInput setLabel(String label) {
            Bundle b = getArguments();
            if (null == b) {
                b = new Bundle();
            }
            b.putString(KEY_LABEL, label);
            setArguments(b);
            if(null != labelTextView) labelTextView.setText(label);
            return this;
        }
    }

    public static class FragName extends FragInput {
        @Override
        public void onButtonClicked(String content) {
            getMainActivity().setName(content);
            getMainActivity().switchToFragment(getMainActivity().fragAge, "Get Age Fragment.");
        }

        private MainActivity getMainActivity() {
            return (MainActivity)getActivity();
        }
    }

    public static class FragAge extends FragInput {
        @Override
        public void onButtonClicked(String content) {
            getMainActivity().setAge(content);
            getMainActivity().fragResult.setLabel("Name: " + getMainActivity().name + ", Age: " + getMainActivity().age);
            getMainActivity().switchToFragment(getMainActivity().fragResult, "Show result");
        }

        private MainActivity getMainActivity() {
            return (MainActivity)getActivity();
        }
    }

    public static class FragOutput extends Fragment {
        final static public String KEY_LABEL = "The label of the output fragment";
        private TextView textView;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_output, container, false);
            textView = view.findViewById(R.id.content);
            textView.setText(getLabel());
            return view;
        }

        private String getLabel() {
            Bundle b = getArguments();
            if (null == b) return "(empty)";
            String s = b.getString(KEY_LABEL);
            if (null == s) {
                return "(empty)";
            } else {
                return s;
            }
        }

        public FragOutput setLabel(String label) {
            Bundle b = getArguments();
            if (null == b) {
                b = new Bundle();
            }
            b.putString(KEY_LABEL, label);
            setArguments(b);
            if(null != textView) textView.setText(label);
            return this;
        }
    }
}
