package org.tirasweel.drivelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            menu.findItem(R.id.menu_register_log).apply {
                isVisible = true
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}