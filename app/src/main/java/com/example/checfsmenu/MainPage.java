package com.example.checfsmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class MainPage extends AppCompatActivity {
    private Button next;
    private TextView randomText;
    private Button randomBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = (Button) findViewById(R.id.nextBtn);
        randomText = (TextView) findViewById(R.id.randomText);
        randomBtn = (Button) findViewById(R.id.randomFood);
        ArrayList<String> foodList;
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefences",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("task list",null);
        Type type=new TypeToken<ArrayList<String>>(){}.getType();
        foodList=gson.fromJson(json,type);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, SecondPage.class);
                startActivity(intent);
            }
        });
        final ArrayList<String> finalFoodList = foodList;
        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r=new Random();
                int randomNumber=r.nextInt(finalFoodList.size());
                randomText.setText(finalFoodList.get(randomNumber));
            }
        });
    }
}
