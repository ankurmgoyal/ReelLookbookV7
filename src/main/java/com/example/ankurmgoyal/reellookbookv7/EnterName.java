package com.example.ankurmgoyal.reellookbookv7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EnterName extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);
    }

    public void buttonClicked(View view){
        EditText editText = (EditText)findViewById(R.id.editText);
        String s = editText.getText().toString();
        Intent i = new Intent(this,TitlePage.class);
        i.putExtra("ID",s);
        startActivity(i);
    }
}
