package com.agd.sa.tictactoe2018;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {
    RadioButton singlePlayer,twoPlayer;
    RadioGroup gameType,gameMode,difftype;
    RadioButton time,point,level;
    Button startGameBtwn;
    RelativeLayout part2,part3,part4;
    public static boolean pointA=false,levelA=false;
    TextView gameTypeText,gameMText,diffText;
    Typeface custom_font;
    boolean isEasy=false,isComplex=false,isMode1=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        custom_font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.custome_font2));

        gameMText=(TextView)findViewById(R.id.gameModeText);
        gameMText.setTypeface(custom_font);

        gameTypeText=(TextView)findViewById(R.id.gameTypeText);
        gameTypeText.setTypeface(custom_font);

        gameMode=(RadioGroup)findViewById(R.id.gameMode);
        singlePlayer =(RadioButton)findViewById(R.id.singlePlayerMode);
        singlePlayer.setTypeface(custom_font);

        twoPlayer =(RadioButton)findViewById(R.id.twoPlayerMode);
        twoPlayer.setTypeface(custom_font);

        gameType=(RadioGroup)findViewById(R.id.gameType);
      //  time=(RadioButton)findViewById(R.id.timeChallenge);
        point=(RadioButton)findViewById(R.id.PointChallenge);
        point.setTypeface(custom_font);

        level=(RadioButton)findViewById(R.id.levelChallenge);
        level.setTypeface(custom_font);

        part2=(RelativeLayout)findViewById(R.id.selectionPart2);
        part3=(RelativeLayout)findViewById(R.id.selectionPart3);

        startGameBtwn=(Button)findViewById(R.id.startGame);
        startGameBtwn.setTypeface(custom_font);








        expandMethod();
    }


    private void expandMethod(){
        singlePlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part2.setVisibility(View.VISIBLE);
            }
        });

        twoPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part2.setVisibility(View.VISIBLE);
            }
        });





        /*time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part3.setVisibility(View.VISIBLE);
            }
        });*/

        level.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part3.setVisibility(View.VISIBLE);
            }
        });

        point.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                part3.setVisibility(View.VISIBLE);
            }
        });

    }

    public void goToGame(View view){
        int selectedId = gameMode.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton rButton = (RadioButton) findViewById(selectedId);


        int selectedId2 = gameType.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton rButton2 = (RadioButton) findViewById(selectedId2);

        if (rButton.getText().equals(getString(R.string.game_mode_1))){

           if(rButton2.getText().equals(getString(R.string.type_2))){
                enterComplexMethod(getString(R.string.enter_point_text),rButton);
                pointA=true;
               levelA=false;
            }else if(rButton2.getText().equals(getString(R.string.type_3))){
                enterComplexMethod(getString(R.string.enter_level_text),rButton);
                levelA=true;
               pointA=false;
            }

        }else if (rButton.getText().equals(getString(R.string.game_mode_2))){


           if(rButton2.getText().equals(getString(R.string.type_2))){
               pointA=true;
               levelA=false;
                enterMethod(getString(R.string.enter_point_text),rButton);
            }else if(rButton2.getText().equals(getString(R.string.type_3))){
               levelA=true;
               pointA=false;
                enterMethod(getString(R.string.enter_level_text),rButton);
            }

        }
    }
    public void menuBack(View view){
        if (GoogleSignIn.comeFromOffline==true){
            Intent i = new Intent(this, GoogleSignIn.class);
            startActivity(i);
            finish();
            GoogleSignIn.comeFromOffline=false;
        }else {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (GoogleSignIn.comeFromOffline==true){
            Intent i = new Intent(this, GoogleSignIn.class);
            startActivity(i);
            finish();
            GoogleSignIn.comeFromOffline=false;
        }else {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void enterMethod(final String text, Button rButton) {

        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.enter, null);
        TextView enterText = (TextView) view1.findViewById(R.id.enterText);
        final EditText enterValue = (EditText) view1.findViewById(R.id.enterValue);
        Button enterGame = (Button) view1.findViewById(R.id.enterGame);
        Button closeEnter = (Button) view1.findViewById(R.id.closeEnter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        enterText.setText(text);
        enterText.setTypeface(custom_font);
        enterValue.setTypeface(custom_font);
        enterGame.setTypeface(custom_font);
        closeEnter.setTypeface(custom_font);

        enterGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = enterValue.getText().toString();
                    if (pointA==true){
                        if (Integer.parseInt(value)>100){
                            Toast.makeText(getApplicationContext(),getString(R.string.max_point_text),Toast.LENGTH_SHORT).show();
                        }else if (Integer.parseInt(value)<5){
                            Toast.makeText(getApplicationContext(),getString(R.string.min_point_text),Toast.LENGTH_SHORT).show();
                        }else {
                            Intent i = new Intent(SelectionActivity.this, OfflineActivity.class);
                            i.putExtra("val", value);
                            startActivity(i);
                            finish();
                        }
                    }else if (levelA==true){
                        if (Integer.parseInt(value)>20){
                            Toast.makeText(getApplicationContext(),getString(R.string.max_level_text),Toast.LENGTH_SHORT).show();
                        }else if(Integer.parseInt(value)<1){
                            Toast.makeText(getApplicationContext(),getString(R.string.min_level_text),Toast.LENGTH_SHORT).show();
                        }else {
                            Intent i = new Intent(SelectionActivity.this, OfflineActivity.class);
                            i.putExtra("val", value);
                            startActivity(i);
                            finish();
                        }
                    }

                }
            });
        closeEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    public void enterComplexMethod(final String text, Button rButton) {
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.enter2, null);
        TextView enterText = (TextView) view1.findViewById(R.id.enterText);
        final EditText enterValue = (EditText) view1.findViewById(R.id.enterValue);
        Button enterGame = (Button) view1.findViewById(R.id.enterGame);
        Button closeEnter = (Button) view1.findViewById(R.id.closeEnter);

        TextView difftext=(TextView)view1.findViewById(R.id.diffTypeText);
        difftext.setTypeface(custom_font);
        //  time=(RadioButton)findViewById(R.id.timeChallenge);
        final Button easy=(Button)view1.findViewById(R.id.easyR);
        easy.setTypeface(custom_font);

      final Button complex=(Button)view1.findViewById(R.id.complexR);
        complex.setTypeface(custom_font);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        enterText.setText(text);
        enterText.setTypeface(custom_font);
        enterValue.setTypeface(custom_font);
        enterGame.setTypeface(custom_font);
        closeEnter.setTypeface(custom_font);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEasy=true;
                isComplex=false;
                easy.setBackgroundResource(R.drawable.button_style8);
                complex.setBackgroundResource(R.drawable.change_button);

            }
        });

        complex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isComplex=true;
                isEasy=false;
                complex.setBackgroundResource(R.drawable.button_style8);
                easy.setBackgroundResource(R.drawable.change_button);
            }
        });



                    enterGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((isComplex == true || isEasy == true)) {
                                String value = enterValue.getText().toString();

                                if (isEasy == true && pointA == true) {
                                    if (Integer.parseInt(value) > 100) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.max_point_text), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(value) < 5) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.min_point_text), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent i = new Intent(SelectionActivity.this, MainActivity.class);
                                        i.putExtra("val", value);
                                        startActivity(i);
                                        finish();
                                    }
                                } else if (isEasy == true && levelA == true) {
                                    if (Integer.parseInt(value) > 20) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.max_level_text), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(value) < 1) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.min_level_text), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent i = new Intent(SelectionActivity.this, MainActivity.class);
                                        i.putExtra("val", value);
                                        startActivity(i);
                                        finish();
                                    }
                                } else if (isComplex == true && pointA == true) {
                                    if (Integer.parseInt(value) > 100) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.max_point_text), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(value) < 5) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.min_point_text), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent i = new Intent(SelectionActivity.this, ComplexActivity.class);
                                        i.putExtra("val", value);
                                        startActivity(i);
                                        finish();
                                    }
                                } else if (isComplex = true && levelA == true) {
                                    if (Integer.parseInt(value) > 20) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.max_level_text), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(value) < 1) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.min_level_text), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent i = new Intent(SelectionActivity.this, ComplexActivity.class);
                                        i.putExtra("val", value);
                                        startActivity(i);
                                        finish();
                                    }
                                }


                            }else {
                                Toast.makeText(SelectionActivity.this, "Click on a Button: Easy or Hard", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                /*} */

        closeEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }



}
