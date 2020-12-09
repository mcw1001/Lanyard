package com.cp470.lanyard;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> rule = new ActivityScenarioRule(LoginActivity.class);

    @Test
    public void testOnCreate() {
        // Assert basic elements of Activity are loaded correctly
        rule.getScenario().onActivity(activity -> {
            assertThat(activity.findViewById(R.id.editTextEmailAddress), instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.editTextPassword), instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.register_button), instanceOf(Button.class));
            assertThat(activity.findViewById(R.id.login_button), instanceOf(Button.class));
        });
    }
}