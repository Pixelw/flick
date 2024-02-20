package tech.pixelw.flick.common.resources

@Deprecated("改为动态下发")
class BanGDream {

    enum class Server(val key: Int) {
        JPN(0),
        GLO(1),
        TPE(2),
        CHN(3),
        KOR(4)
    }

    enum class Band(val key: Int) {
        PPP(1),
        AG(2),
        HHW(3),
        PP(4),
        R(5),
        MOR(21),
        RAS(18),
        MYGO(45),
        BAND_MIX(100)
    }

    enum class CardType(val key: String) {
        RED("powerful"),
        BLUE("cool"),
        YELLOW("happy"),
        GREEN("pure")
    }

    enum class StrengthAttr(val key: String) {
        PERFORMANCE("performance"),
        TECHNIQUE("technique"),
        VISUAL("visual")
    }

    enum class EventType(val key: String) {
        STORY("story"),
        VS("versus"),
        TRY("live_try"),
        MISSION("mission_live"),
        CHALLENGE("challenge"),
        FES("festival"),
        MEDLEY("medley")
    }

}