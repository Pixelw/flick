package tech.pixelw.flick.common.resources

/**
 *
 * NOTE: 关于资源
 *
 * 在此处集中管理各业务块资源来源;
 *
 * 由于未来有兼容其他api的需要, 大部分api不会直接include资源的URL, 需要手动构造;
 *
 * feature中不能硬编码完整URL;
 *
 */
object ResourceConfig {

    // TODO: SLY 24/2/19 动态配置多个资源源站

    var netApiDefault = PresetHosts.LAPI

    var musicFile = netApiDefault
        get() = netApiDefault
        private set

    var musicArtImg = netApiDefault
        get() = netApiDefault
        private set

    var cardArtImg = netApiDefault
        get() = netApiDefault
        private set

    var cardThumbImg = netApiDefault
        get() = netApiDefault
        private set


    fun setBaseApi(host: Host) {
        netApiDefault = host
    }


}