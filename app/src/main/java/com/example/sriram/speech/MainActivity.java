package com.example.sriram.speech;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private int width=800, height=800;
    private float x=400,y=400,vx=10,vy=10,r=30;
    private char command;
    private Canvas c;
    private Paint paint;
    private ImageView imageview;
    private ArrayList<String> matches;
    public ListView mList;
    public Button speakButton;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bitmap b = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        c.drawColor(Color.WHITE);
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);
        imageview=(ImageView) findViewById(R.id.imageView);
        imageview.setImageBitmap(b);
        paint.setColor(Color.BLUE);
        c.drawCircle(x, y, r, paint);
        speakButton = (Button) findViewById(R.id.btn_speak);
        speakButton.setOnClickListener(this);
        voiceinputbuttons();
    }

    public void voiceinputbuttons() {
        speakButton = (Button) findViewById(R.id.btn_speak);
        mList = (ListView) findViewById(R.id.list);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Your Command");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onClick(View v) {
        startVoiceRecognitionActivity();
    }

    public void run() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                }
        );
    }

    void update(){
        paint.setColor(Color.WHITE);
        c.drawCircle(x, y, r, paint);
        if(command=='r')
        {
            x=x+vx;
            paint.setColor(Color.RED);
            c.drawCircle(x, y, r, paint);
        }
        else if(command=='l')
        {
            x=x-vx;
            paint.setColor(Color.GREEN);
            c.drawCircle(x, y, r, paint);
        }
        else if(command=='u')
        {
            y=y-vy;
            paint.setColor(Color.MAGENTA);
            c.drawCircle(x, y, r, paint);
        }
        else if(command=='d')
        {
            y=y+vy;
            paint.setColor(Color.CYAN);
            c.drawCircle(x, y, r, paint);
        }
        if(x+r>=width)
            vx=-vx;
        if(x-r<=0)
            vx=-vx;
        if(y+r>=height)
            vy=-vy;
        if(y-r<=0)
            vy=-vy;

        imageview.invalidate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches);
            mList.setAdapter(adapter);
            if (matches.contains("right")) {
                command='r';
                run();
            }
            else if (matches.contains("left")) {
                command='l';
                run();
            }
            else if (matches.contains("up")) {
                command='u';
                run();
            }
            else if (matches.contains("down")) {
                command='d';
                run();
            }
        }
    }
}
