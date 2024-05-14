/*
 *   Copyright (c) 2019 Google Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under the License
 *
 *   is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *   or implied. See the License for the specific language governing permissions and limitations under
 *   the License.
 */
package com.rasel.androidbaseapp.ui.mycourses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import com.materialstudies.owl.model.courses
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.core.decorator.BottomSpacingItemDecoration
import com.rasel.androidbaseapp.databinding.FragmentMyCoursesBinding
import com.rasel.androidbaseapp.util.SpringAddItemAnimator
import java.util.concurrent.TimeUnit

class MyCoursesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyCoursesBinding.inflate(inflater, container, false).apply {
            list.apply {
                itemAnimator = SpringAddItemAnimator()
                addItemDecoration(
                    BottomSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_2))
                )
                adapter = MyCoursesAdapter().apply {
                    // add data after layout so that animations run
                    doOnNextLayout {
                        submitList(courses)
                        doOnNextLayout {
                            startPostponedEnterTransition()
                        }
                    }
                }
            }
        }
        postponeEnterTransition(1000L, TimeUnit.MILLISECONDS)
        return binding.root
    }
}

