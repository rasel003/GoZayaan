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

package com.rasel.androidbaseapp.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.util.doOnApplyWindowInsets
import com.rasel.androidbaseapp.util.inTransaction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

   // private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // immersive mode so images can draw behind the status bar
        val decor = window.decorView
        val flags = decor.systemUiVisibility or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        decor.systemUiVisibility = flags

        val container: FrameLayout = findViewById(R.id.fragment_container)
        container.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                add(R.id.fragment_container, OnboardingFragment())
            }
        }

       /* viewModel.navigateToSignInDialogAction.observe(this, EventObserver {
            openSignInDialog()
        })*/
    }

    private fun openSignInDialog() {
        //SignInDialogFragment().show(supportFragmentManager, DIALOG_SIGN_IN)
    }

    companion object {
        private const val DIALOG_SIGN_IN = "dialog_sign_in"
    }
}
