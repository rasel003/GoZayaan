/*
 * Copyright 2019 Google LLC
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

package com.rasel.androidbaseapp.ui.counter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rasel.androidbaseapp.databinding.FragmentCounterBinding
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.TemporalUnit

/**
 * A [Fragment] that displays a list of emails.
 */
class CounterFragment : Fragment() {

    private lateinit var binding: FragmentCounterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val internationalDateTime = "2024-02-25T00:05:50Z"

        val timestampInstant = Instant.parse(internationalDateTime)
        val articlePublishedZonedTime = ZonedDateTime.ofInstant(timestampInstant, ZoneId.systemDefault())
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

        // Get current Instant
        val currentZonedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
        //Convert current Instant to local time zone
        val gapInDays = Duration.between(currentZonedTime, articlePublishedZonedTime).toDays()


        //Find difference in current and published date of article
        val finalDate = when (gapInDays) {
            0L -> "Today"
            1L -> "Yesterday"
            else -> articlePublishedZonedTime.format(dateFormatter)
        }
        binding.tvLocalFormat.text = "$internationalDateTime\n = $finalDate"
    }
}
