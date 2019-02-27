package com.example.coroutinedemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException
import kotlin.coroutines.*

fun<T> startCoroutine(c: Continuation<T>, block: suspend () -> T) {
    block.startCoroutine(c)
}

class MainActivity : AppCompatActivity() {

    private var fragName: Fragment? = null
    private var fragAge: Fragment? = null
    private var fragResult: FragOutput? = null
    private var name: String? = null
    private var age: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragName = FragName().setLabel("Your name?")
        fragAge = FragAge().setLabel("Your age?")
        fragResult = FragOutput()

        val complete = object: Continuation<Unit> {
            override val context: CoroutineContext
                get() = Dispatchers.Main

            override fun resumeWith(result: Result<Unit>) {
                if (result.isFailure) {
                    result.exceptionOrNull()?.apply { throw this }
                            ?: throw RuntimeException("Suspend function failure.")
                }
            }
        }

        startCoroutine(complete) {
            coRunner()
        }
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
            mainActivity.doneGetName(content)
        }
    }

    class FragAge : FragInput() {

        private val mainActivity: MainActivity
            get() = activity!! as MainActivity

        override fun onButtonClicked(content: String) {
            mainActivity.doneGetAge(content)
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

    var contGetName: Continuation<String>? = null
    fun doneGetName(name: String) {
        contGetName!!.apply {
            contGetName = null
            resume(name)
        }
    }

    var contGetAge: Continuation<String>? = null
    fun doneGetAge(age: String) {
        contGetAge!!.apply {
            contGetAge = null
            resume(age)
        }
    }

    private suspend fun coRunner() {
        switchToFragment(fragName, "Get Name Fragment.")
        name = suspendCoroutine { cont -> contGetName = cont }
        switchToFragment(fragAge, "Get Age Fragment.")
        age = suspendCoroutine { cont -> contGetAge = cont }
        fragResult!!.setLabel("Name: $name, Age: $age")
        switchToFragment(fragResult, "Show result")
    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}
