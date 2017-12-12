package com.mivas.myharmonicasongs.listener;

/**
 * Listener for {@link com.mivas.myharmonicasongs.CustomizeSectionActivity}
 */
public interface StylePickerListener {

    /**
     * Triggered when a style was selected.
     *
     * @param position The position of the selected style.
     */
    void onStyleSelected(int position);

}
