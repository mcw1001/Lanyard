package com.cp470.lanyard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountItem account = new AccountItem(
                R.drawable.ic_baseline_mail_24,
                "Fake Mail",
                "jeffBob@fake.com",
                "fyt6fygt7ygy76yg"
        );
        setContentView(R.layout.activity_account_detail);
        // TODO replace mock data
        TextView accountTitle = findViewById(R.id.detailTitleAccountItem);
        accountTitle.setText(account.getTitle());
        ImageView accountIcon = findViewById(R.id.detailIconAccountItem);
        accountIcon.setImageResource(account.getImageResource());
        TextView accountUsername = findViewById(R.id.detailUserNameAccountItem);
        accountUsername.setText(account.getUserName());
        TextView accountPassword = findViewById(R.id.detailPassAccountItem);
        accountPassword.setText(account.getPassword());
        TextView accountCreation = findViewById(R.id.detailCreationDate);
        accountCreation.setText("April 2, 2020");
        TextView accountNotes = findViewById(R.id.detailPassNotes);
        accountNotes.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus sem quam, maximus eu vehicula id, sagittis quis sem. In luctus ultricies lacus nec lobortis. Aliquam sed porttitor mauris. " +
                "Etiam sit amet imperdiet ante. Morbi suscipit ultrices ante, ut euismod massa rhoncus et. " +
                "Curabitur libero lacus, scelerisque a orci laoreet, viverra iaculis arcu. " +
                "Duis fermentum lorem in justo blandit eleifend. Curabitur et tincidunt ex. Integer id dolor libero. " +
                "Aenean est sapien, commodo vel est accumsan, tincidunt pellentesque diam. " +
                "Praesent libero quam, vestibulum nec efficitur in, lacinia ut nunc. Suspendisse malesuada euismod nunc vitae euismod. "
        );
    }

    public void onEditAccountClick(View view) {
        // TODO Toggle account edit
    }
}