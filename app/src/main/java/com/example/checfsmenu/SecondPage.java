package com.example.checfsmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SecondPage extends AppCompatActivity {

    ArrayList<String> foodList = new ArrayList<>();

    private Button foodAdd;
    private TextView addText;
    private TextView foodText;
    private Button back;
    private Button removeBtn;
    private Button imageAdd;

    static final String PREF_NAME="Dosya";
    static final String PREF_KEY="Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);
        foodAdd = (Button) findViewById(R.id.addFood);
        addText = (EditText) findViewById(R.id.addText);
        foodText = findViewById(R.id.foodText);
        back = (Button) findViewById(R.id.backBtn);
        removeBtn=(Button)findViewById(R.id.removeBtn);
        imageAdd=(Button)findViewById(R.id.imageAdd);


        foodList.add("Pizza");
        foodList.add("Salata");
        foodList.add("Lahmacun");
        loadData();
        save();
        foods();

        foodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodText.setText("");
                foodList.add(addText.getText().toString());
                save();
                loadData();
                foods();
                Toast.makeText(getApplicationContext(), "Kaydedildi.", Toast.LENGTH_SHORT).show();
                addText.setText("");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondPage.this, MainPage.class);
                startActivity(intent);
            }
        });
       removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData();
                foods();
            }
        });
       imageAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(SecondPage.this, ImageActivity.class);
              startActivity(intent);

               }



       });
    }

    public void foods() {
        for (int i = 0; i < foodList.size(); i++) {
            foodText.append("   ");
            foodText.append(foodList.get(i));
        }
    }
    private void save(){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(foodList);
        editor.putString("task list",json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefences",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("task list",null);
        Type type=new TypeToken<ArrayList<String>>(){}.getType();
        foodList=gson.fromJson(json,type);
        if(foodList==null){
            foodList=new ArrayList<>();
        }
    }
    private void removeData(){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        foodList.clear();
        editor.clear();
        editor.apply();
        foodText.setText("");
    }
}

