package com.mivas.myharmonicasongs.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyUtils {

    private static final float SOUND_VOLUME = 0.3f;

    private static final Map<Integer, Integer> keyOffsetMap = new HashMap<>();

    static {
        keyOffsetMap.put(0, 0);
        keyOffsetMap.put(1, 1);
        keyOffsetMap.put(2, 2);
        keyOffsetMap.put(3, 3);
        keyOffsetMap.put(4, 4);
        keyOffsetMap.put(5, 5);
        keyOffsetMap.put(6, 6);
        keyOffsetMap.put(7, -5);
        keyOffsetMap.put(8, -4);
        keyOffsetMap.put(9, -3);
        keyOffsetMap.put(10, -2);
        keyOffsetMap.put(11, -1);
        keyOffsetMap.put(12, 0);
        keyOffsetMap.put(13, 1);
        keyOffsetMap.put(14, 2);
        keyOffsetMap.put(15, 3);
        keyOffsetMap.put(16, 4);
        keyOffsetMap.put(17, 5);
        keyOffsetMap.put(18, 6);
        keyOffsetMap.put(19, -5);
        keyOffsetMap.put(20, -4);
        keyOffsetMap.put(21, -3);
        keyOffsetMap.put(22, -2);
        keyOffsetMap.put(23, -1);
    }

    private static final List<Double> frequenciesList = new ArrayList<>();

    static {
        // 0
        frequenciesList.add(16.351);
        frequenciesList.add(17.324);
        frequenciesList.add(18.354);
        frequenciesList.add(19.445);
        frequenciesList.add(20.601);
        frequenciesList.add(21.827);
        frequenciesList.add(23.124);
        frequenciesList.add(24.499);
        frequenciesList.add(25.956);
        frequenciesList.add(27.500);
        frequenciesList.add(29.135);
        frequenciesList.add(30.868);

        // 1
        frequenciesList.add(32.703);
        frequenciesList.add(34.648);
        frequenciesList.add(36.708);
        frequenciesList.add(38.891);
        frequenciesList.add(41.203);
        frequenciesList.add(43.654);
        frequenciesList.add(46.249);
        frequenciesList.add(48.999);
        frequenciesList.add(51.913);
        frequenciesList.add(55.000);
        frequenciesList.add(58.270);
        frequenciesList.add(61.735);

        // 2
        frequenciesList.add(65.406);
        frequenciesList.add(69.296);
        frequenciesList.add(73.416);
        frequenciesList.add(77.782);
        frequenciesList.add(82.407);
        frequenciesList.add(87.307);
        frequenciesList.add(92.499);
        frequenciesList.add(97.999);
        frequenciesList.add(103.826);
        frequenciesList.add(110.000);
        frequenciesList.add(116.541);
        frequenciesList.add(123.471);

        // 3
        frequenciesList.add(130.813);
        frequenciesList.add(138.591);
        frequenciesList.add(146.832);
        frequenciesList.add(155.563);
        frequenciesList.add(164.814);
        frequenciesList.add(174.614);
        frequenciesList.add(184.997);
        frequenciesList.add(195.998);
        frequenciesList.add(207.652);
        frequenciesList.add(220.000);
        frequenciesList.add(233.082);
        frequenciesList.add(246.942);

        // 4
        frequenciesList.add(261.626);
        frequenciesList.add(277.183);
        frequenciesList.add(293.665);
        frequenciesList.add(311.127);
        frequenciesList.add(329.628);
        frequenciesList.add(349.228);
        frequenciesList.add(369.994);
        frequenciesList.add(391.995);
        frequenciesList.add(415.305);
        frequenciesList.add(440.000);
        frequenciesList.add(466.164);
        frequenciesList.add(493.883);

        // 5
        frequenciesList.add(523.251);
        frequenciesList.add(554.365);
        frequenciesList.add(587.330);
        frequenciesList.add(622.254);
        frequenciesList.add(659.255);
        frequenciesList.add(698.456);
        frequenciesList.add(739.989);
        frequenciesList.add(783.991);
        frequenciesList.add(830.609);
        frequenciesList.add(880.000);
        frequenciesList.add(932.328);
        frequenciesList.add(987.767);

        // 6
        frequenciesList.add(1046.502);
        frequenciesList.add(1108.731);
        frequenciesList.add(1174.659);
        frequenciesList.add(1244.508);
        frequenciesList.add(1318.510);
        frequenciesList.add(1396.913);
        frequenciesList.add(1479.978);
        frequenciesList.add(1567.982);
        frequenciesList.add(1661.219);
        frequenciesList.add(1760.000);
        frequenciesList.add(1864.655);
        frequenciesList.add(1975.533);

        // 7
        frequenciesList.add(2093.005);
        frequenciesList.add(2217.461);
        frequenciesList.add(2349.318);
        frequenciesList.add(2489.016);
        frequenciesList.add(2637.021);
        frequenciesList.add(2793.826);
        frequenciesList.add(2959.955);
        frequenciesList.add(3135.964);
        frequenciesList.add(3322.438);
        frequenciesList.add(3520.000);
        frequenciesList.add(3729.310);
        frequenciesList.add(3951.066);
    }

    public static final void playSound(double frequency) {
        int sampleRate = 44100;
        byte soundData[] = new byte[sampleRate / 4];

        for (int i = 0; i < soundData.length; i++) {
            byte sample = (byte) (
                    Math.sin(2 * Math.PI * frequency * i / sampleRate) *
                            255);
            soundData[i] = sample;
        }

        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_DEFAULT,
                AudioFormat.ENCODING_PCM_8BIT, soundData.length,
                AudioTrack.MODE_STATIC
        );
        track.write(soundData, 0, soundData.length);
        track.setNotificationMarkerPosition(soundData.length);
        track.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {

            @Override
            public void onMarkerReached(AudioTrack track) {
                track.flush();
                track.stop();
                track.release();
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            track.setVolume(SOUND_VOLUME);
        } else {
            track.setStereoVolume(SOUND_VOLUME, SOUND_VOLUME);
        }

        track.play();

    }

    public static final double getFrequency(int hole, boolean blow, float bend, int key) {
        int frequencyIndex = 36;
        frequencyIndex += keyOffsetMap.get(key);
        frequencyIndex += getNoteOffset(hole, blow, bend);
        return frequenciesList.get(frequencyIndex);
    }

    private static final int getNoteOffset(int hole, boolean blow, float bend) {
        if (hole == 1 && blow && bend == 0f) {
            return 0;
        } else if (hole == 1 && !blow && bend == -0.5f) {
            return 1;
        } else if (hole == 1 && !blow && bend == 0f) {
            return 2;
        } else if (hole == 2 && blow && bend == 0f) {
            return 4;
        } else if (hole == 2 && !blow && bend == -1f) {
            return 5;
        } else if (hole == 2 && !blow && bend == -0.5f) {
            return 6;
        } else if (hole == 2 && !blow && bend == 0f) {
            return 7;
        } else if (hole == 3 && blow && bend == 0f) {
            return 7;
        } else if (hole == 3 && !blow && bend == -1.5f) {
            return 8;
        } else if (hole == 3 && !blow && bend == -1f) {
            return 9;
        } else if (hole == 3 && !blow && bend == -0.5f) {
            return 10;
        } else if (hole == 3 && !blow && bend == 0f) {
            return 11;
        } else if (hole == 4 && blow && bend == 0f) {
            return 12;
        } else if (hole == 4 && !blow && bend == -0.5f) {
            return 13;
        } else if (hole == 4 && !blow && bend == 0f) {
            return 14;
        } else if (hole == 5 && blow && bend == 0f) {
            return 16;
        } else if (hole == 5 && !blow && bend == 0f) {
            return 17;
        } else if (hole == 6 && blow && bend == 0f) {
            return 19;
        } else if (hole == 6 && !blow && bend == -0.5f) {
            return 20;
        } else if (hole == 6 && !blow && bend == 0f) {
            return 21;
        } else if (hole == 7 && !blow && bend == 0f) {
            return 23;
        } else if (hole == 7 && blow && bend == 0f) {
            return 24;
        } else if (hole == 8 && !blow && bend == 0f) {
            return 26;
        } else if (hole == 8 && blow && bend == 0.5f) {
            return 27;
        } else if (hole == 8 && blow && bend == 0f) {
            return 28;
        } else if (hole == 9 && !blow && bend == 0f) {
            return 29;
        } else if (hole == 9 && blow && bend == 0.5f) {
            return 30;
        } else if (hole == 9 && blow && bend == 0f) {
            return 31;
        } else if (hole == 10 && !blow && bend == 0f) {
            return 33;
        } else if (hole == 10 && blow && bend == 1f) {
            return 34;
        } else if (hole == 10 && blow && bend == 0.5f) {
            return 35;
        } else if (hole == 10 && blow && bend == 0f) {
            return 36;
        }
        return 0;
    }
}
