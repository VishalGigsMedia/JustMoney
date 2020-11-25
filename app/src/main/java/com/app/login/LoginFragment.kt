package com.app.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.justmoney.R
import com.app.justmoney.databinding.FragmentLoginBinding
import com.poovam.pinedittextfield.PinField
import org.jetbrains.annotations.NotNull


class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val linePinField: LinePinField = findViewById(R.id.lineField)
        mBinding.squareField.onTextCompleteListener = object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                //Toast.makeText(this, enteredText, Toast.LENGTH_SHORT).show()
                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        }
    }


}