package com.cp470.lanyard;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testOnCreate() {
        // Assert basic elements/controls of MainActivity are loaded correctly
        rule.getScenario().onActivity(activity -> {
            assertThat(activity.findViewById(R.id.iconPickerBt), instanceOf(ImageButton.class));
            assertThat(activity.findViewById(R.id.edit_text_account_title), instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.edit_text_account_userName), instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.passGenButton), instanceOf(Button.class));
            assertThat(activity.findViewById(R.id.passGenSettingsButton), instanceOf(ImageButton.class));
            assertThat(activity.findViewById(R.id.new_password_expiry), instanceOf(Button.class));
            assertThat(activity.findViewById(R.id.save_new_password), instanceOf(Button.class));
        });
    }

    @Test
    public void openDatePicker() {
        rule.getScenario().onActivity(activity -> {
            activity.findViewById(R.id.new_password_expiry).callOnClick();
        });
    }

    @Test
    public void openIconPicker() {
        rule.getScenario().onActivity(activity -> {
            activity.findViewById(R.id.iconPickerBt).callOnClick();
        });
    }
}