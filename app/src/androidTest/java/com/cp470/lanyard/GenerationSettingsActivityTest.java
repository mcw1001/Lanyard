package com.cp470.lanyard;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

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
public class GenerationSettingsActivityTest {
    @Rule
    public ActivityScenarioRule<GenerationSettingsActivity> rule =
            new ActivityScenarioRule<>(GenerationSettingsActivity.class);

    @Test
    public void testOnCreate() {
        // Assert basic elements/controls of MainActivity are loaded correctly
        rule.getScenario().onActivity(activity -> {
            assertThat(activity.findViewById(R.id.passwordLengthSeekBar), instanceOf(SeekBar.class));
            assertThat(activity.findViewById(R.id.settingsOkButton), instanceOf(Button.class));
            assertThat(activity.findViewById(R.id.upperCheck), instanceOf(CheckBox.class));
            assertThat(activity.findViewById(R.id.lowerCheck), instanceOf(CheckBox.class));
            assertThat(activity.findViewById(R.id.numberCheck), instanceOf(CheckBox.class));
            assertThat(activity.findViewById(R.id.symbolCheck), instanceOf(CheckBox.class));
        });
    }

    @Test
    public void testCheckBoxes() {
        rule.getScenario().onActivity(activity -> {
            activity.findViewById(R.id.upperCheck).callOnClick();
        });
    }

    @Test
    public void savePasswordPreferences() {

    }
}