package ca.nait.dmit2504.bestrada.androidroomsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewBinActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.brestrada.binventory.REPLY";

    private EditText mEditTextBinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bin);

        mEditTextBinView = findViewById(R.id.activity_new_bin_edittext);

        final Button saveButton = findViewById(R.id.activity_new_bin_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditTextBinView.getText())) {
                    // No bin name has been entered, set the result as cancelled.
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    // get the name of the bin the user has entered.
                    String binName = mEditTextBinView.getText().toString();
                    // put the new bin in the extras for the reply Intent
                    replyIntent.putExtra(EXTRA_REPLY, binName);
                    // set result status
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}