package tech.pixelw.flick.core.ui;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;

public interface OnInsetsChangeListener {
    default void onImeInsets(@NonNull Insets insets) {
    }

    default void onSystemBarsInsets(@NonNull Insets insets) {
    }

    default void onSafeZoneInsets(@NonNull Insets insets) {
    }
}

//interface OnInsetsChangeListener {
//    fun ime(inset: Insets) {}
//    fun systemBar(inset: Insets) {}
//    fun safeZone(inset: Insets) {}
//}
