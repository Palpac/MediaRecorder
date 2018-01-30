package palpac.media_recorder;

// Based on https://www.youtube.com/watch?v=ZM_jAnx57Nk

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button record, play, stop; // Declarations
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private String sdcard_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = findViewById(R.id.record); // Buttons handle
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        play.setEnabled(false);
        stop.setEnabled(false);
        record.setOnClickListener(RecordListener); // Click Listener declarations
        stop.setOnClickListener(StopListener);
        play.setOnClickListener(PlayListener);

        //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp"; // Recorded file path on SD card
        sdcard_path = Environment.getExternalStorageDirectory().getPath();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////// CLICK LISTENERS
    private View.OnClickListener RecordListener = new View.OnClickListener() { // Listener image PLAY
        public void onClick(View v) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss"); // Get Date and Time
            String currentDateandTime = sdf.format(new Date());
            File file = new File(sdcard_path, "MediaRecorder"); // Create repertory in SDcard if needed
            if (!file.exists()){
                file.mkdirs();
            }
            outputFile = (file.getAbsolutePath() + "/MediaRecorder_" + currentDateandTime + ".3pg"); // Set PATH for recorded audio
            try {
                myAudioRecorder = new MediaRecorder(); // MediaRecorder declaration
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);
                myAudioRecorder.prepare();
                myAudioRecorder.start();
                Toast.makeText(getApplicationContext(), "Recording...",Toast.LENGTH_SHORT).show();
            }
            catch (IllegalStateException e) {
                // On Exception Action
            }
            catch (IOException e) {
                // On Exception Action
            }
            record.setEnabled(false);
            stop.setEnabled(true);
        }
    };
    private View.OnClickListener StopListener = new View.OnClickListener() { // Listener STOP
        @Override
        public void onClick(View v) {
            myAudioRecorder.stop(); // Stop recording
            myAudioRecorder.release();
            play.setEnabled(true);
            record.setEnabled(true);
            stop.setEnabled(false);
            //Toast.makeText(getApplicationContext(), "Recording stopped.",Toast.LENGTH_SHORT).show();
        }
    };
    private View.OnClickListener PlayListener = new View.OnClickListener() { // Listener PLAY
        @Override
        public void onClick(View v) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start(); // Start playing
                //Toast.makeText(getApplicationContext(), "Playing recorded audio.",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                // On Exception Action
            }
        }
    };

    // Place Void here
}
