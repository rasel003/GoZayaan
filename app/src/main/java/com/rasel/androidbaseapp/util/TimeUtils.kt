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

package com.rasel.androidbaseapp.util

import com.rasel.androidbaseapp.data.models.ConferenceDay
import org.threeten.bp.ZonedDateTime

object TimeUtils {

    const val CONFERENCE_DAY1_END = "2024-02-07T22:00:01-07:00"
    const val CONFERENCE_DAY1_START = "2024-02-07T07:00:00-07:00"
    const val CONFERENCE_DAY2_END = "2024-02-08T22:00:01-07:00"
    const val CONFERENCE_DAY2_START = "2024-02-08T08:00:00-07:00"
    const val CONFERENCE_DAY3_END = "2024-02-09T22:00:00-07:00"
    const val CONFERENCE_DAY3_START = "2024-02-09T08:00:00-07:00"


    val ConferenceDays = listOf(
        ConferenceDay(
            ZonedDateTime.parse(CONFERENCE_DAY1_START),
            ZonedDateTime.parse(CONFERENCE_DAY1_END)
        ),
        ConferenceDay(
            ZonedDateTime.parse(CONFERENCE_DAY2_START),
            ZonedDateTime.parse(CONFERENCE_DAY2_END)
        ),
        ConferenceDay(
            ZonedDateTime.parse(CONFERENCE_DAY3_START),
            ZonedDateTime.parse(CONFERENCE_DAY3_END)
        )
    )

    fun conferenceHasStarted(): Boolean {
        return ZonedDateTime.now().isAfter(ConferenceDays.first().start)
    }

}
