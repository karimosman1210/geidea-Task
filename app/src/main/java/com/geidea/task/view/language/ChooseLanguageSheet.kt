package com.geidea.task.view.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.geidea.task.R
import com.geidea.task.databinding.SheetChooseLanguageBinding
import com.geidea.task.utils.PrefManager
import com.geidea.task.utils.PrefManager.Companion.ar
import com.geidea.task.utils.PrefManager.Companion.en
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseLanguageSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "ChoseLngSht"
    }

    @Inject
    lateinit var prefManager: PrefManager

    private lateinit var languageCode: String

    private var languageSelectListener: OnLanguageSelectListener? = null

    fun setLanguageSelectListener(onLanguageSelectListener: OnLanguageSelectListener) {
        languageSelectListener = onLanguageSelectListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = SheetChooseLanguageBinding.inflate(inflater, container, false)

        languageCode = prefManager.getLanguageCode()


        if (languageCode == ar) {
            Glide.with(this)
                .load(R.drawable.ic_radio_checked)
                .into(viewBinding.ivArabicCheck)

            Glide.with(this)
                .load(R.drawable.ic_radio_unchecked)
                .into(viewBinding.ivEnglishCheck)
        } else {
            Glide.with(this)
                .load(R.drawable.ic_radio_checked)
                .into(viewBinding.ivEnglishCheck)

            Glide.with(this)
                .load(R.drawable.ic_radio_unchecked)
                .into(viewBinding.ivArabicCheck)
        }


        viewBinding.ivClose.setOnClickListener {
            dismiss()
        }

        viewBinding.englishContainer.setOnClickListener {
            languageCode = en

            Glide.with(this)
                .load(R.drawable.ic_radio_checked)
                .into(viewBinding.ivEnglishCheck)

            Glide.with(this)
                .load(R.drawable.ic_radio_unchecked)
                .into(viewBinding.ivArabicCheck)
        }

        viewBinding.arabicContainer.setOnClickListener {
            languageCode = ar

            Glide.with(this)
                .load(R.drawable.ic_radio_checked)
                .into(viewBinding.ivArabicCheck)

            Glide.with(this)
                .load(R.drawable.ic_radio_unchecked)
                .into(viewBinding.ivEnglishCheck)
        }

        viewBinding.btnSave.setOnClickListener {
            if (languageCode != prefManager.getLanguageCode()) {
                languageSelectListener?.onLanguageSelect(languageCode)
            }
            dismiss()
        }


        return viewBinding.root
    }

    interface OnLanguageSelectListener {
        fun onLanguageSelect(languageCode: String)
    }
}