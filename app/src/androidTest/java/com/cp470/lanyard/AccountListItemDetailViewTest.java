package com.cp470.lanyard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class AccountListItemDetailViewTest {
    static Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AccountListItemDetailView.class);
    static {
        String documentId = "4";
        Bundle bundle = new Bundle();
        bundle.putString("documentId", documentId);
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<AccountListItemDetailView> rule =
            new ActivityScenarioRule(intent);

    @Test
    public void testOnCreate() {
        // Assert basic elements/controls of activity are loaded correctly
        rule.getScenario().onActivity(activity -> {
            assertThat(activity.findViewById(R.id.image_button_account_image_DETAILVIEW),
                    instanceOf(ImageButton.class));
            assertThat(activity.findViewById(R.id.edit_text_account_title_DETAILVIEW),
                    instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.edit_text_account_userName_DETAILVIEW),
                    instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.edit_text_account_password_DETAILVIEW),
                    instanceOf(EditText.class));
            assertThat(activity.findViewById(R.id.passGenButton), instanceOf(Button.class));
            assertThat(activity.findViewById(R.id.passGenSettingsButton), instanceOf(ImageButton.class));
            assertThat(activity.findViewById(R.id.edit_password_expiry), instanceOf(Button.class));
            assertThat(activity.findViewById(R.id.update_account_password), instanceOf(Button.class));
        });
    }
}