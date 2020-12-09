package com.cp470.lanyard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class AccountDetailActivityTest {
    static Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AccountDetailActivity.class);
    static {
        String documentId = "4";
        Bundle bundle = new Bundle();
        bundle.putString("documentId", documentId);
        intent.putExtras(bundle);
    }

    @Rule
    public ActivityScenarioRule<AccountDetailActivity> rule =
            new ActivityScenarioRule(intent);

    @Test
    public void testOnCreate() {
        // Assert basic elements/controls of activity are loaded correctly
        rule.getScenario().onActivity(activity -> {
            assertThat(activity.findViewById(R.id.detail_loading), instanceOf(ProgressBar.class));
            // Unsure how to mock returning a Firestore entry
//            activity.findViewById(R.id.detail_loading).setVisibility(View.INVISIBLE);
//            activity.findViewById(R.id.account_details).setVisibility(View.VISIBLE);
//            assertThat(activity.findViewById(R.id.detailIconAccountItem), instanceOf(ImageView.class));
//            assertThat(activity.findViewById(R.id.detailTitleAccountItem), instanceOf(TextView.class));
//            assertThat(activity.findViewById(R.id.detailUserNameAccountItem), instanceOf(TextView.class));
//            assertThat(activity.findViewById(R.id.detailPassAccountItem), instanceOf(TextView.class));
//            assertThat(activity.findViewById(R.id.detail_toggle_password), instanceOf(ImageButton.class));
//            assertThat(activity.findViewById(R.id.detailCreationDate), instanceOf(TextView.class));
//            assertThat(activity.findViewById(R.id.detailPassCopy), instanceOf(TextView.class));
        });
    }
}