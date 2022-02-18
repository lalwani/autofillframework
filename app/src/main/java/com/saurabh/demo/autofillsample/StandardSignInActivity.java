/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saurabh.demo.autofillsample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class StandardSignInActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private int autoFillEventId;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());
        mUsernameEditText = findViewById(R.id.usernameField);
        mPasswordEditText = findViewById(R.id.someField);
        AutofillManager afm = getSystemService(AutofillManager.class);
        mUsernameEditText.setFocusable(true);
        mUsernameEditText.setFocusedByDefault(true);
        afm.requestAutofill(mUsernameEditText);

        afm.registerCallback(new AutofillManager.AutofillCallback() {
            @Override
            public void onAutofillEvent(@NonNull View view, int event) {
                super.onAutofillEvent(view, event);
                autoFillEventId = event;
            }

            @Override
            public void onAutofillEvent(@NonNull View view, int virtualId, int event) {
                super.onAutofillEvent(view, virtualId, event);
                autoFillEventId = event;
            }
        });
        mUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (autoFillEventId == AutofillManager.AutofillCallback.EVENT_INPUT_HIDDEN) {
//                    findViewById(R.id.login).performClick();
//                }
            }
        });
        findViewById(R.id.login).setOnClickListener(v -> login());
        findViewById(R.id.clear).setOnClickListener(v -> {

            if (afm != null) {
                afm.cancel();
            }
            resetFields();
        });
    }

    protected int getContentView() {
        return R.layout.login_activity;
    }

    private void resetFields() {
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");
    }

    /**
     * Emulates a login action.
     */
    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        boolean valid = isValidCredentials(username, password);
        if (valid) {
            Intent intent = WelcomeActivity.getStartActivityIntent(StandardSignInActivity.this);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Dummy implementation for demo purposes. A real service should use secure mechanisms to
     * authenticate users.
     */
    public boolean isValidCredentials(String username, String password) {
        return username != null && password != null;
    }
}
