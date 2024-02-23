package tech.pixelw.flick.core.media

enum class PlayerState(key: Int) {

    IDLE(0),

    BUFFERING(1),

    PLAYING(2),

    PAUSED(3),

    SKIPPING_TO_NEXT(4),

    SKIPPING_TO_PREVIOUS(5),

    ENDED(6)

}