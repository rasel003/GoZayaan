/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rasel.androidbaseapp.ui.image_slider.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.rasel.androidbaseapp.data.models.TitleAndId
import com.rasel.androidbaseapp.ui.image_slider.ImageFragment

class ImagePagerAdapter(fragment: Fragment, private val data: List<TitleAndId>) :
    FragmentStatePagerAdapter(
        fragment.getChildFragmentManager(),
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
//        return ImageData.IMAGE_DRAWABLES.size
        return data.size
    }

    override fun getItem(position: Int): Fragment {
//        return ImageFragment.newInstance(ImageData.IMAGE_DRAWABLES[position])
        return ImageFragment.newInstance(data[position])
    }
}
