package com.mivas.myharmonicasongs.listener;

import com.mivas.myharmonicasongs.database.model.DbScrollTimer;

public interface ScrollTimersAdapterListener {

    void onScrollTimerDeleted(DbScrollTimer dbScrollTimer);
    void onTimeSelected(DbScrollTimer dbScrollTimer);
    void onSectionSelected(DbScrollTimer dbScrollTimer);
}
