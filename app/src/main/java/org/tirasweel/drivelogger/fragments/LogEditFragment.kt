package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import org.tirasweel.drivelogger.R
import org.tirasweel.drivelogger.databinding.FragmentLogEditBinding

class LogEditFragment : Fragment() {

    private var binding: FragmentLogEditBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLogEditBinding.inflate(inflater, container, false)

        binding?.toolbar?.apply {
            inflateMenu(R.menu.menu_items)

            val arrowBackIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)
            navigationIcon = arrowBackIcon

            setNavigationOnClickListener {
                back()
            }

            menu.findItem(R.id.menu_register_log).apply {
                isVisible = true
                isEnabled = true
            }

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.menu_register_log -> {
                        Toast.makeText(this.context, "TEST", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        throw IllegalStateException("$item is unexpected here")
                    }
                }

                true
            }
        }


        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * 戻るを押されたとき. @todo 編集していればダイアログを表示する
     */
    private fun back() {
        activity?.finish()
    }

}
