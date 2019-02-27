package com.example.coroutinedemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var fragName: Fragment? = null
    private var fragAge: Fragment? = null
    private var fragResult: FragOutput? = null
    private var name: String? = null
    private var age: String? = null
    private val runner = StageRunner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragName = FragName().setLabel("Your name?")
        fragAge = FragAge().setLabel("Your age?")
        fragResult = FragOutput()
        runner.resume(0)
    }

    fun switchToFragment(fragment: Fragment?, fragName: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.root, fragment!!, fragName)
                .commit()
    }

    abstract class FragInput : Fragment() {
        private var labelTextView: TextView? = null
        val label: String
            get() {
                val bundle = arguments ?: return "(empty)"
                val s = bundle.getString(KEY_LABEL)
                return s ?: "(empty)"
            }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.frag_input, container, false)
            labelTextView = view.findViewById(R.id.label)
            labelTextView!!.text = label
            val button = view.findViewById<Button>(R.id.button)
            button.setOnClickListener {
                val editText = view.findViewById<EditText>(R.id.content)
                onButtonClicked(editText.text.toString())
            }
            return view
        }

        abstract fun onButtonClicked(content: String)

        fun setLabel(label: String): FragInput {
            var b = arguments
            if (null == b) {
                b = Bundle()
            }
            b.putString(KEY_LABEL, label)
            arguments = b
            if (null != labelTextView) labelTextView!!.text = label
            return this
        }

        companion object {

            var KEY_LABEL = "The text to be shown in the label."
        }
    }

    class FragName : FragInput() {

        private val mainActivity: MainActivity
            get() = activity!! as MainActivity

        override fun onButtonClicked(content: String) {
            mainActivity.runner.resume(content)
        }
    }

    class FragAge : FragInput() {

        private val mainActivity: MainActivity
            get() = activity!! as MainActivity

        override fun onButtonClicked(content: String) {
            mainActivity.runner.resume(content)
        }
    }

    class FragOutput : Fragment() {
        private var textView: TextView? = null

        private val label: String
            get() {
                val b = arguments ?: return "(empty)"
                val s = b.getString(KEY_LABEL)
                return s ?: "(empty)"
            }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.frag_output, container, false)
            textView = view.findViewById(R.id.content)
            textView!!.text = label
            return view
        }

        fun setLabel(label: String): FragOutput {
            var b = arguments
            if (null == b) {
                b = Bundle()
            }
            b.putString(KEY_LABEL, label)
            arguments = b
            if (null != textView) textView!!.text = label
            return this
        }

        companion object {
            val KEY_LABEL = "The label of the output fragment"
        }
    }

    internal enum class Stage {
        STAGE_1, STAGE_2, STAGE_3
    }

    internal inner class StageRunner {
        var stage = Stage.STAGE_1
        fun <T> resume(v: T) {
            when (stage) {
                MainActivity.Stage.STAGE_1 -> {
                    switchToFragment(fragName, "Get Name Fragment.")
                    stage = Stage.STAGE_2
                }
                MainActivity.Stage.STAGE_2 -> {
                    name = (v as String)
                    switchToFragment(fragAge, "Get Age Fragment.")
                    stage = Stage.STAGE_3
                }
                else -> {
                    age = (v as String)
                    fragResult!!.setLabel("Name: $name, Age: $age")
                    switchToFragment(fragResult, "Show result")
                }
            }
        }
    }
}
